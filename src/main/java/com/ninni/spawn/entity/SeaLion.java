package com.ninni.spawn.entity;

import com.ninni.spawn.entity.ai.control.GoodAmphibiousMovement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SeaLion extends TamableAnimal {
    public final AnimationState waterIdleAnimationState = new AnimationState();
    public final AnimationState slackingAnimationState = new AnimationState();
    public final AnimationState floppingAnimationState = new AnimationState();
    public int floppingCooldown;

    public SeaLion(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 4.0f);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setMaxUpStep(1);
        this.moveControl = new GoodAmphibiousMovement.GoodAmphibiousMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(6, new GoodAmphibiousMovement.MoveAroundGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isInWaterOrBubble()) {
            if (random.nextInt(60) == 0 && this.floppingCooldown == 0 && this.getMoveControl().hasWanted()) {
                this.floppingCooldown = 5 * 20;
            }
            this.setPose(Pose.SWIMMING);
        }
        else this.setPose(Pose.STANDING);

        if (floppingCooldown > 0) {
            this.floppingCooldown--;
        }

        if ((this.level()).isClientSide()) {

            if (floppingCooldown > 0) {
                this.floppingAnimationState.startIfStopped(this.tickCount);
            } else {
                this.floppingAnimationState.stop();
            }

            this.waterIdleAnimationState.animateWhen(this.isInWaterOrBubble(), this.tickCount);
            this.slackingAnimationState.animateWhen(this.isSlacking(), this.tickCount);
        }
    }

    public boolean isSlacking() {
        return this.isPassenger() && this.getVehicle() instanceof Boat && this.getVehicle().getFirstPassenger() != this;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return pose == Pose.SWIMMING ? EntityDimensions.scalable(0.8F, 0.8F) : super.getDimensions(pose);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return pose == Pose.SWIMMING ? entityDimensions.height * 0.5f : super.getStandingEyeHeight(pose, entityDimensions);
    }

    @Override
    protected PathNavigation createNavigation(Level world) { return new GoodAmphibiousMovement.GoodSwimNavigation(this, world); }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public int getHeadRotSpeed() {
        return this.isInWaterOrBubble() ? 35 : 10;
    }

    public int getMaxHeadYRot() {
        return this.isInWaterOrBubble() ? 5 : 75;
    }

    @Override
    public int getMaxHeadXRot() {
        return this.isInWaterOrBubble() ? 1 : 40;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    public void travel(Vec3 vec3) {
        if (this.isEffectiveAi() && this.isInWaterOrBubble()) {
            this.moveRelative(this.getSpeed(), vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) this.setDeltaMovement(this.getDeltaMovement().add(0.0,0.005,0.0));
        } else {
            super.travel(vec3);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    public static boolean checkAnimalSpawnRules(EntityType<Mob> mobEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return serverLevelAccessor.getBlockState(blockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(serverLevelAccessor, blockPos);
    }
}
