package com.jft.mixin.rules.trialSpawnerRecognitionSLPlayerAsMul;

import com.jft.CarpetJFTSettings;
import com.jft.toolsMager.trialSpawnerRecognitionSLPlayerAsMul.TrialSpawnerSLPlayerHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.UUID;

@Mixin(TrialSpawnerData.class)
public abstract class TrialSpawnerBlockEntityMixin {

    @Unique
    private ServerWorld jft$lastServerWorld;

    @Final @Shadow
    protected Set<UUID> players;

    @Inject(method = "updatePlayers", at = @At("HEAD"))
    private void jft$captureServerWorld(ServerWorld world, BlockPos pos, TrialSpawnerLogic logic, CallbackInfo ci) {
        this.jft$lastServerWorld = world;
    }


    @ModifyReturnValue(method = "getAdditionalPlayers", at = @At("RETURN"))
    private int jft$weightedAdditionalPlayers(int original) {
        int multiplier = CarpetJFTSettings.trialSpawnerRecognitionSLPlayerAsMul - 1;
        if (multiplier <= 0 || this.jft$lastServerWorld == null) {
            return original;
        }
        int weightedCount = 0;
        for (UUID uuid : this.players) {
            if (TrialSpawnerSLPlayerHelper.isSLPlayer(this.jft$lastServerWorld, uuid)) {
                weightedCount += multiplier;
            }
        }
        return Math.max(0, original + weightedCount);
    }
}
