package com.ninni.spawn.entity;

import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class TunaEgg extends Mob implements Bucketable {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(TunaEgg.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> HATCH_TICKS = SynchedEntityData.defineId(TunaEgg.class, EntityDataSerializers.INT);
    public static final String BUCKET_VARIANT_TAG = "BucketVariantTag";

    public TunaEgg(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if (mobSpawnType == MobSpawnType.BUCKET && compoundTag != null && compoundTag.contains(BUCKET_VARIANT_TAG, 3)) {
            this.setHatchTicks(compoundTag.getInt(BUCKET_VARIANT_TAG));
            return spawnGroupData;
        }
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    public void saveToBucketTag(ItemStack itemStack) {
        Bucketable.saveDefaultDataToBucketTag(this, itemStack);
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putInt(BUCKET_VARIANT_TAG, this.getHatchTicks());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROM_BUCKET, false);
        this.entityData.define(HATCH_TICKS, 0);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }
    @Override
    public void setFromBucket(boolean bl) {
        this.entityData.set(FROM_BUCKET, bl);
    }

    public int getHatchTicks() {
        return this.entityData.get(HATCH_TICKS);
    }
    public void setHatchTicks(int hatchTicks) {
        this.entityData.set(HATCH_TICKS, hatchTicks);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("FromBucket", this.fromBucket());
        compoundTag.putInt("HatchTicks", this.getHatchTicks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setFromBucket(compoundTag.getBoolean("FromBucket"));
        this.setHatchTicks(compoundTag.getInt("HatchTicks"));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getHatchTicks() > 0) this.setHatchTicks(this.getHatchTicks() - 1);
        if (this.getHatchTicks() == 0 && this.getLevel() instanceof ServerLevel serverLevel) this.hatch(serverLevel);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        return Bucketable.bucketMobPickup(player, interactionHand, this).orElse(super.mobInteract(player, interactionHand));
    }

    public void hatch(ServerLevel level) {
        Tuna tuna = SpawnEntityType.TUNA.create(level);
        assert tuna != null;
        tuna.setBaby(true);
        tuna.setPersistenceRequired();
        tuna.moveTo(this.getX(), this.getY(), this.getZ(), 0.0f, 0.0f);
        level.addFreshEntity(tuna);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public void loadFromBucketTag(CompoundTag compoundTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return SpawnItems.TUNA_EGG_BUCKET.getDefaultInstance();
    }
}
