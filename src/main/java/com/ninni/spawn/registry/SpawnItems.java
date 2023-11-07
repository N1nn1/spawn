package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.item.AntPupaItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Spawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Spawn.MOD_ID);

    @SuppressWarnings("unused")
    public static final RegistryObject<Item> SPAWN = register("spawn", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    // angler fish
    public static final RegistryObject<Item> ANGLER_FISH_SPAWN_EGG = register("angler_fish_spawn_egg", () -> new ForgeSpawnEggItem(SpawnEntityType.ANGLER_FISH, 0x2A3327, 0x95D930, new Item.Properties()));
    public static final RegistryObject<Item> ANGLER_FISH_BUCKET = register("angler_fish_bucket", () -> new MobBucketItem(SpawnEntityType.ANGLER_FISH, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ANGLER_FISH = register("angler_fish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 3 * 20), 1.0F).alwaysEat().build())));

    // tuna
    public static final RegistryObject<Item> TUNA_SPAWN_EGG = register("tuna_spawn_egg", () -> new ForgeSpawnEggItem(SpawnEntityType.TUNA, 0x4572B3, 0xE9B936, new Item.Properties()));
    public static final RegistryObject<Item> TUNA_CHUNK = register("tuna_chunk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).build())));
    public static final RegistryObject<Item> COOKED_TUNA_CHUNK = register("cooked_tuna_chunk", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).build())));
    public static final RegistryObject<Item> TUNA_SANDWICH = register("tuna_sandwich", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(1.0f).build()).stacksTo(32)));
    public static final RegistryObject<Item> TUNA_EGG_BUCKET = register("tuna_egg_bucket", () -> new MobBucketItem(SpawnEntityType.TUNA_EGG, () -> Fluids.WATER, SpawnSoundEvents.BUCKET_EMPTY_TUNA_EGG, new Item.Properties().stacksTo(1)));

    // seahorse
    public static final RegistryObject<Item> SEAHORSE_SPAWN_EGG = register("seahorse_spawn_egg", () -> new ForgeSpawnEggItem(SpawnEntityType.SEAHORSE, 0xFBC738, 0xFFFFFF, new Item.Properties()));
    public static final RegistryObject<Item> SEAHORSE_BUCKET = register("seahorse_bucket", () -> new MobBucketItem(SpawnEntityType.SEAHORSE, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    // snail
    public static final RegistryObject<Item> SNAIL_SPAWN_EGG = register("snail_spawn_egg", () -> new ForgeSpawnEggItem(SpawnEntityType.SNAIL, 0x5D3F30, 0xF6DEA2, new Item.Properties()));
    public static final RegistryObject<Item> SNAIL_EGGS = register("snail_eggs", () -> new BlockItem(SpawnBlocks.SNAIL_EGGS.get(), new Item.Properties()));
    public static final RegistryObject<Item> SNAIL_SHELL = register("snail_shell", () -> new Item(new Item.Properties()));
    // snail shell blocks
    public static final RegistryObject<Item> BIG_SNAIL_SHELL = register("big_snail_shell", () -> new BlockItem(SpawnBlocks.BIG_SNAIL_SHELL.get(), new Item.Properties()));
    public static final RegistryObject<Item> SNAIL_SHELL_TILES = register("snail_shell_tiles", () -> new BlockItem(SpawnBlocks.SNAIL_SHELL_TILES.get(), new Item.Properties()));
    public static final RegistryObject<Item> SNAIL_SHELL_TILE_STAIRS = register("snail_shell_tile_stairs", () -> new BlockItem(SpawnBlocks.SNAIL_SHELL_TILE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> SNAIL_SHELL_TILE_SLAB = register("snail_shell_tile_slab", () -> new BlockItem(SpawnBlocks.SNAIL_SHELL_TILE_SLAB.get(), new Item.Properties()));
    // misc snail blocks
    public static final RegistryObject<Item> ESCARGOT = register("escargot", () -> new Item(new Item.Properties().craftRemainder(SNAIL_SHELL.get()).stacksTo(1).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.8f).build())));
    public static final RegistryObject<Item> POTTED_SWEET_BERRIES = register("potted_sweet_berries", () -> new BlockItem(SpawnBlocks.POTTED_SWEET_BERRY_BUSH.get(), new Item.Properties().craftRemainder(SNAIL_SHELL.get()).stacksTo(16)));
    // snail mucus blocks
    public static final RegistryObject<Item> MUCUS = register("mucus", () -> new BlockItem(SpawnBlocks.MUCUS.get(), new Item.Properties()));
    public static final RegistryObject<Item> MUCUS_BLOCK = register("mucus_block", () -> new BlockItem(SpawnBlocks.MUCUS_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> GHOSTLY_MUCUS_BLOCK = register("ghostly_mucus_block", () -> new BlockItem(SpawnBlocks.GHOSTLY_MUCUS_BLOCK.get(), new Item.Properties()));

    // hamster
    public static final RegistryObject<Item> HAMSTER_SPAWN_EGG = register("hamster_spawn_egg", () -> new ForgeSpawnEggItem(SpawnEntityType.HAMSTER, 0x92816C, 0xFFFFFF, new Item.Properties()));
    // sunflowers
    public static final RegistryObject<Item> SUNFLOWER = register("sunflower", () -> new BlockItem(SpawnBlocks.SUNFLOWER.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUNFLOWER_SEEDS = register("sunflower_seeds", () -> new ItemNameBlockItem(SpawnBlocks.SUNFLOWER_PLANT.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROASTED_SUNFLOWER_SEEDS = register("roasted_sunflower_seeds", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).fast().build())));

    // ant
    public static final RegistryObject<Item> ANT_SPAWN_EGG = register("ant_spawn_egg", () -> new ForgeSpawnEggItem(SpawnEntityType.ANT, 0x3C3436, 0x282527, new Item.Properties()));
    // ant blocks
    public static final RegistryObject<Item> ANT_MOUND = register("ant_mound", () -> new BlockItem(SpawnBlocks.ANT_MOUND.get(), new Item.Properties()));
    public static final RegistryObject<Item> ANTHILL = register("anthill", () -> new BlockItem(SpawnBlocks.ANTHILL.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_LOG_ANTHILL = register("rotten_log_anthill", () -> new BlockItem(SpawnBlocks.ROTTEN_LOG_ANTHILL.get(), new Item.Properties()));
    public static final RegistryObject<Item> ANT_FARM = register("ant_farm", () -> new BlockItem(SpawnBlocks.ANT_FARM.get(), new Item.Properties()));
    // rotten wood
    public static final RegistryObject<Item> ROTTEN_LOG = register("rotten_log", () -> new BlockItem(SpawnBlocks.ROTTEN_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_WOOD = register("rotten_wood", () -> new BlockItem(SpawnBlocks.ROTTEN_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_ROTTEN_LOG = register("stripped_rotten_log", () -> new BlockItem(SpawnBlocks.STRIPPED_ROTTEN_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_ROTTEN_WOOD = register("stripped_rotten_wood", () -> new BlockItem(SpawnBlocks.STRIPPED_ROTTEN_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_PLANKS = register("rotten_planks", () -> new BlockItem(SpawnBlocks.ROTTEN_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> CRACKED_ROTTEN_PLANKS = register("cracked_rotten_planks", () -> new BlockItem(SpawnBlocks.CRACKED_ROTTEN_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_STAIRS = register("rotten_stairs", () -> new BlockItem(SpawnBlocks.ROTTEN_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_SLAB = register("rotten_slab", () -> new BlockItem(SpawnBlocks.ROTTEN_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_FENCE = register("rotten_fence", () -> new BlockItem(SpawnBlocks.ROTTEN_FENCE.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_FENCE_GATE = register("rotten_fence_gate", () -> new BlockItem(SpawnBlocks.ROTTEN_FENCE_GATE.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_DOOR = register("rotten_door", () -> new DoubleHighBlockItem(SpawnBlocks.ROTTEN_DOOR.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_TRAPDOOR = register("rotten_trapdoor", () -> new BlockItem(SpawnBlocks.ROTTEN_TRAPDOOR.get(), new Item.Properties()));
    public static final RegistryObject<Item> FALLEN_LEAVES = register("fallen_leaves", () -> new BlockItem(SpawnBlocks.FALLEN_LEAVES.get(), new Item.Properties()));
    // ant mount loot
    public static final RegistryObject<Item> ANT_PUPA = register("ant_pupa", () -> new AntPupaItem(new Item.Properties()));
    public static final RegistryObject<Item> MUSIC_DISC_ROT = register("music_disc_rot", () -> new RecordItem(11, SpawnSoundEvents.MUSIC_DISC_ROT.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 154));
    public static final RegistryObject<Item> CROWN_POTTERY_SHERD = register("crown_pottery_sherd", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPADE_POTTERY_SHERD = register("spade_pottery_sherd", () -> new Item(new Item.Properties()));

    private static <I extends Item> RegistryObject<I> register(String id, Supplier<? extends I> item) {
        return ITEMS.register(id, item);
    }
}
