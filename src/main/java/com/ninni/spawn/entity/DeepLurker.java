package com.ninni.spawn.entity;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public interface DeepLurker {
    default float getLurkingPathfindingFavor(BlockPos pos, LevelReader world) {
        int y = pos.getY() + Math.abs(world.getMinBuildHeight());
        return 1f / (y == 0 ? 1 : y);
    }
}
