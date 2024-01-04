package com.ninni.spawn.entity.common;

import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class WaterJumpGoal extends JumpGoal {
    private static final int[] STEPS_TO_CHECK = new int[]{0, 1, 4, 5, 6, 7};
    private final PathfinderMob entity;
    private final int interval;
    private boolean breached;

    public WaterJumpGoal(PathfinderMob entity, int i) {
        this.entity = entity;
        this.interval = reducedTickDelay(i);
    }

    public boolean canUse() {
        if (this.entity.getRandom().nextInt(this.interval) != 0) {
            return false;
        } else {
            Direction direction = this.entity.getMotionDirection();
            int i = direction.getStepX();
            int j = direction.getStepZ();
            BlockPos blockPos = this.entity.blockPosition();

            for (int k : STEPS_TO_CHECK) {
                if (!this.waterIsClear(blockPos, i, j, k) || !this.surfaceIsClear(blockPos, i, j, k)) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean waterIsClear(BlockPos blockPos, int i, int j, int k) {
        BlockPos blockPos2 = blockPos.offset(i * k, 0, j * k);
        return this.entity.level().getFluidState(blockPos2).is(FluidTags.WATER) && !this.entity.level().getBlockState(blockPos2).blocksMotion();
    }

    private boolean surfaceIsClear(BlockPos blockPos, int i, int j, int k) {
        return this.entity.level().getBlockState(blockPos.offset(i * k, 1, j * k)).isAir() && this.entity.level().getBlockState(blockPos.offset(i * k, 2, j * k)).isAir();
    }

    public boolean canContinueToUse() {
        double d = this.entity.getDeltaMovement().y;
        return (!(d * d < 0.029999999329447746) || this.entity.getXRot() == 0.0F || !(Math.abs(this.entity.getXRot()) < 10.0F) || !this.entity.isInWater()) && !this.entity.onGround();
    }

    public boolean isInterruptable() {
        return false;
    }

    public void start() {
        Direction direction = this.entity.getMotionDirection();
        this.entity.setDeltaMovement(this.entity.getDeltaMovement().add((double)direction.getStepX() * 0.8, 0.7, (double)direction.getStepZ() * 0.8));
        this.entity.getNavigation().stop();
    }

    public void stop() {
        this.entity.setXRot(0.0F);
        this.breached = false;
    }

    public void tick() {



        Vec3 vec3 = this.entity.getDeltaMovement();
        if (vec3.y * vec3.y < 0.029999999329447746 && this.entity.getXRot() != 0.0F) {
            this.entity.setXRot(Mth.rotLerp(0.2F, this.entity.getXRot(), 0.0F));
        } else if (vec3.length() > 9.999999747378752E-6) {

            if (!breached) {
                this.entity.playSound(SpawnSoundEvents.FISH_JUMP, 1.0F, 1.0F);
                this.breached = true;
            }

            double d = vec3.horizontalDistance();
            double e = Math.atan2(-vec3.y, d) * 57.2957763671875;
            this.entity.setXRot((float)e);
        }

    }
}
