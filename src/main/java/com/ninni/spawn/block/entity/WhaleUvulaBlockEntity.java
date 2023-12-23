package com.ninni.spawn.block.entity;

import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WhaleUvulaBlockEntity extends BlockEntity {
    public int ticks;
    public boolean shaking;
    public Direction clickDirection;

    public WhaleUvulaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpawnBlockEntityTypes.WHALE_UVULA, blockPos, blockState);
    }


    public boolean triggerEvent(int i, int j) {
        if (i == 1) {
            this.clickDirection = Direction.from3DDataValue(j);
            this.ticks = 0;
            this.shaking = true;
            return true;
        } else {
            return super.triggerEvent(i, j);
        }
    }

    private static void tick(WhaleUvulaBlockEntity blockEntity) {
        if (blockEntity.shaking) {
            ++blockEntity.ticks;
        }

        if (blockEntity.ticks >= 50) {
            blockEntity.shaking = false;
            blockEntity.ticks = 0;
        }
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, WhaleUvulaBlockEntity blockEntity) {
        tick(blockEntity);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, WhaleUvulaBlockEntity blockEntity) {
        tick(blockEntity);
    }

    public void onHit(Direction direction) {
        BlockPos blockPos = this.getBlockPos();
        this.clickDirection = direction;
        if (this.shaking) {
            this.ticks = 0;
        } else {
            this.shaking = true;
        }

        this.level.blockEvent(blockPos, this.getBlockState().getBlock(), 1, direction.get3DDataValue());
    }
}
