package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class SpawnPlacedFeatures {
    public static final ResourceKey<PlacedFeature> PATCH_SUNFLOWER = createKey("patch_sunflower");
    public static final ResourceKey<PlacedFeature> SMALL_ANTHILL = createKey("small_anthill");

    public static ResourceKey<PlacedFeature> createKey(String string) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Spawn.MOD_ID, string));
    }
}
