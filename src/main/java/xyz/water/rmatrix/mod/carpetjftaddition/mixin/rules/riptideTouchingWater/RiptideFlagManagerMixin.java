package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.riptideTouchingWater;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings.riptideTouchingWater;

@Mixin(TridentItem.class)
public abstract class RiptideFlagManagerMixin {

    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private boolean onUseHead(
            PlayerEntity player, Operation<Boolean> original) {
        return shouldSetRule(player, original);
    }

    @WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private boolean onStoppedUsingHead(
            PlayerEntity player, Operation<Boolean> original) {
        return shouldSetRule(player, original);
    }

    @Unique
    public boolean shouldSetRule(PlayerEntity player, Operation<Boolean> original) {
        boolean originalValue = original.call(player);
        return switch (riptideTouchingWater) {
            case "ANY" -> true;
            case "DISABLED" -> false;
            case "WATERRorLAVA" -> player.isTouchingWaterOrRain() || player.isInLava();
            default -> originalValue;
        };
    }
}