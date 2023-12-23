package com.ninni.spawn.block;

import com.ninni.spawn.SpawnProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class WhaleFleshBlock extends Block {
    public static final BooleanProperty TEETH = SpawnProperties.TEETH;

    public WhaleFleshBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(TEETH, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TEETH);
    }

}
