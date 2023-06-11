package com.ninni.spawn.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

public class SpawnDecoratedPotPatterns {

    public static final ResourceKey<String> CROWN = register("crown_pottery_pattern");


    public static ResourceKey<String> register(String string) {
        return ResourceKey.create(Registries.DECORATED_POT_PATTERNS, new ResourceLocation(MOD_ID, string));
    }
}
