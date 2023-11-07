package com.ninni.spawn.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.ninni.spawn.Spawn.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnDecoratedPotPatterns {

    public static final DeferredRegister<String> DECORATED_POT_PATTERNS = DeferredRegister.create(Registries.DECORATED_POT_PATTERNS, MOD_ID);

    public static final RegistryObject<String> CROWN = DECORATED_POT_PATTERNS.register("crown_pottery_pattern", () -> MOD_ID + ":crown_pottery_pattern");
    public static final RegistryObject<String> SPADE = DECORATED_POT_PATTERNS.register("spade_pottery_pattern", () -> MOD_ID + ":spade_pottery_pattern");

}
