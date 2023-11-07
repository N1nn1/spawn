package com.ninni.spawn.level;

import com.mojang.serialization.Codec;
import com.ninni.spawn.block.SunflowerBlock;
import com.ninni.spawn.registry.SpawnBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SunflowerFeature extends Feature<NoneFeatureConfiguration> {

    public SunflowerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        BlockPos blockPos = featurePlaceContext.origin();
        BlockState sunflower = SpawnBlocks.SUNFLOWER.get().defaultBlockState();
        BlockState blockState = sunflower.setValue(SunflowerBlock.HALF, DoubleBlockHalf.LOWER);
        if (!blockState.canSurvive(worldGenLevel, blockPos)) return false;
        if (!worldGenLevel.isEmptyBlock(blockPos.above())) return false;
        BlockPos blockPos2 = blockPos.above();
        worldGenLevel.setBlock(blockPos, DoublePlantBlock.copyWaterloggedFrom(worldGenLevel, blockPos, blockState), 2);
        worldGenLevel.setBlock(blockPos2, DoublePlantBlock.copyWaterloggedFrom(worldGenLevel, blockPos2, sunflower.setValue(SunflowerBlock.ROTATION, SunflowerBlock.getRotationType(worldGenLevel.getLevel())).setValue(SunflowerBlock.HALF, DoubleBlockHalf.UPPER).setValue(SunflowerBlock.SEEDS, true)), 2);
        return true;
    }

}
