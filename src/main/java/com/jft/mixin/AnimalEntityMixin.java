package com.jft.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {

    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    protected void eat(PlayerEntity player, Hand hand, ItemStack stack){}

    @Shadow
    public void lovePlayer(@Nullable PlayerEntity player){}

    @Shadow
    protected void playEatSound() {}

    @Shadow
    public boolean canEat(){return false;}

    @WrapMethod(method = "interactMob")
    protected ActionResult jft$interactMobHook(PlayerEntity player, Hand hand, Operation<ActionResult> original){

        return original.call(player, hand);
    }
}
