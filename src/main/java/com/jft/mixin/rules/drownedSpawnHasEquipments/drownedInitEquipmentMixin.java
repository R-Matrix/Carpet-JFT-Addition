package com.jft.mixin.rules.drownedSpawnHasEquipments;

import com.jft.CarpetJFTSettings;
import net.minecraft.entity.mob.DrownedEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(DrownedEntity.class)
public abstract class drownedInitEquipmentMixin {
    // 修改溺尸生成时携带装备("三叉戟"或"钓鱼竿")的概率
    @ModifyConstant(method = "initEquipment", constant = @Constant(doubleValue = 0.9))
    private double MotifyDrownedSpawnHasEquipments(double constant) {
        double drownSpawnHasEquipments = CarpetJFTSettings.drownedSpawnHasEquipments;
        return drownSpawnHasEquipments == -1 ? constant : (1 - drownSpawnHasEquipments);
    }
}
