package com.ninni.spawn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TrimmedSeagrassBlock extends SeagrassBlock {
    public TrimmedSeagrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(Items.SEAGRASS);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        for (int i = 0; i < 4; ++i) {
            BlockState blockState2 = Blocks.TALL_SEAGRASS.defaultBlockState();
            BlockState blockState3 = blockState2.setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
            BlockPos blockPos2 = blockPos.above();
            if (serverLevel.getBlockState(blockPos2).is(Blocks.WATER)) {
                serverLevel.setBlock(blockPos, blockState2, 2);
                serverLevel.setBlock(blockPos2, blockState3, 2);
            }
        }
    }
}

