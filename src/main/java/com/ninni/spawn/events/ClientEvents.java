package com.ninni.spawn.events;

import com.google.common.collect.Lists;
import com.ninni.spawn.Spawn;
import com.ninni.spawn.client.model.AnglerFishModel;
import com.ninni.spawn.client.model.AntModel;
import com.ninni.spawn.client.model.HamsterModel;
import com.ninni.spawn.client.model.SeahorseModel;
import com.ninni.spawn.client.model.SnailModel;
import com.ninni.spawn.client.model.TunaEggModel;
import com.ninni.spawn.client.model.TunaModel;
import com.ninni.spawn.client.particles.TunaEggParticle;
import com.ninni.spawn.client.renderer.entity.AnglerFishRenderer;
import com.ninni.spawn.client.renderer.entity.AntRenderer;
import com.ninni.spawn.client.renderer.entity.HamsterRenderer;
import com.ninni.spawn.client.renderer.entity.SeahorseRenderer;
import com.ninni.spawn.client.renderer.entity.SnailRenderer;
import com.ninni.spawn.client.renderer.entity.TunaEggRenderer;
import com.ninni.spawn.client.renderer.entity.TunaRenderer;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnParticles;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.ninni.spawn.registry.SpawnItems.*;

@Mod.EventBusSubscriber(modid = Spawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    private static final Function<ItemLike, ItemStack> FUNCTION = ItemStack::new;

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SpawnEntityModelLayers.ANGLER_FISH, AnglerFishModel::getLayerDefinition);
        event.registerLayerDefinition(SpawnEntityModelLayers.ANGLER_FISH_DEFLATED, AnglerFishModel::getDeflatedLayerDefinition);
        event.registerLayerDefinition(SpawnEntityModelLayers.TUNA, TunaModel::getLayerDefinition);
        event.registerLayerDefinition(SpawnEntityModelLayers.TUNA_EGG, TunaEggModel::getLayerDefinition);
        event.registerLayerDefinition(SpawnEntityModelLayers.SEAHORSE, SeahorseModel::getLayerDefinition);
        event.registerLayerDefinition(SpawnEntityModelLayers.SNAIL, SnailModel::getLayerDefinition);
        event.registerLayerDefinition(SpawnEntityModelLayers.HAMSTER, HamsterModel::getLayerDefinition);
        event.registerLayerDefinition(SpawnEntityModelLayers.HAMSTER_STANDING, HamsterModel::getStandingLayerDefinition);
        event.registerLayerDefinition(SpawnEntityModelLayers.ANT, AntModel::getLayerDefinition);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SpawnEntityType.ANGLER_FISH.get(), AnglerFishRenderer::new);
        event.registerEntityRenderer(SpawnEntityType.TUNA.get(), TunaRenderer::new);
        event.registerEntityRenderer(SpawnEntityType.TUNA_EGG.get(), TunaEggRenderer::new);
        event.registerEntityRenderer(SpawnEntityType.SEAHORSE.get(), SeahorseRenderer::new);
        event.registerEntityRenderer(SpawnEntityType.SNAIL.get(), SnailRenderer::new);
        event.registerEntityRenderer(SpawnEntityType.HAMSTER.get(), HamsterRenderer::new);
        event.registerEntityRenderer(SpawnEntityType.ANT.get(), AntRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SpawnParticles.ANGLER_FISH_LANTERN_GLOW.get(), GlowParticle.GlowSquidProvider::new);
        event.registerSpriteSet(SpawnParticles.TUNA_EGG.get(), TunaEggParticle.Factory::new);
    }

    private static void addAfter(MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> map, ItemLike after, ItemLike... block) {
        List<ItemLike> stream = Lists.newArrayList(Arrays.stream(block).toList());
        Collections.reverse(stream);
        stream.forEach(blk -> addAfter(map, after, blk));
    }

    private static void addBefore(MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> map, ItemLike before, ItemLike... block) {
        List<ItemLike> stream = Lists.newArrayList(Arrays.stream(block).toList());
        Collections.reverse(stream);
        stream.forEach(blk -> addBefore(map, before, blk));
    }

    private static void addAfter(MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> map, ItemLike after, ItemLike block) {
        map.putAfter(FUNCTION.apply(after), FUNCTION.apply(block), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    private static void addBefore(MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> map, ItemLike before, ItemLike block) {
        map.putBefore(FUNCTION.apply(before), FUNCTION.apply(block), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    private static void accept(MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> map, ItemLike block) {
        map.put(new ItemStack(block), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    @SubscribeEvent
    public static void buildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries = event.getEntries();
        ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
        if (tabKey.equals(CreativeModeTabs.BUILDING_BLOCKS)) {
            addAfter(entries, Items.WAXED_OXIDIZED_CUT_COPPER_SLAB,
                    SpawnItems.BIG_SNAIL_SHELL.get(),
                    SpawnItems.SNAIL_SHELL_TILES.get(),
                    SpawnItems.SNAIL_SHELL_TILE_STAIRS.get(),
                    SpawnItems.SNAIL_SHELL_TILE_SLAB.get()
            );
            addAfter(entries, Items.CHERRY_BUTTON,
                    SpawnItems.ROTTEN_LOG.get(),
                    SpawnItems.ROTTEN_WOOD.get(),
                    SpawnItems.STRIPPED_ROTTEN_LOG.get(),
                    SpawnItems.STRIPPED_ROTTEN_WOOD.get(),
                    SpawnItems.ROTTEN_PLANKS.get(),
                    SpawnItems.CRACKED_ROTTEN_PLANKS.get(),
                    SpawnItems.ROTTEN_STAIRS.get(),
                    SpawnItems.ROTTEN_SLAB.get(),
                    SpawnItems.ROTTEN_FENCE.get(),
                    SpawnItems.ROTTEN_FENCE_GATE.get(),
                    SpawnItems.ROTTEN_DOOR.get(),
                    SpawnItems.ROTTEN_TRAPDOOR.get()
            );
        }
        if (tabKey.equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
            addAfter(entries, Items.SALMON_BUCKET, SpawnItems.TUNA_EGG_BUCKET.get());
            addAfter(entries, Items.COD_BUCKET, SpawnItems.ANGLER_FISH_BUCKET.get());
            addAfter(entries, Items.TROPICAL_FISH_BUCKET, SpawnItems.SEAHORSE_BUCKET.get());
            addAfter(entries, Items.MILK_BUCKET, SpawnItems.ANT_PUPA.get());
            addAfter(entries, Items.MUSIC_DISC_RELIC, SpawnItems.MUSIC_DISC_ROT.get());
        }
        if (tabKey.equals(CreativeModeTabs.REDSTONE_BLOCKS)) {
            addAfter(entries, Items.HONEY_BLOCK, SpawnItems.MUCUS_BLOCK.get(), SpawnItems.GHOSTLY_MUCUS_BLOCK.get());
        }
        if (tabKey.equals(CreativeModeTabs.NATURAL_BLOCKS)) {
            addBefore(entries, Items.LILAC, SpawnItems.SUNFLOWER.get());
            addAfter(entries, Items.BEETROOT_SEEDS, SUNFLOWER_SEEDS.get());
            addAfter(entries, Items.HONEY_BLOCK, MUCUS_BLOCK.get(), BIG_SNAIL_SHELL.get());
            addAfter(entries, Items.FROGSPAWN, SNAIL_EGGS.get());
            addAfter(entries, Items.HONEYCOMB_BLOCK, ANTHILL.get(), ROTTEN_LOG_ANTHILL.get(), ANT_MOUND.get());
            addAfter(entries, Items.CHERRY_LOG, ROTTEN_LOG.get());
            addAfter(entries, Items.DEAD_BUSH, FALLEN_LEAVES.get());
        }
        if (tabKey.equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            addAfter(entries, Items.GLOW_LICHEN, MUCUS.get());
            addAfter(entries, Items.DECORATED_POT, POTTED_SWEET_BERRIES.get());
            addAfter(entries, Items.BEE_NEST, ANTHILL.get(), ROTTEN_LOG_ANTHILL.get());
            addAfter(entries, Items.BEEHIVE, ANT_FARM.get(), ANT_MOUND.get());
        }
        if (tabKey.equals(CreativeModeTabs.FOOD_AND_DRINKS)) {
            addAfter(entries, Items.MELON_SLICE, ROASTED_SUNFLOWER_SEEDS.get());
            addAfter(entries, Items.PUFFERFISH, ESCARGOT.get());
            addAfter(entries, Items.COOKED_SALMON,
                    TUNA_CHUNK.get(),
                    COOKED_TUNA_CHUNK.get(),
                    ANGLER_FISH.get()
            );
            addAfter(entries, Items.BREAD, TUNA_SANDWICH.get());
        }
        if (tabKey.equals(CreativeModeTabs.INGREDIENTS)) {
            addAfter(entries, Items.BURN_POTTERY_SHERD, CROWN_POTTERY_SHERD.get());
            addAfter(entries, Items.SNORT_POTTERY_SHERD, SPADE_POTTERY_SHERD.get());
            addAfter(entries, Items.HONEYCOMB, SNAIL_SHELL.get());
            addAfter(entries, Items.SCUTE, MUCUS.get());
        }
        if (tabKey.equals(CreativeModeTabs.SPAWN_EGGS)) {
            addAfter(entries, Items.ALLAY_SPAWN_EGG, ANGLER_FISH_SPAWN_EGG.get(), ANT_SPAWN_EGG.get());
            addAfter(entries, Items.SALMON_SPAWN_EGG, SEAHORSE_SPAWN_EGG.get());
            addAfter(entries, Items.SLIME_SPAWN_EGG, SNAIL_SPAWN_EGG.get());
            addAfter(entries, Items.TROPICAL_FISH_SPAWN_EGG, TUNA_SPAWN_EGG.get());
        }
    }

}
