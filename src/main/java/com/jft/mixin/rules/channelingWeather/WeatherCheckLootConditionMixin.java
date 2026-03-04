package com.jft.mixin.rules.channelingWeather;

import com.jft.CarpetJFTSettings;
import com.jft.toolsMager.channelingWeather.ChannelingHelper;
import net.minecraft.loot.condition.WeatherCheckLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;


@Mixin(WeatherCheckLootCondition.class)
public abstract class WeatherCheckLootConditionMixin{

    @Inject(method = "test(Lnet/minecraft/loot/context/LootContext;)Z", at = @At("HEAD"), cancellable = true)
    private void injectWeatherCheckLootCondition(
            LootContext context,
            CallbackInfoReturnable<Boolean> cir
    ) {
        boolean isRaining = context.getWorld().isRaining();
        boolean isThundering = context.getWorld().isThundering();
        if(Objects.equals(context.get(LootContextParameters.ENCHANTMENT_LEVEL), 1) ) {
            ChannelingHelper.setChannelingFlag(true);
            boolean shouldTrigger = shouldTriggerChanneling(isThundering, isRaining);
            cir.setReturnValue(shouldTrigger);
        }
    }

    @Unique
    private static boolean shouldTriggerChanneling(boolean isThundering, boolean isRaining) {
        String mode = CarpetJFTSettings.channelingWeather;

        return switch (mode) {
            case "RAINING" -> isRaining;
            case "ANY" -> true;
            case "DISABLED" -> false;
            default -> isThundering;
        };
    }
}