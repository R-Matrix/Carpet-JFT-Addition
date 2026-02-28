package com.jft.mixin.rules.changeUseTridentTimeTicks;

import com.jft.CarpetJFTSettings;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Mixin(TridentItem.class)
public abstract class TridentItemUsingTimeMixin {

    @ModifyConstant(method = "onStoppedUsing", constant = @Constant(intValue = 10))
    private int changeUseTridentTimeTicks(int constant){
        return CarpetJFTSettings.changeUseTridentTimeTicks;
    }
}
