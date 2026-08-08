package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.reinforcementAttributeInit;

import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

@Mixin(ZombieEntity.class)
public class ZombieEntityInitAttrMixin {
    // 修改溺尸增援初始化属性
    @ModifyArg(method = "initAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/EntityAttributeInstance;setBaseValue(D)V"))
    private double MotifyInitAttributes(double baseValue){
        double reinforcementAttributeInit = CarpetJFTSettings.reinforcementAttributeInit;
        return reinforcementAttributeInit == -1 ? baseValue : reinforcementAttributeInit;
    }
}
