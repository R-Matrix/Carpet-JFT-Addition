package com.jft.mixin.rules.tridentMultipleDamage;

import com.jft.CarpetJFTSettings;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.*;
import java.util.function.Predicate;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityApplyCollisionMixin extends ProjectileEntity{

    public PersistentProjectileEntityApplyCollisionMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }


    @ModifyVariable(method = "applyCollision", at = @At("LOAD"), ordinal = 0)
    private EntityHitResult se(EntityHitResult original, BlockHitResult blockHitResult,
                               @Share("arrayList")LocalRef<ArrayList<EntityHitResult>> arrayListLocalRef){

        if(!CarpetJFTSettings.tridentMultipleDamage) return original;

        Vec3d vec3d = this.getPos();
        ArrayList<EntityHitResult> arrayList = new ArrayList<>(this.jft$collectPiercingCollisions(vec3d, blockHitResult.getPos()));
        arrayList.sort(Comparator.comparingDouble((entityHitResultx) -> vec3d.squaredDistanceTo(entityHitResultx.getEntity().getPos())));
        arrayListLocalRef.set(arrayList);
        return arrayList.isEmpty() ? null : arrayList.getFirst();
    }


    @Definition(id = "entityHitResult", local = @Local(type = EntityHitResult.class))
    @Expression("entityHitResult == null")
    @ModifyExpressionValue(method = "applyCollision", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean se(boolean original, @Share("arrayList")LocalRef<ArrayList<EntityHitResult>> arrayListLocalRef){

        if(!CarpetJFTSettings.tridentMultipleDamage) return original;

        return arrayListLocalRef.get().isEmpty();

    }


    @WrapOperation(method = "applyCollision", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;hitOrDeflect(Lnet/minecraft/util/hit/HitResult;)Lnet/minecraft/entity/ProjectileDeflection;",
            ordinal = 1))
    private ProjectileDeflection ser
            (PersistentProjectileEntity instance, HitResult hitResult,
             Operation<ProjectileDeflection> original, @Share("arrayList")LocalRef<ArrayList<EntityHitResult>> arrayListLocalRef){

        if(!CarpetJFTSettings.tridentMultipleDamage) return original.call(instance, hitResult);

        return this.jft$hitOrDeflect(arrayListLocalRef.get());
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
        return jft$collectPiercingCollisions(this.getEntityWorld(), this, vec3d, vec3d2, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0F), this::canHit);
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
        List<EntityHitResult> list = new ArrayList<>();

        for(Entity entity2 : world.getOtherEntities(entity, box, hitPredicate)) {
            Box box2 = entity2.getBoundingBox().expand(hitboxMargin);
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

