package xyz.water.rmatrix.mapcatalog.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import xyz.water.rmatrix.mapcatalog.config.MapCatalogConfig;
import xyz.water.rmatrix.mapcatalog.protocol.mapcatalog.MapCatalogPacketCodecs;
import xyz.water.rmatrix.mapcatalog.server.MapCatalogServerService;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class MapCatalogCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("mapcatalog")
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("status").executes(context -> status(context.getSource())))
                .then(literal("reload").executes(context -> reload(context.getSource())))
                .then(literal("get")
                        .then(literal("mapSyncEnabled").executes(context -> getMapSyncEnabled(context.getSource())))
                        .then(literal("allowAllPlayers").executes(context -> getAllowAllPlayers(context.getSource())))
                        .then(literal("maxMapsPerSync").executes(context -> getMaxMapsPerSync(context.getSource())))
                        .then(literal("allowAllDimensions").executes(context -> getAllowAllDimensions(context.getSource())))
                        .then(literal("requestCooldownTicks").executes(context -> getRequestCooldownTicks(context.getSource()))))
                .then(literal("set")
                        .then(literal("mapSyncEnabled")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> setMapSyncEnabled(
                                                context.getSource(), BoolArgumentType.getBool(context, "value")))))
                        .then(literal("allowAllPlayers")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> setAllowAllPlayers(
                                                context.getSource(), BoolArgumentType.getBool(context, "value")))))
                        .then(literal("maxMapsPerSync")
                                .then(argument("value", IntegerArgumentType.integer(1, MapCatalogPacketCodecs.MAX_BATCH_ENTRIES))
                                        .executes(context -> setMaxMapsPerSync(
                                                context.getSource(), IntegerArgumentType.getInteger(context, "value")))))
                        .then(literal("allowAllDimensions")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> setAllowAllDimensions(
                                                context.getSource(), BoolArgumentType.getBool(context, "value")))))
                        .then(literal("requestCooldownTicks")
                                .then(argument("value", IntegerArgumentType.integer(0, 20 * 60 * 60))
                                        .executes(context -> setRequestCooldownTicks(
                                                context.getSource(), IntegerArgumentType.getInteger(context, "value")))))));
    }

    private static int status(ServerCommandSource source) {
        MapCatalogConfig config = MapCatalogServerService.mapcatalog$config();
        boolean hasCarpetJft = MapCatalogServerService.mapcatalog$hasCarpetJft();
        String jftStatus = hasCarpetJft
                ? ", jftMapSyncProtocol=" + MapCatalogServerService.mapcatalog$isJftMapSyncProtocolEnabled()
                : "";
        source.sendFeedback(() -> Text.literal("MapCatalog: "
                + (MapCatalogServerService.mapcatalog$isSyncEnabled() ? "启用" : "关闭")
                + " (mapSyncEnabled=" + config.mapcatalog$mapSyncEnabled()
                + jftStatus + ")"
                + ", 地图数量=" + MapCatalogServerService.mapcatalog$mapCount()
                + ", 最大编号=" + MapCatalogServerService.mapcatalog$highestMapId()
                + ", session=" + MapCatalogServerService.mapcatalog$worldSessionId()
                + ", allowAllPlayers=" + config.mapcatalog$allowAllPlayers()
                + ", maxMapsPerSync=" + config.mapcatalog$maxMapsPerSync()
                + ", allowAllDimensions=" + config.mapcatalog$allowAllDimensions()
                + ", requestCooldownTicks=" + config.mapcatalog$requestCooldownTicks()), false);
        return 1;
    }

    private static int reload(ServerCommandSource source) {
        MapCatalogServerService.mapcatalog$reloadConfig();
        source.sendFeedback(() -> Text.literal("已重新加载 MapCatalog 配置: " + MapCatalogConfig.path()), true);
        return 1;
    }

    private static int getMapSyncEnabled(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("mapSyncEnabled=" + MapCatalogServerService.mapcatalog$config().mapcatalog$mapSyncEnabled()), false);
        return 1;
    }

    private static int getAllowAllPlayers(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("allowAllPlayers=" + MapCatalogServerService.mapcatalog$config().mapcatalog$allowAllPlayers()), false);
        return 1;
    }

    private static int getMaxMapsPerSync(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("maxMapsPerSync=" + MapCatalogServerService.mapcatalog$config().mapcatalog$maxMapsPerSync()), false);
        return 1;
    }

    private static int getAllowAllDimensions(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("allowAllDimensions=" + MapCatalogServerService.mapcatalog$config().mapcatalog$allowAllDimensions()), false);
        return 1;
    }

    private static int getRequestCooldownTicks(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("requestCooldownTicks=" + MapCatalogServerService.mapcatalog$config().mapcatalog$requestCooldownTicks()), false);
        return 1;
    }

    private static int setMapSyncEnabled(ServerCommandSource source, boolean value) {
        MapCatalogConfig config = MapCatalogServerService.mapcatalog$config();
        config.mapcatalog$setMapSyncEnabled(value);
        config.save();
        source.sendFeedback(() -> Text.literal("已设置 mapSyncEnabled=" + value), true);
        return 1;
    }

    private static int setAllowAllPlayers(ServerCommandSource source, boolean value) {
        MapCatalogConfig config = MapCatalogServerService.mapcatalog$config();
        config.mapcatalog$setAllowAllPlayers(value);
        config.save();
        source.sendFeedback(() -> Text.literal("已设置 allowAllPlayers=" + value), true);
        return 1;
    }

    private static int setMaxMapsPerSync(ServerCommandSource source, int value) {
        MapCatalogConfig config = MapCatalogServerService.mapcatalog$config();
        config.mapcatalog$setMaxMapsPerSync(value);
        config.save();
        source.sendFeedback(() -> Text.literal("已设置 maxMapsPerSync=" + value), true);
        return 1;
    }

    private static int setAllowAllDimensions(ServerCommandSource source, boolean value) {
        MapCatalogConfig config = MapCatalogServerService.mapcatalog$config();
        config.mapcatalog$setAllowAllDimensions(value);
        config.save();
        source.sendFeedback(() -> Text.literal("已设置 allowAllDimensions=" + value), true);
        return 1;
    }

    private static int setRequestCooldownTicks(ServerCommandSource source, int value) {
        MapCatalogConfig config = MapCatalogServerService.mapcatalog$config();
        config.mapcatalog$setRequestCooldownTicks(value);
        config.save();
        source.sendFeedback(() -> Text.literal("已设置 requestCooldownTicks=" + value), true);
        return 1;
    }

    private MapCatalogCommands() {
    }
}
