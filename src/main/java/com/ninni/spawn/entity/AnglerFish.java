package com.ninni.spawn.entity;


import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.entity.common.DeepLurker;
import com.ninni.spawn.entity.common.FlopConditionable;
import com.ninni.spawn.entity.common.TiltingFishEntity;
import com.ninni.spawn.registry.SpawnCriteriaTriggers;
import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnParticles;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            boolean deflated = this.isDeflated();
            if (deflated && !this.lastDeflated) {
                long time = this.level().getGameTime();
                if (this.lastDeflationSound == 0 || time - this.lastDeflationSound >= 15) {
                    this.level().playSound(null, this, SpawnSoundEvents.ANGLER_FISH_DEFLATE, this.getSoundSource(), this.getSoundVolume(), this.getVoicePitch());
                    this.lastDeflationSound = time;
                }
            }
            this.lastDeflated = deflated;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isUnderWater() && this.tickCount % 4 == 0) {
            this.level().addParticle(SpawnParticles.ANGLER_FISH_LANTERN_GLOW, this.getRandomX(0.15D), this.getY(1.25D), this.getRandomZ(0.15D), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!this.level().isClientSide) {
            if (stack.is(SpawnTags.ANGLER_FISH_LIKES) && !this.isDeflated() && !player.hasEffect(MobEffects.NIGHT_VISION)) {
                long time = this.level().getGameTime();
                if (this.lastEffectGiven == 0 || time - this.lastEffectGiven > EFFECT_DELAY * 20) {
                    if (player instanceof ServerPlayer serverPlayer) SpawnCriteriaTriggers.INTERACT_WITH_ANGLER_FISH.trigger(serverPlayer);
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, EFFECT_DURATION * 20, 0, false, true));
                    if (!player.getAbilities().instabuild) stack.shrink(1);

                    // add visual effects
                    this.playSound(SpawnSoundEvents.ANGLER_FISH_EFFECT_GIVE);
                    if (this.level() instanceof ServerLevel world) {
                        world.sendParticles(SpawnParticles.ANGLER_FISH_LANTERN_GLOW, this.getRandomX(0.1F), this.getY(0.5F), this.getRandomZ(0.1F), 40, 25F, 25F, 25F, 0F);
                    }

                    this.lastEffectGiven = time;
                } else {
                    this.playSound(SpawnSoundEvents.ANGLER_FISH_EFFECT_DENY);
                    if (this.level() instanceof ServerLevel world) {
                        world.sendParticles(SpawnParticles.ANGLER_FISH_LANTERN_GLOW, this.getRandomX(0.05F), this.getY(0.5F), this.getRandomZ(0.05F), 10, 25F, 25F, 25F, 0F);
                    }
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
        return SpawnSoundEvents.FISH_DEATH;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SpawnSoundEvents.FISH_SWIM;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SpawnSoundEvents.FISH_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SpawnSoundEvents.FISH_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SpawnSoundEvents.FISH_FLOP;
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
