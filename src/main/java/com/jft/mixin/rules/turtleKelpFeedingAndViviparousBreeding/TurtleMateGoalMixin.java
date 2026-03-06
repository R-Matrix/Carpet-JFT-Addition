package com.jft.mixin.rules.turtleKelpFeedingAndViviparousBreeding;

import com.jft.toolsMager.turtleKelpFeedingAndViviparousBreeding.ControlBeViviparousAccess;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.entity.passive.TurtleEntity$MateGoal")
public abstract class TurtleMateGoalMixin extends AnimalMateGoal {


    public TurtleMateGoalMixin(AnimalEntity animal, double speed) {
        super(animal, speed);
    }


    @Shadow
    @Final
    private TurtleEntity turtle;


    @ModifyArg(method = "breed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/TurtleEntity;setHasEgg(Z)V"))
    private boolean cancelHasEgg(
            boolean hasEgg, @Share("shouldBeViviparous")LocalBooleanRef beViviparousRef){
        beViviparousRef.set(((ControlBeViviparousAccess)this.turtle).jft$shouldBeViviparous());
        if(beViviparousRef.get())
            return false;
        return hasEgg;
    }


    @ModifyExpressionValue(method = "breed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean addConditions(boolean original, @Share("shouldBeViviparous")LocalBooleanRef beViviparousRef){

        return original && !beViviparousRef.get();
    }


    @Inject(method = "breed", at = @At("TAIL"))
    private void useSuperBreed(CallbackInfo ci, @Share("shouldBeViviparous")LocalBooleanRef beViviparousRef){
        if(beViviparousRef.get() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)){
            super.breed();
            beViviparousRef.set(false);
            ((ControlBeViviparousAccess)this.turtle).setJft$shouldBeViviparous(false);
        }
    }
}
