package com.jft.mixin.rules.channelingWeather;

import com.jft.CarpetJFTSettings;
import com.jft.toolsMager.channelingWeather.ChannelingHelper;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.WeatherCheckLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;


@Mixin(WeatherCheckLootCondition.class)
public abstract class WeatherCheckLootConditionMixin implements LootCondition {

    @Shadow
    public abstract boolean equals(Object par1);

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

//public static int getTridentReturnAcceleration(ServerWorld world, ItemStack stack, Entity user) {
//    MutableFloat mutableFloat = new MutableFloat(0.0F);
//    forEachEnchantment(stack, (enchantment, level) -> enchantment.value().modifyTridentReturnAcceleration(world, level, stack, user, mutableFloat));
//    return Math.max(0, mutableFloat.intValue());
//}
//
//public static float getCrossbowChargeTime(ItemStack stack, LivingEntity user, float baseCrossbowChargeTime) {
//    MutableFloat mutableFloat = new MutableFloat(baseCrossbowChargeTime);
//    forEachEnchantment(stack, (enchantment, level) -> enchantment.value().modifyCrossbowChargeTime(user.getRandom(), level, mutableFloat));
//    return Math.max(0.0F, mutableFloat.floatValue());
//}
//
//public static float getTridentSpinAttackStrength(ItemStack stack, LivingEntity user) {
//    MutableFloat mutableFloat = new MutableFloat(0.0F);
//    forEachEnchantment(stack, (enchantment, level) -> enchantment.value().modifyTridentSpinAttackStrength(user.getRandom(), level, mutableFloat));
//    return mutableFloat.floatValue();
//}
//
//public static boolean hasAnyEnchantmentsIn(ItemStack stack, TagKey<Enchantment> tag) {
//    ItemEnchantmentsComponent itemEnchantmentsComponent = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
//
//    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
//        RegistryEntry<Enchantment> registryEntry = (RegistryEntry<Enchantment>)entry.getKey();
//        if (registryEntry.isIn(tag)) {
//            return true;
//        }
//    }
//
//    return false;
//}
//
//public static boolean hasAnyEnchantmentsWith(ItemStack stack, ComponentType<?> componentType) {
//    MutableBoolean mutableBoolean = new MutableBoolean(false);
//    forEachEnchantment(stack, (enchantment, level) -> {
//        if (enchantment.value().effects().contains(componentType)) {
//            mutableBoolean.setTrue();
//        }
//    });
//    return mutableBoolean.booleanValue();
//}