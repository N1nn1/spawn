package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.item.EscargotItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;

@SuppressWarnings("unused")
public class SpawnItems {

    public static final Item SPAWN = register("spawn", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC).fireproof()));

    public static final Item SNAIL_SPAWN_EGG = register("snail_spawn_egg", new SpawnEggItem(SpawnEntityType.SNAIL, 0x5D3F30, 0xF6DEA2, new Item.Properties().stacksTo(64)));
    public static final Item SNAIL_EGGS = register("snail_eggs", new BlockItem(SpawnBlocks.SNAIL_EGGS, new FabricItemSettings()));
    public static final Item SNAIL_SHELL = register("snail_shell", new Item(new FabricItemSettings()));
    public static final Item BIG_SNAIL_SHELL = register("big_snail_shell", new BlockItem(SpawnBlocks.BIG_SNAIL_SHELL, new FabricItemSettings()));
    public static final Item SNAIL_SHELL_TILES = register("snail_shell_tiles", new BlockItem(SpawnBlocks.SNAIL_SHELL_TILES, new FabricItemSettings()));
    public static final Item SNAIL_SHELL_TILE_STAIRS = register("snail_shell_tile_stairs", new BlockItem(SpawnBlocks.SNAIL_SHELL_TILE_STAIRS, new FabricItemSettings()));
    public static final Item SNAIL_SHELL_TILE_SLAB = register("snail_shell_tile_slab", new BlockItem(SpawnBlocks.SNAIL_SHELL_TILE_SLAB, new FabricItemSettings()));
    public static final Item ESCARGOT = register("escargot", new EscargotItem(new FabricItemSettings().recipeRemainder(SNAIL_SHELL).maxCount(1).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.8f).build())));
    public static final Item POTTED_SWEET_BERRIES = register("potted_sweet_berries", new BlockItem(SpawnBlocks.POTTED_SWEET_BERRY_BUSH, new FabricItemSettings().recipeRemainder(SNAIL_SHELL).maxCount(16)));
    public static final Item MUCUS = register("mucus", new BlockItem(SpawnBlocks.MUCUS, new FabricItemSettings()));
    public static final Item MUCUS_BLOCK = register("mucus_block", new BlockItem(SpawnBlocks.MUCUS_BLOCK, new FabricItemSettings()));
    public static final Item GHOSTLY_MUCUS_BLOCK = register("ghostly_mucus_block", new BlockItem(SpawnBlocks.GHOSTLY_MUCUS_BLOCK, new FabricItemSettings()));


    private static Item register(String id, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Spawn.MOD_ID, id), item);
    }
}
