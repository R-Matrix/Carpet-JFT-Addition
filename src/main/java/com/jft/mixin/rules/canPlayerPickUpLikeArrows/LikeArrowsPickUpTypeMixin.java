package com.jft.mixin.rules.canPlayerPickUpLikeArrows;

import com.jft.CarpetJFTSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.OminousItemSpawnerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class LikeArrowsPickUpTypeMixin extends ProjectileEntity {

    @Shadow
    public PersistentProjectileEntity.PickupPermission pickupType;

    public LikeArrowsPickUpTypeMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "setOwner", at = @At("TAIL"), cancellable = true)
    private void ModifyPickUpType(Entity entity, CallbackInfo ci){
        if(CarpetJFTSettings.canPlayerPickUpLikeArrows) {
            if (!(entity instanceof PlayerEntity || entity instanceof OminousItemSpawnerEntity)) {
                this.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                ci.cancel();
            }
        }
    }

}