package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

@SuppressWarnings("deprecation")
public class SpawnBlocks {

    // snail
    public static final Block SNAIL_EGGS = register("snail_eggs", new SnailEggsBlock(FabricBlockSettings.of().mapColor(MapColor.TERRACOTTA_WHITE).noCollision().strength(0.2F).sounds(SoundType.FROGSPAWN).pushReaction(PushReaction.DESTROY)));
    // snail shell blocks
    public static final Block BIG_SNAIL_SHELL = register("big_snail_shell", new BigSnailShellBlock(FabricBlockSettings.of().mapColor(MapColor.COLOR_BROWN).strength(1.5f, 1200.0f).sounds(SpawnSoundEvents.SNAIL_SHELL)));
    public static final Block SNAIL_SHELL_TILES = register("snail_shell_tiles", new Block(FabricBlockSettings.copyOf(BIG_SNAIL_SHELL).strength(1f, 1200.0f)));
    public static final Block SNAIL_SHELL_TILE_STAIRS = register("snail_shell_tile_stairs", new StairBlock(SNAIL_SHELL_TILES.defaultBlockState(), FabricBlockSettings.copyOf(SNAIL_SHELL_TILES)));
    public static final Block SNAIL_SHELL_TILE_SLAB = register("snail_shell_tile_slab", new SlabBlock(FabricBlockSettings.copyOf(SNAIL_SHELL_TILES)));
    // misc snail blocks
    public static final Block POTTED_SWEET_BERRY_BUSH = register("potted_sweet_berry_bush", new PottedSweetBerryBushBlock(FabricBlockSettings.of().mapColor(MapColor.COLOR_BROWN).strength(1.5f, 1200.0f).sounds(SpawnSoundEvents.SNAIL_SHELL)));
    // snail mucus blocks
    public static final Block MUCUS = register("mucus", new MucusBlock(FabricBlockSettings.of().mapColor(MapColor.TERRACOTTA_YELLOW).sounds(SpawnSoundEvents.MUCUS).nonOpaque().pushReaction(PushReaction.DESTROY)));
    public static final Block MUCUS_BLOCK = register("mucus_block", new MucusBlockBlock(FabricBlockSettings.copyOf(MUCUS)));
    public static final Block GHOSTLY_MUCUS_BLOCK = register("ghostly_mucus_block", new GhostlyMucusBlockBlock(FabricBlockSettings.copyOf(MUCUS)));

    // hamster
    // sunflowers
    public static final Block SUNFLOWER = register("sunflower", new SunflowerBlock(FabricBlockSettings.copyOf(Blocks.SUNFLOWER).randomTicks()));
    public static final Block SUNFLOWER_PLANT = register("sunflower_plant", new SunflowerPlantBlock(FabricBlockSettings.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));

    // ant
    // ant blocks
    public static final Block ANT_MOUND = register("ant_mound", new AntMoundBlock(Blocks.COARSE_DIRT, FabricBlockSettings.create().mapColor(MapColor.DIRT).strength(0.25f).pushReaction(PushReaction.DESTROY).sound(SoundType.SUSPICIOUS_GRAVEL), SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_GRAVEL_COMPLETED));
    public static final Block ANTHILL = register("anthill", new AnthillBlock(FabricBlockSettings.create().mapColor(MapColor.DIRT).strength(0.5f).sound(SoundType.GRAVEL)));
    public static final Block ROTTEN_LOG_ANTHILL = register("rotten_log_anthill", new AnthillBlock(FabricBlockSettings.create().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).ignitedByLava().strength(1.6f)));
    public static final Block ANT_FARM = register("ant_farm", new AnthillBlock(FabricBlockSettings.create().mapColor(MapColor.GRASS).nonOpaque().strength(2.0f).sound(SoundType.GLASS)));
    // rotten wood
    public static final Block ROTTEN_LOG = register("rotten_log", new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG).strength(1.0f).pushReaction(PushReaction.DESTROY)));
    public static final Block ROTTEN_WOOD = register("rotten_wood", new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD).strength(1.0f).pushReaction(PushReaction.DESTROY)));
    public static final Block STRIPPED_ROTTEN_LOG = register("stripped_rotten_log", new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG).strength(0.8f).pushReaction(PushReaction.DESTROY)));
    public static final Block STRIPPED_ROTTEN_WOOD = register("stripped_rotten_wood", new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD).strength(0.8f).pushReaction(PushReaction.DESTROY)));
    public static final Block ROTTEN_PLANKS = register("rotten_planks", new Block(FabricBlockSettings.create().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(1.0f, 1.0f).sound(SoundType.WOOD).ignitedByLava()));
    public static final Block CRACKED_ROTTEN_PLANKS = register("cracked_rotten_planks", new Block(FabricBlockSettings.create().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(0.8f, 0.0f).sound(SoundType.WOOD).ignitedByLava()));
    public static final Block ROTTEN_STAIRS = register("rotten_stairs", new StairBlock(ROTTEN_PLANKS.defaultBlockState(), FabricBlockSettings.copyOf(ROTTEN_PLANKS)));
    public static final Block ROTTEN_SLAB = register("rotten_slab", new SlabBlock(FabricBlockSettings.copyOf(ROTTEN_PLANKS)));
    public static final Block ROTTEN_FENCE = register("rotten_fence", new FenceBlock(FabricBlockSettings.copy(ROTTEN_PLANKS).forceSolidOn()));
    public static final Block ROTTEN_FENCE_GATE = register("rotten_fence_gate", new FenceGateBlock(FabricBlockSettings.copy(ROTTEN_PLANKS).forceSolidOn(), SpawnWoodType.ROTTEN));
    public static final Block ROTTEN_DOOR = register("rotten_door", new DoorBlock(FabricBlockSettings.copy(ROTTEN_PLANKS).noOcclusion(), SpawnBlockSetType.ROTTEN));
    public static final Block ROTTEN_TRAPDOOR = register("rotten_trapdoor", new TrapDoorBlock(FabricBlockSettings.copy(ROTTEN_PLANKS).noOcclusion().isValidSpawn(SpawnBlocks::never), SpawnBlockSetType.ROTTEN));
    public static final Block FALLEN_LEAVES = register("fallen_leaves", new FallenLeavesBlock(FabricBlockSettings.create().noOcclusion().sound(SoundType.GRASS).instabreak().ignitedByLava().pushReaction(PushReaction.DESTROY).mapColor(DyeColor.BROWN).noCollission()));

    private static Block register(String id, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Spawn.MOD_ID, id), block);
    }

    private static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }
}
