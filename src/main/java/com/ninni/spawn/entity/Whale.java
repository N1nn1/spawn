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
    public final WhalePart head;
    private final WhalePart body;
    private final WhalePart tail;
    private final WhalePart[] subEntities;
    public final double[][] positions = new double[64][3];
    public int posPointer = -1;

    public Whale(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.head = new WhalePart(this, "head", 1.0F, 1.0F);
        this.body = new WhalePart(this, "body", 3.0F, 3.0F);
        this.tail = new WhalePart(this, "tail", 3.0F, 3.0F);
        this.subEntities = new WhalePart[]{this.head, this.body, this.tail};
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
    public void aiStep() {
        super.aiStep();

        if (!this.isDeadOrDying() && !this.isNoAi()) {
            if (this.posPointer < 0) {
                for (int i = 0; i < this.positions.length; ++i) {
                    this.positions[i][0] = this.getYRot();
                    this.positions[i][1] = this.getY();
                }
            }
            this.posPointer++;
            if (this.posPointer == this.positions.length) {
                this.posPointer = 0;
            }
            this.positions[this.posPointer][0] = this.getYRot();
            this.positions[this.posPointer][1] = this.getY();
            Vec3[] avector3d = new Vec3[this.subEntities.length];

            for (int j = 0; j < this.subEntities.length; ++j) {
                avector3d[j] = new Vec3(this.subEntities[j].getX(), this.subEntities[j].getY(), this.subEntities[j].getZ());
            }

            final float f15 = (float)(this.getLatencyPos(5, 1.0F)[1] - this.getLatencyPos(10, 1.0F)[1]) * 10.0F * Mth.DEG_TO_RAD;
            final float f16 = Mth.cos(f15);
            final float v = Mth.sin(f15);
            final float f17 = this.getYRot() * Mth.DEG_TO_RAD;
            final float pitch = this.getXRot() * Mth.DEG_TO_RAD;
            final float xRotDiv90 = Math.abs(this.getXRot() / 90F);
            final float f3 = Mth.sin(f17) * (1 - xRotDiv90);
            final float f18 = Mth.cos(f17) * (1 - xRotDiv90);

            this.tickPart(this.head, f3 * 0.5F, -pitch * 0.5F, -f18 * 0.5F);
            this.tickPart(this.body, (f3) * -3.5F, -pitch * 3F, (f18) * 3.5F);
            this.tickPart(this.tail, f3 * -7F, -pitch * 5F, -f18 * -7F);
            double[] adouble = this.getLatencyPos(5, 1.0F);

            final double[] adouble1 = this.getLatencyPos(15 + 5, 1.0F);
            final float f7 = this.getYRot() * Mth.DEG_TO_RAD + (float) Mth.wrapDegrees(adouble1[0] - adouble[0]) * Mth.DEG_TO_RAD;
            final float f19 = 1 - Math.abs(this.getXRot() / 90F);
            final float f20 = Mth.sin(f7) * f19;
            final float f21 = Mth.cos(f7) * f19;
            final float f22 = -3.6F;
            final float f23 = f22 - 2F;
            this.tickPart(this.tail, -(f3 * 0.5F + f20 * f23) * f16, pitch * 1.5F, (f18 * 0.5F + f21 * f23) * f16);

            int ac;
            for(ac = 0; ac < 1; ++ac) {
                WhalePart part = null;
                if (ac == 0) {
                    part = this.tail;
                }

                double[] es = this.getLatencyPos(12 + ac * 2, 1.0F);
                float ad = this.getYRot() * 0.017453292F + this.rotWrap(es[0] - adouble[0]) * 0.017453292F;
                float o = Mth.sin(ad);
                float p = Mth.cos(ad);
                float ae = (float)(ac + 1) * 2.0F;
                assert part != null;
                this.tickPart(part, (-(f3 * 1.5F + o * ae) * f16), es[1] - adouble[1] - ((ae + 1.5F) * v) + 1.5, ((f18 * 1.5F + p * ae) * f16));
            }

            for (int l = 0; l < this.subEntities.length; ++l) {
                this.subEntities[l].xo = avector3d[l].x;
                this.subEntities[l].yo = avector3d[l].y;
                this.subEntities[l].zo = avector3d[l].z;
                this.subEntities[l].xOld = avector3d[l].x;
                this.subEntities[l].yOld = avector3d[l].y;
                this.subEntities[l].zOld = avector3d[l].z;
            }
        }
    }

    private float rotWrap(double d) {
        return (float)Mth.wrapDegrees(d);
    }

    private void tickPart(WhalePart whalePart, double d, double e, double f) {
        whalePart.setPos(this.getX() + d, this.getY() + e, this.getZ() + f);
    }

    public double[] getLatencyPos(int i, float f) {
        if (this.isDeadOrDying()) f = 0.0F;
        f = 1.0F - f;
        int j = this.posPointer - i & 63;
        int k = this.posPointer - i - 1 & 63;
        double[] ds = new double[3];
        double d = this.positions[j][0];
        double e = Mth.wrapDegrees(this.positions[k][0] - d);
        ds[0] = d + e * (double)f;
        d = this.positions[j][1];
        e = this.positions[k][1] - d;
        ds[1] = d + e * (double)f;
        ds[2] = Mth.lerp(f, this.positions[j][2], this.positions[k][2]);
        return ds;
    }


    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        WhalePart[] whaleParts = this.subEntities;

        for(int i = 0; i < whaleParts.length; ++i) {
            whaleParts[i].setId(i + clientboundAddEntityPacket.getId());
        }

    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isEffectiveAi() && this.isInWater()) {

            this.moveRelative(this.getSpeed(), vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(vec3);
        }

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
