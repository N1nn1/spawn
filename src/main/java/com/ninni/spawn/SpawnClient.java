package com.ninni.spawn;

import com.ninni.spawn.registry.SpawnVanillaIntegration;
import net.fabricmc.api.ClientModInitializer;

public class SpawnClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SpawnVanillaIntegration.clientInit();
	}
}