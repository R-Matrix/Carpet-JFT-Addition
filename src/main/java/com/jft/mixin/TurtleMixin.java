package com.jft.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TurtleEntity.class)
public abstract class TurtleMixin extends AnimalEntityMixin {


    protected TurtleMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    public boolean jft$isBreedingKelpItem(ItemStack stack) {
        return stack.isOf(Items.KELP);
    }


    @Override
    protected ActionResult jft$interactMobHook(PlayerEntity player, Hand hand, Operation<ActionResult> original){

        ItemStack itemStack = player.getStackInHand(hand);
        if(jft$isBreedingKelpItem(itemStack)){
            int i = this.getBreedingAge();
            if (!this.getWorld().isClient && i == 0 && this.canEat()) {
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                this.playEatSound();
                return ActionResult.SUCCESS_SERVER;
            }
        }
        return super.jft$interactMobHook(player, hand, original);
    }


}
