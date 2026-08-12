package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.canArrowDamageItemFrame;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.interceptItemFrameDropBlock.IsOffsetSpecialBlock;

@Mixin(ItemFrameEntity.class)
public abstract class CanArrowDamageItemFrame {
    @Inject(method = "damage", at = @At(value = "INVOKE",
            //#if MC >= 12102
            target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;dropHeldStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Z)V"),
            //#else
            //$$ target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;dropHeldStack(Lnet/minecraft/entity/Entity;Z)V"),
            //#endif
            cancellable = true)
    //#if MC >= 12102
    private void interceptArrowDamageItemFrame(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
    //#else
    //$$ private void interceptArrowDamageItemFrame(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
    //#endif
        if(CarpetJFTSettings.canArrowDamageItemFrame){
            if(source.getSource() instanceof ProjectileEntity){
                if(((IsOffsetSpecialBlock)this).Jft$isOffsetSpecialBlock()){
                    cir.cancel();
                }
            }
        }
    }
}
