package com.jft.toolsMager.Frog;

import com.jft.CarpetJFTAddition;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FrogViviparousActivity extends Activity {

    public static FrogViviparousActivity VIVIPAROUS_ACTIVITY;

    public FrogViviparousActivity(String id) {
        super(id);
    }

    private static FrogViviparousActivity register(String id) {
        return Registry.register(Registries.ACTIVITY, Identifier.of(CarpetJFTAddition.MOD_ID, id), new FrogViviparousActivity(id));
    }

    public static void init(){
        VIVIPAROUS_ACTIVITY = register("viviparous");
    }
}
