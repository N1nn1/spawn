package com.ninni.spawn.entity.ai.control;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GoodAmphibiousMovement {

    public static class GoodAmphibiousMoveControl extends MoveControl {

        public GoodAmphibiousMoveControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.STRAFE || this.operation == Operation.JUMPING || (this.mob.onGround() && !this.mob.isInWaterOrBubble())) {
                super.tick();
            }
            if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
                double d = this.wantedX - this.mob.getX();
                double e = this.wantedY - this.mob.getY();
                double f = this.wantedZ - this.mob.getZ();
                double g = d * d + e * e + f * f;
                if (g < 2.5) {
                    this.mob.setZza(0.0F);
                } else {
                    float movementSpeed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    this.mob.setYRot(this.rotlerp(this.mob.getYRot(), (float) (Mth.atan2(f, d) * 57.3) - 90.0F, 10.0F));
                    this.mob.yBodyRot = this.mob.getYRot();
                    this.mob.yHeadRot = this.mob.getYRot();
                    if (this.mob.isInWaterOrBubble()) {
                        this.mob.setSpeed(movementSpeed * 0.4F);
                        if (this.mob.onGround()) this.mob.setSpeed(movementSpeed * 0.1F);
                        float j = -((float) (Mth.atan2(e, Mth.sqrt((float) (d * d + f * f))) * 57.3));
                        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), Mth.clamp(Mth.wrapDegrees(j), -85.0F, 85.0F), 5.0F));
                        this.mob.zza = Mth.cos(this.mob.getXRot() * (float) Math.PI / 180f) * movementSpeed;
                        this.mob.yya = -Mth.sin(this.mob.getXRot() * (float) Math.PI / 180f) * movementSpeed;
                    }
                }
            }
        }
    }

    public static class GoodAmphibiousLookControl extends SmoothSwimmingLookControl {

        public GoodAmphibiousLookControl(Mob mob) {
            super(mob, 10);
        }

        @Override
        public void tick() {
            if (mob.isInWaterOrBubble()) {
                if (this.lookAtCooldown > 0) {
                    --this.lookAtCooldown;
                    this.getYRotD().ifPresent(float_ -> this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, float_ + 20.0f, this.yMaxRotSpeed));
                    this.getXRotD().ifPresent(float_ -> this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), float_ + 10.0f, this.xMaxRotAngle)));
                } else {
                    if (this.mob.getNavigation().isDone()) {
                        this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), 0.0f, 5.0f));
                    }
                    this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
                }
                float f = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
                if (f < -10) {
                    this.mob.yBodyRot -= 4.0f;
                } else if (f > 10) {
                    this.mob.yBodyRot += 4.0f;
                }
            } else {
                if (this.resetXRotOnTick()) {
                    this.mob.setXRot(0.0f);
                }
                if (this.lookAtCooldown > 0) {
                    --this.lookAtCooldown;
                    this.getYRotD().ifPresent(float_ -> this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, float_, this.yMaxRotSpeed));
                    this.getXRotD().ifPresent(float_ -> this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), float_, this.xMaxRotAngle)));
                } else {
                    this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0f);
                }
                this.clampHeadRotationToBody();
            }
        }
    }


    public static class GoodSwimNavigation extends WaterBoundPathNavigation {
        public GoodSwimNavigation(Mob mob, Level world) { super(mob, world); }

        @Override
        protected PathFinder createPathFinder(int range) {
            this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
            return new PathFinder(this.nodeEvaluator, range);
        }

        @Override
        protected boolean canUpdatePath() {
            return true;
        }

        @Override
        public boolean isStableDestination(BlockPos blockPos) {
            return !this.level.getBlockState(blockPos.below(2)).isAir();
        }
    }

    public static class MoveAroundGoal extends RandomStrollGoal {

        public MoveAroundGoal(PathfinderMob pathfinderMob) {
            super(pathfinderMob, 1.0, 10);
        }

        @Nullable
        protected Vec3 getPosition() {
            return this.mob.isInWaterOrBubble() ? BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 7) :  DefaultRandomPos.getPos(this.mob, 10, 7);
        }
    }
}