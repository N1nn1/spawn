package com.ninni.spawn;

import com.google.common.reflect.Reflection;
import com.ninni.spawn.client.model.SpawnEntityModelLayers;
import com.ninni.spawn.client.renderer.entity.SnailRenderer;
import com.ninni.spawn.registry.SpawnBlocks;
import com.ninni.spawn.registry.SpawnEntityType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

public class SpawnClient implements ClientModInitializer {

	@Override
	@SuppressWarnings("UnstableApiUsage")
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

		Reflection.initialize(SpawnEntityModelLayers.class);
		EntityRendererRegistry.register(SpawnEntityType.SNAIL, SnailRenderer::new);
	}
}