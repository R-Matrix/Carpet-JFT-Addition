package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.turtleEggsDriedKelpBlockFaster;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings.turtleEggsDriedKelpBlockFaster;


@Mixin(TurtleEggBlock.class)
public class TurtleEggsMixin {

    @Final
    @Shadow
    public static IntProperty HATCH;

    @Final
    @Shadow
    public static IntProperty EGGS;

    @Unique
    private static boolean isDriedKelpBlock(BlockView world, BlockPos pos){
        return world.getBlockState(pos).isOf(Blocks.DRIED_KELP_BLOCK);
    }

    @Unique
    private static boolean isDriedKelpBlockBelow(BlockView world, BlockPos pos){
        return turtleEggsDriedKelpBlockFaster && isDriedKelpBlock(world, pos.down());
    }

    @Unique
    private boolean jft$shouldHatchProgress(World world) {
        float f = world.getSkyAngle(1.0F);
        return f < 0.69 && f > 0.65 || world.random.nextInt(50) == 0;
    }

    @Inject(method = "randomTick", at = @At("TAIL"))
    private void driedRandomTick(
            BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci){

        if (this.jft$shouldHatchProgress(world) && isDriedKelpBlockBelow(world, pos) && turtleEggsDriedKelpBlockFaster) {
            int i = state.get(HATCH);
            if (i < 2) {
                world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                world.setBlockState(pos, state.with(HATCH, i + 1), Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
            } else {
                world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                world.removeBlock(pos, false);
                world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(state));

                for (int j = 0; j < state.get(EGGS); j++) {
                    world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));
                    //#if MC >= 12102
                    TurtleEntity turtleEntity = EntityType.TURTLE.create(world, SpawnReason.BREEDING);
                    //#else
                    //$$ TurtleEntity turtleEntity = EntityType.TURTLE.create(world);
                    //#endif
                    if (turtleEntity != null) {
                        turtleEntity.setBreedingAge(-24000);
                        turtleEntity.setHomePos(pos);
                        turtleEntity.refreshPositionAndAngles(pos.getX() + 0.3 + j * 0.2, pos.getY(), pos.getZ() + 0.3, 0.0F, 0.0F);
                        world.spawnEntity(turtleEntity);
                    }
                }
            }
        }
    }
}
