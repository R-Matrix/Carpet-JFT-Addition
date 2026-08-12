package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.impalingWaterContact;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.impalingWaterContact.ImpalingContext;

// 仅在 1.21.1 等旧版本生效 (1.21.2+ 由 JFTMixinConfigPlugin 禁用)
@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

	//#if MC >= 12102
	// 1.21.2+ 附魔数据驱动后, Enchantments.IMPALING 为 RegistryEntry, impaling 上下文由 EnchantmentMixin 捕获
	//#else
	//$$ @WrapMethod(method = "getDamage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;F)F")
	//$$ private static float captureImpalingContext(ServerWorld world, ItemStack stack, Entity target, DamageSource source, float baseDamage, Operation<Float> original) {
	//$$ 	ImpalingContext.IS_IMPALING.set(stack != null && EnchantmentHelper.getLevel(
	//$$ 			world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.IMPALING).orElseThrow(), stack) > 0);
	//$$ 	ImpalingContext.USER.set(target);
	//$$ 	try {
	//$$ 		return original.call(world, stack, target, source, baseDamage);
	//$$ 	} finally {
	//$$ 		ImpalingContext.IS_IMPALING.remove();
	//$$ 		ImpalingContext.USER.remove();
	//$$ 	}
	//$$ }
	//#endif
}
