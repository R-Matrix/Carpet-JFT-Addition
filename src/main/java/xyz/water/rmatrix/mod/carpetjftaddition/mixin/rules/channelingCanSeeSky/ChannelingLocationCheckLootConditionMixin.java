package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.channelingCanSeeSky;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.entity.LocationPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

import java.util.Optional;


@Mixin(LocationCheckLootCondition.class)
public abstract class ChannelingLocationCheckLootConditionMixin {

	@Shadow
	public abstract Optional<LocationPredicate> predicate();

	@ModifyReturnValue(method = "test(Lnet/minecraft/loot/context/LootContext;)Z", at = @At("RETURN"))
	private boolean modifyPredicate(boolean origin, LootContext context) {
		boolean isTridentHit = context.get(LootContextParameters.THIS_ENTITY) instanceof TridentEntity  // blockHit
				|| context.get(LootContextParameters.DIRECT_ATTACKING_ENTITY) instanceof TridentEntity; // EntityHit
		boolean isSkyPredicate = this.predicate().map(p -> p.canSeeSky().orElse(false)).orElse(false);
		if (!CarpetJFTSettings.channelingCanSeeSky && isTridentHit && isSkyPredicate) {
			return true;
		}
		return origin;
	}
}
