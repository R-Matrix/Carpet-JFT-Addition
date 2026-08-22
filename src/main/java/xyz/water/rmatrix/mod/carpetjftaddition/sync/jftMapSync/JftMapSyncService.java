package xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTAddition;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;
import xyz.water.rmatrix.mod.carpetjftaddition.command.JftMapSyncCommands;
import xyz.water.rmatrix.mod.carpetjftaddition.configs.JftMapSyncConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class JftMapSyncService {
    public static final int PROTOCOL_VERSION = 1;

    private static final Pattern jft$MAP_FILE_PATTERN = Pattern.compile("map_(\\d+)\\.dat");
    private static final Map<Integer, ServerMapInfo> jft$maps = new HashMap<>();
    private static final Map<UUID, Integer> jft$lastRequestTicks = new HashMap<>();

    private static MinecraftServer jft$server;
    private static UUID jft$worldSessionId = new UUID(0L, 0L);
    private static JftMapSyncConfig jft$config = new JftMapSyncConfig();
    private static int jft$highestMapId = -1;
    private static boolean jft$initialized;

    public static void jft$init() {
        if (jft$initialized) {
            return;
        }
        jft$initialized = true;

        PayloadTypeRegistry.playC2S().register(MapSyncRequestC2S.ID, MapSyncRequestC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(MapSyncStartS2C.ID, MapSyncStartS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(MapSyncBatchS2C.ID, MapSyncBatchS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(MapSyncEndS2C.ID, MapSyncEndS2C.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(MapSyncRequestC2S.ID, JftMapSyncService::jft$handleRequest);

        ServerLifecycleEvents.SERVER_STARTED.register(JftMapSyncService::jft$onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(JftMapSyncService::jft$onServerStopping);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                JftMapSyncCommands.jft$register(dispatcher));
    }

    public static void jft$onServerStarted(MinecraftServer server) {
        jft$server = server;
        jft$worldSessionId = UUID.randomUUID();
        jft$config = JftMapSyncConfig.jft$load();
        jft$lastRequestTicks.clear();
        jft$scanMaps(server);
        CarpetJFTAddition.LOGGER.info("JFTM 地图同步已初始化: {} 张地图, 最大编号 {}, session {}",
                jft$maps.size(), jft$highestMapId, jft$worldSessionId);
    }

    public static void jft$onServerStopping(MinecraftServer server) {
        if (jft$server != server) {
            return;
        }
        jft$maps.clear();
        jft$lastRequestTicks.clear();
        jft$highestMapId = -1;
        jft$server = null;
        jft$worldSessionId = new UUID(0L, 0L);
    }

    public static void jft$onMapStatePut(ServerWorld world, MapIdComponent mapId, MapState mapState) {
        if (jft$server == null || world.getServer() != jft$server || mapId == null || mapState == null) {
            return;
        }
        ServerMapInfo mapInfo = jft$fromMapState(mapId.id(), mapState);
        jft$maps.put(mapInfo.mapId(), mapInfo);
        jft$highestMapId = Math.max(jft$highestMapId, mapInfo.mapId());
    }

    private static void jft$scanMaps(MinecraftServer server) {
        jft$maps.clear();
        jft$highestMapId = -1;
        Path dataDirectory = server.getSavePath(WorldSavePath.ROOT).resolve("data");
        if (!Files.isDirectory(dataDirectory)) {
            return;
        }

        ServerWorld overworld = server.getOverworld();
        try (Stream<Path> paths = Files.list(dataDirectory)) {
            paths.filter(Files::isRegularFile)
                    .map(path -> new MapFile(path, jft$parseMapId(path)))
                    .filter(mapFile -> mapFile.mapId() >= 0)
                    .sorted(Comparator.comparingInt(MapFile::mapId))
                    .forEach(mapFile -> {
                        try {
                            MapState mapState = jft$getMapState(overworld, mapFile.mapId());
                            if (mapState != null) {
                                ServerMapInfo mapInfo = jft$fromMapState(mapFile.mapId(), mapState);
                                jft$maps.put(mapInfo.mapId(), mapInfo);
                                jft$highestMapId = Math.max(jft$highestMapId, mapInfo.mapId());
                            }
                        } catch (RuntimeException exception) {
                            CarpetJFTAddition.LOGGER.warn("无法加载地图文件 {}", mapFile.path(), exception);
                        }
                    });
        } catch (IOException exception) {
            CarpetJFTAddition.LOGGER.warn("无法扫描地图目录 {}", dataDirectory, exception);
        }
    }

    private static MapState jft$getMapState(ServerWorld world, int mapId) {
        //#if MC >= 12105
        //$$ return world.getPersistentStateManager().get(
        //$$         MapState.createStateType(new MapIdComponent(mapId)));
        //#else
        return world.getPersistentStateManager().get(
                MapState.getPersistentStateType(), "map_" + mapId);
        //#endif
    }

    private static int jft$parseMapId(Path path) {
        Matcher matcher = jft$MAP_FILE_PATTERN.matcher(path.getFileName().toString());
        if (!matcher.matches()) {
            return -1;
        }
        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private static ServerMapInfo jft$fromMapState(int mapId, MapState mapState) {
        List<BannerMarker> banners = new ArrayList<>();
        for (MapBannerMarker banner : mapState.getBanners()) {
            banners.add(new BannerMarker(
                    banner.pos().getX(),
                    banner.pos().getZ(),
                    banner.color(),
                    banner.name()
            ));
        }
        banners.sort(Comparator.comparingInt(BannerMarker::worldX)
                .thenComparingInt(BannerMarker::worldZ)
                .thenComparing(banner -> banner.name().map(Object::toString).orElse("")));

        return new ServerMapInfo(
                mapId,
                mapState.dimension.getValue(),
                mapState.centerX,
                mapState.centerZ,
                mapState.scale,
                mapState.locked,
                new MapClassification(mapState.hasExplorationMapDecoration(), banners)
        );
    }

    private static void jft$handleRequest(MapSyncRequestC2S payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        if (!ServerPlayNetworking.canSend(player, MapSyncStartS2C.ID)) {
            return;
        }

        if (payload.protocolVersion() != PROTOCOL_VERSION) {
            jft$sendDenied(player);
            return;
        }
        if (!CarpetJFTSettings.jftMapSyncProtocol) {
            jft$sendDenied(player);
            return;
        }

        JftMapSyncConfig config = jft$config();
        if (!config.jft$allowAllPlayers() && !player.hasPermissionLevel(2)) {
            jft$sendDenied(player);
            return;
        }

        int currentTick = context.server().getTicks();
        Integer lastRequestTick = jft$lastRequestTicks.get(player.getUuid());
        if (lastRequestTick != null
                && currentTick - lastRequestTick < config.jft$requestCooldownTicks()) {
            jft$sendDenied(player);
            return;
        }
        jft$lastRequestTicks.put(player.getUuid(), currentTick);

        boolean fullSync = payload.forceFullSync() || !jft$worldSessionId.equals(payload.worldSessionId());
        Identifier playerDimension = player.getServerWorld().getRegistryKey().getValue();
        List<ServerMapInfo> maps = jft$snapshotMaps(playerDimension, config.jft$allowAllDimensions());
        if (!fullSync) {
            maps = maps.stream()
                    .filter(map -> map.mapId() > payload.knownMaxMapId())
                    .toList();
        }

        SyncMode mode = fullSync ? SyncMode.FULL : (maps.isEmpty() ? SyncMode.NO_CHANGE : SyncMode.DELTA);
        ServerPlayNetworking.send(player, new MapSyncStartS2C(
                mode,
                jft$worldSessionId,
                jft$highestMapId,
                maps.size()
        ));

        for (int start = 0; start < maps.size(); start += config.jft$maxMapsPerSync()) {
            int end = Math.min(start + config.jft$maxMapsPerSync(), maps.size());
            ServerPlayNetworking.send(player, new MapSyncBatchS2C(maps.subList(start, end)));
        }
        ServerPlayNetworking.send(player, new MapSyncEndS2C(jft$worldSessionId, jft$highestMapId));
    }

    private static void jft$sendDenied(ServerPlayerEntity player) {
        if (ServerPlayNetworking.canSend(player, MapSyncStartS2C.ID)) {
            ServerPlayNetworking.send(player, new MapSyncStartS2C(
                    SyncMode.DENIED,
                    jft$worldSessionId,
                    jft$highestMapId,
                    0
            ));
        }
    }

    private static List<ServerMapInfo> jft$snapshotMaps(Identifier playerDimension, boolean allowAllDimensions) {
        return jft$maps.values().stream()
                .filter(map -> allowAllDimensions || map.dimension().equals(playerDimension))
                .sorted(Comparator.comparingInt(ServerMapInfo::mapId))
                .toList();
    }

    public static JftMapSyncConfig jft$config() {
        return jft$config;
    }

    public static void jft$reloadConfig() {
        jft$config = JftMapSyncConfig.jft$load();
    }

    public static UUID jft$worldSessionId() {
        return jft$worldSessionId;
    }

    public static int jft$mapCount() {
        return jft$maps.size();
    }

    public static int jft$highestMapId() {
        return jft$highestMapId;
    }

    private record MapFile(Path path, int mapId) {
    }

    private JftMapSyncService() {
    }
}
