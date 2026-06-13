package com.jft.mixin.rules.canArrowDamageItemFrame;

import com.jft.CarpetJFTSettings;
import com.jft.toolsMager.canInterceptItemFrameDropBlock.isOffsetSpacialBlock;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntity.class)
public abstract class canArrowDamageItemFrame {
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;dropHeldStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Z)V"), cancellable = true)
    private void interceptArrowDamageItemFrame(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(CarpetJFTSettings.canArrowDamageItemFrame){
            if(source.getSource() instanceof ProjectileEntity){
                if(((isOffsetSpacialBlock)this).Jft$isOffsetSpacialBlock()){
                    cir.cancel();
                }
            }
        }
    }
}
