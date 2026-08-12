package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.tridentMultipleDamage;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.tridentMultipleDamage.PiercingCollisionHelper;

import java.util.*;
import java.util.function.Predicate;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

//#if MC >= 12102
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
//#endif

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityApplyCollisionMixin extends ProjectileEntity{

    public PersistentProjectileEntityApplyCollisionMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract boolean canHit(Entity entity);

    //#if MC >= 12102
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



    @Expression("? == null")
    @ModifyExpressionValue(method = "applyCollision", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
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
    //#else
    //$$ // ===== 1.21.1: 在 tick 的碰撞判定处注入, 复现 1.21.4 applyCollision 钩子的行为 =====
    //$$
    //$$ @Unique
    //$$ private float jft$preHitYaw;
    //$$ @Unique
    //$$ private float jft$preHitPitch;
    //$$ @Unique
    //$$ private Vec3d jft$anchorPos;
    //$$
    //$$ // Hook A: 拦截 getEntityCollision, 用 contains 收集路径上所有实体, 返回第一个或 null
    //$$ @WrapOperation(method = "tick", at = @At(value = "INVOKE",
    //$$         target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;getEntityCollision(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/hit/EntityHitResult;"))
    //$$ private EntityHitResult collectCollisions(PersistentProjectileEntity instance, Vec3d vec3d, Vec3d vec3d2,
    //$$         Operation<EntityHitResult> original, @Share("arrayList")LocalRef<ArrayList<EntityHitResult>> arrayListLocalRef){
    //$$     this.jft$anchorPos = null;
    //$$     if(!CarpetJFTSettings.tridentMultipleDamage) return original.call(instance, vec3d, vec3d2);
    //$$     ArrayList<EntityHitResult> arrayList = new ArrayList<>(PiercingCollisionHelper.collect(instance.getWorld(), instance, vec3d, vec3d2,
    //$$             instance.getBoundingBox().stretch(instance.getVelocity()).expand(1.0F), this::canHit));
    //$$     arrayList.sort(Comparator.comparingDouble((entityHitResultx) -> vec3d.squaredDistanceTo(entityHitResultx.getEntity().getPos())));
    //$$     arrayListLocalRef.set(arrayList);
    //$$     return arrayList.isEmpty() ? null : arrayList.getFirst();
    //$$ }
    //$$
    //$$ // Hook B: 拦截 tick 里的 hitOrDeflect, 改为对收集列表循环 (多目标 + 悬停反复命中)
    //$$ @WrapOperation(method = "tick", at = @At(value = "INVOKE",
    //$$         target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;hitOrDeflect(Lnet/minecraft/util/hit/HitResult;)Lnet/minecraft/entity/ProjectileDeflection;"))
    //$$ private ProjectileDeflection multiHit(PersistentProjectileEntity instance, HitResult hitResult,
    //$$         Operation<ProjectileDeflection> original, @Share("arrayList")LocalRef<ArrayList<EntityHitResult>> arrayListLocalRef){
    //$$     if(!CarpetJFTSettings.tridentMultipleDamage) return original.call(instance, hitResult);
    //$$     ArrayList<EntityHitResult> arrayList = arrayListLocalRef.get();
    //$$     if(arrayList == null || arrayList.isEmpty()) return original.call(instance, hitResult);
    //$$
    //$$     // 记录命中前速度方向 (1.21.1 原生 tick 会用命中后的速度吸附旋转, 方向被 (-0.01,-0.1,-0.01) 取反)
    //$$     Vec3d velocity = instance.getVelocity();
    //$$     this.jft$preHitYaw = (float)(MathHelper.atan2(velocity.x, velocity.z) * 57.2957763671875D);
    //$$     this.jft$preHitPitch = (float)(MathHelper.atan2(velocity.y, velocity.horizontalLength()) * 57.2957763671875D);
    //$$
    //$$     // 复现 1.21.4 applyCollision 的 setPosition(hitPos): 把三叉戟钉在首次命中锚点上,
    //$$     // 保留每次命中后的移动抖动, 但每 tick 拉回锚点, 抵消缓慢漂移/上升
    //$$     if(this.jft$anchorPos == null){
    //$$         this.jft$anchorPos = hitResult.getPos();
    //$$     }
    //$$     instance.setPosition(this.jft$anchorPos);
    //$$
    //$$     return this.jft$hitOrDeflect(arrayList);
    //$$ }
    //$$
    //$$ // 1.21.1 原生 tick 碰撞处理后会把 yaw/pitch 吸附到"命中后"速度方向 (符号被取反 → 箭头朝上)
    //$$ // 命中 tick 用"命中前"速度方向覆盖, 复现 1.21.4 的旋转行为 (悬停时重力让速度偏下 → 箭头逐渐朝下)
    //$$ @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setYaw(F)V", ordinal = 1))
    //$$ private void keepYaw1(PersistentProjectileEntity instance, float yaw, Operation<Void> original){
    //$$     if(CarpetJFTSettings.tridentMultipleDamage && this.jft$anchorPos != null){
    //$$         original.call(instance, this.jft$preHitYaw);
    //$$     }else{
    //$$         original.call(instance, yaw);
    //$$     }
    //$$ }
    //$$ @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setPitch(F)V", ordinal = 1))
    //$$ private void keepPitch(PersistentProjectileEntity instance, float pitch, Operation<Void> original){
    //$$     if(CarpetJFTSettings.tridentMultipleDamage && this.jft$anchorPos != null){
    //$$         original.call(instance, this.jft$preHitPitch);
    //$$     }else{
    //$$         original.call(instance, pitch);
    //$$     }
    //$$ }
    //#endif


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
        return PiercingCollisionHelper.collect(this.getEntityWorld(), this, vec3d, vec3d2, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0F), this::canHit);
    }
}
