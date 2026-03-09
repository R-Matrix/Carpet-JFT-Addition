package com.jft.toolsMager.frogDyeFeedingAndViviparousBreeding;

import com.jft.CarpetJFTAddition;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class FrogDyeTemptationSensorType {

        public static SensorType<FrogDyeTemptationSensor> FROG_DYE_TEMPTATION;

        public static void init() {
            FROG_DYE_TEMPTATION = Registry.register(
                    Registries.SENSOR_TYPE,
                    Identifier.of(CarpetJFTAddition.MOD_ID, "frog_dye_temptation"),
                    new SensorType<>(FrogDyeTemptationSensor::new)
            );
        }
}
