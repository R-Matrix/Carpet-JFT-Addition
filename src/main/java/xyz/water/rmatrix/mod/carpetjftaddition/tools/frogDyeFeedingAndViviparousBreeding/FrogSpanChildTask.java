package xyz.water.rmatrix.mod.carpetjftaddition.tools.frogDyeFeedingAndViviparousBreeding;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.TaskTriggerer;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
//#if MC >= 12105
//$$ import net.minecraft.entity.passive.FrogVariants;
//$$ import net.minecraft.registry.RegistryKeys;
//#else
import net.minecraft.registry.Registries;
//#endif
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
                                        //#if MC >= 12102
                                        FrogEntity frogBaby = EntityType.FROG.create(world, SpawnReason.BREEDING);
                                        //#else
                                        //$$ FrogEntity frogBaby = EntityType.FROG.create(world);
                                        //#endif
                                        if(frogBaby != null) {
                                        //#if MC >= 12102
                                        //#if MC >= 12105
                                        //$$ RegistryEntry<FrogVariant> variant = switch (flagValue) {
                                        //$$     case 1 -> world.getRegistryManager().getOrThrow(RegistryKeys.FROG_VARIANT).getOrThrow(FrogVariants.COLD);
                                        //$$     case 2 -> world.getRegistryManager().getOrThrow(RegistryKeys.FROG_VARIANT).getOrThrow(FrogVariants.WARM);
                                        //$$     default -> world.getRegistryManager().getOrThrow(RegistryKeys.FROG_VARIANT).getOrThrow(FrogVariants.TEMPERATE);
                                        //$$ };
                                        //$$
                                        //$$ ((FrogEntityVariantAccess) frogBaby).jft$setVariant(variant);
                                        //#else
                                            RegistryEntry<FrogVariant> variant = switch (flagValue) {
                                                case 1 -> Registries.FROG_VARIANT.getOrThrow(FrogVariant.COLD);
                                                case 2 -> Registries.FROG_VARIANT.getOrThrow(FrogVariant.WARM);
                                                default -> Registries.FROG_VARIANT.getOrThrow(FrogVariant.TEMPERATE);
                                            };

                                            frogBaby.setVariant(variant);
                                        //#endif
                                        //#else
                                            //$$ RegistryEntry<FrogVariant> variant = switch (flagValue) {
                                            //$$     case 1 -> Registries.FROG_VARIANT.getEntry(FrogVariant.COLD).orElseThrow();
                                            //$$     case 2 -> Registries.FROG_VARIANT.getEntry(FrogVariant.WARM).orElseThrow();
                                            //$$     default -> Registries.FROG_VARIANT.getEntry(FrogVariant.TEMPERATE).orElseThrow();
                                            //$$ };
                                            //$$
                                            //$$ frogBaby.setVariant(variant);
                                            //#endif
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
