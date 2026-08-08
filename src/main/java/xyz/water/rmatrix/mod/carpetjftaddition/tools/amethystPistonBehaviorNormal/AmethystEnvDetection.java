package xyz.water.rmatrix.mod.carpetjftaddition.tools.amethystPistonBehaviorNormal;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

public class AmethystEnvDetection {

    public static boolean isTargetAmethystBlock(BlockState state){
        return CarpetJFTSettings.amethystPistonBehaviorNormal &&(state.isOf(Blocks.AMETHYST_CLUSTER) || state.isOf(Blocks.SMALL_AMETHYST_BUD));
    }
}
