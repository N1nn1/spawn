package com.ninni.spawn.entity;


import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

public class AnglerFish extends TiltingFishEntity implements Bucketable, DeepLurker, FlopConditionable {
    public static final Ingredient TEMPT_INGREDIENT = Ingredient.of(SpawnTags.ANGLER_FISH_TEMPTS);
    public static final String LAST_EFFECT_GIVEN_KEY = "LastEffectGiven";

    public static final int EFFECT_DURATION = 300;
    public static final int EFFECT_DELAY = EFFECT_DURATION + 30;

    private boolean lastDeflated;
    private long lastDeflationSound;
    private long lastEffectGiven;

    public AnglerFish(EntityType<? extends AnglerFish> type, Level world) {
        super(type, world);
    }

    public boolean isDeflated() {
        return this.getAirSupply() < this.getMaxAirSupply() - 4 || !this.isAlive();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.25F, TEMPT_INGREDIENT, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            boolean deflated = this.isDeflated();
            if (deflated && !this.lastDeflated) {
                long time = this.level.getGameTime();
                if (this.lastDeflationSound == 0 || time - this.lastDeflationSound >= 15) {
                    this.level.playSound(null, this, SpawnSoundEvents.ENTITY_ANGLER_FISH_DEFLATE, this.getSoundSource(), this.getSoundVolume(), this.getVoicePitch());
                    this.lastDeflationSound = time;
                }
            }
            this.lastDeflated = deflated;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        //TODO
        //if (this.level.isClientSide && this.isUnderWater() && this.tickCount % 2 == 0) {
        //    EntityHelper.addParticle(this, SofishticatedParticleTypes.ANGLER_FISH_LANTERN_GLOW, 0.15D, 1.25D);
        //}
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!this.level.isClientSide) {
            if (stack.is(SpawnTags.ANGLER_FISH_FEEDS) && !this.isDeflated() && !player.hasEffect(MobEffects.NIGHT_VISION)) {
                long time = this.level.getGameTime();
                if (this.lastEffectGiven == 0 || time - this.lastEffectGiven > EFFECT_DELAY * 20) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, EFFECT_DURATION * 20, 0, false, true));
                    if (!player.getAbilities().instabuild) stack.shrink(1);

                    // add visual effects
                    this.playSound(SpawnSoundEvents.ENTITY_ANGLER_FISH_EFFECT_GIVE);
                    //TODO
                    //EntityHelper.spawnParticles(player, SofishticatedParticleTypes.ANGLER_FISH_LANTERN_GLOW, 0.1D, 0.5D, 0.25D, 40, 1.0D);

                    this.lastEffectGiven = time;
                } else {
                    this.playSound(SpawnSoundEvents.ENTITY_ANGLER_FISH_EFFECT_DENY);
                    //TODO
                    //EntityHelper.spawnParticles(this, SofishticatedParticleTypes.ANGLER_FISH_LANTERN_GLOW, 0.05D, 0.5D, 0.25D, 10, 0.0D);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean doesFlopWhileOutOfWater() {
        int air = this.getAirSupply();
        return air % (air > 40 ? 15 : 10) + this.random.nextInt(5) == 0 && this.isAlive();
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        return this.getLurkingPathfindingFavor(blockPos, levelReader);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return SpawnItems.ANGLER_FISH_BUCKET.getDefaultInstance();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SpawnSoundEvents.ENTITY_FISH_DEATH;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SpawnSoundEvents.ENTITY_FISH_SWIM;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SpawnSoundEvents.ENTITY_FISH_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SpawnSoundEvents.ENTITY_FISH_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SpawnSoundEvents.ENTITY_FISH_FLOP;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putLong(LAST_EFFECT_GIVEN_KEY, this.lastEffectGiven);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.lastEffectGiven = compoundTag.getLong(LAST_EFFECT_GIVEN_KEY);
    }

}
