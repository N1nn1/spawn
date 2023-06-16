package com.ninni.spawn.entity.ai;

import com.ninni.spawn.entity.Ant;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CollectResourceGoal extends Goal {
    private final Ant ant;
    private ItemEntity target;

    public CollectResourceGoal(Ant ant) {
        this.ant = ant;
    }

    @Override
    public boolean canUse() {
        if (this.target == null) {
            this.target = this.getTarget();
        }
        return this.ant.getInventory().isEmpty() && this.target != null;
    }

    @Override
    public void start() {
        if (this.target != null) {
            this.ant.getNavigation().moveTo(this.target, 1.0F);
            this.ant.getLookControl().setLookAt(this.target);
        }
    }

    public ItemEntity getTarget() {
        List<ItemEntity> list = this.ant.level().getEntitiesOfClass(ItemEntity.class, this.ant.getBoundingBox().inflate(32.0, 16.0, 32.0), itemEntity -> true);
        list.sort(Comparator.comparingDouble(this.ant::distanceToSqr));
        Optional<ItemEntity> optional = list.stream().filter(itemEntity -> this.ant.wantsToPickUp(itemEntity.getItem())).filter(itemEntity -> itemEntity.closerThan(this.ant, 32.0)).filter(this.ant::hasLineOfSight).findFirst();
        return optional.orElse(null);
    }

}
