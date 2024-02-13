package com.ninni.spawn.block.entity;

import com.ninni.spawn.block.ClamLauncherBlock;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ClamLauncherBlockEntity extends BlockEntity {
    public int tickCount;
    public float activeRotation;

    public ClamLauncherBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpawnBlockEntityTypes.CLAM_LAUNCHER, blockPos, blockState);
    }


    public static void clientTick(Level level, BlockPos blockPos, BlockState state, ClamLauncherBlockEntity blockEntity) {
        ++blockEntity.tickCount;


        if (state.getValue(ClamLauncherBlock.POWERED)) {
            blockEntity.activeRotation += 0.2f;
        } else blockEntity.activeRotation = 0;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState state, ClamLauncherBlockEntity blockEntity) {
        ++blockEntity.tickCount;

        if (state.getValue(ClamLauncherBlock.COOLDOWN) > 0) {
            level.setBlockAndUpdate(blockPos, state.setValue(ClamLauncherBlock.COOLDOWN, state.getValue(ClamLauncherBlock.COOLDOWN) - 1));
        }

    }
}
