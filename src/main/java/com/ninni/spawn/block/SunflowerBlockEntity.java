package com.ninni.spawn.block;

import com.ninni.spawn.block.state.properties.SunflowerRotation;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import com.ninni.spawn.registry.SpawnBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SunflowerBlockEntity extends BlockEntity {

    public SunflowerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpawnBlockEntityTypes.SUNFLOWER, blockPos, blockState);
    }

    public static void tick(Level world, BlockPos blockPos, BlockState blockState, SunflowerBlockEntity sunflowerBlockEntity) {
        SunflowerRotation sunflowerRotation = SunflowerBlock.getRotationType(world);
        SunflowerRotation currentRotation = blockState.getValue(SunflowerBlock.ROTATION);
        if (currentRotation == sunflowerRotation || !blockState.is(SpawnBlocks.SUNFLOWER)) {
            return;
        }
        boolean flag = blockState.getValue(SunflowerBlock.SEEDS) || currentRotation == SunflowerRotation.NIGHT && sunflowerRotation == SunflowerRotation.MORNING;
        world.setBlock(blockPos, blockState.setValue(SunflowerBlock.ROTATION, sunflowerRotation).setValue(SunflowerBlock.SEEDS, flag), 2);
    }

}
