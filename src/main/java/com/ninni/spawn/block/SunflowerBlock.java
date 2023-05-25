package com.ninni.spawn.block;

import com.ninni.spawn.SpawnProperties;
import com.ninni.spawn.block.state.properties.SunflowerRotation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class SunflowerBlock extends DoublePlantBlock {
    public static final EnumProperty<SunflowerRotation> ROTATION = SpawnProperties.SUNFLOWER_ROTATION;

    public SunflowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ROTATION, SunflowerRotation.DAY).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION, HALF);
    }
}
