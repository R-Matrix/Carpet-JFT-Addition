package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.channelingCanSeeSky;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.entity.EntityPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

import java.util.Optional;

@Mixin(EntityPropertiesLootCondition.class)
public abstract class ChannelingEntityPropertiesCanSeeSkyMixin {

	@Shadow
	public abstract Optional<EntityPredicate> predicate();

	@ModifyReturnValue(method = "test(Lnet/minecraft/loot/context/LootContext;)Z", at = @At("RETURN"))
	private boolean modifyPredicate(boolean origin, LootContext context) {
		boolean isTridentHit = context.get(LootContextParameters.DIRECT_ATTACKING_ENTITY) instanceof TridentEntity;
		boolean hasSkyRequirement = this.predicate()
				.map(ep -> ep.location().located().map(loc -> loc.canSeeSky().orElse(false)).orElse(false))
				.orElse(false);
		if (!CarpetJFTSettings.channelingCanSeeSky && isTridentHit && hasSkyRequirement) {
			return true;
		}
		return origin;
	}
}
