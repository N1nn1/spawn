package com.ninni.spawn.block;

import com.ninni.spawn.block.entity.WhaleUvulaBlockEntity;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WhaleUvulaBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = Shapes.or(Block.box(2, 0, 2, 14, 10, 14), Block.box(6, 10, 6, 10, 16, 10));

    public WhaleUvulaBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        Direction direction = blockHitResult.getDirection();
        this.attemptToRing(player, level, blockPos, direction);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public void attemptToRing(@Nullable Entity entity, Level level, BlockPos blockPos, @Nullable Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!level.isClientSide && blockEntity instanceof WhaleUvulaBlockEntity) {
            if (direction == null) {
                direction = Direction.NORTH;
            }
            ((WhaleUvulaBlockEntity)blockEntity).onHit(direction);
            level.gameEvent(entity, GameEvent.BLOCK_CHANGE, blockPos);
        }
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SpawnBlockEntityTypes.WHALE_UVULA, level.isClientSide ? WhaleUvulaBlockEntity::clientTick : WhaleUvulaBlockEntity::serverTick);
    }

    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WhaleUvulaBlockEntity(blockPos, blockState);
    }
}
