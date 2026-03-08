package com.jft.mixin.rules.drownedSpawnHasEquipments;

import com.jft.CarpetJFTSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.mob.DrownedEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DrownedEntity.class)
public abstract class drownedInitEquipmentMixin {
    // 修改溺尸生成时携带装备("三叉戟"或"钓鱼竿")的概率

    @ModifyExpressionValue(method = "initEquipment", at = @At(value = "CONSTANT", args = "doubleValue=0.9"))
    private double se(double original){
        return CarpetJFTSettings.drownedSpawnHasEquipments == -1 ?
            original :
                1 - CarpetJFTSettings.drownedSpawnHasEquipments;
    }
}
