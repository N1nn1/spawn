package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.level.AnthillConfig;
import com.ninni.spawn.level.AnthillFeature;
import com.ninni.spawn.level.RottenLogFeature;
import com.ninni.spawn.level.RottenLogStumpFeature;
import com.ninni.spawn.level.SunflowerFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SpawnFeatures {

    public static final Feature<NoneFeatureConfiguration> SUNFLOWER = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Spawn.MOD_ID, "sunflower"), new SunflowerFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<AnthillConfig> ANTHILL = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Spawn.MOD_ID, "anthill"), new AnthillFeature(AnthillConfig.CODEC));
    public static final Feature<NoneFeatureConfiguration> ROTTEN_LOG = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Spawn.MOD_ID, "rotten_log"), new RottenLogFeature(NoneFeatureConfiguration.CODEC));
    public static final Feature<NoneFeatureConfiguration> ROTTEN_LOG_STUMP = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Spawn.MOD_ID, "rotten_log_stump"), new RottenLogStumpFeature(NoneFeatureConfiguration.CODEC));

}
