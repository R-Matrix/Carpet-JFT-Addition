package com.jft.toolsMager.Frog;

import com.jft.CarpetJFTAddition;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class FrogMemoryModuleType<U> {

    public static MemoryModuleType<Integer> DYE_FLAG ;
    public static MemoryModuleType<Boolean> VIVIPAROUS_MODEL;

    public static <U> MemoryModuleType<U> register(String id) {
        return Registry.register(
                Registries.MEMORY_MODULE_TYPE,
                Identifier.of(CarpetJFTAddition.MOD_ID, id),
                new MemoryModuleType<>(Optional.empty())  // 无 Codec
        );
    }

    public static void init() {
        DYE_FLAG = register("dye_flag");
        VIVIPAROUS_MODEL = register("viviparous_model");
    } // 触发静态初始化


}
