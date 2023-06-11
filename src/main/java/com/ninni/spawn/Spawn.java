package com.ninni.spawn;

import com.google.common.reflect.Reflection;
import com.ninni.spawn.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
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
				SpawnDecoratedPotPatterns.class,
				SpawnItems.class,
				SpawnBlocks.class
		);
		BiomeModifications.create(new ResourceLocation(Spawn.MOD_ID, "replace_sunflower_patch")).add(ModificationPhase.REPLACEMENTS, biomeSelectionContext -> biomeSelectionContext.hasPlacedFeature(VegetationPlacements.PATCH_SUNFLOWER), biomeModificationContext -> {
			BiomeModificationContext.GenerationSettingsContext generationSettings = biomeModificationContext.getGenerationSettings();
			if (generationSettings.removeFeature(VegetationPlacements.PATCH_SUNFLOWER)) {
				generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SpawnPlacedFeatures.PATCH_SUNFLOWER);
			}
		});
	}
}
