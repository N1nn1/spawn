package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Spawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Spawn.MOD_ID);

    public static final RegistryObject<SimpleParticleType> ANGLER_FISH_LANTERN_GLOW = register("angler_fish_lantern_glow", false);
    public static final RegistryObject<SimpleParticleType> TUNA_EGG = register("tuna_egg", false);

    private static RegistryObject<SimpleParticleType> register(String id, boolean flag) {
        return PARTICLE_TYPES.register(id, () -> new SimpleParticleType(flag));
    }
}
