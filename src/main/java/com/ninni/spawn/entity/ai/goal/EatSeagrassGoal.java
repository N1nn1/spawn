package com.ninni.spawn.entity.ai.goal;

import com.ninni.spawn.entity.SeaCow;
import com.ninni.spawn.registry.SpawnBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.EnumSet;
import java.util.function.Predicate;

public class EatSeagrassGoal extends Goal {
    private static final Predicate<BlockState> IS_TALL_GRASS = BlockStatePredicate.forBlock(Blocks.TALL_SEAGRASS);
    private final SeaCow seaCow;
    private final Level level;
    private int eatAnimationTick;

    public EatSeagrassGoal(SeaCow seaCow) {
        this.seaCow = seaCow;
        this.level = seaCow.level();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        BlockPos blockPos = seaCow.getHeadBlockPos();
        if (IS_TALL_GRASS.test(this.level.getBlockState(blockPos)) && this.seaCow.getMunchingCooldown() == 0 && this.seaCow.isInWaterOrBubble()) {
            return (this.level.getBlockState(blockPos).getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER && this.level.getBlockState(blockPos.below()).is(SpawnBlocks.ALGAL_SAND)) || (this.level.getBlockState(blockPos).getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER && this.level.getBlockState(blockPos.below().below()).is(SpawnBlocks.ALGAL_SAND));
        }
        return false;
    }

    @Override
    public void start() {
        this.eatAnimationTick = this.adjustedTickDelay(40);
        this.level.broadcastEntityEvent(this.seaCow, (byte)10);
        this.seaCow.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.eatAnimationTick = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.eatAnimationTick > 0 && this.seaCow.getMunchingCooldown() == 0;
    }

    public int getEatAnimationTick() {
        return this.eatAnimationTick;
    }

    @Override
    public void tick() {
        BlockPos blockPos = seaCow.getHeadBlockPos();

        this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        if (this.eatAnimationTick != this.adjustedTickDelay(4)) {
            return;
        }
        if (IS_TALL_GRASS.test(this.level.getBlockState(blockPos))) {
            if (this.level.getBlockState(blockPos).getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER && this.level.getBlockState(blockPos.below()).is(SpawnBlocks.ALGAL_SAND)) {
                this.level.setBlock(blockPos, SpawnBlocks.TRIMMED_SEAGRASS.defaultBlockState(), 3);
                this.seaCow.ate();
            } else if (this.level.getBlockState(blockPos).getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER && this.level.getBlockState(blockPos.below().below()).is(SpawnBlocks.ALGAL_SAND)) {
                this.level.setBlock(blockPos.below(), SpawnBlocks.TRIMMED_SEAGRASS.defaultBlockState(), 3);
                this.seaCow.ate();
            }
        }
    }
}

