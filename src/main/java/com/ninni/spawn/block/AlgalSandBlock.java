package com.ninni.spawn.block;

import com.ninni.spawn.registry.SpawnBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class AlgalSandBlock extends Block implements BonemealableBlock {

    public AlgalSandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {

        if (!canBeAlgalSand(serverLevel, blockPos)) {
            serverLevel.scheduleTick(blockPos, this, 1);
            return;
        }

        if (serverLevel.getBiome(blockPos).is(SpawnBiomes.SEAGRASS_MEADOW)) {
            BlockState blockState2 = this.defaultBlockState();
            for (int i = 0; i < 4; ++i) {
                BlockPos blockPos2 = blockPos.offset(randomSource.nextInt(3) - 1, randomSource.nextInt(5) - 3, randomSource.nextInt(3) - 1);
                if (!serverLevel.getBlockState(blockPos2).is(Blocks.SAND) || !canBeAlgalSand(serverLevel, blockPos2)) continue;
                serverLevel.setBlockAndUpdate(blockPos2, blockState2);
            }
        }
    }

    private static boolean canBeAlgalSand(LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.above();
        FluidState fluidState = levelReader.getBlockState(blockPos2).getFluidState();

        return fluidState.is(FluidTags.WATER) && fluidState.getAmount() > 4;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        serverLevel.setBlockAndUpdate(blockPos, Blocks.SAND.defaultBlockState());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        return canBeAlgalSand(levelReader, blockPos);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        //BlockPos blockPos2 = blockPos.above();
        //ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
        //Registry<ConfiguredFeature<?, ?>> registry = serverLevel.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);

        //this.place(registry, NetherFeatures.CRIMSON_FOREST_VEGETATION_BONEMEAL, serverLevel, chunkGenerator, randomSource, blockPos2);
    }

    private void place(Registry<ConfiguredFeature<?, ?>> registry, ResourceKey<ConfiguredFeature<?, ?>> resourceKey, ServerLevel serverLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
        registry.getHolder(resourceKey).ifPresent(reference -> (reference.value()).place(serverLevel, chunkGenerator, randomSource, blockPos));
    }
}

