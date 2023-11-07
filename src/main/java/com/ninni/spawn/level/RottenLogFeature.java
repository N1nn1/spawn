package com.ninni.spawn.level;

import com.mojang.serialization.Codec;
import com.ninni.spawn.block.AnthillBlock;
import com.ninni.spawn.registry.SpawnBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class RottenLogFeature extends Feature<NoneFeatureConfiguration> {

    public RottenLogFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel world = featurePlaceContext.level();
        BlockPos blockPos = featurePlaceContext.origin();
        RandomSource random = featurePlaceContext.random();
        int length = UniformInt.of(4, 6).sample(random);
        boolean rottenAnthill = true;
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockState belowState = world.getBlockState(blockPos.below());
        if (!(belowState.is(BlockTags.DIRT) || belowState.is(Blocks.COARSE_DIRT))) {
            return false;
        } else {
            BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
            for (int i = 0; i <= length; i++) {
                BlockState relativeBelowState = world.getBlockState(mutableBlockPos.relative(direction).below());
                boolean shouldDrop = relativeBelowState.isAir() || relativeBelowState.is(BlockTags.REPLACEABLE);
                if (shouldDrop) {
                    mutableBlockPos.move(Direction.DOWN);
                }
                mutableBlockPos.move(direction);
                BlockState replaceState = world.getBlockState(mutableBlockPos);
                if (replaceState.is(BlockTags.SMALL_FLOWERS) || replaceState.isAir() || replaceState.is(BlockTags.REPLACEABLE)) {
                    if (i == 1 && !shouldDrop) {
                        continue;
                    }
                    if (i == 0) {
                        world.setBlock(mutableBlockPos, SpawnBlocks.ROTTEN_LOG.get().defaultBlockState(), 2);
                    } else {
                        BlockState blockState = random.nextInt(5) == 0 && rottenAnthill ? SpawnBlocks.ROTTEN_LOG_ANTHILL.get().defaultBlockState().setValue(AnthillBlock.FACING, direction) : SpawnBlocks.ROTTEN_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, direction.getAxis());
                        world.setBlock(mutableBlockPos, blockState, 2);
                        if (blockState.is(SpawnBlocks.ROTTEN_LOG_ANTHILL.get())) {
                            rottenAnthill = false;
                        }
                    }
                }
            }
            return true;
        }
    }

}
