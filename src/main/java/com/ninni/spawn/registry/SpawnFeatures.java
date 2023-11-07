package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.level.AnthillConfig;
import com.ninni.spawn.level.AnthillFeature;
import com.ninni.spawn.level.RottenLogFeature;
import com.ninni.spawn.level.RottenLogStumpFeature;
import com.ninni.spawn.level.SunflowerFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Spawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Spawn.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SUNFLOWER = FEATURES.register("sunflower", () -> new SunflowerFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<AnthillConfig>> ANTHILL = FEATURES.register("anthill", () -> new AnthillFeature(AnthillConfig.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ROTTEN_LOG = FEATURES.register("rotten_log", () -> new RottenLogFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ROTTEN_LOG_STUMP = FEATURES.register("rotten_log_stump", () -> new RottenLogStumpFeature(NoneFeatureConfiguration.CODEC));

}
