package com.jft.mixin.rules.shulkerBaseTickIfOnTwistingVine;

import com.jft.CarpetJFTSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerEntity.class)
public abstract class shulkerEntityMixin extends GolemEntity {


    protected shulkerEntityMixin(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method="tick", at=@At("HEAD"), cancellable = true)
    private void cancelAllActivityIfOnTwistingVine(CallbackInfo ci){
        if(CarpetJFTSettings.shulkerBaseTickIfOnTwistingVine){
            boolean bl = this.getWorld().getBlockState(this.getBlockPos()).isOf(Blocks.TWISTING_VINES) ||
                    this.getWorld().getBlockState(this.getBlockPos()).isOf(Blocks.TWISTING_VINES_PLANT);
            if(bl){
                this.baseTick();
                ci.cancel();
                return;
            }
        }
    }
}
