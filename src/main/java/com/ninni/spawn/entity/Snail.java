package com.ninni.spawn.entity;

import com.google.common.collect.Lists;
import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.block.SnailEggsBlock;
import com.ninni.spawn.registry.SpawnBlocks;
import com.ninni.spawn.registry.SpawnCriteriaTriggers;
import com.ninni.spawn.registry.SpawnItems;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Snail extends Animal {
    private static final EntityDataAccessor<Integer> SCARED_TICKS = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WET_TICKS = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHELL_GROWTH = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);
    private final UniformInt regrowthTicks = UniformInt.of(12000, 24000);
    private int cooldown = 2;

    public Snail(EntityType<? extends Snail> entityType, Level world) {
        super(entityType, world);
        this.setMaxUpStep(1);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(SpawnTags.SNAIL_FEEDS);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0F));
        this.goalSelector.addGoal(2, new SnailLayEggGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25, Ingredient.of(SpawnTags.SNAIL_TEMPTS), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1));
        this.goalSelector.addGoal(6, new SnailWanderGoal(this, 1));
        this.goalSelector.addGoal(7, new SnailLookAtEntityGoal(this, Player.class, 6));
        this.goalSelector.addGoal(8, new SnailLookAroundGoal(this));
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal other) {
        if(other instanceof Snail otherSnail) {
            ServerPlayer serverPlayerEntity = this.getLoveCause();
            if (serverPlayerEntity == null) serverPlayerEntity = otherSnail.getLoveCause();
            if (serverPlayerEntity != null) {
                serverPlayerEntity.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayerEntity, this, otherSnail, null);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayerEntity, otherSnail, this, null);
            }
            this.setAge(6000);
            otherSnail.setAge(6000);
            this.resetLove();
            otherSnail.resetLove();
            this.setHasEgg(true);
            otherSnail.setHasEgg(true);
            world.broadcastEntityEvent(this, EntityEvent.IN_LOVE_HEARTS);
            if (world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                world.addFreshEntity(new ExperienceOrb(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SCARED_TICKS, 0);
        this.entityData.define(WET_TICKS, 0);
        this.entityData.define(SHELL_GROWTH, 0);
        this.entityData.define(HAS_EGG, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("ScaredTicks", this.getScaredTicks());
        compoundTag.putInt("WetTicks", this.getWetTicks());
        compoundTag.putInt("Shelled", this.getShellGrowthTicks());
        compoundTag.putBoolean("HasEgg", this.hasEgg());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setScaredTicks(compoundTag.getInt("ScaredTicks"));
        this.setWetTicks(compoundTag.getInt("WetTicks"));
        this.setShellGrowthTicks(compoundTag.getInt("Shelled"));
        this.setHasEgg(compoundTag.getBoolean("HasEgg"));
    }

    public int getScaredTicks() {
        return this.entityData.get(SCARED_TICKS);
    }
    public void setScaredTicks(int scaredTicks) {
        this.entityData.set(SCARED_TICKS, scaredTicks);
    }
    public boolean isScared() {
        return this.getScaredTicks() > 0;
    }
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    public int getWetTicks() {
        return this.entityData.get(WET_TICKS);
    }
    public void setWetTicks(int wetTicks) {this.entityData.set(WET_TICKS, wetTicks);}
    public void addWetTicks(int wetTicks) {
        this.setWetTicks(this.getWetTicks() + wetTicks);
    }

    public int getShellGrowthTicks() {
        return this.entityData.get(SHELL_GROWTH);
    }
    public void setShellGrowthTicks(int shellTicks) {
        this.entityData.set(SHELL_GROWTH, shellTicks);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (this.isFood(itemStack) && this.getAge() == 0) {
            if(itemStack.is(Items.MUSHROOM_STEW)) this.spawnAtLocation(Items.BOWL);
            this.playSound(SpawnSoundEvents.SNAIL_EAT, 1, 1);
        }

        if (itemStack.is(Items.WATER_BUCKET) && !this.isScared() && this.getWetTicks() < 900) {
            if (!this.level().isClientSide) {
                if (!player.getAbilities().instabuild) player.setItemInHand(player.getUsedItemHand(), Items.BUCKET.getDefaultInstance());
                this.addWetTicks(300);
                this.playSound(SoundEvents.BUCKET_EMPTY, 1, 1);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (getWetTicks() > 0) {
            this.addWetTicks(-1);
            if (random.nextInt(10) == 0) {
                level().addParticle(ParticleTypes.FALLING_WATER, this.getRandomX(0.6), this.getY() + random.nextDouble(), this.getRandomZ(0.6), 0.0, 0.0, 0.0);
            }
        }
        if (this.getBlockStateOn().is(SpawnTags.MUCUS_SOLIDIFIER) || this.isInWaterOrRain()) this.addWetTicks(1);

        if (!this.level().isClientSide && this.onGround() && this.getWetTicks() > 0) {
            BlockState blockState = SpawnBlocks.MUCUS.defaultBlockState();
            for (int l = 0; l < 4; ++l) {
                if (cooldown < 0) {
                    int i = Mth.floor(this.getX() + (double)((float)(l % 2 * 2 - 1) * 0.25f));
                    BlockPos blockPos2 = new BlockPos(i, Mth.floor(this.getY()), Mth.floor(this.getZ() + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25f)));
                    if (!this.level().getBlockState(blockPos2).isAir() || !blockState.canSurvive(this.level(), blockPos2)) continue;
                    this.level().setBlockAndUpdate(blockPos2, blockState);
                    cooldown = 10;
                    this.playStepSound(blockPos2, blockState);
                    this.level().gameEvent(GameEvent.BLOCK_PLACE, blockPos2, GameEvent.Context.of(this, blockState));
                }
            }
            cooldown--;
        }

        //code snatched with permission from orcinus (ily)
        this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(2D), this::isValidEntity).forEach(player -> this.setScaredTicks(100));

        if (this.getScaredTicks() > 0) {
            this.getNavigation().stop();
            this.setScaredTicks(this.getScaredTicks() - 1);
        }

        if (!this.level().isClientSide()) {
            int shellGrowthTicks = this.getShellGrowthTicks();
            if (shellGrowthTicks > 0 && !this.isBaby()) {
                if (shellGrowthTicks == 1) {
                    this.playSound(SpawnSoundEvents.SNAIL_SHELL_GROW, 1.0F, 1.0F);
                }
                this.setShellGrowthTicks(shellGrowthTicks - 1);
            }
        }
    }

    private boolean isValidEntity(Player entity) {
        return this.getShellGrowthTicks() == 0 && !entity.isSpectator() && entity.isAlive() && !entity.getAbilities().instabuild && !entity.isShiftKeyDown();
    }

    @Override
    public boolean isPushable() {
        return !this.isScared();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        //code snatched with permission from lunarbunten (ily)
        if (!this.level().isClientSide && this.getShellGrowthTicks() == 0) {
            if (source.is(DamageTypeTags.IS_PROJECTILE)) {

                if (!this.isScared()) {
                    this.spawnAtLocation(new ItemStack(SpawnItems.SNAIL_SHELL), 0.1F);
                    this.playSound(SpawnSoundEvents.SNAIL_HURT_HIDDEN, 1.0F, 1.0F);
                    this.setShellGrowthTicks(this.regrowthTicks.sample(this.random));
                }
                return false;
            } else {
                this.setScaredTicks(100);
            }
        }

        if (source.getEntity() instanceof LivingEntity && amount < 12 && !level().isClientSide) {

            if (this.isScared()) {
                playSound(SpawnSoundEvents.SNAIL_HURT_HIDDEN, 1, 1);
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isScared();
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isScared()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0, 1, 0));
            vec3 = vec3.multiply(0, 1, 0);
        }
        super.travel(vec3);
    }


    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return "Gary".equals(ChatFormatting.stripFormatting(this.getName().getString())) ? SoundEvents.CAT_AMBIENT : super.getAmbientSound();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SpawnSoundEvents.SNAIL_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isScared() ? SpawnSoundEvents.SNAIL_DEATH_HIDDEN : SpawnSoundEvents.SNAIL_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SpawnSoundEvents.SNAIL_SLIDE, 0.15f, 1.0f);
    }


    public static class SnailLayEggGoal extends Goal {
        private final Snail snail;
        private BlockPos layPos;

        SnailLayEggGoal(Snail snail) {
            this.snail = snail;
        }

        @Override
        public boolean canUse() {
            BlockPos layPos = this.getLayPosition();
            if (this.layPos == null && layPos != null) {
                this.layPos = layPos;
            }
            return this.layPos != null && this.snail.hasEgg();
        }

        @Override
        public boolean canContinueToUse() {
            return this.layPos != null && this.snail.level().getBlockState(this.layPos).is(BlockTags.DIRT) && this.snail.level().getBlockState(this.layPos.above()).isAir();
        }


        @Override
        public void start() {
            if (this.layPos != null) {
                Vec3 vec3d = Vec3.atCenterOf(this.layPos);
                this.snail.getNavigation().moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0F);
                this.snail.getLookControl().setLookAt(vec3d);
            }
        }

        @Override
        public void tick() {
            if (this.layPos != null) {
                Vec3 vec3d = Vec3.atCenterOf(this.layPos);
                double distance = Mth.sqrt((float) this.snail.distanceToSqr(vec3d));
                if (distance <= 2) {
                    this.snail.playSound(SpawnSoundEvents.SNAIL_LAY_EGGS, 1, 1);
                    this.snail.level().setBlock(this.layPos, SpawnBlocks.SNAIL_EGGS.defaultBlockState().setValue(SnailEggsBlock.getFaceProperty(Direction.DOWN), true), 2);
                    this.snail.setHasEgg(false);
                }
            }
        }

        @Nullable
        private BlockPos getLayPosition() {
            List<BlockPos> list = Lists.newArrayList();
            int range = 8;
            for (int x = -range; x <= range; x++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos position = new BlockPos((int)this.snail.getX() + x, (int)this.snail.getY(), (int)this.snail.getZ() + z);
                    if (this.snail.level().getBlockState(position.below()).is(BlockTags.DIRT) && this.snail.level().getBlockState(position).isAir()) {
                        list.add(position);
                    }
                }
            }
            if (list.isEmpty()) {
                return null;
            }
            return list.get(this.snail.random.nextInt(list.size()));
        }
    }

    public static class SnailWanderGoal extends WaterAvoidingRandomStrollGoal {
        private final Snail mob;

        public SnailWanderGoal(Snail mob, double d) {
            super(mob, d);
            this.mob = mob;
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.mob.isScared();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.mob.isScared();
        }
    }

    public static class SnailLookAroundGoal extends RandomLookAroundGoal {
        private final Snail mob;

        public SnailLookAroundGoal(Snail mob) {
            super(mob);
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.mob.isScared();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.mob.isScared();
        }
    }

    public static class SnailLookAtEntityGoal extends LookAtPlayerGoal {
        private final Snail mob;

        public SnailLookAtEntityGoal(Snail mob, Class<? extends LivingEntity> targetType, float range) {
            super(mob, targetType, range);
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.mob.isScared();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.mob.isScared();
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    public static boolean canSpawn(EntityType<Snail> snailEntityEntityType, ServerLevelAccessor world, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        return world.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(world, pos);
    }
}
