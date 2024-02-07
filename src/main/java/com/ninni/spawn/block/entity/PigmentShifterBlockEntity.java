package com.ninni.spawn.block.entity;

import com.ninni.spawn.block.PigmentShifterBlock;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PigmentShifterBlockEntity extends BlockEntity {
    public int tickCount;
    private float activeRotation;

    public PigmentShifterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpawnBlockEntityTypes.PIGMENT_SHIFTER, blockPos, blockState);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, PigmentShifterBlockEntity blockEntity) {
        RandomSource randomSource = level.random;

        ++blockEntity.tickCount;

        if (blockEntity.getBlockState().getValue(PigmentShifterBlock.WATERLOGGED)) {
            blockEntity.activeRotation += 1.0f;

            if (randomSource.nextInt(10) == 0) {
                double d = (double) blockPos.getX() + 0.5 + randomSource.nextFloat() - randomSource.nextFloat();
                double e = (double) blockPos.getY() + 0.7 + randomSource.nextFloat() - randomSource.nextFloat();
                double f = (double) blockPos.getZ() + 0.5 + randomSource.nextFloat() - randomSource.nextFloat();
                level.addParticle(ParticleTypes.BUBBLE, d, e, f, 0, 0, 0);
            }
        }
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, PigmentShifterBlockEntity blockEntity) {
        ++blockEntity.tickCount;
    }

    public float getActiveRotation(float f) {
        return (this.activeRotation + f) * -0.0375f;
    }
}
