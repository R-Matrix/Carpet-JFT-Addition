package xyz.water.rmatrix.mod.carpetjftaddition.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTAddition;
import xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync.JftMapSyncPacketCodecs;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class JftMapSyncConfig {
    public static final int DEFAULT_MAX_MAPS_PER_SYNC = 128;
    public static final int DEFAULT_REQUEST_COOLDOWN_TICKS = 40;
    public static final int MAX_ALLOWED_BATCH_SIZE = JftMapSyncPacketCodecs.jft$MAX_BATCH_ENTRIES;

    private static final Gson jft$GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path jft$PATH = FabricLoader.getInstance().getConfigDir()
            .resolve("carpet-jft-addition")
            .resolve("carpetjftaddition-jftm.json");

    private boolean allowAllPlayers = true;
    private int maxMapsPerSync = DEFAULT_MAX_MAPS_PER_SYNC;
    private boolean allowAllDimensions = true;
    private int requestCooldownTicks = DEFAULT_REQUEST_COOLDOWN_TICKS;

    public static JftMapSyncConfig jft$load() {
        if (!Files.isRegularFile(jft$PATH)) {
            JftMapSyncConfig config = new JftMapSyncConfig();
            config.jft$save();
            return config;
        }

        try (Reader reader = Files.newBufferedReader(jft$PATH, StandardCharsets.UTF_8)) {
            JftMapSyncConfig config = jft$GSON.fromJson(reader, JftMapSyncConfig.class);
            if (config == null) {
                config = new JftMapSyncConfig();
            }
            if (config.jft$validate()) {
                config.jft$save();
            }
            return config;
        } catch (IOException | JsonParseException | IllegalStateException exception) {
            CarpetJFTAddition.LOGGER.warn("无法读取 JFTM 配置文件 {}, 将使用默认配置", jft$PATH, exception);
            JftMapSyncConfig config = new JftMapSyncConfig();
            config.jft$save();
            return config;
        }
    }

    public void jft$save() {
        try {
            Files.createDirectories(jft$PATH.getParent());
            Path temporaryPath = jft$PATH.resolveSibling(jft$PATH.getFileName() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(temporaryPath, StandardCharsets.UTF_8)) {
                jft$GSON.toJson(this, writer);
            }
            try {
                Files.move(temporaryPath, jft$PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryPath, jft$PATH, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            CarpetJFTAddition.LOGGER.warn("无法写入 JFTM 配置文件 {}", jft$PATH, exception);
        }
    }

    private boolean jft$validate() {
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

    public boolean jft$allowAllPlayers() {
        return allowAllPlayers;
    }

    public int jft$maxMapsPerSync() {
        return maxMapsPerSync;
    }

    public boolean jft$allowAllDimensions() {
        return allowAllDimensions;
    }

    public int jft$requestCooldownTicks() {
        return requestCooldownTicks;
    }

    public void jft$setAllowAllPlayers(boolean value) {
        allowAllPlayers = value;
    }

    public void jft$setMaxMapsPerSync(int value) {
        if (value < 1 || value > MAX_ALLOWED_BATCH_SIZE) {
            throw new IllegalArgumentException("maxMapsPerSync 必须在 1 到 " + MAX_ALLOWED_BATCH_SIZE + " 之间");
        }
        maxMapsPerSync = value;
    }

    public void jft$setAllowAllDimensions(boolean value) {
        allowAllDimensions = value;
    }

    public void jft$setRequestCooldownTicks(int value) {
        if (value < 0 || value > 20 * 60 * 60) {
            throw new IllegalArgumentException("requestCooldownTicks 必须在 0 到 72000 之间");
        }
        requestCooldownTicks = value;
    }

    public static Path jft$path() {
        return jft$PATH;
    }
}
