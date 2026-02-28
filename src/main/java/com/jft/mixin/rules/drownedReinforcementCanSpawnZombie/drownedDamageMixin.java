package com.jft.mixin.rules.drownedReinforcementCanSpawnZombie;

import com.jft.CarpetJFTSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ZombieEntity.class)
public abstract class drownedDamageMixin {

    // 修改溺尸的僵尸增援以回退<1.21.2的表现
    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ZombieEntity;getType()Lnet/minecraft/entity/EntityType;", ordinal = 0))
    private EntityType<? extends ZombieEntity> ChangeReinforcementOfDrowned(ZombieEntity instance){

        return (instance.getType() == EntityType.DROWNED && CarpetJFTSettings.drownReinforcementCanSpawnZombie) ?
                EntityType.ZOMBIE :
                instance.getType();
    }
}
