package xyz.water.rmatrix.mapcatalog.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;
import xyz.water.rmatrix.mapcatalog.MapCatalogServer;
import xyz.water.rmatrix.mapcatalog.protocol.mapcatalog.MapCatalogPacketCodecs;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class MapCatalogConfig {
    public static final boolean DEFAULT_MAP_SYNC_ENABLED = true;
    public static final int DEFAULT_MAX_MAPS_PER_SYNC = 128;
    public static final int DEFAULT_REQUEST_COOLDOWN_TICKS = 40;
    public static final int MAX_ALLOWED_BATCH_SIZE = MapCatalogPacketCodecs.MAX_BATCH_ENTRIES;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir()
            .resolve("carpet-jft-addition")
            .resolve("mapcatalog.json");

    private boolean mapSyncEnabled = DEFAULT_MAP_SYNC_ENABLED;
    private boolean allowAllPlayers = true;
    private int maxMapsPerSync = DEFAULT_MAX_MAPS_PER_SYNC;
    private boolean allowAllDimensions = true;
    private int requestCooldownTicks = DEFAULT_REQUEST_COOLDOWN_TICKS;

    public static MapCatalogConfig load() {
        if (!Files.isRegularFile(PATH)) {
            MapCatalogConfig config = new MapCatalogConfig();
            config.save();
            return config;
        }

        try (Reader reader = Files.newBufferedReader(PATH, StandardCharsets.UTF_8)) {
            MapCatalogConfig config = GSON.fromJson(reader, MapCatalogConfig.class);
            if (config == null) {
                config = new MapCatalogConfig();
            }
            if (config.validate()) {
                config.save();
            }
            return config;
        } catch (IOException | JsonParseException | IllegalStateException exception) {
            MapCatalogServer.LOGGER.warn("无法读取 MapCatalog 配置文件 {}, 将使用默认配置", PATH, exception);
            MapCatalogConfig config = new MapCatalogConfig();
            config.save();
            return config;
        }
    }

    public void save() {
        try {
            Files.createDirectories(PATH.getParent());
            Path temporaryPath = PATH.resolveSibling(PATH.getFileName() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(temporaryPath, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }
            try {
                Files.move(temporaryPath, PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryPath, PATH, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            MapCatalogServer.LOGGER.warn("无法写入 MapCatalog 配置文件 {}", PATH, exception);
        }
    }

    private boolean validate() {
        boolean changed = false;
        if (maxMapsPerSync < 1 || maxMapsPerSync > MAX_ALLOWED_BATCH_SIZE) {
            maxMapsPerSync = DEFAULT_MAX_MAPS_PER_SYNC;
            changed = true;
        }
        if (requestCooldownTicks < 0 || requestCooldownTicks > 20 * 60 * 60) {
            requestCooldownTicks = DEFAULT_REQUEST_COOLDOWN_TICKS;
            changed = true;
        }
        return changed;
    }

    public boolean mapcatalog$mapSyncEnabled() {
        return mapSyncEnabled;
    }

    public boolean mapcatalog$allowAllPlayers() {
        return allowAllPlayers;
    }

    public int mapcatalog$maxMapsPerSync() {
        return maxMapsPerSync;
    }

    public boolean mapcatalog$allowAllDimensions() {
        return allowAllDimensions;
    }

    public int mapcatalog$requestCooldownTicks() {
        return requestCooldownTicks;
    }

    public void mapcatalog$setMapSyncEnabled(boolean value) {
        mapSyncEnabled = value;
    }

    public void mapcatalog$setAllowAllPlayers(boolean value) {
        allowAllPlayers = value;
    }

    public void mapcatalog$setMaxMapsPerSync(int value) {
        if (value < 1 || value > MAX_ALLOWED_BATCH_SIZE) {
            throw new IllegalArgumentException("maxMapsPerSync 必须在 1 到 " + MAX_ALLOWED_BATCH_SIZE + " 之间");
        }
        maxMapsPerSync = value;
    }

    public void mapcatalog$setAllowAllDimensions(boolean value) {
        allowAllDimensions = value;
    }

    public void mapcatalog$setRequestCooldownTicks(int value) {
        if (value < 0 || value > 20 * 60 * 60) {
            throw new IllegalArgumentException("requestCooldownTicks 必须在 0 到 72000 之间");
        }
        requestCooldownTicks = value;
    }

    public static Path path() {
        return PATH;
    }
}
