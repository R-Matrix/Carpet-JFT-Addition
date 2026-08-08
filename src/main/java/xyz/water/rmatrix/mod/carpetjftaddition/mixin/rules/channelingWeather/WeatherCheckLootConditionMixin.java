package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.channelingWeather;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.loot.condition.WeatherCheckLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.channelingWeather.ChannelingHelper;

import java.util.Objects;


@Mixin(WeatherCheckLootCondition.class)
public abstract class WeatherCheckLootConditionMixin{

    @ModifyReturnValue(method = "test(Lnet/minecraft/loot/context/LootContext;)Z", at = @At("RETURN"))
    private boolean injectWeatherCheckLootCondition(
            boolean original, LootContext context
    ) {
        boolean isRaining = context.getWorld().isRaining();
        boolean isThundering = context.getWorld().isThundering();
        if(Objects.equals(context.get(LootContextParameters.ENCHANTMENT_LEVEL), 1) ) {
            ChannelingHelper.setChannelingFlag(true);
            return shouldTriggerChanneling(isThundering, isRaining);

        }
        return original;
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