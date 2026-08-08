package xyz.water.rmatrix.mod.carpetjftaddition.tools.frogDyeFeedingAndViviparousBreeding;

import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTAddition;

public class FrogViviparousActivity extends Activity {

    public static FrogViviparousActivity VIVIPAROUS_ACTIVITY;

    public FrogViviparousActivity(String id) {
        super(id);
    }

    private static FrogViviparousActivity register() {
        return Registry.register(Registries.ACTIVITY, Identifier.of(CarpetJFTAddition.MOD_ID, "viviparous"), new FrogViviparousActivity("viviparous"));
    }

    public static void init(){
        VIVIPAROUS_ACTIVITY = register();
    }
}
