package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.turtleKelpFeedingAndViviparousBreeding;

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
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.turtleKelpFeedingAndViviparousBreeding.ControlBeViviparousAccess;

@Mixin(TurtleEntity.class)
public abstract class TurtleEntityMixin extends AnimalEntityMixin implements ControlBeViviparousAccess {


    protected TurtleEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    public boolean jft$isBreedingKelpItem(ItemStack stack) {
        return CarpetJFTSettings.turtleKelpFeedingAndViviparousBreeding && stack.isOf(Items.KELP);
    }

    @Unique
    private boolean jft$shouldBeViviparous = false;


    @Override
    protected ActionResult jft$interactMobHook(PlayerEntity player, Hand hand, Operation<ActionResult> original){

        ItemStack itemStack = player.getStackInHand(hand);
        if(jft$isBreedingKelpItem(itemStack)){
            int i = this.getBreedingAge();
            if (!this.getWorld().isClient && i == 0 && this.canEat()) {
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                //#if MC >= 12102
                this.playEatSound();
                //#else
                //$$ // 1.21.1 的 AnimalEntity 交互流程不播放进食音效, 保持原版行为即可
                //#endif
                this.setJft$shouldBeViviparous(true);
                //#if MC >= 12102
                return ActionResult.SUCCESS_SERVER;
                //#else
                //$$ return ActionResult.SUCCESS;
                //#endif
            }
        }
        return super.jft$interactMobHook(player, hand, original);
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
