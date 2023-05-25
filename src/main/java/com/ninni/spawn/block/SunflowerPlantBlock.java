package com.ninni.spawn.block;

import com.ninni.spawn.registry.SpawnBlocks;
import com.ninni.spawn.registry.SpawnItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class SunflowerPlantBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape SAPLING_SHAPE = Block.box(3, 0, 3, 13, 6, 13);
    private static final VoxelShape MID_GROWTH_SHAPE = Block.box(2, 0, 2, 14, 14, 14);


    public SunflowerPlantBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(AGE, 0));
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        int i = blockState.getValue(AGE);
        if (randomSource.nextInt(5) == 0 && serverLevel.getRawBrightness(blockPos.above(), 0) >= 9) {
            if (i < 3) {
                BlockState blockState2 = blockState.setValue(AGE, i + 1);
                serverLevel.setBlock(blockPos, blockState2, 2);
                serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState2));
            }
            if (i == 3) {
                BlockState blockState2 = SpawnBlocks.SUNFLOWER.defaultBlockState().setValue(SunflowerBlock.HALF, DoubleBlockHalf.LOWER).setValue(SunflowerBlock.ROTATION, SunflowerBlock.getRotationType(serverLevel));
                BlockState blockState3 = SpawnBlocks.SUNFLOWER.defaultBlockState().setValue(SunflowerBlock.HALF, DoubleBlockHalf.UPPER).setValue(SunflowerBlock.ROTATION, SunflowerBlock.getRotationType(serverLevel));
                serverLevel.setBlock(blockPos, blockState2, 2);
                serverLevel.setBlock(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()), blockState3, 2);
                serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState2));
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(SpawnItems.SUNFLOWER_SEEDS);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(AGE) == 0) {
            return SAPLING_SHAPE;
        }
        if (blockState.getValue(AGE) < 3) {
            return MID_GROWTH_SHAPE;
        }
        return super.getShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        int i = blockState.getValue(AGE);
        if (i < 3) {
            BlockState blockState2 = blockState.setValue(AGE, i + 1);
            serverLevel.setBlock(blockPos, blockState2, 2);
            serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState2));
        }
        if (i == 3) {
            BlockState blockState2 = SpawnBlocks.SUNFLOWER.defaultBlockState().setValue(SunflowerBlock.HALF, DoubleBlockHalf.LOWER).setValue(SunflowerBlock.ROTATION, SunflowerBlock.getRotationType(serverLevel));
            BlockState blockState3 = SpawnBlocks.SUNFLOWER.defaultBlockState().setValue(SunflowerBlock.HALF, DoubleBlockHalf.UPPER).setValue(SunflowerBlock.ROTATION, SunflowerBlock.getRotationType(serverLevel));
            serverLevel.setBlock(blockPos, blockState2, 2);
            serverLevel.setBlock(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()), blockState3, 2);
            serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState2));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

}
