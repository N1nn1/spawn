package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class SpawnConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SUNFLOWER = createKey("patch_sunflower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_ANTHILL = createKey("small_anthill");

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String string) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Spawn.MOD_ID, string));
    }
}
