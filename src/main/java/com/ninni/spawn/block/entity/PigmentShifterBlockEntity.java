package com.ninni.spawn.block.entity;

import com.ninni.spawn.block.PigmentShifterBlock;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

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

            if (randomSource.nextInt(20) == 0) {
                Vec3 vec32 = new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 1.5, blockPos.getZ() + 0.5);
                float j = (-0.5f + randomSource.nextFloat()) * (3.0f );
                float k = -1.0f + randomSource.nextFloat();
                float f = (-0.5f + randomSource.nextFloat()) * (3.0f);
                Vec3 vec33 = new Vec3(j, k, f);
                level.addParticle(ParticleTypes.NAUTILUS, vec32.x, vec32.y, vec32.z, vec33.x, vec33.y, vec33.z);
            }

            if (randomSource.nextInt(5) == 0) {
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
