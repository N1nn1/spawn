package com.ninni.spawn.entity.ai.goal;

import com.ninni.spawn.entity.common.BoidFishEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

// Code took and modified from https://github.com/Tomate0613/boids,
// I went ahead and used it because the project's license is MIT,
// but if you are the author or someone that knows the author reading this
// and you are not ok with me using it, please put me in contact with the author directly and I will act accordingly by removing it

public class HeightBoundsGoal extends Goal {
    private final BoidFishEntity mob;

    public HeightBoundsGoal(BoidFishEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return (this.isTooHigh() || this.isTooLow()) && mob.isInWaterOrBubble() && (mob.isFollower() || mob.hasFollowers());
    }

    @Override
    public void tick() {


        mob.addDeltaMovement(bounds());
    }

    public Vec3 bounds() {
        var amount = 0.1;
        var dY = Mth.abs((float) mob.getDeltaMovement().y);

        if (dY > amount) {
            amount = dY;
        }



        if (this.isTooHigh()) {
            return new Vec3(0, -amount, 0);
        }
        if (this.isTooLow())
            return new Vec3(0, amount, 0);

        return Vec3.ZERO;
    }

    private boolean isTooHigh() {
        return !mob.level().getBlockState(mob.blockPosition().above()).is(Blocks.WATER) && !mob.level().getBlockState(mob.blockPosition().above(2)).is(Blocks.WATER);
    }

    private boolean isTooLow() {
        return !mob.level().getBlockState(mob.blockPosition().below()).is(Blocks.WATER);
    }
}