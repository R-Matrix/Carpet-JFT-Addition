package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.animalsSpawningSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings.animalsRaleSetting;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {

    //#if MC >= 12102
    @WrapOperation(method = "collectSpawnableGroups", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/SpawnGroup;isRare()Z"))
    //#else
    //$$ @WrapOperation(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/SpawnGroup;isRare()Z"))
    //#endif
    private static boolean se(SpawnGroup instance, Operation<Boolean> original){

        if(instance.getName().equals("creature")){
//            LOGGER.info("Animal spawned!");
            return animalsRaleSetting;
        }
        return original.call(instance);
    }
}
