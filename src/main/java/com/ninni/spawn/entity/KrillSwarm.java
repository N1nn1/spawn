package com.ninni.spawn.entity;

import com.ninni.spawn.entity.common.DeepLurker;
import com.ninni.spawn.entity.common.FlopConditionable;
import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public class KrillSwarm extends AbstractSchoolingFish implements FlopConditionable, DeepLurker {

    public KrillSwarm(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1.0D, 10));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.65D)
                .add(Attributes.MAX_HEALTH, 1.0);
    }

    public void aiStep() {
        super.aiStep();
        float size = 0.4f;
        if (random.nextInt(2) == 0 && this.isInWaterOrBubble()) this.level().addParticle(SpawnParticleTypes.KRILL, this.getRandomX(size), this.getRandomY(), this.getRandomZ(size), 0.0, 0.0, 0.0);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return SpawnItems.KRILL_SWARM_BUCKET.getDefaultInstance();
    }

    @Override
    protected SoundEvent getFlopSound() {
        return null;
    }

    @Override
    public boolean doesFlopWhileOutOfWater() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
        if (entity instanceof KrillSwarm) {
            super.doPush(entity);
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        return this.getLurkingPathfindingFavor(blockPos, levelReader);
    }

    @Override
    protected void blockedByShield(LivingEntity livingEntity) {
        livingEntity.knockback(0, this.getX(), this.getZ());
    }

    @Override
    public boolean startRiding(Entity p_20330_) {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

}
