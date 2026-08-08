package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.impalingWaterContact;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

import java.util.List;


@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {


    @Unique
    private static final ThreadLocal<Boolean> IS_IMPALING_FLAG_REF = new ThreadLocal<>();
    @Unique
    private static final ThreadLocal<Entity> USER_REF = new ThreadLocal<>();

    @ModifyArg(method = "modifyValue(Lnet/minecraft/component/ComponentType;Lnet/minecraft/server/world/ServerWorld;ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lorg/apache/commons/lang3/mutable/MutableFloat;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;applyEffects(Ljava/util/List;Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)V"))
    private List<EnchantmentEffectEntry<EnchantmentValueEffect>> se
            (List<EnchantmentEffectEntry<EnchantmentValueEffect>> entries,
             @Local(argsOnly = true) ServerWorld world, @Local(argsOnly = true) Entity user, @Local(argsOnly = true) ItemStack stack){

        IS_IMPALING_FLAG_REF.set(stack != null && EnchantmentHelper.getLevel(
                world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.IMPALING), stack)> 0);
        USER_REF.set(user);

        return entries;
    }

    @WrapOperation(method = "applyEffects", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/enchantment/effect/EnchantmentEffectEntry;test(Lnet/minecraft/loot/context/LootContext;)Z"))
    private static boolean redirectTestMethod(
            EnchantmentEffectEntry<?> entries, LootContext lootContext,
            Operation<Boolean> original) {
        if (CarpetJFTSettings.impalingWaterContact && IS_IMPALING_FLAG_REF.get()){
            if(USER_REF.get().isAlive()&& USER_REF.get().isTouchingWaterOrRain()){
                return true;
            }
        }

        IS_IMPALING_FLAG_REF.remove();
        USER_REF.remove();

        return original.call(entries, lootContext);
    }
}

