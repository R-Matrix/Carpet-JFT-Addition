package com.jft.mixin.rules.canTillFarmlandBelowBlock;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

import static com.jft.CarpetJFTSettings.canTillFarmlandBelowBlock;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    @ModifyReturnValue(method = "canTillFarmland", at = @At("RETURN"))
    private static boolean se(boolean original, ItemUsageContext context) {
        if (canTillFarmlandBelowBlock)
            return original || jft$isInJft$canTillFarmlandBelowBlockList(context);
        return original;
    }


    @Unique
    private static List<Block> jft$canTillFarmlandBelowBlockList = new ArrayList<>(
            List.of(Blocks.WATER,
                    Blocks.BEDROCK,
                    Blocks.ICE)
    );

    @Unique
    private static boolean jft$isInJft$canTillFarmlandBelowBlockList(ItemUsageContext context){
        return jft$canTillFarmlandBelowBlockList
                .contains(context.getWorld().getBlockState(context.getBlockPos().up()).getBlock());
    }
}
