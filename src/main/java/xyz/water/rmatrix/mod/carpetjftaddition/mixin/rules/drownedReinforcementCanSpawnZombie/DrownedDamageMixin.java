package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.drownedReinforcementCanSpawnZombie;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;


@Mixin(ZombieEntity.class)
public abstract class DrownedDamageMixin {

    // 修改溺尸的僵尸增援以回退<1.21.2的表现
    @WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ZombieEntity;getType()Lnet/minecraft/entity/EntityType;", ordinal = 0))
    private EntityType<? extends ZombieEntity> ChangeReinforcementOfDrowned(
            ZombieEntity instance, Operation<EntityType<? extends ZombieEntity>> original){

        return (instance.getType() == EntityType.DROWNED && CarpetJFTSettings.drownedReinforcementCanSpawnZombie) ?
                EntityType.ZOMBIE :
                original.call(instance);
    }
}
