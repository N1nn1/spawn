package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.item.EscargotItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;

@SuppressWarnings("unused")
public class SpawnItems {

    public static final Item SPAWN = register("spawn", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC).fireproof()));

    public static final Item ANGLER_FISH_SPAWN_EGG = register("angler_fish_spawn_egg", new SpawnEggItem(SpawnEntityType.ANGLER_FISH, 0x2A3327, 0x95D930, new Item.Properties().stacksTo(64)));
    public static final Item ANGLER_FISH_BUCKET = register("angler_fish_bucket", new MobBucketItem(SpawnEntityType.ANGLER_FISH, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final Item ANGLER_FISH = register("angler_fish", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 3 * 20), 1.0F).alwaysEat().build())));

    public static final Item TUNA_SPAWN_EGG = register("tuna_spawn_egg", new SpawnEggItem(SpawnEntityType.TUNA, 0x4572B3, 0xE9B936, new Item.Properties().stacksTo(64)));
    public static final Item TUNA_CHUNK = register("tuna_chunk", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).build())));
    public static final Item COOKED_TUNA_CHUNK = register("cooked_tuna_chunk", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).build())));
    public static final Item TUNA_SANDWICH = register("tuna_sandwich", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(8).saturationMod(1.0f).build()).stacksTo(32)));
    public static final Item TUNA_EGG_BUCKET = register("tuna_egg_bucket", new MobBucketItem(SpawnEntityType.TUNA_EGG, Fluids.WATER, SpawnSoundEvents.BUCKET_EMPTY_TUNA_EGG, new Item.Properties().stacksTo(1)));

    public static final Item SEAHORSE_SPAWN_EGG = register("seahorse_spawn_egg", new SpawnEggItem(SpawnEntityType.SEAHORSE, 0xFBC738, 0xFFFFFF, new Item.Properties().stacksTo(64)));
    public static final Item SEAHORSE_BUCKET = register("seahorse_bucket", new MobBucketItem(SpawnEntityType.SEAHORSE, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

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

    public static final Item HAMSTER_SPAWN_EGG = register("hamster_spawn_egg", new SpawnEggItem(SpawnEntityType.HAMSTER, 0x92816C, 0xFFFFFF, new Item.Properties().stacksTo(64)));
    public static final Item SUNFLOWER = register("sunflower", new BlockItem(SpawnBlocks.SUNFLOWER, new FabricItemSettings()));
    public static final Item SUNFLOWER_SEEDS = register("sunflower_seeds", new ItemNameBlockItem(SpawnBlocks.SUNFLOWER_PLANT, new FabricItemSettings()));
    public static final Item ROASTED_SUNFLOWER_SEEDS = register("roasted_sunflower_seeds", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).fast().build())));

    public static final Item ANT_SPAWN_EGG = register("ant_spawn_egg", new SpawnEggItem(SpawnEntityType.ANT, 0x3C3436, 0x282527, new Item.Properties().stacksTo(64)));

    private static Item register(String id, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Spawn.MOD_ID, id), item);
    }
}
