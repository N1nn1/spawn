package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;

public class SpawnItems {

    @SuppressWarnings("unused")
    public static final Item SPAWN = register("spawn", new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC).fireproof()));

    // angler fish
    public static final Item ANGLER_FISH_SPAWN_EGG = register("angler_fish_spawn_egg", new SpawnEggItem(SpawnEntityType.ANGLER_FISH, 0x2A3327, 0x95D930, new Item.Properties()));
    public static final Item ANGLER_FISH_BUCKET = register("angler_fish_bucket", new MobBucketItem(SpawnEntityType.ANGLER_FISH, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final Item ANGLER_FISH = register("angler_fish", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 3 * 20), 1.0F).alwaysEat().build())));

    // tuna
    public static final Item TUNA_SPAWN_EGG = register("tuna_spawn_egg", new SpawnEggItem(SpawnEntityType.TUNA, 0x4572B3, 0xE9B936, new Item.Properties()));
    public static final Item TUNA_CHUNK = register("tuna_chunk", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).build())));
    public static final Item COOKED_TUNA_CHUNK = register("cooked_tuna_chunk", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).build())));
    public static final Item TUNA_SANDWICH = register("tuna_sandwich", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(8).saturationMod(1.0f).build()).stacksTo(32)));
    public static final Item TUNA_EGG_BUCKET = register("tuna_egg_bucket", new MobBucketItem(SpawnEntityType.TUNA_EGG, Fluids.WATER, SpawnSoundEvents.BUCKET_EMPTY_TUNA_EGG, new Item.Properties().stacksTo(1)));

    // seahorse
    public static final Item SEAHORSE_SPAWN_EGG = register("seahorse_spawn_egg", new SpawnEggItem(SpawnEntityType.SEAHORSE, 0xFBC738, 0xFFFFFF, new Item.Properties()));
    public static final Item SEAHORSE_BUCKET = register("seahorse_bucket", new MobBucketItem(SpawnEntityType.SEAHORSE, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    // snail
    public static final Item SNAIL_SPAWN_EGG = register("snail_spawn_egg", new SpawnEggItem(SpawnEntityType.SNAIL, 0x5D3F30, 0xF6DEA2, new Item.Properties()));
    public static final Item SNAIL_EGGS = register("snail_eggs", new BlockItem(SpawnBlocks.SNAIL_EGGS, new FabricItemSettings()));
    public static final Item SNAIL_SHELL = register("snail_shell", new Item(new FabricItemSettings()));
    // snail shell blocks
    public static final Item BIG_SNAIL_SHELL = register("big_snail_shell", new BlockItem(SpawnBlocks.BIG_SNAIL_SHELL, new FabricItemSettings()));
    public static final Item SNAIL_SHELL_TILES = register("snail_shell_tiles", new BlockItem(SpawnBlocks.SNAIL_SHELL_TILES, new FabricItemSettings()));
    public static final Item SNAIL_SHELL_TILE_STAIRS = register("snail_shell_tile_stairs", new BlockItem(SpawnBlocks.SNAIL_SHELL_TILE_STAIRS, new FabricItemSettings()));
    public static final Item SNAIL_SHELL_TILE_SLAB = register("snail_shell_tile_slab", new BlockItem(SpawnBlocks.SNAIL_SHELL_TILE_SLAB, new FabricItemSettings()));
    // misc snail blocks
    public static final Item ESCARGOT = register("escargot", new EscargotItem(new FabricItemSettings().recipeRemainder(SNAIL_SHELL).maxCount(1).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.8f).build())));
    public static final Item POTTED_SWEET_BERRIES = register("potted_sweet_berries", new BlockItem(SpawnBlocks.POTTED_SWEET_BERRY_BUSH, new FabricItemSettings().recipeRemainder(SNAIL_SHELL).maxCount(16)));
    // snail mucus blocks
    public static final Item MUCUS = register("mucus", new BlockItem(SpawnBlocks.MUCUS, new FabricItemSettings()));
    public static final Item MUCUS_BLOCK = register("mucus_block", new BlockItem(SpawnBlocks.MUCUS_BLOCK, new FabricItemSettings()));
    public static final Item GHOSTLY_MUCUS_BLOCK = register("ghostly_mucus_block", new BlockItem(SpawnBlocks.GHOSTLY_MUCUS_BLOCK, new FabricItemSettings()));

    // hamster
    public static final Item HAMSTER_SPAWN_EGG = register("hamster_spawn_egg", new SpawnEggItem(SpawnEntityType.HAMSTER, 0x92816C, 0xFFFFFF, new Item.Properties()));
    // sunflowers
    public static final Item SUNFLOWER = register("sunflower", new BlockItem(SpawnBlocks.SUNFLOWER, new FabricItemSettings()));
    public static final Item SUNFLOWER_SEEDS = register("sunflower_seeds", new ItemNameBlockItem(SpawnBlocks.SUNFLOWER_PLANT, new FabricItemSettings()));
    public static final Item ROASTED_SUNFLOWER_SEEDS = register("roasted_sunflower_seeds", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).fast().build())));

    // ant
    public static final Item ANT_SPAWN_EGG = register("ant_spawn_egg", new SpawnEggItem(SpawnEntityType.ANT, 0x3C3436, 0x282527, new Item.Properties()));
    // ant blocks
    public static final Item ANT_MOUND = register("ant_mound", new BlockItem(SpawnBlocks.ANT_MOUND, new FabricItemSettings()));
    public static final Item ANTHILL = register("anthill", new BlockItem(SpawnBlocks.ANTHILL, new FabricItemSettings()));
    public static final Item ROTTEN_LOG_ANTHILL = register("rotten_log_anthill", new BlockItem(SpawnBlocks.ROTTEN_LOG_ANTHILL, new FabricItemSettings()));
    public static final Item ANT_FARM = register("ant_farm", new BlockItem(SpawnBlocks.ANT_FARM, new FabricItemSettings()));
    // rotten wood
    public static final Item ROTTEN_LOG = register("rotten_log", new BlockItem(SpawnBlocks.ROTTEN_LOG, new FabricItemSettings()));
    public static final Item ROTTEN_WOOD = register("rotten_wood", new BlockItem(SpawnBlocks.ROTTEN_WOOD, new FabricItemSettings()));
    public static final Item STRIPPED_ROTTEN_LOG = register("stripped_rotten_log", new BlockItem(SpawnBlocks.STRIPPED_ROTTEN_LOG, new FabricItemSettings()));
    public static final Item STRIPPED_ROTTEN_WOOD = register("stripped_rotten_wood", new BlockItem(SpawnBlocks.STRIPPED_ROTTEN_WOOD, new FabricItemSettings()));
    public static final Item ROTTEN_PLANKS = register("rotten_planks", new BlockItem(SpawnBlocks.ROTTEN_PLANKS, new FabricItemSettings()));
    public static final Item CRACKED_ROTTEN_PLANKS = register("cracked_rotten_planks", new BlockItem(SpawnBlocks.CRACKED_ROTTEN_PLANKS, new FabricItemSettings()));
    public static final Item ROTTEN_STAIRS = register("rotten_stairs", new BlockItem(SpawnBlocks.ROTTEN_STAIRS, new FabricItemSettings()));
    public static final Item ROTTEN_SLAB = register("rotten_slab", new BlockItem(SpawnBlocks.ROTTEN_SLAB, new FabricItemSettings()));
    public static final Item ROTTEN_FENCE = register("rotten_fence", new BlockItem(SpawnBlocks.ROTTEN_FENCE, new FabricItemSettings()));
    public static final Item ROTTEN_FENCE_GATE = register("rotten_fence_gate", new BlockItem(SpawnBlocks.ROTTEN_FENCE_GATE, new FabricItemSettings()));
    public static final Item ROTTEN_DOOR = register("rotten_door", new DoubleHighBlockItem(SpawnBlocks.ROTTEN_DOOR, new FabricItemSettings()));
    public static final Item ROTTEN_TRAPDOOR = register("rotten_trapdoor", new BlockItem(SpawnBlocks.ROTTEN_TRAPDOOR, new FabricItemSettings()));
    public static final Item FALLEN_LEAVES = register("fallen_leaves", new BlockItem(SpawnBlocks.FALLEN_LEAVES, new FabricItemSettings()));
    // ant mount loot
    public static final Item ANT_PUPA = register("ant_pupa", new AntPupaItem(new Item.Properties()));
    public static final Item MUSIC_DISC_ROT = register("music_disc_rot", new RecordItem(11, SpawnSoundEvents.MUSIC_DISC_ROT, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 154));
    public static final Item CROWN_POTTERY_SHERD = register("crown_pottery_sherd", new Item(new Item.Properties()));
    public static final Item SPADE_POTTERY_SHERD = register("spade_pottery_sherd", new Item(new Item.Properties()));

    public static final Item ALGAL_SAND = register("algal_sand", new BlockItem(SpawnBlocks.ALGAL_SAND, new FabricItemSettings()));

    // clam
    public static final Item SHELL_FRAGMENTS = register("shell_fragments", new Item(new FabricItemSettings()));
    public static final Item CLAM_SPAWN_EGG = register("clam_spawn_egg", new SpawnEggItem(SpawnEntityType.CLAM, 0xB9776B, 0xEAC6AB, new Item.Properties()));
    public static final Item CLAM = register("clam", new ClamItem(new Item.Properties().stacksTo(1).craftRemainder(SHELL_FRAGMENTS)));
    public static final Item CLAM_CASE = register("clam_case", new ClamCaseItem(new FabricItemSettings().stacksTo(1)));
    public static final Item PIGMENT_SHIFTER = register("pigment_shifter", new BlockItem(SpawnBlocks.PIGMENT_SHIFTER, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item CLAM_LAUNCHER = register("clam_launcher", new BlockItem(SpawnBlocks.CLAM_LAUNCHER, new FabricItemSettings()));
    public static final Item CLAM_MEAT = register("clam_meat", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).build())));
    public static final Item COOKED_CLAM_MEAT = register("cooked_clam_meat", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).build())));
    public static final Item CLAM_CHOWDER = register("clam_chowder", new BowlFoodItem(new FabricItemSettings().stacksTo(1).food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).build())));


    // sea cow
    public static final Item SEA_COW_SPAWN_EGG = register("sea_cow_spawn_egg", new SpawnEggItem(SpawnEntityType.SEA_COW, 0x696969, 0x7C7C7C, new Item.Properties()));
    public static final Item SCHOOL_POTTERY_SHERD = register("school_pottery_sherd", new Item(new Item.Properties()));
    public static final Item SHELL_POTTERY_SHERD = register("shell_pottery_sherd", new Item(new Item.Properties()));

    // octopus
    //public static final Item OCTOPUS_SPAWN_EGG = register("octopus_spawn_egg", new SpawnEggItem(SpawnEntityType.OCTOPUS, 0xB51C65, 0xFFBE2B, new Item.Properties()));
    public static final Item CAPTURED_OCTOPUS = register("captured_octopus", new CapturedOctopusItem(new Item.Properties().stacksTo(1)));

    // sea lion
    //public static final Item SEA_LION_SPAWN_EGG = register("sea_lion_spawn_egg", new SpawnEggItem(SpawnEntityType.SEA_LION, 0x5E4834, 0xC08650, new Item.Properties()));

    // herring
    public static final Item HERRING_SPAWN_EGG = register("herring_spawn_egg", new SpawnEggItem(SpawnEntityType.HERRING, 0x1051AD, 0xCACACA, new Item.Properties()));
    public static final Item HERRING_BUCKET = register("herring_bucket", new MobBucketItem(SpawnEntityType.HERRING, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final Item HERRING = register("herring", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).build())));
    public static final Item COOKED_HERRING = register("cooked_herring", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.5f).build())));

    // whale
    public static final Item WHALE_SPAWN_EGG = register("whale_spawn_egg", new SpawnEggItem(SpawnEntityType.WHALE, 0x3C4A56, 0xD1D3D3, new Item.Properties()));
    public static final Item WHALE_FLESH = register("whale_flesh", new BlockItem(SpawnBlocks.WHALE_FLESH, new FabricItemSettings()));
    public static final Item WHALE_UVULA = register("whale_uvula", new BlockItem(SpawnBlocks.WHALE_UVULA, new FabricItemSettings()));

    // krill
    public static final Item KRILL_SWARM_SPAWN_EGG = register("krill_swarm_spawn_egg", new SpawnEggItem(SpawnEntityType.KRILL_SWARM, 0xEE7F4F, 0xFFBCA0, new Item.Properties()));
    public static final Item KRILL_SWARM_BUCKET = register("krill_swarm_bucket", new MobBucketItem(SpawnEntityType.KRILL_SWARM, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));


    private static Item register(String id, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Spawn.MOD_ID, id), item);
    }
}
