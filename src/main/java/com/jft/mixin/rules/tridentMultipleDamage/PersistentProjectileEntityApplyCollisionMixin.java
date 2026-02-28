package com.jft.mixin.rules.tridentMultipleDamage;

import com.jft.CarpetJFTSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.function.Predicate;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityApplyCollisionMixin extends ProjectileEntity{

    public PersistentProjectileEntityApplyCollisionMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public byte getPierceLevel(){
        return 0;
    }

    @Inject(method = "applyCollision", at = @At("HEAD"), cancellable = true)
    private void TickInjectMixin(BlockHitResult blockHitResult, CallbackInfo ci){
        if(CarpetJFTSettings.tridentMultipleDamage){
            while (this.isAlive()) {
                Vec3d vec3d = this.getPos();
                ArrayList<EntityHitResult> arrayList = new ArrayList(this.jft$collectPiercingCollisions(vec3d, blockHitResult.getPos()));
                arrayList.sort(Comparator.comparingDouble(entityHitResultx -> vec3d.squaredDistanceTo(entityHitResultx.getEntity().getPos())));
                EntityHitResult entityHitResult = arrayList.isEmpty() ? null : (EntityHitResult) arrayList.getFirst();
                Vec3d vec3d2 = ((HitResult) Objects.requireNonNullElse(entityHitResult, blockHitResult)).getPos();
                this.setPosition(vec3d2);
                this.tickBlockCollision(vec3d, vec3d2);
                if (this.portalManager != null && this.portalManager.isInPortal()) {
                    this.tickPortalTeleportation();
                }

                if (arrayList.isEmpty()) {
                    if (this.isAlive() && blockHitResult.getType() != HitResult.Type.MISS) {
                        this.hitOrDeflect(blockHitResult);
                        this.velocityDirty = true;
                    }
                    break;
                } else if (this.isAlive() && !this.noClip) {
                    ProjectileDeflection projectileDeflection = this.jft$hitOrDeflect(arrayList);
                    this.velocityDirty = true;
                    if (this.getPierceLevel() > 0 && projectileDeflection == ProjectileDeflection.NONE) {
                        continue;
                    }
                    break;
                }
            }
            ci.cancel();
        }
    }

    @Unique
    private ProjectileDeflection jft$hitOrDeflect(Collection<EntityHitResult> hitResults) {
        for (EntityHitResult entityHitResult : hitResults) {
            ProjectileDeflection projectileDeflection = this.hitOrDeflect(entityHitResult);
            if (!this.isAlive() || projectileDeflection != ProjectileDeflection.NONE) {
                return projectileDeflection;
            }
        }

        return ProjectileDeflection.NONE;
    }

    @Unique
    private Collection<EntityHitResult> jft$collectPiercingCollisions(Vec3d vec3d, Vec3d vec3d2) {
        return jft$collectPiercingCollisions(this.getEntityWorld(), this, vec3d, vec3d2, this.getBoundingBox().stretch(this.getVelocity()).expand((double)1.0F), this::canHit);
    }

    @Unique
    private static Collection<EntityHitResult> jft$collectPiercingCollisions(World world, Entity entity, Vec3d from, Vec3d to, Box box, Predicate<Entity> hitPredicate) {
        return jft$collectPiercingCollisions(world, entity, from, to, box, hitPredicate, jft$getToleranceMargin(entity));
    }

    @Unique
    private static float jft$getToleranceMargin(Entity entity) {
            return Math.max(0.0F, Math.min(0.3F, (float)(entity.age - 2) / 20.0F));
    }

    @Unique
    private static Collection<EntityHitResult> jft$collectPiercingCollisions(World world, Entity entity, Vec3d from, Vec3d to, Box box, Predicate<Entity> hitPredicate, float hitboxMargin) {
        List<EntityHitResult> list = new ArrayList();

        for(Entity entity2 : world.getOtherEntities(entity, box, hitPredicate)) {
            Box box2 = entity2.getBoundingBox().expand((double)hitboxMargin);
            if (box2.contains(from)) {
                list.add(new EntityHitResult(entity2, from));
            } else {
                Optional<Vec3d> optional = box2.raycast(from, to);
                optional.ifPresent((pos) -> list.add(new EntityHitResult(entity2, pos)));
            }
        }

        return list;
    }
}

