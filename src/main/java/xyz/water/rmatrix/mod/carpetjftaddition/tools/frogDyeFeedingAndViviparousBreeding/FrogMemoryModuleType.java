package xyz.water.rmatrix.mod.carpetjftaddition.tools.frogDyeFeedingAndViviparousBreeding;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTAddition;

import java.util.Optional;

public class FrogMemoryModuleType{

    public static MemoryModuleType<Integer> DYE_FLAG ;

    public static <U> MemoryModuleType<U> register(String id) {
        return Registry.register(
                Registries.MEMORY_MODULE_TYPE,
                Identifier.of(CarpetJFTAddition.MOD_ID, id),
                new MemoryModuleType<>(Optional.empty())
        );
    }

    public static void init() {
        DYE_FLAG = register("dye_flag");
    }


}
