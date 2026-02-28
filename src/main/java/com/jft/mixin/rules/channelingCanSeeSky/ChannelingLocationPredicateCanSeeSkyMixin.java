package com.jft.mixin.rules.channelingCanSeeSky;

import com.jft.CarpetJFTSettings;
import com.jft.toolsMager.channelingWeather.ChannelingHelper;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LocationPredicate.class)
public abstract class ChannelingLocationPredicateCanSeeSkyMixin {

    @Inject(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;isSkyVisible(Lnet/minecraft/util/math/BlockPos;)Z"), cancellable = true)
    private void InjectLocationPredicate(ServerWorld world, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if(!CarpetJFTSettings.channelingCanSeeSky && ChannelingHelper.isFromChannelingFlag()){
            ChannelingHelper.setChannelingFlag(false);
            cir.setReturnValue(true);
        }
     }

}
