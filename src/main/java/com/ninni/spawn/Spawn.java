package com.ninni.spawn;

import com.google.common.reflect.Reflection;
import com.ninni.spawn.registry.SpawnCriteriaTriggers;
import com.ninni.spawn.registry.*;
import net.fabricmc.api.ModInitializer;

public class Spawn implements ModInitializer {
	public static final String MOD_ID = "spawn";

	@Override
	public void onInitialize() {
		Reflection.initialize(
				SpawnSoundEvents.class,
				SpawnEntityType.class,
				SpawnFeatures.class,
				SpawnMenuTypes.class,
				SpawnBlockEntityTypes.class,
				SpawnCreativeModeTab.class,
				SpawnItems.class,
				SpawnBlocks.class,
				SpawnCriteriaTriggers.class,
				SpawnParticles.class,
				SpawnPointsOfInterests.class,
				SpawnDecoratedPotPatterns.class
		);
		SpawnVanillaIntegration.serverInit();
	}
}
