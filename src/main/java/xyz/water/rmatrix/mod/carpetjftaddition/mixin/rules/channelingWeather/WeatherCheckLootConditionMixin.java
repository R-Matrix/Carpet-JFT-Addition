package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.channelingWeather;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.loot.condition.WeatherCheckLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;


@Mixin(WeatherCheckLootCondition.class)
public abstract class WeatherCheckLootConditionMixin {

    @ModifyReturnValue(method = "test(Lnet/minecraft/loot/context/LootContext;)Z", at = @At("RETURN"))
    private boolean injectWeatherCheckLootCondition(
            boolean original, LootContext context
        ) {
        if (context.get(LootContextParameters.THIS_ENTITY) instanceof TridentEntity
                || context.get(LootContextParameters.DIRECT_ATTACKING_ENTITY) instanceof TridentEntity) {
            return shouldTriggerChanneling(
                    context.getWorld().isThundering(),
                    context.getWorld().isRaining()
            );
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
