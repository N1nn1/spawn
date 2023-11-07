package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.ninni.spawn.registry.SpawnItems.*;

@Mod.EventBusSubscriber(modid = Spawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Spawn.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ITEM_GROUP = CREATIVE_MODE_TABS.register("item_group", () -> CreativeModeTab.builder().icon(SPAWN.get()::getDefaultInstance).title(Component.translatable("spawn.item_group")).displayItems((featureFlagSet, output) -> {
                // angler fish
                output.accept(ANGLER_FISH_SPAWN_EGG.get());
                output.accept(ANGLER_FISH_BUCKET.get());
                output.accept(ANGLER_FISH.get());

                // tuna
                output.accept(TUNA_SPAWN_EGG.get());
                output.accept(TUNA_CHUNK.get());
                output.accept(COOKED_TUNA_CHUNK.get());
                output.accept(TUNA_SANDWICH.get());
                output.accept(TUNA_EGG_BUCKET.get());

                // seahorse
                output.accept(SEAHORSE_SPAWN_EGG.get());
                output.accept(SEAHORSE_BUCKET.get());

                // snail
                output.accept(SNAIL_SPAWN_EGG.get());
                output.accept(SNAIL_SHELL.get());
                output.accept(ESCARGOT.get());
                output.accept(POTTED_SWEET_BERRIES.get());
                output.accept(BIG_SNAIL_SHELL.get());
                output.accept(SNAIL_SHELL_TILES.get());
                output.accept(SNAIL_SHELL_TILE_STAIRS.get());
                output.accept(SNAIL_SHELL_TILE_SLAB.get());
                output.accept(SNAIL_EGGS.get());
                output.accept(MUCUS.get());
                output.accept(MUCUS_BLOCK.get());
                output.accept(GHOSTLY_MUCUS_BLOCK.get());

                // hamster
                output.accept(HAMSTER_SPAWN_EGG.get());
                output.accept(SUNFLOWER.get());
                output.accept(SUNFLOWER_SEEDS.get());
                output.accept(ROASTED_SUNFLOWER_SEEDS.get());

                // ant
                output.accept(ANT_SPAWN_EGG.get());
                output.accept(ANT_MOUND.get());
                output.accept(ANTHILL.get());
                output.accept(ROTTEN_LOG_ANTHILL.get());
                output.accept(ANT_FARM.get());
                output.accept(ROTTEN_LOG.get());
                output.accept(ROTTEN_WOOD.get());
                output.accept(STRIPPED_ROTTEN_LOG.get());
                output.accept(STRIPPED_ROTTEN_WOOD.get());
                output.accept(ROTTEN_PLANKS.get());
                output.accept(CRACKED_ROTTEN_PLANKS.get());
                output.accept(ROTTEN_STAIRS.get());
                output.accept(ROTTEN_SLAB.get());
                output.accept(ROTTEN_FENCE.get());
                output.accept(ROTTEN_FENCE_GATE.get());
                output.accept(ROTTEN_DOOR.get());
                output.accept(ROTTEN_TRAPDOOR.get());
                output.accept(FALLEN_LEAVES.get());
                output.accept(ANT_PUPA.get());
                output.accept(MUSIC_DISC_ROT.get());
                output.accept(CROWN_POTTERY_SHERD.get());
                output.accept(SPADE_POTTERY_SHERD.get());
            }).build()
    );

}
