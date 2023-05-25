package com.ninni.spawn;

import com.google.common.reflect.Reflection;
import com.ninni.spawn.client.inventory.HamsterInventoryMenu;
import com.ninni.spawn.client.inventory.HamsterInventoryScreen;
import com.ninni.spawn.client.particles.TunaEggParticle;
import com.ninni.spawn.client.renderer.entity.AnglerFishRenderer;
import com.ninni.spawn.client.renderer.entity.HamsterRenderer;
import com.ninni.spawn.client.renderer.entity.SeahorseRenderer;
import com.ninni.spawn.client.renderer.entity.SnailRenderer;
import com.ninni.spawn.client.renderer.entity.TunaEggRenderer;
import com.ninni.spawn.client.renderer.entity.TunaRenderer;
import com.ninni.spawn.entity.Hamster;
import com.ninni.spawn.registry.SpawnBlocks;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class SpawnClient implements ClientModInitializer {

	public static final ResourceLocation OPEN_HAMSTER_SCREEN = new ResourceLocation(Spawn.MOD_ID, "open_hamster_screen");

	@Override
	public void onInitializeClient() {

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(),
				SpawnBlocks.MUCUS,
				SpawnBlocks.MUCUS_BLOCK,
				SpawnBlocks.GHOSTLY_MUCUS_BLOCK,
				SpawnBlocks.SNAIL_EGGS
		);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
				SpawnBlocks.POTTED_SWEET_BERRY_BUSH,
				SpawnBlocks.SUNFLOWER,
				SpawnBlocks.SUNFLOWER_PLANT
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

		ClientPlayNetworking.registerGlobalReceiver(OPEN_HAMSTER_SCREEN, (client, handler, buf, responseSender) -> {
			int id = buf.readInt();
			Level level = client.level;
			Optional.ofNullable(level).ifPresent(world -> {
				Entity entity = world.getEntity(id);
				if (entity instanceof Hamster hamster) {
					int slotCount = buf.readInt();
					int syncId = buf.readInt();
					LocalPlayer clientPlayerEntity = client.player;
					SimpleContainer simpleInventory = new SimpleContainer(slotCount);
					assert clientPlayerEntity != null;
					HamsterInventoryMenu reindeerScreenHandler = new HamsterInventoryMenu(syncId, clientPlayerEntity.getInventory(), simpleInventory, hamster);
					clientPlayerEntity.containerMenu = reindeerScreenHandler;
					client.execute(() -> client.setScreen(new HamsterInventoryScreen(reindeerScreenHandler, clientPlayerEntity.getInventory(), hamster)));
				}
			});
		});
	}
}