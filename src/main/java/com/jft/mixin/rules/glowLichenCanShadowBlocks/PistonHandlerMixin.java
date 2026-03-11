package com.jft.mixin.rules.glowLichenCanShadowBlocks;

import com.jft.CarpetJFTSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(PistonHandler.class)
public class PistonHandlerMixin {

    @Shadow
    @Final
    private World world;

    @Shadow
    @Final
    private List<BlockPos> brokenBlocks;

    @Shadow
    @Final
    private List<BlockPos> movedBlocks;

    @ModifyReturnValue(method = "tryMove", at = @At(value = "RETURN", ordinal = 10))
    private boolean ModifyBrokenBlocksList(boolean original, @Local(name = "blockPos2") BlockPos blockPos2){

        if(CarpetJFTSettings.glowLichenCanShadowBlocks && this.world.getBlockState(blockPos2).isOf(Blocks.GLOW_LICHEN)){
            this.brokenBlocks.removeLast();
            this.movedBlocks.add(blockPos2);
        }

        return original;
    }
}
