package com.jft.toolsMager.amethystPistonBehaviorNormal;

import com.jft.CarpetJFTSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class AmethystTest {

    public static boolean isTargetAmethystBlock(BlockState state){
        return CarpetJFTSettings.amethystPistonBehaviorNormal &&(state.isOf(Blocks.AMETHYST_CLUSTER) || state.isOf(Blocks.SMALL_AMETHYST_BUD));
    }
}
