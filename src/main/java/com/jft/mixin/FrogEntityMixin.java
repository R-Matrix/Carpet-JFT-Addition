package com.jft.mixin;

import com.jft.mixin.rules.turtleKelpFeedingAndViviparousBreeding.AnimalEntityMixin;
import com.jft.toolsMager.turtleKelpFeedingAndViviparousBreeding.ControlBeViviparousAccess;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FrogEntity.class)
public abstract class FrogEntityMixin extends AnimalEntityMixin implements VariantHolder<RegistryEntry<FrogVariant>>, ControlBeViviparousAccess {

    protected FrogEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private int breedingDyeFlag = -1;

    @Unique
    private boolean jft$shouldBeViviparous = false;

    @ModifyReturnValue(method = "isBreedingItem", at = @At("RETURN"))
    private boolean jft$recoverBreedingDyeFlag(boolean original){
        if(original) setBreedingDyeFlag(-1);
        return original;
    }

    @Unique
    public boolean jft$isBreedingDyeItem(ItemStack stack) {
        if (stack.isOf(Items.GREEN_DYE)) {
            setBreedingDyeFlag(0);
            return true;
        }
        if (stack.isOf(Items.WHITE_DYE)) {
            setBreedingDyeFlag(1);
            return true;
        }
        if (stack.isOf(Items.ORANGE_DYE)) {
            setBreedingDyeFlag(2);
            return true;
        }
        return false;
    }

    @Override
    protected ActionResult jft$interactMobHook(PlayerEntity player, Hand hand, Operation<ActionResult> original){
        ItemStack itemStack = player.getStackInHand(hand);
        if(jft$isBreedingDyeItem(itemStack)){
            int i = this.getBreedingAge();
            if (!this.getWorld().isClient && i == 0 && this.canEat()) {
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                this.playEatSound();
                this.setJft$shouldBeViviparous(true);
                return ActionResult.SUCCESS_SERVER;
            }
        }
        return super.jft$interactMobHook(player, hand, original);
    }




    @Unique
    public int getBreedingDyeFlag() {
        return this.breedingDyeFlag;
    }

    @Unique
    public void setBreedingDyeFlag(int value){
        this.breedingDyeFlag = value;
    }

    @Override
    public boolean jft$shouldBeViviparous() {
        return this.jft$shouldBeViviparous;
    }

    @Override
    public void setJft$shouldBeViviparous(boolean value) {
        this.jft$shouldBeViviparous = value;
    }
}
