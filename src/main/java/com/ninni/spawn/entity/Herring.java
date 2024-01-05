package com.ninni.spawn.entity;

import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Herring extends AbstractSchoolingFish {
    public Herring(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0);
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
