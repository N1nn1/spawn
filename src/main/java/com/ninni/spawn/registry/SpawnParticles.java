package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public interface SpawnParticles {
    SimpleParticleType ANGLER_FISH_LANTERN_GLOW = register("angler_fish_lantern_glow", FabricParticleTypes.simple());
    SimpleParticleType TUNA_EGG = register("tuna_egg", FabricParticleTypes.simple());

    private static SimpleParticleType register(String id, SimpleParticleType type) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(Spawn.MOD_ID, id), type);
    }
}
