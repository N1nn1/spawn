package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.level.AnthillFeature;
import com.ninni.spawn.level.SunflowerFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SpawnFeatures {

    public static final Feature<NoneFeatureConfiguration> SUNFLOWER = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Spawn.MOD_ID, "sunflower"), new SunflowerFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> ANTHILL = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Spawn.MOD_ID, "anthill"), new AnthillFeature(NoneFeatureConfiguration.CODEC));

}
