package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.goatHayFeedingAndChildScreaming;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.turtleKelpFeedingAndViviparousBreeding.AnimalEntityMixin;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.goatHayFeedingAndChildScreaming.GoatHayFlagAccess;

import static xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings.goatHayFeedingAndChildScreaming;

@Mixin(GoatEntity.class)
public abstract class GoatEntityMixin extends AnimalEntityMixin implements GoatHayFlagAccess {

    protected GoatEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    //#if MC >= 12110
    //$$ @Unique
    //$$ private boolean jft$isClient() {
    //$$     return this.getEntityWorld().isClient();
    //$$ }
    //#else
    @Unique
    private boolean jft$isClient() {
        return this.getWorld().isClient;
    }
    //#endif


    @Unique
    private boolean jft$isBreedingHayBlock(ItemStack itemStack){
        return goatHayFeedingAndChildScreaming && itemStack.isOf(Items.HAY_BLOCK);
    }


    @Unique
    private Boolean jft$HayFlag = false;

    @Override
    public boolean jft$HayFlag(){return this.jft$HayFlag;}

    @Override
    public void setJft$HayFlag(boolean value){this.jft$HayFlag = value;}



    @Override
    protected ActionResult jft$interactMobHook(PlayerEntity player, Hand hand, Operation<ActionResult> original){
        ItemStack itemStack = player.getStackInHand(hand);
        if(jft$isBreedingHayBlock(itemStack)){
            int i = this.getBreedingAge();
            if (!this.jft$isClient() && i == 0 && this.canEat()) {
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                //#if MC >= 12102
                this.playEatSound();
                //#else
                //$$ // 1.21.1 的 AnimalEntity 交互流程不播放进食音效, 保持原版行为即可
                //#endif
                this.setJft$HayFlag(true);
                //#if MC >= 12102
                return ActionResult.SUCCESS_SERVER;
                //#else
                //$$ return ActionResult.SUCCESS;
                //#endif
            }
        }
        return super.jft$interactMobHook(player, hand, original);
    }


    @ModifyArg(method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/GoatEntity;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/GoatEntity;setScreaming(Z)V"))
    private boolean se(boolean screaming,@Local(argsOnly = true) PassiveEntity passiveEntity, @Local(ordinal = 1) PassiveEntity passiveEntity2){

        boolean bl2 = screaming || ((GoatHayFlagAccess)passiveEntity2).jft$HayFlag();

        ((GoatHayFlagAccess)passiveEntity).setJft$HayFlag(false);
        ((GoatHayFlagAccess)passiveEntity2).setJft$HayFlag(false);

        return bl2;
    }

}
