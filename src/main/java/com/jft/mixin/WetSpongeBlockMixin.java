package com.jft.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.WetSpongeBlock;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.jft.CarpetJFTSettings.wetSpongeDriesOnDesertAndBedLands;

@Mixin(WetSpongeBlock.class)
public abstract class WetSpongeBlockMixin {

    @WrapOperation(method = "onBlockAdded", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/dimension/DimensionType;ultrawarm()Z"))
    private boolean se(DimensionType instance, Operation<Boolean> original, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos blockPos){

        if(wetSpongeDriesOnDesertAndBedLands)
            return original.call(instance) || world.getBiome(blockPos).isIn(BiomeTags.IS_BADLANDS) || world.getBiome(blockPos).matchesKey(BiomeKeys.DESERT);
        return original.call(instance);
    }
}
