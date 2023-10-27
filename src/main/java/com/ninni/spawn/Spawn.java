package com.ninni.spawn;

import com.google.common.reflect.Reflection;
import com.ninni.spawn.registry.SpawnCriteriaTriggers;
import com.ninni.spawn.registry.*;
import net.fabricmc.api.ModInitializer;

//TODO Advancements
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
				SpawnCriteriaTriggers.class,
				SpawnPointsOfInterests.class,
				SpawnDecoratedPotPatterns.class
		);
		SpawnVanillaIntegration.serverInit();
	}
}
