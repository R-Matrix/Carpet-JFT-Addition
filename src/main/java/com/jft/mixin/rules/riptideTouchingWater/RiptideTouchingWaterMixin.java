package com.jft.mixin.rules.riptideTouchingWater;

import com.jft.toolsMager.riptideTouchingWeather.TridentRiptideHelper;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static com.jft.CarpetJFTSettings.riptideTouchingWater;

@Mixin(Entity.class)
public abstract class RiptideTouchingWaterMixin {

    @Shadow
    public abstract boolean isInLava();

    @Inject(method = "isTouchingWaterOrRain", at = @At("RETURN"), cancellable = true)
    public void InjectEntity(CallbackInfoReturnable<Boolean> cir){
        if(TridentRiptideHelper.jft$getIsOnTridentRiptideUsingOrStopUsing()){
            if(Objects.equals(riptideTouchingWater, "ANY")){
                cir.setReturnValue(true);
            }
            if(Objects.equals(riptideTouchingWater, "DISABLED")){
                cir.setReturnValue(false);
            }
            if(Objects.equals(riptideTouchingWater, "WATERRorLAVA")){
                cir.setReturnValue(cir.getReturnValue() || this.isInLava());
            }
        }
    }
}
