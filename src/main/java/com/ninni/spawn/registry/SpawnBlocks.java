package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class SpawnBlocks {
    public static final Block SNAIL_EGGS = register("snail_eggs", new SnailEggsBlock(FabricBlockSettings.of(Material.CLAY, MaterialColor.TERRACOTTA_WHITE).noCollision().strength(0.2F).sounds(SoundType.FROGSPAWN)));
    public static final Block BIG_SNAIL_SHELL = register("big_snail_shell", new BigSnailShellBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_BROWN).strength(1.5f, 1200.0f).sounds(SpawnSoundEvents.SNAIL_SHELL)));
    public static final Block SNAIL_SHELL_TILES = register("snail_shell_tiles", new Block(FabricBlockSettings.copyOf(BIG_SNAIL_SHELL).strength(1f, 1200.0f)));
    public static final Block SNAIL_SHELL_TILE_STAIRS = register("snail_shell_tile_stairs", new StairBlock(SNAIL_SHELL_TILES.defaultBlockState(), FabricBlockSettings.copyOf(SNAIL_SHELL_TILES)));
    public static final Block SNAIL_SHELL_TILE_SLAB = register("snail_shell_tile_slab", new SlabBlock(FabricBlockSettings.copyOf(SNAIL_SHELL_TILES)));
    public static final Block POTTED_SWEET_BERRY_BUSH = register("potted_sweet_berry_bush", new PottedSweetBerryBushBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.COLOR_BROWN).strength(1.5f, 1200.0f).sounds(SpawnSoundEvents.SNAIL_SHELL)));
    public static final Block MUCUS = register("mucus", new MucusBlock(FabricBlockSettings.of(Material.CLAY, MaterialColor.COLOR_YELLOW).sounds(SpawnSoundEvents.MUCUS).nonOpaque()));
    public static final Block MUCUS_BLOCK = register("mucus_block", new MucusBlockBlock(FabricBlockSettings.copyOf(MUCUS)));
    public static final Block GHOSTLY_MUCUS_BLOCK = register("ghostly_mucus_block", new GhostlyMucusBlockBlock(FabricBlockSettings.copyOf(MUCUS)));

    public static final Block SUNFLOWER = register("sunflower", new SunflowerBlock(FabricBlockSettings.copyOf(Blocks.SUNFLOWER)));

    private static Block register(String id, Block block) { 
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Spawn.MOD_ID, id), block); 
    }
}
