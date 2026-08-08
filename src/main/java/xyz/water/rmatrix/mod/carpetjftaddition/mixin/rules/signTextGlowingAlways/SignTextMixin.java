package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.signTextGlowingAlways;

import net.minecraft.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

@Mixin(SignText.class)
public abstract class SignTextMixin {

    @ModifyArgs(method = "<init>()V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/entity/SignText;<init>([Lnet/minecraft/text/Text;[Lnet/minecraft/text/Text;Lnet/minecraft/util/DyeColor;Z)V"))
    private static void se(Args args){

        if(CarpetJFTSettings.signTextGlowingAlways) args.set(3, true);

    }
}
