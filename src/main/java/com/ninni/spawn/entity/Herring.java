package com.ninni.spawn.entity;

import com.ninni.spawn.entity.common.BoidGoal;
import com.ninni.spawn.entity.common.HeightBoundsGoal;
import com.ninni.spawn.entity.common.LimitSpeedAndLookInVelocityDirectionGoal;
import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Herring extends AbstractSchoolingFish {
    public Herring(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02f, 0.1f, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new BoidGoal(this, 0.2f, 0.4f, 8 / 20f, 1 / 20f));
        this.goalSelector.addGoal(3, new HeightBoundsGoal(this));
        this.goalSelector.addGoal(2, new LimitSpeedAndLookInVelocityDirectionGoal(this, 0.3f, 0.4f));
    }


    @Override
    public int getMaxSchoolSize() {
        return 20;
    }


    @Override
    protected SoundEvent getSwimSound() {
        return this.random.nextInt(10) == 0 ? super.getSwimSound() : SoundEvents.EMPTY;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(SpawnItems.HERRING_BUCKET);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SpawnSoundEvents.FISH_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SpawnSoundEvents.FISH_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SpawnSoundEvents.FISH_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SpawnSoundEvents.FISH_FLOP;
    }
}
