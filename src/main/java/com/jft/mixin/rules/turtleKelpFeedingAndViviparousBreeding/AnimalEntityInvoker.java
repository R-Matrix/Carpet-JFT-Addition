package com.jft.mixin.rules.turtleKelpFeedingAndViviparousBreeding;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AnimalEntity.class)
public interface AnimalEntityInvoker {

    @Invoker("breed")
    void invokerBreed(ServerWorld world, AnimalEntity other);
}
