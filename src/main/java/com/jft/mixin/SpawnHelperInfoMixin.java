package com.jft.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.jft.CarpetJFTSettings.animalsSpanLimit;

@Mixin(SpawnHelper.Info.class)
public class SpawnHelperInfoMixin {

    @WrapOperation(method = "isBelowCap", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/SpawnGroup;getCapacity()I"))
    private int modifyLimit(SpawnGroup instance, Operation<Integer> original){
        if(instance.getName().equals("creature")){
            return animalsSpanLimit == original.call(instance)?
                    original.call(instance) :
                    animalsSpanLimit;
        }
        return original.call(instance);
    }
}
