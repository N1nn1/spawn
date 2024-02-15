package com.ninni.spawn.entity;

import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Whale extends Animal {

    public Whale(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 6, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 6);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.8));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.4, Ingredient.of(SpawnTags.TUNA_TEMPTS), false));
        this.goalSelector.addGoal(4, new RandomWhaleSwimming(this, 1D, 10, 34, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 300.0)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1F)
                .add(Attributes.MOVEMENT_SPEED, 1.2F)
                .add(Attributes.ATTACK_DAMAGE, 30.0);
    }

    @Override
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        this.handleAirSupply(i);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.5f;
    }

    protected void handleAirSupply(int i) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(i - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0f);
            }
        } else {
            this.setAirSupply(300);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SpawnSoundEvents.FISH_DEATH;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SpawnSoundEvents.BIG_FISH_SWIM;
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
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {}

    @Override
    public void push(Entity entity) {
    }

    public void pushEntities() {

    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return SpawnEntityType.WHALE.create(serverLevel);
    }

    @SuppressWarnings("unused, deprecation")
    public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<Whale> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        int i = levelAccessor.getSeaLevel();
        int j = i - 13;
        return blockPos.getY() >= j && blockPos.getY() <= i && levelAccessor.getFluidState(blockPos.below()).is(FluidTags.WATER) && levelAccessor.getBlockState(blockPos.above()).is(Blocks.WATER);
    }

    public static class RandomWhaleSwimming extends RandomStrollGoal {
        private final int xzSpread;
        private final boolean submerged;

        public RandomWhaleSwimming(PathfinderMob creature, double speed, int chance, int xzSpread, boolean submerged) {
            super(creature, speed, chance, false);
            this.xzSpread = xzSpread;
            this.submerged = submerged;
        }

        public boolean canUse() {
            if (this.mob.isVehicle()|| mob.getTarget() != null || !this.mob.isInWater() && !this.mob.isInLava()) {
                return false;
            } else {
                if (!this.forceTrigger) {
                    if (this.mob.getRandom().nextInt(this.interval) != 0) {
                        return false;
                    }
                }
                Vec3 vector3d = this.getPosition();
                if (vector3d == null) {
                    return false;
                } else {
                    this.wantedX = vector3d.x;
                    this.wantedY = vector3d.y;
                    this.wantedZ = vector3d.z;
                    this.forceTrigger = false;
                    return true;
                }
            }
        }


        public static BlockPos blockPosFromVec3(Vec3 vec3){
            return new BlockPos((int) vec3.x, (int) vec3.y, (int) vec3.z);
        }

        @Nullable
        protected Vec3 getPosition() {
            if (this.mob.hasRestriction() && this.mob.distanceToSqr(Vec3.atCenterOf(this.mob.getRestrictCenter())) > this.mob.getRestrictRadius() * this.mob.getRestrictRadius()) {
                return DefaultRandomPos.getPosTowards(this.mob, xzSpread, 3, Vec3.atBottomCenterOf(this.mob.getRestrictCenter()), 3);
            }
            int ySpread = 3;
            if (this.mob.getRandom().nextFloat() < 0.3F) {
                Vec3 vector3d = findSurfaceTarget(this.mob);
                if (vector3d != null) {
                    return vector3d;
                }
            }
            Vec3 vector3d = DefaultRandomPos.getPos(this.mob, xzSpread, ySpread);

            for (int i = 0; vector3d != null && !this.mob.level().getBlockState(blockPosFromVec3(vector3d)).isPathfindable(this.mob.level(), blockPosFromVec3(vector3d), PathComputationType.WATER) && i++ < 15; vector3d = DefaultRandomPos.getPos(this.mob, 10, ySpread)) {
            }
            if (submerged && vector3d != null) {
                if (!this.mob.level().getFluidState(blockPosFromVec3(vector3d).above()).is(FluidTags.WATER)) {
                    vector3d = vector3d.add(0, -2, 0);
                } else if (!this.mob.level().getFluidState(blockPosFromVec3(vector3d).above(2)).is(FluidTags.WATER)) {
                    vector3d = vector3d.add(0, -3, 0);
                }
            }
            return vector3d;
        }


        private Vec3 findSurfaceTarget(PathfinderMob creature) {
            BlockPos upPos = creature.blockPosition();
            while (creature.level().getFluidState(upPos).is(FluidTags.WATER) || creature.level().getFluidState(upPos).is(FluidTags.LAVA)) {
                upPos = upPos.above();
            }
            if ((this.mob.level().getBlockState(upPos.below().offset(0, 1, 0)).isAir() && this.mob.level().getBlockState(upPos.below().offset(0, 2, 0)).isAir()) && (this.mob.level().getFluidState(upPos.below()).is(FluidTags.LAVA) || this.mob.level().getFluidState(upPos.below()).is(FluidTags.WATER) && !this.mob.level().getBlockState(upPos.below()).blocksMotion())) {
                return new Vec3(upPos.getX() + 0.5F, upPos.getY() - 1F, upPos.getZ() + 0.5F);
            }
            return null;
        }
    }
}
