package com.ninni.spawn.entity;

import com.ninni.spawn.registry.SpawnItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Octopus extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Octopus.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<BlockPos>> LOCKED_CHEST = SynchedEntityData.defineId(Octopus.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(Octopus.class, EntityDataSerializers.OPTIONAL_UUID);
    public static List<Item> items = List.of(
            Items.BUCKET,
            Items.GLASS_BOTTLE,
            SpawnItems.SNAIL_SHELL,
            Items.BOWL,
            Items.CAULDRON,
            Items.HOPPER,
            Items.HOPPER_MINECART,
            Items.MINECART,
            Items.NAUTILUS_SHELL,
            Items.FLOWER_POT,
            Items.GOAT_HORN,
            Items.BELL
    );
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState waterIdleAnimationState = new AnimationState();


    public Octopus(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setMaxUpStep(1);
        this.moveControl = new OctopusMoveControl(this);
        this.lookControl = new OctopusLookControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(6, new OctopusRandomStroll(this));
        this.goalSelector.addGoal(6, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec3, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (items.contains(itemStack.getItem()) && this.isAlive()) {

            ItemStack itemStack2 = new ItemStack(SpawnItems.CAPTURED_OCTOPUS);
            this.saveDataToItem(itemStack2, itemStack);
            ItemStack capturedOctopus =  ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
            //this.playSound(this.getPickupSound(), 1.0f, 1.0f);
            player.setItemInHand(interactionHand, capturedOctopus);

            this.discard();

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.interactAt(player, vec3, interactionHand);
    }

    public void saveDataToItem(ItemStack outputStack, ItemStack inputStack) {
        CompoundTag compoundTag = outputStack.getOrCreateTag();

        if (this.hasCustomName()) outputStack.setHoverName(this.getCustomName());

        if (this.isNoAi()) compoundTag.putBoolean("NoAI", this.isNoAi());
        if (this.isSilent()) compoundTag.putBoolean("Silent", this.isSilent());
        if (this.isNoGravity()) compoundTag.putBoolean("NoGravity", this.isNoGravity());
        if (this.hasGlowingTag()) compoundTag.putBoolean("Glowing", this.hasGlowingTag());
        if (this.isInvulnerable()) compoundTag.putBoolean("Invulnerable", this.isInvulnerable());

        compoundTag.putFloat("Health", this.getHealth());
        compoundTag.putInt("Item", getId(inputStack.getItem()));
        if (inputStack.hasTag()) compoundTag.put("ItemTag", inputStack.getTag());
    }

    public void loadDataFromItem(CompoundTag tag) {

        if (tag.contains("NoAI")) this.setNoAi(tag.getBoolean("NoAI"));
        if (tag.contains("Silent")) this.setSilent(tag.getBoolean("Silent"));
        if (tag.contains("NoGravity")) this.setNoGravity(tag.getBoolean("NoGravity"));
        if (tag.contains("Glowing")) this.setGlowingTag(tag.getBoolean("Glowing"));
        if (tag.contains("Invulnerable")) this.setInvulnerable(tag.getBoolean("Invulnerable"));

        if (tag.contains("Health", 99)) this.setHealth(tag.getFloat("Health"));
    }

    @Override
    public void tick() {
        super.tick();

        setNoGravity(this.isLocking());


        //TODO these dont work, reference camel code
        if ((this.level()).isClientSide()) {
            this.waterIdleAnimationState.animateWhen(this.isInWaterOrBubble() && !this.onGround(), this.tickCount);
            this.idleAnimationState.animateWhen((!this.isInWaterOrBubble() || this.isInWaterOrBubble() && this.onGround()), this.tickCount);

            if (!this.isInWaterOrBubble()) {
                if (random.nextInt(10) == 0) {
                    level().addParticle(ParticleTypes.FALLING_WATER, this.getRandomX(0.6), this.getY() + random.nextDouble(), this.getRandomZ(0.6), 0.0, 0.0, 0.0);
                }
            }
        } else {
            if (this.isLocking()) {
                BlockState state = this.level().getBlockState(getLockingPos());
                BlockPos pos = new BlockPos((int)this.getX(), (int)this.getY(), (int)this.getZ());
                if (!(state.getBlock() instanceof ChestBlock) || ((state.getBlock() instanceof ChestBlock) && state.getValue(ChestBlock.TYPE) != ChestType.SINGLE)) {
                    this.stopLocking();
                    this.entityData.set(DATA_OWNERUUID_ID, Optional.empty());
                } else {
                    if (!pos.equals(this.getLockingPos())) {
                        this.setPos(this.getLockingPos().getX() + 0.5f, this.getLockingPos().getY(), this.getLockingPos().getZ() + 0.5f);
                    }
                    if (this.getYHeadRot() != state.getValue(ChestBlock.FACING).toYRot()) {
                        this.setYHeadRot(state.getValue(ChestBlock.FACING).toYRot());
                    }
                }
            }
        }
    }

    public void travel(Vec3 vec3) {
        if (!this.isLocking()) {
            if (this.isEffectiveAi() && this.isInWater()) {
                this.moveRelative(this.getSpeed(), vec3);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
                this.setDeltaMovement(this.getDeltaMovement().add(0.0,0.0025D,0.0));
            } else {
                super.travel(vec3);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float f) {
        if (this.isLocking() && !(source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_LIGHTNING) || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))) {
            return false;
        }
        return super.hurt(source, f);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROM_BUCKET, false);
        this.entityData.define(LOCKED_CHEST, Optional.empty());
        this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("FromBucket", this.fromBucket());
        if (this.getOwnerUUID() != null) {
            compoundTag.putUUID("Owner", this.getOwnerUUID());
        }
        if (this.isLocking()) {
            compoundTag.put("LockedChestPos", NbtUtils.writeBlockPos(this.getLockingPos()));
        }
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        UUID uUID;
        super.readAdditionalSaveData(compoundTag);
        this.setFromBucket(compoundTag.getBoolean("FromBucket"));
        if (compoundTag.hasUUID("Owner")) {
            uUID = compoundTag.getUUID("Owner");
        } else {
            String string = compoundTag.getString("Owner");
            uUID = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), string);
        }
        if (uUID != null) {
            try {
                this.setOwnerUUID(uUID);
            } catch (Throwable ignored) {
            }
        }
        if (compoundTag.contains("LockedChestPos", 10)) {
            this.setLockingPos(NbtUtils.readBlockPos(compoundTag.getCompound("LockedChestPos")));
        }
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNERUUID_ID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uUID) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(uUID));
    }

    public boolean isLocking() {
        return this.entityData.get(LOCKED_CHEST).isPresent();
    }

    public void stopLocking() {
        this.entityData.set(LOCKED_CHEST, Optional.empty());
    }

    public void setLockingPos(@Nullable BlockPos blockPos) {
        this.getEntityData().set(LOCKED_CHEST, Optional.ofNullable(blockPos));
    }

    @Nullable
    public BlockPos getLockingPos() {
        return this.getEntityData().get(LOCKED_CHEST).orElse(null);
    }

    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    public void setFromBucket(boolean bl) {
        this.entityData.set(FROM_BUCKET, bl);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.5f;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.isUnobstructed(this);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket() || this.isLocking();
    }


    public int getHeadRotSpeed() {
        return 35;
    }

    public int getMaxHeadYRot() {
        return 5;
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    public static int getId(Item item) {
        if (item == Items.BUCKET) return 0;
        else if (item == Items.GLASS_BOTTLE) return 1;
        else if (item == SpawnItems.SNAIL_SHELL) return 2;
        else if (item == Items.BOWL) return 3;
        else if (item == Items.CAULDRON) return 4;
        else if (item == Items.HOPPER) return 5;
        else if (item == Items.HOPPER_MINECART) return 6;
        else if (item == Items.MINECART) return 7;
        else if (item == Items.NAUTILUS_SHELL) return 8;
        else if (item == Items.FLOWER_POT) return 9;
        else if (item == Items.GOAT_HORN) return 10;
        else if (item == Items.BELL) return 11;
        return 0;
    }

    public static Item getItem(int id) {
        if (id == 0) return Items.BUCKET;
        else if (id == 1) return Items.GLASS_BOTTLE;
        else if (id == 2) return SpawnItems.SNAIL_SHELL;
        else if (id == 3) return Items.BOWL;
        else if (id == 4) return Items.CAULDRON;
        else if (id == 5) return Items.HOPPER;
        else if (id == 6) return Items.HOPPER_MINECART;
        else if (id == 7) return Items.MINECART;
        else if (id == 8) return Items.NAUTILUS_SHELL;
        else if (id == 9) return Items.FLOWER_POT;
        else if (id == 10) return Items.GOAT_HORN;
        else if (id == 11) return Items.BELL;
        else return Items.BUCKET;
    }


    @Override
    protected PathNavigation createNavigation(Level world) { return new OctopusSwimNavigation(this, world); }

    static class OctopusSwimNavigation extends WaterBoundPathNavigation {
        OctopusSwimNavigation(Octopus octopus, Level world) { super(octopus, world); }

        @Override
        protected PathFinder createPathFinder(int range) {
            this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
            return new PathFinder(this.nodeEvaluator, range);
        }

        @Override
        protected boolean canUpdatePath() {
            return true;
        }

        @Override
        public boolean isStableDestination(BlockPos blockPos) {
            return !this.level.getBlockState(blockPos.below(2)).isAir();
        }
    }

    static class OctopusMoveControl extends MoveControl {
        private final Octopus octopus;

        public OctopusMoveControl(Octopus octopus) {
            super(octopus);
            this.octopus = octopus;
        }

        @Override
        public void tick() {
            if (!this.octopus.isLocking()) {
                if (this.operation == Operation.STRAFE || this.operation == Operation.JUMPING || (this.mob.onGround() && !this.octopus.isInWaterOrBubble())) { super.tick();}
                if (this.operation == Operation.MOVE_TO && !this.octopus.getNavigation().isDone()) {
                    double d = this.wantedX - this.octopus.getX();
                    double e = this.wantedY - this.octopus.getY();
                    double f = this.wantedZ - this.octopus.getZ();
                    double g = d * d + e * e + f * f;
                    if (g < 2.5) {
                        this.mob.setZza(0.0F);
                    } else {
                        float movementSpeed = (float) (this.speedModifier * this.octopus.getAttributeValue(Attributes.MOVEMENT_SPEED));
                        this.octopus.setYRot(this.rotlerp(this.octopus.getYRot(), (float) (Mth.atan2(f, d) * 57.3) - 90.0F, 10.0F));
                        this.octopus.yBodyRot = this.octopus.getYRot();
                        this.octopus.yHeadRot = this.octopus.getYRot();
                        if (this.octopus.isInWaterOrBubble()) {
                            this.octopus.setSpeed(movementSpeed * 0.4F);
                            if (this.octopus.onGround()) this.octopus.setSpeed(movementSpeed * 0.1F);
                            float j = -((float) (Mth.atan2(e, Mth.sqrt((float) (d * d + f * f))) * 57.3));
                            this.octopus.setXRot(this.rotlerp(this.octopus.getXRot(), Mth.clamp(Mth.wrapDegrees(j), -85.0F, 85.0F), 5.0F));
                            this.octopus.zza = Mth.cos(this.octopus.getXRot() * (float) Math.PI / 180f) * movementSpeed;
                            this.octopus.yya = -Mth.sin(this.octopus.getXRot() * (float) Math.PI / 180f);
                        }
                    }
                }
            }
        }
    }

    static class OctopusLookControl extends LookControl {
        private final Octopus octopus;

        public OctopusLookControl(Octopus octopus) {
            super(octopus);
            this.octopus = octopus;
        }

        @Override
        public void tick() {
            if (!this.octopus.isLocking()) {
                super.tick();
            }
        }
    }

    public class OctopusRandomStroll extends RandomStrollGoal {

        public OctopusRandomStroll(PathfinderMob pathfinderMob) {
            super(pathfinderMob, 1.0);
        }

        @Override
        public boolean canUse() {
            if (Octopus.this.isInWaterOrBubble() && !Octopus.this.onGround()) {
                return false;
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if (Octopus.this.isInWaterOrBubble() && !Octopus.this.onGround()) {
                return false;
            }
            return super.canContinueToUse();
        }
    }

    public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<Octopus> mobEntityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        int i = levelAccessor.getSeaLevel();
        int j = i - 13;
        return blockPos.getY() >= j && blockPos.getY() <= i && levelAccessor.getFluidState(blockPos.below()).is(FluidTags.WATER) && levelAccessor.getBlockState(blockPos.above()).is(Blocks.WATER);
    }

}
