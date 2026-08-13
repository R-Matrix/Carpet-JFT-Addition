package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.impalingWaterContact;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

import java.util.List;
import java.util.function.Consumer;


@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {


    @WrapOperation(method = "modifyValue(Lnet/minecraft/component/ComponentType;Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lorg/apache/commons/lang3/mutable/MutableFloat;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;applyEffects(Ljava/util/List;Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)V"))
    private void modifyCondition(
            List<EnchantmentEffectEntry<EnchantmentValueEffect>> entries,
            LootContext lootContext,
            Consumer<EnchantmentValueEffect> effectConsumer,
            Operation<Void> original,
            @Local(argsOnly = true) ServerWorld world,
            @Local(argsOnly = true) Entity user) {
        if (CarpetJFTSettings.impalingWaterContact
                && jft$isImpaling(world)
                && user != null && user.isAlive() && user.isTouchingWaterOrRain()) {
            for (EnchantmentEffectEntry<EnchantmentValueEffect> entry : entries) {
                effectConsumer.accept(entry.effect());
            }
            return;
        }
        original.call(entries, lootContext, effectConsumer);
    }

    @Unique
    private boolean jft$isImpaling(ServerWorld world) {
        //#if MC >= 12102
        return world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT)
                .getOrThrow(Enchantments.IMPALING).value() == (Enchantment) (Object) this;
        //#else
        //$$ return world.getRegistryManager().get(RegistryKeys.ENCHANTMENT)
        //$$         .getEntry(Enchantments.IMPALING).orElseThrow().value() == (Enchantment)(Object) this;
        //#endif
    }
}
