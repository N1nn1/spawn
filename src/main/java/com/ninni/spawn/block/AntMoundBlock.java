package com.ninni.spawn.block;

import com.ninni.spawn.entity.Ant;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AntMoundBlock extends BrushableBlock {
    public AntMoundBlock(Block block, Properties properties, SoundEvent soundEvent, SoundEvent soundEvent2) {
        super(block, properties, soundEvent, soundEvent2);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
        if (blockEntity instanceof BrushableBlockEntity brushableBlockEntity) {
            brushableBlockEntity.checkReset();
        }
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BrushableBlockEntity(blockPos, blockState);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
        this.angerNearbyAnts(level, blockPos);
    }

    private void angerNearbyAnts(Level world, BlockPos pos) {
        List<Ant> antList = world.getEntitiesOfClass(Ant.class, new AABB(pos).inflate(8.0, 6.0, 8.0));
        if (!antList.isEmpty()) {
            List<Player> playerList = world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8.0, 6.0, 8.0));
            for (Ant ant : antList) {
                if (ant.getTarget() != null) continue;
                ant.setTarget(playerList.get(world.random.nextInt(playerList.size())));
            }
        }
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos blockPos, FallingBlockEntity fallingBlockEntity) {
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
    }
}
