package com.ninni.spawn;

import com.google.common.reflect.Reflection;
import com.ninni.spawn.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;

public class Spawn implements ModInitializer {
	public static final String MOD_ID = "spawn";

	@Override
	public void onInitialize() {
		Reflection.initialize(
				SpawnSoundEvents.class,
				SpawnEntityType.class,
				SpawnFeatures.class,
				SpawnBlockEntityTypes.class,
				SpawnCreativeModeTab.class,
				SpawnItems.class,
				SpawnBlocks.class,
				SpawnPointsOfInterests.class,
				SpawnDecoratedPotPatterns.class
		);

		SpawnVanillaIntegration.serverInit();
	}
}
