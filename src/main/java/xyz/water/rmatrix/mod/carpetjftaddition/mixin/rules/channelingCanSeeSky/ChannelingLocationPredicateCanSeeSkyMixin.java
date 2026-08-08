package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.channelingCanSeeSky;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;
import xyz.water.rmatrix.mod.carpetjftaddition.tools.channelingWeather.ChannelingHelper;


@Mixin(LocationPredicate.class)
public abstract class ChannelingLocationPredicateCanSeeSkyMixin {


    @WrapOperation(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;isSkyVisible(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean InjectLocationPredicate(ServerWorld instance, BlockPos blockPos, Operation<Boolean> original) {
        if(!CarpetJFTSettings.channelingCanSeeSky && ChannelingHelper.isFromChannelingFlag()){
            ChannelingHelper.setChannelingFlag(false);
            return true;
        }
        return original.call(instance, blockPos);
    }

}
