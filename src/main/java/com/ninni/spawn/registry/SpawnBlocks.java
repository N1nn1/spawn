package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.block.AntMoundBlock;
import com.ninni.spawn.block.AnthillBlock;
import com.ninni.spawn.block.BigSnailShellBlock;
import com.ninni.spawn.block.FallenLeavesBlock;
import com.ninni.spawn.block.GhostlyMucusBlockBlock;
import com.ninni.spawn.block.MucusBlock;
import com.ninni.spawn.block.MucusBlockBlock;
import com.ninni.spawn.block.PottedSweetBerryBushBlock;
import com.ninni.spawn.block.SnailEggsBlock;
import com.ninni.spawn.block.StrippablePlankBlock;
import com.ninni.spawn.block.SunflowerBlock;
import com.ninni.spawn.block.SunflowerPlantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Spawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Spawn.MOD_ID);

    // snail
    public static final RegistryObject<Block> SNAIL_EGGS = register("snail_eggs", () -> new SnailEggsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).noCollission().strength(0.2F).sound(SoundType.FROGSPAWN).pushReaction(PushReaction.DESTROY)));
    // snail shell blocks
    public static final RegistryObject<Block> BIG_SNAIL_SHELL = register("big_snail_shell", () -> new BigSnailShellBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(1.5f, 1200.0f).sound(SpawnSoundEvents.SNAIL_SHELL)));
    public static final RegistryObject<Block> SNAIL_SHELL_TILES = register("snail_shell_tiles", () -> new Block(BlockBehaviour.Properties.copy(BIG_SNAIL_SHELL.get()).strength(1f, 1200.0f)));
    public static final RegistryObject<Block> SNAIL_SHELL_TILE_STAIRS = register("snail_shell_tile_stairs", () -> new StairBlock(SNAIL_SHELL_TILES.get().defaultBlockState(), BlockBehaviour.Properties.copy(SNAIL_SHELL_TILES.get())));
    public static final RegistryObject<Block> SNAIL_SHELL_TILE_SLAB = register("snail_shell_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(SNAIL_SHELL_TILES.get())));
    // misc snail blocks
    public static final RegistryObject<Block> POTTED_SWEET_BERRY_BUSH = register("potted_sweet_berry_bush", () -> new PottedSweetBerryBushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(1.5f, 1200.0f).sound(SpawnSoundEvents.SNAIL_SHELL)));
    // snail mucus blocks
    public static final RegistryObject<Block> MUCUS = register("mucus", () -> new MucusBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_YELLOW).sound(SpawnSoundEvents.MUCUS).noOcclusion().pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> MUCUS_BLOCK = register("mucus_block", () -> new MucusBlockBlock(BlockBehaviour.Properties.copy(MUCUS.get())));
    public static final RegistryObject<Block> GHOSTLY_MUCUS_BLOCK = register("ghostly_mucus_block", () -> new GhostlyMucusBlockBlock(BlockBehaviour.Properties.copy(MUCUS.get())));

    // hamster
    // sunflowers
    public static final RegistryObject<Block> SUNFLOWER = register("sunflower", () -> new SunflowerBlock(BlockBehaviour.Properties.copy(Blocks.SUNFLOWER).randomTicks()));
    public static final RegistryObject<Block> SUNFLOWER_PLANT = register("sunflower_plant", () -> new SunflowerPlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));

    // ant
    // ant blocks
    public static final RegistryObject<Block> ANT_MOUND = register("ant_mound", () -> new AntMoundBlock(Blocks.COARSE_DIRT, BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.25f).pushReaction(PushReaction.DESTROY).sound(SoundType.SUSPICIOUS_GRAVEL), SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_GRAVEL_COMPLETED));
    public static final RegistryObject<Block> ANTHILL = register("anthill", () -> new AnthillBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5f).sound(SoundType.GRAVEL)));
    public static final RegistryObject<Block> ROTTEN_LOG_ANTHILL = register("rotten_log_anthill", () -> new AnthillBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).sound(SpawnSoundEvents.ROTTEN_WOOD).ignitedByLava().strength(1.6f)));
    public static final RegistryObject<Block> ANT_FARM = register("ant_farm", () -> new AnthillBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).noOcclusion().strength(2.0f).sound(SoundType.GLASS)));
    // rotten wood
    public static final RegistryObject<Block> ROTTEN_LOG = register("rotten_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(1.0f).pushReaction(PushReaction.DESTROY).sound(SpawnSoundEvents.ROTTEN_WOOD)));
    public static final RegistryObject<Block> ROTTEN_WOOD = register("rotten_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).strength(1.0f).pushReaction(PushReaction.DESTROY).sound(SpawnSoundEvents.ROTTEN_WOOD)));
    public static final RegistryObject<Block> STRIPPED_ROTTEN_LOG = register("stripped_rotten_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).strength(0.8f).pushReaction(PushReaction.DESTROY).sound(SpawnSoundEvents.ROTTEN_WOOD)));
    public static final RegistryObject<Block> STRIPPED_ROTTEN_WOOD = register("stripped_rotten_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD).strength(0.8f).pushReaction(PushReaction.DESTROY).sound(SpawnSoundEvents.ROTTEN_WOOD)));
    public static final RegistryObject<Block> CRACKED_ROTTEN_PLANKS = register("cracked_rotten_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(0.8f, 0.0f).sound(SoundType.WOOD).sound(SpawnSoundEvents.CRACKED_ROTTEN_WOOD).ignitedByLava()));
    public static final RegistryObject<Block> ROTTEN_PLANKS = register("rotten_planks", () -> new StrippablePlankBlock(CRACKED_ROTTEN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(1.0f, 1.0f).sound(SpawnSoundEvents.ROTTEN_WOOD).ignitedByLava()));
    public static final RegistryObject<Block> ROTTEN_STAIRS = register("rotten_stairs", () -> new StairBlock(ROTTEN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(ROTTEN_PLANKS.get())));
    public static final RegistryObject<Block> ROTTEN_SLAB = register("rotten_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(ROTTEN_PLANKS.get())));
    public static final RegistryObject<Block> ROTTEN_FENCE = register("rotten_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(ROTTEN_PLANKS.get()).forceSolidOn()));
    public static final RegistryObject<Block> ROTTEN_FENCE_GATE = register("rotten_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(ROTTEN_PLANKS.get()).forceSolidOn(), SpawnWoodType.ROTTEN));
    public static final RegistryObject<Block> ROTTEN_DOOR = register("rotten_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(ROTTEN_PLANKS.get()).noOcclusion(), SpawnBlockSetType.ROTTEN));
    public static final RegistryObject<Block> ROTTEN_TRAPDOOR = register("rotten_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(ROTTEN_PLANKS.get()).noOcclusion().isValidSpawn(SpawnBlocks::never), SpawnBlockSetType.ROTTEN));
    public static final RegistryObject<Block> FALLEN_LEAVES = register("fallen_leaves", () -> new FallenLeavesBlock(BlockBehaviour.Properties.of().noOcclusion().sound(SpawnSoundEvents.FALLEN_LEAVES).instabreak().ignitedByLava().pushReaction(PushReaction.DESTROY).mapColor(DyeColor.BROWN).noCollission()));

    private static <B extends Block> RegistryObject<B> register(String id, Supplier<? extends B> block) {
        return BLOCKS.register(id, block);
    }

    private static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }
}
