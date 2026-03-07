package com.jft.toolsMager.frogDyeFeedingAndViviparousBreeding;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jft.mixin.rules.frogDyeFeedingAndViviparousBreeding.FrogBrainInvoker;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class AddViviparousSpawnActivities implements FrogBrainInvoker {

    public static void jft$addViviparousSpawnActivities(Brain<FrogEntity> brain) {
        brain.setTaskList(
                FrogViviparousActivity.VIVIPAROUS_ACTIVITY,
                ImmutableList.of(
                        Pair.of(0, LookAtMobWithIntervalTask.follow(EntityType.PLAYER, 6.0F, UniformIntProvider.create(30, 60))),
                        Pair.of(1,
                                UpdateAttackTargetTask.create(
                                        (world, frog) -> FrogBrainInvoker.isNotBreeding(frog), (world, frog) -> frog.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_ATTACKABLE)
                                )
                        ),
                        Pair.of(2, FrogSpanChildTask.create()),
                        Pair.of(3,
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
                        Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryModuleState.VALUE_ABSENT),
                        Pair.of(MemoryModuleType.IS_PREGNANT, MemoryModuleState.VALUE_PRESENT),
                        Pair.of(FrogMemoryModuleType.DYE_FLAG, MemoryModuleState.VALUE_PRESENT)
                )
        );
    }

}
