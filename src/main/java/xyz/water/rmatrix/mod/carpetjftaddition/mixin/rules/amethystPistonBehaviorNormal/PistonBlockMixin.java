package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.amethystPistonBehaviorNormal;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static xyz.water.rmatrix.mod.carpetjftaddition.tools.amethystPistonBehaviorNormal.AmethystEnvDetection.isTargetAmethystBlock;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {


    @WrapOperation(method = "isMovable", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;getPistonBehavior()Lnet/minecraft/block/piston/PistonBehavior;"))
    private static PistonBehavior modifySheBehaviorNormal1 (BlockState instance, Operation<PistonBehavior> original){

        if(isTargetAmethystBlock(instance)) return PistonBehavior.NORMAL;
        return original.call(instance);
    }

    @WrapOperation(method = "onSyncedBlockEvent", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;getPistonBehavior()Lnet/minecraft/block/piston/PistonBehavior;"))
    private static PistonBehavior modifySheBehaviorNormal2 (BlockState instance, Operation<PistonBehavior> original){

        if(isTargetAmethystBlock(instance)) return PistonBehavior.NORMAL;
        return original.call(instance);
    }



}
