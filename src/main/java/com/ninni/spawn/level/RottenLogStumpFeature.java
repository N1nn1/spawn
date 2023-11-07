package com.ninni.spawn.level;

import com.mojang.serialization.Codec;
import com.ninni.spawn.registry.SpawnBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class RottenLogStumpFeature extends Feature<NoneFeatureConfiguration> {

    public RottenLogStumpFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel world = featurePlaceContext.level();
        BlockPos blockPos = featurePlaceContext.origin();
        RandomSource random = featurePlaceContext.random();
        BlockState belowState = world.getBlockState(blockPos.below());
        if (!(belowState.is(BlockTags.DIRT) || belowState.is(Blocks.COARSE_DIRT)) || !this.checkPosition(world, blockPos)) {
            return false;
        } else {
            int range = 1;
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int height;
            for (int x = -range; x <= range; x++) {
                for (int z = -range; z <= range; z++) {
                    boolean xx = x == -range && random.nextFloat() > 0.4F;
                    boolean xxxx = x == range && random.nextFloat() > 0.4F;
                    boolean zz = z == -range && random.nextFloat() > 0.4F;
                    boolean zzzz = z == range && random.nextFloat() > 0.4F;
                    boolean center = x == 0 && z == 0;
                    if (!center) {
                        height = UniformInt.of(1, 3).sample(random);
                    } else {
                        height = UniformInt.of(4, 7).sample(random);
                    }
                    for (int y = 0; y < height; y++) {
                        if ((xx || xxxx) && (zz || zzzz)) {
                            continue;
                        }
                        BlockPos pos = new BlockPos(blockPos.getX() + x, blockPos.getY() + y, blockPos.getZ() + z);
                        if (world.getBlockState(pos).is(BlockTags.REPLACEABLE)) {
                            world.setBlock(pos, SpawnBlocks.ROTTEN_LOG.get().defaultBlockState(), 2);
                        }
                        if (center && random.nextInt(2) == 0) {
                            for (int length = 0; length < world.getMaxBuildHeight(); length++) {
                                BlockPos downPos = blockPos.above(height).relative(direction).below(length);
                                if (!world.getBlockState(downPos).is(BlockTags.REPLACEABLE)) {
                                    break;
                                }
                                world.setBlock(downPos, SpawnBlocks.ROTTEN_LOG.get().defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    private boolean checkPosition(WorldGenLevel world, BlockPos blockPos) {
        int range = 1;
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                BlockPos pos = new BlockPos(blockPos.getX() + x, blockPos.getY(), blockPos.getZ() + z);
                if (world.getBlockState(pos.below()).isAir()) {
                    return false;
                }
            }
        }
        return true;
    }

}
