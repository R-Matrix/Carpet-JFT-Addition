package com.jft.mixin.rules.amethystPistonBehaviorNormal;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.block.piston.PistonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.jft.toolsMager.amethystPistonBehaviorNormal.AmethystTest.isTargetAmethystBlock;

@Mixin(PistonHandler.class)
public class PistonHandlerMixin {


    @WrapOperation(method = "tryMove", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;getPistonBehavior()Lnet/minecraft/block/piston/PistonBehavior;"))
    private PistonBehavior modifySheBehaviorNormal(BlockState instance, Operation<PistonBehavior> original){

        if(isTargetAmethystBlock(instance)) return PistonBehavior.NORMAL;
        return original.call(instance);
    }

}
