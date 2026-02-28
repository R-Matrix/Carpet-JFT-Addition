package com.jft.mixin.rules.riptideTouchingWater;

import com.jft.toolsMager.riptideTouchingWeather.TridentRiptideHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public abstract class RiptideFlagManagerMixin{

    @Inject(method = "use", at = @At("HEAD"))
    private void onUseHead(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        TridentRiptideHelper.jft$setIsOnTridentRiptideUsingOrStopUsing(true);
    }

    @Inject(method = "use", at = @At("RETURN"))
    private void onUseReturn(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        TridentRiptideHelper.jft$setIsOnTridentRiptideUsingOrStopUsing(false);
    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"))
    private void onStopUseHead(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir){
        TridentRiptideHelper.jft$setIsOnTridentRiptideUsingOrStopUsing(true);
    }

    @Inject(method = "onStoppedUsing", at = @At("RETURN"))
    private void onStopUseReturn(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir){
        TridentRiptideHelper.jft$setIsOnTridentRiptideUsingOrStopUsing(false);
    }
}
