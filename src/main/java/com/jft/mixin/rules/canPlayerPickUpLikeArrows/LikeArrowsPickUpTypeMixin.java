package com.jft.mixin.rules.canPlayerPickUpLikeArrows;

import com.jft.CarpetJFTSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PersistentProjectileEntity.class)
public abstract class LikeArrowsPickUpTypeMixin extends ProjectileEntity {

    @Shadow
    public PersistentProjectileEntity.PickupPermission pickupType;

    public LikeArrowsPickUpTypeMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "setOwner", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;setOwner(Lnet/minecraft/entity/Entity;)V", shift = At.Shift.AFTER), argsOnly = true)
    private Entity se(Entity value){
        if(CarpetJFTSettings.canPlayerPickUpLikeArrows && isInstanceofCanSummonProjectileEntity(value)){
            this.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        }
        return value;
    }

    @Unique
    private boolean isInstanceofCanSummonProjectileEntity(Entity entity){
        return (entity instanceof SkeletonEntity || entity instanceof StrayEntity || entity instanceof BoggedEntity ||
                entity instanceof WitherSkeletonEntity || entity instanceof PillagerEntity || entity instanceof PiglinEntity
                || entity instanceof DrownedEntity);
    }
}