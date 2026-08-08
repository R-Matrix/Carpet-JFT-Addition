package xyz.water.rmatrix.mod.carpetjftaddition.tools.frogDyeFeedingAndViviparousBreeding;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

public class FrogSpanChildTask {

    public static Task<LivingEntity> create() {
        return TaskTriggerer.task(
                context -> context.group(
                                context.queryMemoryAbsent(MemoryModuleType.ATTACK_TARGET),
                                context.queryMemoryValue(FrogMemoryModuleType.DYE_FLAG),
                                context.queryMemoryValue(MemoryModuleType.IS_PREGNANT)
                        )
                        .apply(
                                context,
                                (attackTarget, dyeFlag, isPregnant) ->
                                        (world, entity, time) -> {
                                    if (entity.isOnGround()) {
                                        int flagValue = context.getValue(dyeFlag);
                                        FrogEntity frogBaby = EntityType.FROG.create(world, SpawnReason.BREEDING);
                                        if(frogBaby != null) {
                                            RegistryEntry<FrogVariant> variant = switch (flagValue) {
                                                case 1 -> Registries.FROG_VARIANT.getOrThrow(FrogVariant.COLD);
                                                case 2 -> Registries.FROG_VARIANT.getOrThrow(FrogVariant.WARM);
                                                default -> Registries.FROG_VARIANT.getOrThrow(FrogVariant.TEMPERATE);
                                            };

                                            frogBaby.setVariant(variant);
                                            frogBaby.setPos(entity.getX(), entity.getY(), entity.getZ());
                                            world.spawnEntity(frogBaby);


                                            isPregnant.forget();
                                            dyeFlag.forget();

                                        }


                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                        )
        );
    }
}
