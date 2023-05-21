package com.ninni.spawn;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import static com.ninni.spawn.Spawn.MOD_ID;

public interface SpawnTags {
    TagKey<Item> SNAIL_TEMPT_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "snail_tempt_items"));

    TagKey<Block> MUCUS_SOLIDIFIER = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "mucus_solidifier"));
    TagKey<Block> MUCUS_SOLIDIFICATION_SPREADER = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "mucus_solidification_spreader"));

    TagKey<Biome> SNAIL_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "snail_spawns"));
}