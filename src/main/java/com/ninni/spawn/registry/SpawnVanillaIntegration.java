package com.ninni.spawn.registry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.ninni.spawn.Spawn;
import com.ninni.spawn.mixin.ComposterBlockAccessor;
import com.ninni.spawn.mixin.FireBlockAccessor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;

import java.util.Map;

public class SpawnVanillaIntegration {

    public static void serverInit() {
        registerStrippables();
        registerFlammables();
        registerCompostables();
        registerPotPatterns();
    }

    private static void registerPotPatterns() {
        ImmutableMap.Builder<Item, ResourceKey<String>> map = ImmutableMap.builder();
        map.putAll(DecoratedPotPatterns.ITEM_TO_POT_TEXTURE);
        map.put(SpawnItems.CROWN_POTTERY_SHERD.get(), SpawnDecoratedPotPatterns.CROWN.getKey());
        map.put(SpawnItems.SPADE_POTTERY_SHERD.get(), SpawnDecoratedPotPatterns.SPADE.getKey());
        DecoratedPotPatterns.ITEM_TO_POT_TEXTURE = map.build();
    }

    private static void registerStrippables() {
        Map<Block, Block> map = Maps.newHashMap(AxeItem.STRIPPABLES);
        map.put(SpawnBlocks.ROTTEN_LOG.get(), SpawnBlocks.STRIPPED_ROTTEN_LOG.get());
        map.put(SpawnBlocks.ROTTEN_WOOD.get(), SpawnBlocks.STRIPPED_ROTTEN_WOOD.get());
    }

    private static void registerFlammables() {
        FireBlockAccessor accessor = (FireBlockAccessor) Blocks.FIRE;
        accessor.callSetFlammable(SpawnBlocks.FALLEN_LEAVES.get(), 60, 100);
        accessor.callSetFlammable(SpawnBlocks.SUNFLOWER.get(), 60, 100);
        accessor.callSetFlammable(SpawnBlocks.ROTTEN_LOG.get(), 5, 5);
        accessor.callSetFlammable(SpawnBlocks.ROTTEN_WOOD.get(), 5, 5);
        accessor.callSetFlammable(SpawnBlocks.STRIPPED_ROTTEN_LOG.get(), 5, 5);
        accessor.callSetFlammable(SpawnBlocks.STRIPPED_ROTTEN_WOOD.get(), 5, 5);
        accessor.callSetFlammable(SpawnBlocks.CRACKED_ROTTEN_PLANKS.get(), 5, 20);
        accessor.callSetFlammable(SpawnBlocks.ROTTEN_PLANKS.get(), 5, 20);
        accessor.callSetFlammable(SpawnBlocks.ROTTEN_SLAB.get(), 5, 20);
        accessor.callSetFlammable(SpawnBlocks.ROTTEN_STAIRS.get(), 5, 20);
        accessor.callSetFlammable(SpawnBlocks.ROTTEN_FENCE.get(), 5, 20);
        accessor.callSetFlammable(SpawnBlocks.ROTTEN_FENCE_GATE.get(), 5, 20);
    }

    private static void registerCompostables() {
        ComposterBlockAccessor.callAdd(0.25F, SpawnItems.FALLEN_LEAVES.get());
        ComposterBlockAccessor.callAdd(0.65F, SpawnItems.SUNFLOWER.get());
        ComposterBlockAccessor.callAdd(0.8F, SpawnItems.TUNA_SANDWICH.get());
        ComposterBlockAccessor.callAdd(0.8F, SpawnItems.SNAIL_SHELL.get());
        ComposterBlockAccessor.callAdd(1.0F, SpawnItems.ROTTEN_LOG.get());
        ComposterBlockAccessor.callAdd(1.0F, SpawnItems.ROTTEN_WOOD.get());
        ComposterBlockAccessor.callAdd(1.0F, SpawnItems.STRIPPED_ROTTEN_LOG.get());
        ComposterBlockAccessor.callAdd(1.0F, SpawnItems.STRIPPED_ROTTEN_WOOD.get());
        ComposterBlockAccessor.callAdd(1.0F, SpawnItems.CRACKED_ROTTEN_PLANKS.get());
        ComposterBlockAccessor.callAdd(1.0F, SpawnItems.ROTTEN_PLANKS.get());
    }

}
