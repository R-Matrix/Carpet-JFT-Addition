package com.jft.mixin.rules.allowAmethystBudCanGrowAtWater;

import com.jft.CarpetJFTSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.BuddingAmethystBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BuddingAmethystBlock.class)
public class BuddingAmethystBlockMixin {

    @ModifyReturnValue(method = "canGrowIn", at = @At("RETURN"))
    private static boolean se(boolean original, BlockState state){

        return CarpetJFTSettings.allowAmethystBudCanGrowAtWater ? original : state.isAir();
    }
}
