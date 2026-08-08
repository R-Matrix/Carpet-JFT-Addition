package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.frogDyeFeedingAndViviparousBreeding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.ai.brain.sensor.TemptationsSensor;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

@Mixin(TemptationsSensor.class)
public class FrogTemptationSensorMixin {

    @Unique
    private static ThreadLocal<PathAwareEntity> PathAwareEntityRef = new ThreadLocal<>();


    @Inject(method = "sense(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/PathAwareEntity;)V", at = @At("HEAD"))
    private void setPathAwareEntityRef(ServerWorld serverWorld, PathAwareEntity pathAwareEntity, CallbackInfo ci){
        PathAwareEntityRef.set(pathAwareEntity);
    }

    @ModifyReturnValue(method = "test(Lnet/minecraft/item/ItemStack;)Z", at = @At("RETURN"))
    private boolean se(boolean original, ItemStack stack){

        if(PathAwareEntityRef.get() instanceof FrogEntity) {
            PathAwareEntityRef.remove();
            if (CarpetJFTSettings.frogDyeFeedingAndViviparousBreeding)
                return original || stack.isOf(Items.GREEN_DYE) || stack.isOf(Items.ORANGE_DYE) || stack.isOf(Items.WHITE_DYE);
        }
        PathAwareEntityRef.remove();
        return original;
    }

}
