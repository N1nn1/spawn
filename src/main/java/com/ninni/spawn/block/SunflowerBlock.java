package com.ninni.spawn.block;

import com.ninni.spawn.SpawnProperties;
import com.ninni.spawn.block.state.properties.SunflowerRotation;
import com.ninni.spawn.registry.SpawnItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("deprecation")
public class SunflowerBlock extends DoublePlantBlock {
    public static final EnumProperty<SunflowerRotation> ROTATION = SpawnProperties.SUNFLOWER_ROTATION;
    public static final BooleanProperty SEEDS = SpawnProperties.SEEDS;

    public SunflowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(SEEDS, true).setValue(ROTATION, SunflowerRotation.DAY).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (blockState.getValue(ROTATION) != SunflowerRotation.NIGHT && blockState.getValue(SEEDS) && blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            level.setBlock(blockPos, blockState.setValue(SEEDS, false), 3);
            int j = 1 + level.random.nextInt(2);
            SunflowerBlock.popResource(level, blockPos, new ItemStack(SpawnItems.SUNFLOWER_SEEDS, j));
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION, HALF, SEEDS);
    }
}
