package com.jft.mixin.rules.trialSpawnerRecognitionSLPlayerAsMul;

import com.jft.CarpetJFTSettings;
import com.jft.toolsMager.trialSpawnerRecognitionSLPlayerAsMul.TrialSpawnerSLPlayerHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.enums.TrialSpawnerState;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

/**
 * 奖励发放侧: 原版 EJECTING_REWARD 阶段每 30 tick 弹 1 份奖励并移除 1 个已登记玩家。
 * 这里让 SL 假人那一轮直接弹出“倍数”份奖励, 使奖励总数与加权后的战斗人数一致。
 */
@Mixin(TrialSpawnerState.class)
public abstract class TrialSpawnerStateMixin {

    @WrapOperation(
            method = "method_55211",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/spawner/TrialSpawnerLogic;ejectLootTable(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/registry/RegistryKey;)V")
    )
    private static void jft$ejectWeightedRewards(TrialSpawnerLogic instance, ServerWorld world,
                                                 BlockPos pos, RegistryKey<LootTable> lootTable,
                                                 Operation<Void> original) {
        int multiplier = CarpetJFTSettings.trialSpawnerRecognitionSLPlayerAsMul;
        if (multiplier == 1) {
            original.call(instance, world, pos, lootTable);
            return;
        }

        TrialSpawnerData data = instance.getData();
        UUID nextPlayer = ((TrialSpawnerDataAccessor) data).getPlayers().iterator().next();
        int ejections = TrialSpawnerSLPlayerHelper.isSLPlayer(world, nextPlayer) ? Math.max(0, multiplier) : 1;
        for (int i = 0; i < ejections; i++) {
            original.call(instance, world, pos, lootTable);
        }
    }
}
