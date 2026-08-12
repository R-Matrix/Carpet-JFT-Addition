package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.frogDyeFeedingAndViviparousBreeding;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;
import xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.turtleKelpFeedingAndViviparousBreeding.AnimalEntityMixin;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.frogDyeFeedingAndViviparousBreeding.AddViviparousSpawnActivities;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.frogDyeFeedingAndViviparousBreeding.FrogEntityDyeFlagAccess;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.frogDyeFeedingAndViviparousBreeding.FrogMemoryModuleType;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.turtleKelpFeedingAndViviparousBreeding.ControlBeViviparousAccess;


@Mixin(FrogEntity.class)
public abstract class FrogEntityMixin extends AnimalEntityMixin
        implements VariantHolder<RegistryEntry<FrogVariant>>, ControlBeViviparousAccess, FrogEntityDyeFlagAccess {

    protected FrogEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }


    @Final
    @Shadow
    protected static ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;

    @Final
    @Shadow
    protected static ImmutableList<SensorType<? extends Sensor<? super FrogEntity>>> SENSORS;

    @Unique
    private int breedingDyeFlag = -1;

    @Unique
    private boolean jft$shouldBeViviparous = false;

    @Unique
    public boolean jft$isBreedingDyeItem(ItemStack stack) {
        if(CarpetJFTSettings.frogDyeFeedingAndViviparousBreeding) {
            if (stack.isOf(Items.GREEN_DYE)) {
                this.jft$setBreedingDyeFlag(1);
                return true;
            }
            if (stack.isOf(Items.WHITE_DYE)) {
                this.jft$setBreedingDyeFlag(2);
                return true;
            }
            if (stack.isOf(Items.ORANGE_DYE)) {
                this.jft$setBreedingDyeFlag(0);
                return true;
            }
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


    @Inject(method = "breed", at = @At("TAIL"))
    private void setFlagObBreed(ServerWorld world, AnimalEntity other, CallbackInfo ci){
        if(CarpetJFTSettings.frogDyeFeedingAndViviparousBreeding && this.jft$shouldBeViviparous()) {
            this.getBrain().remember(FrogMemoryModuleType.DYE_FLAG, this.jft$getBreedingDyeFlag());
        }
        this.setJft$shouldBeViviparous(false);
        this.jft$setBreedingDyeFlag(-1);
    }


    @WrapMethod(method = "createBrainProfile")
    private Brain.Profile<FrogEntity> addMyMemoryModuleType(Operation<Brain.Profile<FrogEntity>> original){

        ImmutableList<MemoryModuleType<?>> newMemoryModel =
                ImmutableList.<MemoryModuleType<?>>builder()
                    .addAll(MEMORY_MODULES)
                    .add(FrogMemoryModuleType.DYE_FLAG)
                    .build();

        return Brain.createProfile(newMemoryModel, SENSORS);
    }


    @ModifyReturnValue(method = "deserializeBrain", at = @At("RETURN"))
    private Brain<FrogEntity> addMyActivityAtBrain(Brain<FrogEntity> original){

        AddViviparousSpawnActivities.jft$addViviparousSpawnActivities(original);
        return original;
    }




    @Override
    public int jft$getBreedingDyeFlag() {
        return this.breedingDyeFlag;
    }

    @Override
    public void jft$setBreedingDyeFlag(int value){
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
