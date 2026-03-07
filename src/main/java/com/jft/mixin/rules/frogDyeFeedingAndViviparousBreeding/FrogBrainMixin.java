package com.jft.mixin.rules.frogDyeFeedingAndViviparousBreeding;

import com.google.common.collect.ImmutableList;
import com.jft.toolsMager.frogDyeFeedingAndViviparousBreeding.FrogViviparousActivity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.passive.FrogBrain;
import net.minecraft.entity.passive.FrogEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(FrogBrain.class)
public class FrogBrainMixin{

    @Shadow
    private static boolean isNotBreeding(FrogEntity frog) {return false;}

    @WrapMethod(method = "updateActivities")
    private static void modifyUpdateActivities(FrogEntity frog, Operation<Void> original) {
        frog.getBrain().resetPossibleActivities(
                ImmutableList.of(
                        Activity.TONGUE, FrogViviparousActivity.VIVIPAROUS_ACTIVITY ,Activity.LAY_SPAWN, Activity.LONG_JUMP, Activity.SWIM, Activity.IDLE));
    }
}
