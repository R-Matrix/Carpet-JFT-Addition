package xyz.water.rmatrix.mapcatalog.server;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import xyz.water.rmatrix.mapcatalog.MapCatalogServer;
import xyz.water.rmatrix.mapcatalog.command.MapCatalogCommands;
import xyz.water.rmatrix.mapcatalog.config.MapCatalogConfig;
import xyz.water.rmatrix.mapcatalog.protocol.mapcatalog.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class MapCatalogServerService {
    private static final Pattern MAP_FILE_PATTERN = Pattern.compile("map_(\\d+)\\.dat");
    private static final String CARPET_JFT_MOD_ID = "carpetjftaddition";
    private static final String CARPET_JFT_ENTRYPOINT = "mapcatalog-carpet-jft";
    private static final Map<Integer, MapCatalogMapInfo> MAPS = new HashMap<>();
    private static final Map<Integer, Long> MAP_REVISIONS = new HashMap<>();
    private static final Map<MapState, Integer> MAP_IDS = new IdentityHashMap<>();
    private static final Map<UUID, Integer> LAST_REQUEST_TICKS = new HashMap<>();

    private static MinecraftServer server;
    private static UUID worldSessionId = new UUID(0L, 0L);
    private static MapCatalogConfig config = new MapCatalogConfig();
    private static int highestMapId = -1;
    private static long catalogRevision;
    private static boolean initialized;
    private static boolean carpetJftEntrypointLookupAttempted;
    private static BooleanSupplier carpetJftMapSyncRule;

    public static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;

        PayloadTypeRegistry.playC2S().register(MapCatalogSyncRequestC2S.ID, MapCatalogSyncRequestC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(MapCatalogSyncStartS2C.ID, MapCatalogSyncStartS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(MapCatalogSyncBatchS2C.ID, MapCatalogSyncBatchS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(MapCatalogSyncEndS2C.ID, MapCatalogSyncEndS2C.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(MapCatalogSyncRequestC2S.ID, MapCatalogServerService::mapcatalog$handleRequest);

        ServerLifecycleEvents.SERVER_STARTED.register(MapCatalogServerService::mapcatalog$onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(MapCatalogServerService::mapcatalog$onServerStopping);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                MapCatalogCommands.register(dispatcher));
    }

    public static void mapcatalog$onServerStarted(MinecraftServer startedServer) {
        server = startedServer;
        worldSessionId = UUID.randomUUID();
        config = MapCatalogConfig.load();
        LAST_REQUEST_TICKS.clear();
        mapcatalog$scanMaps(startedServer);
        MapCatalogServer.LOGGER.info("MapCatalog 已初始化: {} 张地图, 最大编号 {}, session {}",
                MAPS.size(), highestMapId, worldSessionId);
    }

    public static void mapcatalog$onServerStopping(MinecraftServer stoppingServer) {
        if (server != stoppingServer) {
            return;
        }
        MAPS.clear();
        MAP_REVISIONS.clear();
        MAP_IDS.clear();
        LAST_REQUEST_TICKS.clear();
        highestMapId = -1;
        catalogRevision = 0L;
        server = null;
        worldSessionId = new UUID(0L, 0L);
    }

    public static void mapcatalog$onMapStatePut(ServerWorld world, MapIdComponent mapId, MapState mapState) {
        if (server == null || world.getServer() != server || mapId == null || mapState == null) {
            return;
        }
        mapcatalog$updateMapState(mapId.id(), mapState, true);
    }

    public static void mapcatalog$onMapStateBannerChanged(MapState mapState) {
        if (server == null || mapState == null) {
            return;
        }

        Integer mapId = MAP_IDS.get(mapState);
        if (mapId != null) {
            mapcatalog$updateMapState(mapId, mapState, true);
        }
    }

    private static void mapcatalog$scanMaps(MinecraftServer minecraftServer) {
        MAPS.clear();
        MAP_REVISIONS.clear();
        MAP_IDS.clear();
        highestMapId = -1;
        catalogRevision = 0L;
        Path dataDirectory = minecraftServer.getSavePath(WorldSavePath.ROOT).resolve("data");
        if (!Files.isDirectory(dataDirectory)) {
            return;
        }

        ServerWorld overworld = minecraftServer.getOverworld();
        try (Stream<Path> paths = Files.list(dataDirectory)) {
            paths.filter(Files::isRegularFile)
                    .map(path -> new MapFile(path, mapcatalog$parseMapId(path)))
                    .filter(mapFile -> mapFile.mapId() >= 0)
                    .sorted(Comparator.comparingInt(MapFile::mapId))
                    .forEach(mapFile -> {
                        try {
                            MapState mapState = mapcatalog$getMapState(overworld, mapFile.mapId());
                            if (mapState != null) {
                                mapcatalog$updateMapState(mapFile.mapId(), mapState, false);
                            }
                        } catch (RuntimeException exception) {
                            MapCatalogServer.LOGGER.warn("无法加载地图文件 {}", mapFile.path(), exception);
                        }
                    });
        } catch (IOException exception) {
            MapCatalogServer.LOGGER.warn("无法扫描地图目录 {}", dataDirectory, exception);
        }
    }

    private static MapState mapcatalog$getMapState(ServerWorld world, int mapId) {
        //#if MC >= 12105
        //$$ return world.getPersistentStateManager().get(
        //$$         MapState.createStateType(new MapIdComponent(mapId)));
        //#else
        return world.getPersistentStateManager().get(
                MapState.getPersistentStateType(), "map_" + mapId);
        //#endif
    }

    private static int mapcatalog$parseMapId(Path path) {
        Matcher matcher = MAP_FILE_PATTERN.matcher(path.getFileName().toString());
        if (!matcher.matches()) {
            return -1;
        }
        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private static MapCatalogMapInfo mapcatalog$fromMapState(int mapId, MapState mapState) {
        List<MapCatalogBanner> banners = new ArrayList<>();
        for (MapBannerMarker banner : mapState.getBanners()) {
            banners.add(new MapCatalogBanner(
                    banner.pos().getX(),
                    banner.pos().getZ(),
                    banner.color(),
                    banner.name()
            ));
        }
        banners.sort(Comparator.comparingInt(MapCatalogBanner::worldX)
                .thenComparingInt(MapCatalogBanner::worldZ)
                .thenComparing(banner -> banner.name().map(Object::toString).orElse("")));

        return new MapCatalogMapInfo(
                mapId,
                mapState.dimension.getValue(),
                mapState.centerX,
                mapState.centerZ,
                mapState.scale,
                mapState.locked,
                new MapCatalogClassification(mapState.hasExplorationMapDecoration(), banners)
        );
    }

    private static void mapcatalog$updateMapState(int mapId, MapState mapState, boolean markChanged) {
        MapCatalogMapInfo mapInfo = mapcatalog$fromMapState(mapId, mapState);
        MapCatalogMapInfo previous = MAPS.put(mapId, mapInfo);
        MAP_IDS.entrySet().removeIf(entry -> entry.getValue().equals(mapId) && entry.getKey() != mapState);
        MAP_IDS.put(mapState, mapId);
        highestMapId = Math.max(highestMapId, mapId);

        if (!MAP_REVISIONS.containsKey(mapId)) {
            MAP_REVISIONS.put(mapId, catalogRevision);
        }
        if (markChanged && !mapInfo.equals(previous)) {
            catalogRevision++;
            MAP_REVISIONS.put(mapId, catalogRevision);
        }
    }

    private static void mapcatalog$handleRequest(
            MapCatalogSyncRequestC2S payload,
            ServerPlayNetworking.Context context
    ) {
        ServerPlayerEntity player = context.player();
        if (!ServerPlayNetworking.canSend(player, MapCatalogSyncStartS2C.ID)) {
            return;
        }

        MapCatalogConfig currentConfig = mapcatalog$config();
        if (payload.protocolVersion() != MapCatalogPacketCodecs.PROTOCOL_VERSION
                || !mapcatalog$isSyncEnabled(currentConfig)) {
            mapcatalog$sendDenied(player);
            return;
        }
        if (!currentConfig.mapcatalog$allowAllPlayers() && !player.hasPermissionLevel(2)) {
            mapcatalog$sendDenied(player);
            return;
        }

        int currentTick = context.server().getTicks();
        Integer lastRequestTick = LAST_REQUEST_TICKS.get(player.getUuid());
        if (lastRequestTick != null
                && currentTick - lastRequestTick < currentConfig.mapcatalog$requestCooldownTicks()) {
            mapcatalog$sendDenied(player);
            return;
        }
        LAST_REQUEST_TICKS.put(player.getUuid(), currentTick);

        boolean fullSync = payload.forceFullSync() || !worldSessionId.equals(payload.worldSessionId());
        Identifier playerDimension = player.getServerWorld().getRegistryKey().getValue();
        List<MapCatalogMapInfo> maps = mapcatalog$snapshotMaps(
                playerDimension,
                currentConfig.mapcatalog$allowAllDimensions());
        if (!fullSync) {
            maps = maps.stream()
                    .filter(map -> MAP_REVISIONS.getOrDefault(map.mapId(), 0L)
                            > payload.knownCatalogRevision())
                    .toList();
        }

        MapCatalogSyncMode mode = fullSync
                ? MapCatalogSyncMode.FULL
                : (maps.isEmpty() ? MapCatalogSyncMode.NO_CHANGE : MapCatalogSyncMode.DELTA);
        ServerPlayNetworking.send(player, new MapCatalogSyncStartS2C(
                mode,
                worldSessionId,
                catalogRevision,
                highestMapId,
                maps.size()
        ));

        for (int start = 0; start < maps.size(); start += currentConfig.mapcatalog$maxMapsPerSync()) {
            int end = Math.min(start + currentConfig.mapcatalog$maxMapsPerSync(), maps.size());
            ServerPlayNetworking.send(player, new MapCatalogSyncBatchS2C(maps.subList(start, end)));
        }
        ServerPlayNetworking.send(player, new MapCatalogSyncEndS2C(
                worldSessionId, catalogRevision, highestMapId));
    }

    private static void mapcatalog$sendDenied(ServerPlayerEntity player) {
        if (ServerPlayNetworking.canSend(player, MapCatalogSyncStartS2C.ID)) {
            ServerPlayNetworking.send(player, new MapCatalogSyncStartS2C(
                    MapCatalogSyncMode.DENIED,
                    worldSessionId,
                    catalogRevision,
                    highestMapId,
                    0
            ));
        }
    }

    private static List<MapCatalogMapInfo> mapcatalog$snapshotMaps(
            Identifier playerDimension,
            boolean allowAllDimensions
    ) {
        return MAPS.values().stream()
                .filter(map -> allowAllDimensions || map.dimension().equals(playerDimension))
                .sorted(Comparator.comparingInt(MapCatalogMapInfo::mapId))
                .toList();
    }

    public static MapCatalogConfig mapcatalog$config() {
        return config;
    }

    public static boolean mapcatalog$isSyncEnabled() {
        return mapcatalog$isSyncEnabled(config);
    }

    public static boolean mapcatalog$hasCarpetJft() {
        return FabricLoader.getInstance().isModLoaded(CARPET_JFT_MOD_ID);
    }

    public static boolean mapcatalog$isJftMapSyncProtocolEnabled() {
        if (!mapcatalog$hasCarpetJft()) {
            return true;
        }

        if (!carpetJftEntrypointLookupAttempted) {
            carpetJftEntrypointLookupAttempted = true;
            try {
                List<EntrypointContainer<BooleanSupplier>> entrypoints = FabricLoader.getInstance()
                        .getEntrypointContainers(CARPET_JFT_ENTRYPOINT, BooleanSupplier.class);
                if (!entrypoints.isEmpty()) {
                    carpetJftMapSyncRule = entrypoints.get(0).getEntrypoint();
                } else {
                    MapCatalogServer.LOGGER.warn(
                            "检测到 Carpet JFT Addition，但没有找到 MapCatalog 规则 EntryPoint；按启用处理"
                    );
                }
            } catch (RuntimeException exception) {
                MapCatalogServer.LOGGER.warn(
                        "无法加载 Carpet JFT Addition 的 MapCatalog 规则 EntryPoint；按启用处理",
                        exception
                );
            }
        }

        if (carpetJftMapSyncRule == null) {
            return true;
        }

        try {
            return carpetJftMapSyncRule.getAsBoolean();
        } catch (RuntimeException exception) {
            MapCatalogServer.LOGGER.warn(
                    "读取 Carpet JFT Addition 的 MapCatalog 规则失败；按启用处理",
                    exception
            );
            return true;
        }
    }

    public static void mapcatalog$reloadConfig() {
        config = MapCatalogConfig.load();
    }

    public static UUID mapcatalog$worldSessionId() {
        return worldSessionId;
    }

    public static int mapcatalog$mapCount() {
        return MAPS.size();
    }

    public static int mapcatalog$highestMapId() {
        return highestMapId;
    }

    private static boolean mapcatalog$isSyncEnabled(MapCatalogConfig currentConfig) {
        return currentConfig.mapcatalog$mapSyncEnabled()
                && mapcatalog$isJftMapSyncProtocolEnabled();
    }

    private record MapFile(Path path, int mapId) {
    }

    private MapCatalogServerService() {
    }
}
