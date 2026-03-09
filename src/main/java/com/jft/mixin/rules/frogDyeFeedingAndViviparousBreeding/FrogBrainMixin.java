package com.jft.mixin.rules.frogDyeFeedingAndViviparousBreeding;

import com.jft.CarpetJFTSettings;
import com.jft.toolsMager.frogDyeFeedingAndViviparousBreeding.FrogViviparousActivity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.passive.FrogBrain;
import net.minecraft.entity.passive.FrogEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;


@Mixin(FrogBrain.class)
public class FrogBrainMixin{

    @Shadow
    private static boolean isNotBreeding(FrogEntity frog) {return false;}

    @ModifyArg(method = "updateActivities", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/ai/brain/Brain;resetPossibleActivities(Ljava/util/List;)V"))
    private static List<Activity> modifyUpdateActivities(List<Activity> activities){

        List<Activity> actCopy = new ArrayList<>(activities);

        boolean isEqBoolean = activities.contains(FrogViviparousActivity.VIVIPAROUS_ACTIVITY);

        if(CarpetJFTSettings.frogDyeFeedingAndViviparousBreeding && !isEqBoolean){
            int indexOfLaySpawn = actCopy.indexOf(Activity.LAY_SPAWN);
            actCopy.add(indexOfLaySpawn, FrogViviparousActivity.VIVIPAROUS_ACTIVITY);
        }

        if(!CarpetJFTSettings.frogDyeFeedingAndViviparousBreeding && isEqBoolean){
            actCopy.remove(FrogViviparousActivity.VIVIPAROUS_ACTIVITY);
        }

        return actCopy;
    }
}
