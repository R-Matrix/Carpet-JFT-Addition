package com.jft.mixin.rules.animalsSpawningSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.jft.CarpetJFTSettings.animalsRaleSetting;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {

    @WrapOperation(method = "collectSpawnableGroups", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/SpawnGroup;isRare()Z"))
    private static boolean se(SpawnGroup instance, Operation<Boolean> original){

        if(instance.getName().equals("creature")){
//            LOGGER.info("Animal spawned!");
            return animalsRaleSetting;
        }
        return original.call(instance);
    }
}
