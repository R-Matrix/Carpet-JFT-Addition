package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.turtleKelpFeedingAndViviparousBreeding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
//#if MC >= 12108
//$$ import net.minecraft.entity.mob.MobEntity;
//#else
import net.minecraft.entity.mob.PathAwareEntity;
//#endif
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

@Mixin(TemptGoal.class)
public abstract class TemptGoalMixin {

    //#if MC >= 12108
    //$$ @Shadow
    //$$ @Final
    //$$ protected MobEntity mob;
    //#else
    @Shadow
    @Final
    protected PathAwareEntity mob;
    //#endif

    //#if MC >= 12108
    //$$ private MobEntity jft$getMob() {
    //$$     return this.mob;
    //$$ }
    //#else
    private PathAwareEntity jft$getMob() {
        return this.mob;
    }
    //#endif

    @ModifyReturnValue(method = "isTemptedBy", at = @At("RETURN"))
    private boolean asd(boolean original, LivingEntity entity){
        if(CarpetJFTSettings.turtleKelpFeedingAndViviparousBreeding && this.jft$getMob() instanceof TurtleEntity){
            return (entity.getMainHandStack().isOf(Items.KELP) || entity.getOffHandStack().isOf(Items.KELP)) || original;
        }
        return original;
    }
}
