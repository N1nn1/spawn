package com.ninni.spawn.registry;

import com.google.common.reflect.Reflection;
import com.ninni.spawn.Spawn;
import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.client.inventory.HamsterInventoryMenu;
import com.ninni.spawn.client.inventory.HamsterInventoryScreen;
import com.ninni.spawn.client.particles.TunaEggParticle;
import com.ninni.spawn.client.renderer.entity.*;
import com.ninni.spawn.entity.Hamster;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.mixin.biome.NetherBiomePresetMixin;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.Optional;

public class SpawnVanillaIntegration {
    public static final ResourceLocation OPEN_HAMSTER_SCREEN = new ResourceLocation(Spawn.MOD_ID, "open_hamster_screen");

    public static void serverInit() {
        registerBiomeModifications();
        registerStrippables();
        registerFlammables();
        registerCompostables();
    }

    private static void registerBiomeModifications() {
        BiomeModifications.create(new ResourceLocation(Spawn.MOD_ID, "replace_sunflower_patch")).add(ModificationPhase.REPLACEMENTS, biomeSelectionContext -> biomeSelectionContext.hasPlacedFeature(VegetationPlacements.PATCH_SUNFLOWER), biomeModificationContext -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = biomeModificationContext.getGenerationSettings();
            if (generationSettings.removeFeature(VegetationPlacements.PATCH_SUNFLOWER)) {
                generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SpawnPlacedFeatures.PATCH_SUNFLOWER);
            }
        });
        BiomeModifications.addFeature(BiomeSelectors.tag(SpawnTags.SMALL_ANTHILL_GENERATES), GenerationStep.Decoration.VEGETAL_DECORATION, SpawnPlacedFeatures.SMALL_ANTHILL);
    }

    private static void registerStrippables() {
        StrippableBlockRegistry.register(SpawnBlocks.ROTTEN_LOG, SpawnBlocks.STRIPPED_ROTTEN_LOG);
        StrippableBlockRegistry.register(SpawnBlocks.ROTTEN_WOOD, SpawnBlocks.STRIPPED_ROTTEN_WOOD);
    }

    private static void registerFlammables() {
        FlammableBlockRegistry defaultInstance = FlammableBlockRegistry.getDefaultInstance();
        defaultInstance.add(SpawnBlocks.FALLEN_LEAVES, 60, 100);
        defaultInstance.add(SpawnBlocks.SUNFLOWER, 60, 100);
        defaultInstance.add(SpawnBlocks.ROTTEN_LOG, 5, 5);
        defaultInstance.add(SpawnBlocks.ROTTEN_WOOD, 5, 5);
        defaultInstance.add(SpawnBlocks.STRIPPED_ROTTEN_LOG, 5, 5);
        defaultInstance.add(SpawnBlocks.STRIPPED_ROTTEN_WOOD, 5, 5);
        defaultInstance.add(SpawnBlocks.CRACKED_ROTTEN_PLANKS, 5, 20);
        defaultInstance.add(SpawnBlocks.ROTTEN_PLANKS, 5, 20);
        defaultInstance.add(SpawnBlocks.ROTTEN_SLAB, 5, 20);
        defaultInstance.add(SpawnBlocks.ROTTEN_STAIRS, 5, 20);
        defaultInstance.add(SpawnBlocks.ROTTEN_FENCE, 5, 20);
        defaultInstance.add(SpawnBlocks.ROTTEN_FENCE_GATE, 5, 20);
    }

    private static void registerCompostables() {
        CompostingChanceRegistry defaultInstance = CompostingChanceRegistry.INSTANCE;
        defaultInstance.add(SpawnItems.FALLEN_LEAVES, 0.25F);
        defaultInstance.add(SpawnItems.SUNFLOWER, 0.65F);
        defaultInstance.add(SpawnItems.TUNA_SANDWICH, 0.8F);
        defaultInstance.add(SpawnItems.SNAIL_SHELL, 0.8F);
        defaultInstance.add(SpawnItems.ROTTEN_LOG, 1F);
        defaultInstance.add(SpawnItems.ROTTEN_WOOD, 1F);
        defaultInstance.add(SpawnItems.STRIPPED_ROTTEN_LOG, 1F);
        defaultInstance.add(SpawnItems.STRIPPED_ROTTEN_WOOD, 1F);
        defaultInstance.add(SpawnItems.CRACKED_ROTTEN_PLANKS, 1F);
        defaultInstance.add(SpawnItems.ROTTEN_PLANKS, 1F);
    }

    @Environment(EnvType.CLIENT)
    public static class Client {

        public static void clientInit() {
            registerModelLayers();
            registerParticles();
            registerBlockRenderLayers();
            registerScreens();
        }

        private static void registerScreens() {
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
                        HamsterInventoryMenu hamsterInventoryMenu = new HamsterInventoryMenu(syncId, clientPlayerEntity.getInventory(), simpleInventory, hamster);
                        clientPlayerEntity.containerMenu = hamsterInventoryMenu;
                        client.execute(() -> client.setScreen(new HamsterInventoryScreen(hamsterInventoryMenu, clientPlayerEntity.getInventory(), hamster)));
                    }
                });
            });
        }

        private static void registerBlockRenderLayers() {
            BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(),
                    SpawnBlocks.MUCUS,
                    SpawnBlocks.MUCUS_BLOCK,
                    SpawnBlocks.GHOSTLY_MUCUS_BLOCK,
                    SpawnBlocks.SNAIL_EGGS
            );
            BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                    SpawnBlocks.FALLEN_LEAVES,
                    SpawnBlocks.ANT_FARM,
                    SpawnBlocks.POTTED_SWEET_BERRY_BUSH,
                    SpawnBlocks.SUNFLOWER,
                    SpawnBlocks.SUNFLOWER_PLANT
            );
        }

        private static void registerModelLayers() {
            Reflection.initialize(SpawnEntityModelLayers.class);
            EntityRendererRegistry.register(SpawnEntityType.ANGLER_FISH, AnglerFishRenderer::new);
            EntityRendererRegistry.register(SpawnEntityType.TUNA, TunaRenderer::new);
            EntityRendererRegistry.register(SpawnEntityType.TUNA_EGG, TunaEggRenderer::new);
            EntityRendererRegistry.register(SpawnEntityType.SEAHORSE, SeahorseRenderer::new);
            EntityRendererRegistry.register(SpawnEntityType.SNAIL, SnailRenderer::new);
            EntityRendererRegistry.register(SpawnEntityType.HAMSTER, HamsterRenderer::new);
            EntityRendererRegistry.register(SpawnEntityType.ANT, AntRenderer::new);
            EntityRendererRegistry.register(SpawnEntityType.WHALE, WhaleRenderer::new);
        }

        private static void registerParticles() {
            ParticleFactoryRegistry.getInstance().register(SpawnParticles.ANGLER_FISH_LANTERN_GLOW, GlowParticle.GlowSquidProvider::new);
            ParticleFactoryRegistry.getInstance().register(SpawnParticles.TUNA_EGG, TunaEggParticle.Factory::new);
        }

    }
}
