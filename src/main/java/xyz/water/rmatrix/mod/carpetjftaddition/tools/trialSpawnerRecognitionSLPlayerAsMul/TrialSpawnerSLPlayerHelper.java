package xyz.water.rmatrix.mod.carpetjftaddition.tools.trialSpawnerRecognitionSLPlayerAsMul;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

/**
 * 试炼刷怪笼 SL 假人识别
 */
public final class TrialSpawnerSLPlayerHelper {

    /** 被识别为假人的玩家名前缀 */
    public static final String NAME_PREFIX = "bot_sljr";

    private TrialSpawnerSLPlayerHelper() {}

    /**
     * 判断某个在线玩家是否为 SL 假人
     */
    public static boolean isSLPlayer(ServerWorld world, UUID uuid) {
        if (world == null || uuid == null) {
            return false;
        }
        ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(uuid);
        //#if MC >= 12110
        //$$ return player != null && player.getGameProfile().name().startsWith(NAME_PREFIX);
        //#else
        return player != null && player.getGameProfile().getName().startsWith(NAME_PREFIX);
        //#endif
    }
}
