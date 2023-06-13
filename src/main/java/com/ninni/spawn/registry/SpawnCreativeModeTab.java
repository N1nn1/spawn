package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;

import static com.ninni.spawn.registry.SpawnItems.*;

public class SpawnCreativeModeTab {

    static {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.addAfter(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB,
                    BIG_SNAIL_SHELL,
                    SNAIL_SHELL_TILES,
                    SNAIL_SHELL_TILE_STAIRS,
                    SNAIL_SHELL_TILE_SLAB
            );
            entries.addAfter(Items.CHERRY_BUTTON,
                    ROTTEN_LOG,
                    ROTTEN_WOOD,
                    STRIPPED_ROTTEN_LOG,
                    STRIPPED_ROTTEN_WOOD,
                    ROTTEN_PLANKS,
                    CRACKED_ROTTEN_PLANKS,
                    ROTTEN_STAIRS,
                    ROTTEN_SLAB,
                    ROTTEN_FENCE,
                    ROTTEN_FENCE_GATE,
                    ROTTEN_DOOR,
                    ROTTEN_TRAPDOOR
            );
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.addAfter(Items.SALMON_BUCKET, TUNA_EGG_BUCKET);
            entries.addAfter(Items.COD_BUCKET, ANGLER_FISH_BUCKET);
            entries.addAfter(Items.TROPICAL_FISH_BUCKET, SEAHORSE_BUCKET);
            entries.addAfter(Items.MILK_BUCKET, ANT_PUPA);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> {
            entries.addAfter(Items.HONEY_BLOCK, MUCUS_BLOCK, GHOSTLY_MUCUS_BLOCK);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.addBefore(Items.LILAC, SUNFLOWER);
            entries.addAfter(Items.BEETROOT_SEEDS, SUNFLOWER_SEEDS);
            entries.addAfter(Items.HONEY_BLOCK, MUCUS_BLOCK, BIG_SNAIL_SHELL);
            entries.addAfter(Items.FROGSPAWN, SNAIL_EGGS);
            entries.addAfter(Items.HONEYCOMB_BLOCK, ANTHILL, ROTTEN_LOG_ANTHILL, ANT_MOUND);
            entries.addAfter(Items.CHERRY_LOG, ROTTEN_LOG);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.GLOW_LICHEN, MUCUS);
            entries.addAfter(Items.DECORATED_POT, POTTED_SWEET_BERRIES);
            entries.addAfter(Items.BEE_NEST, ANTHILL, ROTTEN_LOG_ANTHILL);
            entries.addAfter(Items.BEEHIVE, ANT_FARM, ANT_MOUND);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.addAfter(Items.MELON_SLICE, ROASTED_SUNFLOWER_SEEDS);
            entries.addAfter(Items.PUFFERFISH, ESCARGOT);
            entries.addAfter(Items.COOKED_SALMON,
                    TUNA_CHUNK,
                    COOKED_TUNA_CHUNK,
                    ANGLER_FISH
            );
            entries.addAfter(Items.BREAD, TUNA_SANDWICH);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.BURN_POTTERY_SHERD, CROWN_POTTERY_SHERD);
            entries.addAfter(Items.SNORT_POTTERY_SHERD, SPADE_POTTERY_SHERD);
            entries.addAfter(Items.HONEYCOMB, SNAIL_SHELL);
            entries.addAfter(Items.SCUTE, MUCUS);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.addAfter(Items.ALLAY_SPAWN_EGG, ANGLER_FISH_SPAWN_EGG, ANT_SPAWN_EGG);
            entries.addAfter(Items.SALMON_SPAWN_EGG, SEAHORSE_SPAWN_EGG);
            entries.addAfter(Items.SLIME_SPAWN_EGG, SNAIL_SPAWN_EGG);
            entries.addAfter(Items.TROPICAL_FISH_SPAWN_EGG, TUNA_SPAWN_EGG);
        });
    }

    public static final CreativeModeTab ITEM_GROUP = register("item_group", FabricItemGroup.builder().icon(SPAWN::getDefaultInstance).title(Component.translatable("spawn.item_group")).displayItems((featureFlagSet, output) -> {
                // angler fish
                output.accept(ANGLER_FISH_SPAWN_EGG);
                output.accept(ANGLER_FISH_BUCKET);
                output.accept(ANGLER_FISH);

                // tuna
                output.accept(TUNA_SPAWN_EGG);
                output.accept(TUNA_CHUNK);
                output.accept(COOKED_TUNA_CHUNK);
                output.accept(TUNA_SANDWICH);
                output.accept(TUNA_EGG_BUCKET);

                // seahorse
                output.accept(SEAHORSE_SPAWN_EGG);
                output.accept(SEAHORSE_BUCKET);

                // snail
                output.accept(SNAIL_SPAWN_EGG);
                output.accept(SNAIL_SHELL);
                output.accept(ESCARGOT);
                output.accept(POTTED_SWEET_BERRIES);
                output.accept(BIG_SNAIL_SHELL);
                output.accept(SNAIL_SHELL_TILES);
                output.accept(SNAIL_SHELL_TILE_STAIRS);
                output.accept(SNAIL_SHELL_TILE_SLAB);
                output.accept(SNAIL_EGGS);
                output.accept(MUCUS);
                output.accept(MUCUS_BLOCK);
                output.accept(GHOSTLY_MUCUS_BLOCK);

                // hamster
                output.accept(HAMSTER_SPAWN_EGG);
                output.accept(SUNFLOWER);
                output.accept(SUNFLOWER_SEEDS);
                output.accept(ROASTED_SUNFLOWER_SEEDS);

                // ant
                output.accept(ANT_SPAWN_EGG);
                output.accept(ANT_MOUND);
                output.accept(ANTHILL);
                output.accept(ROTTEN_LOG_ANTHILL);
                output.accept(ANT_FARM);
                output.accept(ROTTEN_LOG);
                output.accept(ROTTEN_WOOD);
                output.accept(STRIPPED_ROTTEN_LOG);
                output.accept(STRIPPED_ROTTEN_WOOD);
                output.accept(ROTTEN_PLANKS);
                output.accept(CRACKED_ROTTEN_PLANKS);
                output.accept(ROTTEN_STAIRS);
                output.accept(ROTTEN_SLAB);
                output.accept(ROTTEN_FENCE);
                output.accept(ROTTEN_FENCE_GATE);
                output.accept(ROTTEN_DOOR);
                output.accept(ROTTEN_TRAPDOOR);
                output.accept(ANT_PUPA);
                output.accept(CROWN_POTTERY_SHERD);
                output.accept(SPADE_POTTERY_SHERD);
            }).build()
    );

    private static CreativeModeTab register(String id, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Spawn.MOD_ID, id), tab);
    }
}
