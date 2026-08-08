package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.wetSpongeDriesOnDesertAndBedLands;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WetSpongeBlock;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.biome.BiomeKeys;
import org.spongepowered.asm.mixin.Mixin;

import static xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings.wetSpongeDriesOnDesertAndBedLands;

@Mixin(WetSpongeBlock.class)
public abstract class WetSpongeBlockMixin {

    @WrapMethod(method = "onBlockAdded")
    private void sec(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, Operation<Void> original){
        if(wetSpongeDriesOnDesertAndBedLands) {
            if(world.getBiome(pos).isIn(BiomeTags.IS_BADLANDS) || world.getBiome(pos).matchesKey(BiomeKeys.DESERT)){
                world.setBlockState(pos, Blocks.SPONGE.getDefaultState(), Block.NOTIFY_ALL);
                world.syncWorldEvent(WorldEvents.WET_SPONGE_DRIES_OUT, pos, 0);
                world.playSound(null, pos, SoundEvents.BLOCK_WET_SPONGE_DRIES, SoundCategory.BLOCKS, 1.0F, (1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F);
            }
        }
        original.call(state, world, pos, oldState, notify);
    }
}
