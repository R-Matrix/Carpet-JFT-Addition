package com.jft.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.FrogBrain;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FrogBrain.class)
public class FrogBrainMixin {

    @Shadow
    private static boolean isNotBreeding(FrogEntity frog) {return false;}


    // todo
    @Unique
    private static void jft$addViviparousSpawnActivities(Brain<FrogEntity> brain) {
        brain.setTaskList(
                Activity.,
                ImmutableList.of(
                        Pair.of(0, LookAtMobWithIntervalTask.follow(EntityType.PLAYER, 6.0F, UniformIntProvider.create(30, 60))),
                        Pair.of(
                                1,
                                UpdateAttackTargetTask.create(
                                        (world, frog) -> isNotBreeding(frog), (world, frog) -> frog.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_ATTACKABLE)
                                )
                        ),
                        Pair.of(2, WalkTowardsWaterTask.create(8, 1.0F)),
                        Pair.of(3, LayFrogSpawnTask.create(Blocks.FROGSPAWN)),
                        Pair.of(
                                4,
                                new RandomTask<>(
                                        ImmutableList.of(
                                                Pair.of(StrollTask.create(1.0F), 2),
                                                Pair.of(GoToLookTargetTask.create(1.0F, 3), 1),
                                                Pair.of(new CroakTask(), 2),
                                                Pair.of(TaskTriggerer.predicate(Entity::isOnGround), 1)
                                        )
                                )
                        )
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryModuleState.VALUE_ABSENT), Pair.of(MemoryModuleType.IS_PREGNANT, MemoryModuleState.VALUE_PRESENT)
                )
        );
    }

}
