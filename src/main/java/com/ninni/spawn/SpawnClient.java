package com.ninni.spawn;

import com.google.common.reflect.Reflection;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import com.ninni.spawn.client.particles.TunaEggParticle;
import com.ninni.spawn.client.renderer.entity.*;
import com.ninni.spawn.registry.SpawnBlocks;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.renderer.RenderType;

public class SpawnClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(),
				SpawnBlocks.MUCUS,
				SpawnBlocks.MUCUS_BLOCK,
				SpawnBlocks.GHOSTLY_MUCUS_BLOCK,
				SpawnBlocks.SNAIL_EGGS
		);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
				SpawnBlocks.POTTED_SWEET_BERRY_BUSH
		);

		ParticleFactoryRegistry.getInstance().register(SpawnParticles.ANGLER_FISH_LANTERN_GLOW, GlowParticle.GlowSquidProvider::new);
		ParticleFactoryRegistry.getInstance().register(SpawnParticles.TUNA_EGG, TunaEggParticle.Factory::new);

		Reflection.initialize(SpawnEntityModelLayers.class);
		EntityRendererRegistry.register(SpawnEntityType.ANGLER_FISH, AnglerFishRenderer::new);
		EntityRendererRegistry.register(SpawnEntityType.TUNA, TunaRenderer::new);
		EntityRendererRegistry.register(SpawnEntityType.TUNA_EGG, TunaEggRenderer::new);
		EntityRendererRegistry.register(SpawnEntityType.SEAHORSE, SeahorseRenderer::new);
		EntityRendererRegistry.register(SpawnEntityType.SNAIL, SnailRenderer::new);
		EntityRendererRegistry.register(SpawnEntityType.HAMSTER, HamsterRenderer::new);
	}
}