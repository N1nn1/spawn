package com.ninni.spawn.entity.common;

import com.mojang.datafixers.DataFixUtils;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;
import java.util.function.Predicate;

public class OrganizeBoidSchoolingGoal extends Goal {
    private final BoidFishEntity fish;
    private static final int INTERVAL_TICKS = 200;
    private int nextStartTick;

    public OrganizeBoidSchoolingGoal(BoidFishEntity boidFishEntity) {
        this.fish = boidFishEntity;
        this.nextStartTick = this.nextStartTick(boidFishEntity);
    }

    protected int nextStartTick(BoidFishEntity abstractSchoolingFish) {
        return FollowFlockLeaderGoal.reducedTickDelay(INTERVAL_TICKS + abstractSchoolingFish.getRandom().nextInt(INTERVAL_TICKS) % 20);
    }

    @Override
    public boolean canUse() {
        if (this.fish.hasFollowers()) {
            return false;
        }
        if (this.fish.isFollower()) {
            return true;
        }
        if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        }
        this.nextStartTick = this.nextStartTick(this.fish);
        Predicate<BoidFishEntity> predicate = abstractSchoolingFish -> abstractSchoolingFish.canBeFollowed() || !abstractSchoolingFish.isFollower();
        List<? extends BoidFishEntity> list = this.fish.level().getEntitiesOfClass(this.fish.getClass(), this.fish.getBoundingBox().inflate(8.0, 8.0, 8.0), predicate);
        BoidFishEntity abstractSchoolingFish2 = DataFixUtils.orElse(list.stream().filter(BoidFishEntity::canBeFollowed).findAny(), this.fish);
        abstractSchoolingFish2.addFollowers(list.stream().filter(abstractSchoolingFish -> !abstractSchoolingFish.isFollower()));
        return this.fish.isFollower();
    }

    @Override
    public boolean canContinueToUse() {
        return this.fish.isFollower() && this.fish.inRangeOfLeader();
    }

    @Override
    public void stop() {
        this.fish.stopFollowing();
    }
}

