package com.ninni.spawn.block;

import com.ninni.spawn.SpawnProperties;
import com.ninni.spawn.block.state.properties.SunflowerRotation;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class SunflowerBlock extends DoublePlantBlock implements BonemealableBlock, EntityBlock {
    public static final EnumProperty<SunflowerRotation> ROTATION = SpawnProperties.SUNFLOWER_ROTATION;
    public static final BooleanProperty SEEDS = SpawnProperties.SEEDS;

    public SunflowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(SEEDS, false).setValue(ROTATION, SunflowerRotation.DAY).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (blockState.getValue(ROTATION) != SunflowerRotation.NIGHT && blockState.getValue(SEEDS) && blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            level.setBlock(blockPos, blockState.setValue(SEEDS, false), 3);
            int j = 1 + level.random.nextInt(2);
            level.playSound(null, blockPos, SpawnSoundEvents.SUNFLOWER_SEED_PICKUP, SoundSource.BLOCKS, 1.0f, 0.8f + level.random.nextFloat() * 0.4f);
            SunflowerBlock.popResource(level, blockPos, new ItemStack(SpawnItems.SUNFLOWER_SEEDS, j));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    public static SunflowerRotation getRotationType(Level world) {
        if (world.isNight() || world.dimension() != Level.OVERWORLD) {
            return SunflowerRotation.NIGHT;
        } else if (world.dayTime() < 2000) {
            return SunflowerRotation.MORNING;
        } else if (world.dayTime() >= 7000) {
            return SunflowerRotation.EVENING;
        } else {
            return SunflowerRotation.DAY;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION, HALF, SEEDS);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        return blockState.getValue(HALF) == DoubleBlockHalf.UPPER && !blockState.getValue(SEEDS);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(HALF) == DoubleBlockHalf.UPPER && !blockState.getValue(SEEDS);
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        BlockState blockState2 = blockState.setValue(SEEDS, true);
        serverLevel.setBlock(blockPos, blockState2, 2);
        serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState2));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SunflowerBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return !level.isClientSide && blockState.getValue(SunflowerBlock.HALF) == DoubleBlockHalf.UPPER ? createTickerHelper(blockEntityType, SpawnBlockEntityTypes.SUNFLOWER, SunflowerBlockEntity::tick) : null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityType<E> blockEntityType2, BlockEntityTicker<? super E> blockEntityTicker) {
        return blockEntityType2 == blockEntityType ? (BlockEntityTicker<A>) blockEntityTicker : null;
    }

}
