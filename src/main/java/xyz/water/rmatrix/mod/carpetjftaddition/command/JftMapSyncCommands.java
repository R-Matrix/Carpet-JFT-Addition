//#if MC >= 12104
package xyz.water.rmatrix.mod.carpetjftaddition.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import xyz.water.rmatrix.mod.carpetjftaddition.configs.JftMapSyncConfig;
import xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync.JftMapSyncService;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class JftMapSyncCommands {
    public static void jft$register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("jft-sync")
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("status").executes(context -> jft$status(context.getSource())))
                .then(literal("reload").executes(context -> jft$reload(context.getSource())))
                .then(literal("get")
                        .then(literal("allowAllPlayers").executes(context -> jft$getAllowAllPlayers(context.getSource())))
                        .then(literal("maxMapsPerSync").executes(context -> jft$getMaxMapsPerSync(context.getSource())))
                        .then(literal("allowAllDimensions").executes(context -> jft$getAllowAllDimensions(context.getSource())))
                        .then(literal("requestCooldownTicks").executes(context -> jft$getRequestCooldownTicks(context.getSource()))))
                .then(literal("set")
                        .then(literal("allowAllPlayers")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> jft$setAllowAllPlayers(
                                                context.getSource(), BoolArgumentType.getBool(context, "value")))))
                        .then(literal("maxMapsPerSync")
                                .then(argument("value", IntegerArgumentType.integer(1, JftMapSyncConfig.MAX_ALLOWED_BATCH_SIZE))
                                        .executes(context -> jft$setMaxMapsPerSync(
                                                context.getSource(), IntegerArgumentType.getInteger(context, "value")))))
                        .then(literal("allowAllDimensions")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> jft$setAllowAllDimensions(
                                                context.getSource(), BoolArgumentType.getBool(context, "value")))))
                        .then(literal("requestCooldownTicks")
                                .then(argument("value", IntegerArgumentType.integer(0, 20 * 60 * 60))
                                        .executes(context -> jft$setRequestCooldownTicks(
                                                context.getSource(), IntegerArgumentType.getInteger(context, "value")))))));
    }

    private static int jft$status(ServerCommandSource source) {
        JftMapSyncConfig config = JftMapSyncService.jft$config();
        source.sendFeedback(() -> Text.literal("JFTM-同步协议: "
                + (xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings.jftMapSyncProtocol ? "启用" : "关闭")
                + ", 地图数量=" + JftMapSyncService.jft$mapCount()
                + ", 最大编号=" + JftMapSyncService.jft$highestMapId()
                + ", session=" + JftMapSyncService.jft$worldSessionId()
                + ", allowAllPlayers=" + config.jft$allowAllPlayers()
                + ", maxMapsPerSync=" + config.jft$maxMapsPerSync()
                + ", allowAllDimensions=" + config.jft$allowAllDimensions()
                + ", requestCooldownTicks=" + config.jft$requestCooldownTicks()), false);
        return 1;
    }

    private static int jft$reload(ServerCommandSource source) {
        JftMapSyncService.jft$reloadConfig();
        source.sendFeedback(() -> Text.literal("已重新加载 JFTM 配置: " + JftMapSyncConfig.jft$path()), true);
        return 1;
    }

    private static int jft$getAllowAllPlayers(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("allowAllPlayers=" + JftMapSyncService.jft$config().jft$allowAllPlayers()), false);
        return 1;
    }

    private static int jft$getMaxMapsPerSync(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("maxMapsPerSync=" + JftMapSyncService.jft$config().jft$maxMapsPerSync()), false);
        return 1;
    }

    private static int jft$getAllowAllDimensions(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("allowAllDimensions=" + JftMapSyncService.jft$config().jft$allowAllDimensions()), false);
        return 1;
    }

    private static int jft$getRequestCooldownTicks(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("requestCooldownTicks=" + JftMapSyncService.jft$config().jft$requestCooldownTicks()), false);
        return 1;
    }

    private static int jft$setAllowAllPlayers(ServerCommandSource source, boolean value) {
        JftMapSyncConfig config = JftMapSyncService.jft$config();
        config.jft$setAllowAllPlayers(value);
        config.jft$save();
        source.sendFeedback(() -> Text.literal("已设置 allowAllPlayers=" + value), true);
        return 1;
    }

    private static int jft$setMaxMapsPerSync(ServerCommandSource source, int value) {
        JftMapSyncConfig config = JftMapSyncService.jft$config();
        config.jft$setMaxMapsPerSync(value);
        config.jft$save();
        source.sendFeedback(() -> Text.literal("已设置 maxMapsPerSync=" + value), true);
        return 1;
    }

    private static int jft$setAllowAllDimensions(ServerCommandSource source, boolean value) {
        JftMapSyncConfig config = JftMapSyncService.jft$config();
        config.jft$setAllowAllDimensions(value);
        config.jft$save();
        source.sendFeedback(() -> Text.literal("已设置 allowAllDimensions=" + value), true);
        return 1;
    }

    private static int jft$setRequestCooldownTicks(ServerCommandSource source, int value) {
        JftMapSyncConfig config = JftMapSyncService.jft$config();
        config.jft$setRequestCooldownTicks(value);
        config.jft$save();
        source.sendFeedback(() -> Text.literal("已设置 requestCooldownTicks=" + value), true);
        return 1;
    }

    private JftMapSyncCommands() {
    }
}
//#endif
