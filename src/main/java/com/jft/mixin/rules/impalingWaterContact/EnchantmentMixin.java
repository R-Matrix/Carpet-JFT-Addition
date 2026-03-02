package com.jft.mixin.rules.impalingWaterContact;

import com.jft.CarpetJFTSettings;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Unique
    private static boolean jft$isImpalingFlag;

    @Unique
    private static Entity jft$beAttractedEntity;

    @Inject(method = "modifyValue(Lnet/minecraft/component/ComponentType;Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lorg/apache/commons/lang3/mutable/MutableFloat;)V", at = @At("HEAD"))
    private void conveyVariables(ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> type, ServerWorld world, int level, ItemStack stack, Entity user, DamageSource damageSource, MutableFloat value, CallbackInfo ci){
        if (stack != null && EnchantmentHelper.getLevel(
                world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.IMPALING), stack) > 0){
            jft$isImpalingFlag = true;
            jft$beAttractedEntity = user;
        }
    }

    @Redirect(method = "applyEffects", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/enchantment/effect/EnchantmentEffectEntry;test(Lnet/minecraft/loot/context/LootContext;)Z"))
    private static boolean redirectTestMethod(EnchantmentEffectEntry<EnchantmentValueEffect> effect, LootContext lootContext) {
        if (CarpetJFTSettings.impalingWaterContact && jft$isImpalingFlag){
            jft$isImpalingFlag = false;
            if(jft$beAttractedEntity != null && jft$beAttractedEntity.isAlive()&& jft$beAttractedEntity.isTouchingWaterOrRain()){
                return true;
            }
        }
        return effect.test(lootContext);
    }
}

