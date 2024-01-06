package com.ninni.spawn.entity;

import com.mojang.serialization.Codec;
import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public class Seahorse extends AbstractFish implements VariantHolder<Seahorse.Pattern> {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(Seahorse.class, EntityDataSerializers.INT);

    public Seahorse(EntityType<? extends Seahorse> type, Level world) {
        super(type, world);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        SpawnGroupData spawnGroupData2 = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        if (mobSpawnType == MobSpawnType.BUCKET && compoundTag != null && compoundTag.contains("BucketVariantTag", 3)) {
            this.setPackedVariant(compoundTag.getInt("BucketVariantTag"));
        } else {
            RandomSource randomSource = serverLevelAccessor.getRandom();
            Variant variant;
                Pattern[] patterns = Pattern.values();
                DyeColor[] dyeColors = DyeColor.values();
                Pattern pattern = Util.getRandom(patterns, randomSource);
                DyeColor dyeColor = Util.getRandom(dyeColors, randomSource);
                DyeColor dyeColor2 = Util.getRandom(dyeColors, randomSource);
                variant = new Variant(pattern, dyeColor, dyeColor2);

            this.setPackedVariant(variant.getPackedId());
        }
        return spawnGroupData2;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.65D)
                .add(Attributes.MAX_HEALTH, 4.0);
    }
    
    @Override
    protected SoundEvent getFlopSound() {
        return SpawnSoundEvents.FISH_FLOP;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.95F;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return SpawnItems.SEAHORSE_BUCKET.getDefaultInstance();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SpawnSoundEvents.SEAHORSE_DEATH;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SpawnSoundEvents.FISH_SWIM;
    }

    @Override
    public @Nullable SoundEvent getAmbientSound() {
        return this.isUnderWater() ? (this.random.nextInt(3) == 0
                ? SpawnSoundEvents.SEAHORSE_GROWL
                : SpawnSoundEvents.SEAHORSE_CLICK
        ) : null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SpawnSoundEvents.SEAHORSE_HURT;
    }

    public static String getPredefinedName(int i) {
        return "entity.spawn.seahorse.variant.predefined." + i;
    }

    static int packVariant(Pattern pattern, DyeColor dyeColor, DyeColor dyeColor2) {
        return pattern.getPackedId() & '\uffff' | (dyeColor.getId() & 255) << 16 | (dyeColor2.getId() & 255) << 24;
    }

    public static DyeColor getBaseColor(int i) {
        return DyeColor.byId(i >> 16 & 255);
    }

    public static DyeColor getPatternColor(int i) {
        return DyeColor.byId(i >> 24 & 255);
    }

    public static Pattern getPattern(int i) {
        return Pattern.byId(i & '\uffff');
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.getPackedVariant());
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setPackedVariant(compoundTag.getInt("Variant"));
    }

    private void setPackedVariant(int i) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, i);
    }

    private int getPackedVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public DyeColor getBaseColor() {
        return getBaseColor(this.getPackedVariant());
    }

    public DyeColor getPatternColor() {
        return getPatternColor(this.getPackedVariant());
    }

    public Pattern getVariant() {
        return getPattern(this.getPackedVariant());
    }

    public void setVariant(Pattern pattern) {
        int i = this.getPackedVariant();
        DyeColor dyeColor = getBaseColor(i);
        DyeColor dyeColor2 = getPatternColor(i);
        this.setPackedVariant(packVariant(pattern, dyeColor, dyeColor2));
    }

    public void saveToBucketTag(ItemStack itemStack) {
        super.saveToBucketTag(itemStack);
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putInt("BucketVariantTag", this.getPackedVariant());
    }
    
    public enum Pattern implements StringRepresentable {
        RIDGED("ridged", Seahorse.Base.SMALL, 0),
        SHARP("sharp", Seahorse.Base.SMALL, 1),
        HOODED("hooded", Seahorse.Base.SMALL, 2),
        SPOTTED("spotted", Seahorse.Base.SMALL, 3),
        BANDED("banded", Seahorse.Base.LARGE, 0),
        WAVY("wavy", Seahorse.Base.LARGE, 1),
        RIBBED("ribbed", Seahorse.Base.LARGE, 2),
        CURLED("curled", Seahorse.Base.LARGE, 3);

        public static final Codec<Pattern> CODEC = StringRepresentable.fromEnum(Pattern::values);
        private static final IntFunction<Pattern> BY_ID = ByIdMap.sparse(Pattern::getPackedId, values(), RIDGED);
        private final String name;
        private final Component displayName;
        private final Seahorse.Base base;
        private final int packedId;

        Pattern(String string2, Seahorse.Base base, int j) {
            this.name = string2;
            this.base = base;
            this.packedId = base.id | j << 8;
            this.displayName = Component.translatable("entity.spawn.seahorse.variant." + this.name);
        }

        public static Pattern byId(int i) {
            return BY_ID.apply(i);
        }

        public Seahorse.Base base() {
            return this.base;
        }

        public int getPackedId() {
            return this.packedId;
        }

        public String getSerializedName() {
            return this.name;
        }

        public Component displayName() {
            return this.displayName;
        }
    }

    public record Variant(Pattern pattern, DyeColor dyeColor, DyeColor dyeColor2) {
        public Variant(Pattern pattern, DyeColor dyeColor, DyeColor dyeColor2) {
            this.pattern = pattern;
            this.dyeColor = dyeColor;
            this.dyeColor2 = dyeColor2;
        }

        public int getPackedId() {
            return packVariant(this.pattern, this.dyeColor, this.dyeColor2);
        }

        public Pattern pattern() {
            return this.pattern;
        }

        public DyeColor dyeColor() {
            return this.dyeColor;
        }

        public DyeColor dyeColor2() {
            return this.dyeColor2;
        }
    }

    public enum Base {
        SMALL(0),
        LARGE(1);

        final int id;

        Base(int j) {
            this.id = j;
        }
    }
}
