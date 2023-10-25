package com.ninni.spawn.block;

import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class StrippablePlankBlock extends Block {
    public BlockState strippedBlockState;

    public StrippablePlankBlock(BlockState strippedBlockState, Properties properties) {
        super(properties);
        this.strippedBlockState = strippedBlockState;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getItemInHand(interactionHand).getItem() instanceof AxeItem) {
            level.setBlock(blockPos, strippedBlockState, 4);
            level.playSound(player, blockPos, SpawnSoundEvents.ROTTEN_WOOD_CRACK, SoundSource.BLOCKS, 1.0f, 1.0f);
            player.getItemInHand(interactionHand).hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(interactionHand));
            return InteractionResult.SUCCESS;
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}
