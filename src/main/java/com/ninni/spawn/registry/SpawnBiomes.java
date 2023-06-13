package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class SpawnBiomes {

    public static final ResourceKey<Biome> ANT_GARDENS = register("ant_gardens");

    private static ResourceKey<Biome> register(String string) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(Spawn.MOD_ID, string));
    }

}
