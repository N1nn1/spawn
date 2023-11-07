package com.ninni.spawn;

import com.google.common.reflect.Reflection;
import com.ninni.spawn.network.SpawnNetworkHandler;
import com.ninni.spawn.registry.SpawnBiomeModifiers;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import com.ninni.spawn.registry.SpawnBlocks;
import com.ninni.spawn.registry.SpawnCreativeModeTab;
import com.ninni.spawn.registry.SpawnCriteriaTriggers;
import com.ninni.spawn.registry.SpawnDecoratedPotPatterns;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnFeatures;
import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnParticles;
import com.ninni.spawn.registry.SpawnPointsOfInterests;
import com.ninni.spawn.registry.SpawnSoundEvents;
import com.ninni.spawn.registry.SpawnVanillaIntegration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.system.windows.POINT;

//TODO Advancements
@Mod(Spawn.MOD_ID)
public class Spawn {
	public static final String MOD_ID = "spawn";

	public Spawn() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus eventBus = MinecraftForge.EVENT_BUS;
		modEventBus.addListener(this::commonSetup);

		SpawnBlocks.BLOCKS.register(modEventBus);
		SpawnBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);
		SpawnBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
		SpawnCreativeModeTab.CREATIVE_MODE_TABS.register(modEventBus);
		SpawnDecoratedPotPatterns.DECORATED_POT_PATTERNS.register(modEventBus);
		SpawnEntityType.ENTITY_TYPES.register(modEventBus);
		SpawnFeatures.FEATURES.register(modEventBus);
		SpawnParticles.PARTICLE_TYPES.register(modEventBus);
		SpawnSoundEvents.SOUND_EVENTS.register(modEventBus);
		SpawnPointsOfInterests.POI_TYPES.register(modEventBus);
		SpawnItems.ITEMS.register(modEventBus);

		eventBus.register(this);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SpawnVanillaIntegration.serverInit();
			SpawnNetworkHandler.init();
		});
	}
}
