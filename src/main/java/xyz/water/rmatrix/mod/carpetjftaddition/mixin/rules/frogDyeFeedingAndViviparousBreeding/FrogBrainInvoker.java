package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.frogDyeFeedingAndViviparousBreeding;

import net.minecraft.entity.ai.brain.task.TargetUtil;
import net.minecraft.entity.passive.FrogBrain;
import net.minecraft.entity.passive.FrogEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FrogBrain.class)
public interface FrogBrainInvoker {

    @Invoker(value = "isNotBreeding")
    static boolean isNotBreeding(FrogEntity frog) {
        return !TargetUtil.hasBreedTarget(frog);
    }
}
