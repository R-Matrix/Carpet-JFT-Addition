package com.jft.toolsMager.frogDyeFeedingAndViviparousBreeding;

import com.jft.CarpetJFTSettings;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.TemptationsSensor;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class FrogDyeTemptationSensor extends TemptationsSensor {

    private static final TargetPredicate TEMPTER_PREDICATE = TargetPredicate.createNonAttackable().ignoreVisibility();

    public FrogDyeTemptationSensor() {
        super(stack -> false);
    }

    @Override
    protected void sense(ServerWorld serverWorld, PathAwareEntity pathAwareEntity) {

        Brain<?> brain = pathAwareEntity.getBrain();
        TargetPredicate targetPredicate = TEMPTER_PREDICATE.copy().setBaseMaxDistance(
                (float)pathAwareEntity.getAttributeValue(EntityAttributes.TEMPT_RANGE));
        Predicate<ItemStack> itemStackPredicate = getTemptationPredicate(pathAwareEntity);

        List<ServerPlayerEntity> list = serverWorld.getPlayers()
                .stream()
                .filter(EntityPredicates.EXCEPT_SPECTATOR)
                .filter(player -> targetPredicate.test(serverWorld, pathAwareEntity, player))
                .filter(player -> itemStackPredicate.test(player.getMainHandStack()) ||
                                                itemStackPredicate.test(player.getOffHandStack()))
                .filter(playerx -> !pathAwareEntity.hasPassenger(playerx))
                .sorted(Comparator.comparingDouble(pathAwareEntity::squaredDistanceTo))
                .toList();

        if (!list.isEmpty()) {
            PlayerEntity playerEntity = list.getFirst();
            brain.remember(MemoryModuleType.TEMPTING_PLAYER, playerEntity);
        } else {
            brain.forget(MemoryModuleType.TEMPTING_PLAYER);
        }


    }

    private Predicate<ItemStack> getTemptationPredicate(PathAwareEntity pathAwareEntity) {
        Predicate<ItemStack> basePredicate = stack -> stack.isIn(ItemTags.FROG_FOOD);
        if (pathAwareEntity instanceof FrogEntity) {
            if (CarpetJFTSettings.frogDyeFeedingAndViviparousBreeding)
                return stack -> basePredicate.test(stack) ||
                        stack.isOf(Items.GREEN_DYE) || stack.isOf(Items.ORANGE_DYE) || stack.isOf(Items.WHITE_DYE);
        }

        return basePredicate;
    }
}
