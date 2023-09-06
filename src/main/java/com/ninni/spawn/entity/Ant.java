package com.ninni.spawn.entity;

import com.google.common.collect.Lists;
import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.block.entity.AnthillBlockEntity;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.*;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ant extends TamableAnimal implements NeutralMob{
    private static final EntityDataAccessor<Boolean> DATA_HAS_RESOURCE = SynchedEntityData.defineId(Ant.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ABDOMEN_COLOR = SynchedEntityData.defineId(Ant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(Ant.class, EntityDataSerializers.INT);
    public static final String TAG_CANNOT_ENTER_ANTHILL_TICKS = "CannotEnterAnthillTicks";
    public static final String TAG_TICKS_SINCE_GATHERING = "TicksSinceGathering";
    public static final String TAG_HAS_RESOURCE = "HasResource";
    public static final String TAG_ABDOMEN_COLOR = "AbdomenColor";
    public static final String TAG_RESOURCE_POS = "ResourcePos";
    public static final String TAG_ANTHILL_POS = "AnthillPos";
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    @Nullable
    private UUID persistentAngerTarget;
    int ticksWithoutResourceSinceExitingAnthill;
    private int stayOutOfAnthillCountdown;
    int remainingCooldownBeforeLocatingNewAnthill;
    int remainingCooldownBeforeLocatingNewResource;
    @Nullable
    BlockPos savedResourcePos;
    @Nullable
    BlockPos anthillPos;
    Ant.AntGatherGoal antGatherGoal;
    Ant.AntGoToAnthillGoal goToAnthillGoal;
    private Ant.AntGoToKnownResourceGoal goToKnownResourceGoal;
    private int underWaterTicks;

    public Ant(EntityType<? extends Ant> entityType, Level level) {
        super(entityType, level);
        this.lookControl = new AntLookControl(this);
        this.remainingCooldownBeforeLocatingNewResource = Mth.nextInt(this.random, 20, 60);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Mth.nextInt(random, 12, 20));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Mth.nextInt(random, 1, 3));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.nextDouble(random, 0.225, 0.3));
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HAS_RESOURCE, false);
        this.entityData.define(DATA_ABDOMEN_COLOR, DyeColor.RED.getId());
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }
    
    public static float[] getColorArray(DyeColor dyeColor) {
        if (dyeColor == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        }
        float[] fs = dyeColor.getTextureDiffuseColors();
        return new float[]{fs[0] * 0.75F, fs[1] * 0.75F, fs[2] * 0.75F};
    }
    public DyeColor getAbdomenColor() {
        return DyeColor.byId(this.entityData.get(DATA_ABDOMEN_COLOR));
    }
    public void setAbdomenColor(DyeColor dyeColor) {
        this.entityData.set(DATA_ABDOMEN_COLOR, dyeColor.getId());
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        if (levelReader.getBlockState(blockPos).isAir()) {
            return 10.0f;
        }
        return 0.0f;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new AntAttackGoal(this, 1.4f, true));
        this.goalSelector.addGoal(1, new AntEnterAnthillGoal());
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(SpawnTags.ANT_TEMPTS), false));
        this.antGatherGoal = new AntGatherGoal();
        this.goalSelector.addGoal(4, this.antGatherGoal);
        this.goToAnthillGoal = new AntGoToAnthillGoal();
        this.goalSelector.addGoal(5, this.goToAnthillGoal);
        this.goalSelector.addGoal(5, new  FollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false));
        this.goalSelector.addGoal(5, new AntLocateAnthillGoal());
        this.goToKnownResourceGoal = new AntGoToKnownResourceGoal();
        this.goalSelector.addGoal(6, this.goToKnownResourceGoal);
        this.goalSelector.addGoal(8, new AntWanderGoal(this));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Ant.class, false, this::getTerritorialTarget));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new AntHurtByOtherGoal(this).setAlertOthers());
        this.targetSelector.addGoal(4, new AntBecomeAngryTargetGoal(this));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    private boolean getTerritorialTarget(LivingEntity livingEntity) {
        return livingEntity instanceof Ant ant && ant.isTame() && !ant.isInSittingPose() && !this.isInSittingPose() && this.isTame() && ant.getAbdomenColor() != this.getAbdomenColor() && !this.isBaby() && !ant.isBaby();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        Item item = itemStack.getItem();

        if (this.isTame()) {
            if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                if(itemStack.is(Items.HONEY_BOTTLE)) this.spawnAtLocation(Items.GLASS_BOTTLE);
                if(itemStack.is(Items.MUSHROOM_STEW)) this.spawnAtLocation(Items.BOWL);

                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                this.heal(item.getFoodProperties().getNutrition());
                return InteractionResult.SUCCESS;
            } else if (item instanceof DyeItem) {
                DyeColor dyeColor = ((DyeItem)item).getDyeColor();
                if (dyeColor == this.getAbdomenColor()) return super.mobInteract(player, interactionHand);
                this.setAbdomenColor(dyeColor);
                if (player.getAbilities().instabuild) return InteractionResult.SUCCESS;
                itemStack.shrink(1);
                return InteractionResult.SUCCESS;
            }
            InteractionResult interactionResult = super.mobInteract(player, interactionHand);
            if (interactionResult.consumesAction() && !this.isBaby() || !this.isOwnedBy(player)) return interactionResult;
            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, interactionHand);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.hasAnthill()) {
            compoundTag.put(TAG_ANTHILL_POS, NbtUtils.writeBlockPos(this.getAnthillPos()));
        }
        if (this.hasSavedResourcePos() && !this.isTame()) {
            compoundTag.put(TAG_RESOURCE_POS, NbtUtils.writeBlockPos(this.getSavedResourcePos()));
        }
        compoundTag.putByte(TAG_ABDOMEN_COLOR, (byte)this.getAbdomenColor().getId());
        compoundTag.putBoolean(TAG_HAS_RESOURCE, this.hasResource());
        compoundTag.putInt(TAG_TICKS_SINCE_GATHERING, this.ticksWithoutResourceSinceExitingAnthill);
        compoundTag.putInt(TAG_CANNOT_ENTER_ANTHILL_TICKS, this.stayOutOfAnthillCountdown);
        this.addPersistentAngerSaveData(compoundTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains(TAG_ABDOMEN_COLOR, 99)) {
            this.setAbdomenColor(DyeColor.byId(compoundTag.getInt(TAG_ABDOMEN_COLOR)));
        }
        this.anthillPos = null;
        if (compoundTag.contains(TAG_ANTHILL_POS)) {
            this.anthillPos = NbtUtils.readBlockPos(compoundTag.getCompound(TAG_ANTHILL_POS));
        }
        this.savedResourcePos = null;
        if (compoundTag.contains(TAG_RESOURCE_POS) && !this.isTame()) {
            this.savedResourcePos = NbtUtils.readBlockPos(compoundTag.getCompound(TAG_RESOURCE_POS));
        }
        super.readAdditionalSaveData(compoundTag);
        this.setHasResource(compoundTag.getBoolean(TAG_HAS_RESOURCE));
        this.ticksWithoutResourceSinceExitingAnthill = compoundTag.getInt(TAG_TICKS_SINCE_GATHERING);
        this.stayOutOfAnthillCountdown = compoundTag.getInt(TAG_CANNOT_ENTER_ANTHILL_TICKS);
        this.readPersistentAngerSaveData(this.level(), compoundTag);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasResource() && this.random.nextFloat() < 0.05f) {
            for (int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.spawnFluidParticle(this.level(), this.getX() - (double)0.3f, this.getX() + (double)0.3f, this.getZ() - (double)0.3f, this.getZ() + (double)0.3f, this.getY(0.5), ParticleTypes.FALLING_NECTAR);
            }
        }
    }

    private void spawnFluidParticle(Level level, double d, double e, double f, double g, double h, ParticleOptions particleOptions) {
        level.addParticle(particleOptions, Mth.lerp(level.random.nextDouble(), d, e), h, Mth.lerp(level.random.nextDouble(), f, g), 0.0, 0.0, 0.0);
    }

    @Nullable
    public BlockPos getSavedResourcePos() {
        return this.savedResourcePos;
    }

    public boolean hasSavedResourcePos() {
        return this.savedResourcePos != null;
    }

    private boolean isTiredOfLookingForResource() {
        return this.ticksWithoutResourceSinceExitingAnthill > 3600;
    }

    boolean wantsToEnterAnthill() {
        if (this.stayOutOfAnthillCountdown > 0 || this.antGatherGoal.isGathering() || this.getTarget() != null) {
            return false;
        }
        return this.isTiredOfLookingForResource() || this.level().isRaining() || this.level().isNight() || this.hasResource();
    }

    public void setStayOutOfAnthillCountdown(int i) {
        this.stayOutOfAnthillCountdown = i;
    }

    @Override
    protected void customServerAiStep() {
        this.underWaterTicks = this.isInWaterOrBubble() ? ++this.underWaterTicks : 0;
        if (this.underWaterTicks > 20) {
            this.hurt(this.damageSources().drown(), 1.0f);
        }
        if (!this.hasResource()) {
            ++this.ticksWithoutResourceSinceExitingAnthill;
        }
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), false);
        }
    }

    public void resetTicksWithoutResourceSinceExitingAnthill() {
        this.ticksWithoutResourceSinceExitingAnthill = 0;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, i);
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uUID) {
        this.persistentAngerTarget = uUID;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    private boolean doesAnthillHaveSpace(BlockPos blockPos) {
        BlockEntity blockEntity = this.level().getBlockEntity(blockPos);
        if (blockEntity instanceof AnthillBlockEntity) {
            return !((AnthillBlockEntity)blockEntity).isFullOfAnts();
        }
        return false;
    }

    @VisibleForDebug
    public boolean hasAnthill() {
        return this.anthillPos != null;
    }

    @Nullable
    @VisibleForDebug
    public BlockPos getAnthillPos() {
        return this.anthillPos;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.stayOutOfAnthillCountdown > 0) {
                --this.stayOutOfAnthillCountdown;
            }
            if (this.remainingCooldownBeforeLocatingNewAnthill > 0) {
                --this.remainingCooldownBeforeLocatingNewAnthill;
            }
            if (this.remainingCooldownBeforeLocatingNewResource > 0) {
                --this.remainingCooldownBeforeLocatingNewResource;
            }
            if (this.tickCount % 20 == 0 && !this.isAnthillValid()) {
                this.anthillPos = null;
            }
        }
    }

    boolean isAnthillValid() {
        if (!this.hasAnthill()) {
            return false;
        }
        if (this.isTooFarAway(this.anthillPos)) {
            return false;
        }
        BlockEntity blockEntity = this.level().getBlockEntity(this.anthillPos);
        return blockEntity != null && blockEntity.getType() == SpawnBlockEntityTypes.ANTHILL;
    }

    public boolean hasResource() {
        return entityData.get(DATA_HAS_RESOURCE);
    }

    public void setHasResource(boolean bl) {
        if (bl) {
            this.resetTicksWithoutResourceSinceExitingAnthill();
        }
        this.entityData.set(DATA_HAS_RESOURCE, bl);
    }


    boolean isTooFarAway(BlockPos blockPos) {
        return !this.closerThan(blockPos, 32);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation groundPathNavigation = new GroundPathNavigation(this, level){

            @Override
            public boolean isStableDestination(BlockPos blockPos) {
                return !this.level.getBlockState(blockPos.below()).isAir();
            }

            @Override
            public void tick() {
                if (Ant.this.antGatherGoal.isGathering()) {
                    return;
                }
                super.tick();
            }
        };
        groundPathNavigation.setCanOpenDoors(false);
        groundPathNavigation.setCanFloat(false);
        groundPathNavigation.setCanPassDoors(true);
        return groundPathNavigation;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(SpawnTags.ANT_FEEDS);
    }

    boolean isResourceValid(BlockPos blockPos) {
        return this.level().isLoaded(blockPos) && this.level().getBlockState(blockPos).is(SpawnTags.ANT_RESOURCE);
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        }
        if (!this.level().isClientSide) {
            this.antGatherGoal.stopGathering();
        }
        return super.hurt(damageSource, f);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> tagKey) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.01, 0.0));
    }

    boolean closerThan(BlockPos blockPos, int i) {
        return blockPos.closerThan(this.blockPosition(), i);
    }

    class AntGatherGoal extends BaseAntGoal {
        private final Predicate<BlockState> VALID_GATHERING_BLOCKS;
        private int successfulGatheringTicks;
        private int lastSoundPlayedTick;
        private boolean gathering;
        @Nullable
        private Vec3 hoverPos;
        private int gatheringTicks;

        AntGatherGoal() {
            this.VALID_GATHERING_BLOCKS = blockState -> {
                if (blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED)) {
                    return false;
                }
                if (blockState.is(SpawnTags.ANT_RESOURCE)) {
                    if (blockState.is(Blocks.TALL_GRASS)) {
                        return blockState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER;
                    }
                    return true;
                }
                return false;
            };
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canAntUse() {
            if (Ant.this.remainingCooldownBeforeLocatingNewResource > 0) return false;
            if (Ant.this.hasResource()) return false;
            if (Ant.this.isTame()) return false;

            Optional<BlockPos> optional = this.findNearbyResource();
            if (optional.isPresent()) {
                Ant.this.savedResourcePos = optional.get();
                Ant.this.navigation.moveTo((double) Ant.this.savedResourcePos.getX() + 0.5, Ant.this.savedResourcePos.getY(), (double) Ant.this.savedResourcePos.getZ() + 0.5, 1.2f);
                return true;
            }
            Ant.this.remainingCooldownBeforeLocatingNewResource = Mth.nextInt(Ant.this.random, 20, 60);
            return false;
        }

        @Override
        public boolean canAntContinueToUse() {
            if (!this.gathering) {
                return false;
            }
            if (Ant.this.isTame()) return false;
            if (!Ant.this.hasSavedResourcePos()) {
                return false;
            }
            if (Ant.this.level().isRaining()) {
                return false;
            }
            if (this.hasGatherdLongEnough()) {
                return Ant.this.random.nextFloat() < 0.2f;
            }
            if (Ant.this.tickCount % 20 == 0 && !Ant.this.isResourceValid(Ant.this.savedResourcePos)) {
                Ant.this.savedResourcePos = null;
                return false;
            }
            return true;
        }

        private boolean hasGatherdLongEnough() {
            return this.successfulGatheringTicks > 200;
        }

        boolean isGathering() {
            return this.gathering;
        }

        void stopGathering() {
            this.gathering = false;
        }

        @Override
        public void start() {
            this.successfulGatheringTicks = 0;
            this.gatheringTicks = 0;
            this.lastSoundPlayedTick = 0;
            this.gathering = true;
            Ant.this.resetTicksWithoutResourceSinceExitingAnthill();
        }

        @Override
        public void stop() {
            if (this.hasGatherdLongEnough()) {
                Ant.this.setHasResource(true);
            }
            this.gathering = false;
            Ant.this.navigation.stop();
            Ant.this.remainingCooldownBeforeLocatingNewResource = 200;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {

            ++this.gatheringTicks;
            if (this.gatheringTicks > 400) {
                Ant.this.savedResourcePos = null;
                return;
            }
            Vec3 vec3 = Vec3.atBottomCenterOf(Ant.this.savedResourcePos);
            if (vec3.distanceTo(Ant.this.position()) > 1.0) {
                this.hoverPos = vec3;
                this.setWantedPos();
                return;
            }
            if (this.hoverPos == null) {
                this.hoverPos = vec3;
            }
            boolean bl = Ant.this.position().distanceTo(this.hoverPos) <= 0.1;
            boolean bl2 = true;
            if (!bl && this.gatheringTicks > 400) {
                Ant.this.savedResourcePos = null;
                return;
            }
            if (bl) {
                boolean bl3 = Ant.this.random.nextInt(25) == 0;
                if (bl3) {
                    this.hoverPos = new Vec3(vec3.x() + (double)this.getOffset(), vec3.y(), vec3.z() + (double)this.getOffset());
                    Ant.this.navigation.stop();
                } else {
                    bl2 = false;
                }
                Ant.this.getLookControl().setLookAt(vec3.x(), vec3.y() + 0.2, vec3.z());
            }
            if (bl2) {
                this.setWantedPos();
            }
            ++this.successfulGatheringTicks;
            if (Ant.this.random.nextFloat() < 0.05f && this.successfulGatheringTicks > this.lastSoundPlayedTick + 60) {
                this.lastSoundPlayedTick = this.successfulGatheringTicks;
            }
        }

        private void setWantedPos() {
            Ant.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.7f);
        }

        private float getOffset() {
            return (Ant.this.random.nextFloat() * 2.0f - 1.0f) * 0.33333334f;
        }

        private Optional<BlockPos> findNearbyResource() {
            return this.findNearestBlock(this.VALID_GATHERING_BLOCKS, 5.0);
        }

        private Optional<BlockPos> findNearestBlock(Predicate<BlockState> predicate, double d) {
            BlockPos blockPos = Ant.this.blockPosition();
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int i = 0;
            while ((double)i <= d) {
                int j = 0;
                while ((double)j < d) {
                    int k = 0;
                    while (k <= j) {
                        int l = k < j && k > -j ? j : 0;
                        while (l <= j) {
                            mutableBlockPos.setWithOffset(blockPos, k, i - 1, l);
                            if (blockPos.closerThan(mutableBlockPos, d) && predicate.test(Ant.this.level().getBlockState(mutableBlockPos))) {
                                return Optional.of(mutableBlockPos);
                            }
                            l = l > 0 ? -l : 1 - l;
                        }
                        k = k > 0 ? -k : 1 - k;
                    }
                    ++j;
                }
                i = i > 0 ? -i : 1 - i;
            }
            return Optional.empty();
        }
    }

    class AntLookControl extends LookControl {
        AntLookControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        protected boolean resetXRotOnTick() {
            return !Ant.this.antGatherGoal.isGathering();
        }
    }

    class AntAttackGoal extends MeleeAttackGoal {
        AntAttackGoal(PathfinderMob pathfinderMob, double d, boolean bl) {
            super(pathfinderMob, d, bl);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && Ant.this.isAngry();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && Ant.this.isAngry();
        }
    }

    class AntEnterAnthillGoal extends BaseAntGoal {
        AntEnterAnthillGoal() {
        }

        @Override
        public boolean canAntUse() {
            BlockEntity blockEntity;
            if (Ant.this.hasAnthill() && Ant.this.wantsToEnterAnthill() && Ant.this.anthillPos.closerToCenterThan(Ant.this.position(), 2.0) && (blockEntity = Ant.this.level().getBlockEntity(Ant.this.anthillPos)) instanceof AnthillBlockEntity) {
                AnthillBlockEntity antanthillBlockEntity = (AnthillBlockEntity)blockEntity;
                if (antanthillBlockEntity.isFullOfAnts()) {
                    Ant.this.anthillPos = null;
                } else {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canAntContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            BlockEntity blockEntity = Ant.this.level().getBlockEntity(Ant.this.anthillPos);
            if (blockEntity instanceof AnthillBlockEntity antanthillBlockEntity) {
                antanthillBlockEntity.tryEnterAnthill(Ant.this, Ant.this.hasResource());
            }
        }
    }

    class AntLocateAnthillGoal extends BaseAntGoal {
        AntLocateAnthillGoal() {
        }

        @Override
        public boolean canAntUse() {
            return Ant.this.remainingCooldownBeforeLocatingNewAnthill == 0 && !Ant.this.hasAnthill() && Ant.this.wantsToEnterAnthill();
        }

        @Override
        public boolean canAntContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Ant.this.remainingCooldownBeforeLocatingNewAnthill = 200;
            List<BlockPos> list = this.findNearbyAnthillsWithSpace();
            if (list.isEmpty()) {
                return;
            }
            for (BlockPos blockPos : list) {
                if (Ant.this.goToAnthillGoal.isTargetBlacklisted(blockPos)) continue;
                Ant.this.anthillPos = blockPos;
                return;
            }
            Ant.this.goToAnthillGoal.clearBlacklist();
            Ant.this.anthillPos = list.get(0);
        }

        private List<BlockPos> findNearbyAnthillsWithSpace() {
            BlockPos blockPos = Ant.this.blockPosition();
            PoiManager poiManager = ((ServerLevel) Ant.this.level()).getPoiManager();
            Stream<PoiRecord> stream = poiManager.getInRange(holder -> holder.is(SpawnTags.ANT_HOME), blockPos, 20, PoiManager.Occupancy.ANY);
            return stream.map(PoiRecord::getPos).filter(Ant.this::doesAnthillHaveSpace).sorted(Comparator.comparingDouble(blockPos2 -> blockPos2.distSqr(blockPos))).collect(Collectors.toList());
        }
    }

    @VisibleForDebug
    public class AntGoToAnthillGoal extends BaseAntGoal {
        int travellingTicks;
        final List<BlockPos> blacklistedTargets;
        @Nullable
        private Path lastPath;
        private int ticksStuck;

        AntGoToAnthillGoal() {
            this.travellingTicks = Ant.this.level().random.nextInt(10);
            this.blacklistedTargets = Lists.newArrayList();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canAntUse() {
            return Ant.this.anthillPos != null && !Ant.this.hasRestriction() && Ant.this.wantsToEnterAnthill() && !this.hasReachedTarget(Ant.this.anthillPos) && Ant.this.level().getBlockState(Ant.this.anthillPos).is(SpawnTags.ANTHILLS);
        }

        @Override
        public boolean canAntContinueToUse() {
            return this.canAntUse();
        }

        @Override
        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            Ant.this.navigation.stop();
            Ant.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (Ant.this.anthillPos == null) {
                return;
            }
            ++this.travellingTicks;
            if (this.travellingTicks > this.adjustedTickDelay(600)) {
                this.dropAndBlacklistAnthill();
                return;
            }
            if (Ant.this.navigation.isInProgress()) {
                return;
            }
            if (Ant.this.closerThan(Ant.this.anthillPos, 16)) {
                boolean bl = this.pathfindDirectlyTowards(Ant.this.anthillPos);
                if (!bl) {
                    this.dropAndBlacklistAnthill();
                } else if (this.lastPath != null && Ant.this.navigation.getPath().sameAs(this.lastPath)) {
                    ++this.ticksStuck;
                    if (this.ticksStuck > 60) {
                        this.dropAnthill();
                        this.ticksStuck = 0;
                    }
                } else {
                    this.lastPath = Ant.this.navigation.getPath();
                }
                return;
            }
            if (Ant.this.isTooFarAway(Ant.this.anthillPos)) {
                this.dropAnthill();
                return;
            }
            Ant.this.moveTo(Ant.this.anthillPos, 0, 0);
        }

        private boolean pathfindDirectlyTowards(BlockPos blockPos) {
            Ant.this.navigation.setMaxVisitedNodesMultiplier(10.0f);
            Ant.this.navigation.moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0);
            return Ant.this.navigation.getPath() != null && Ant.this.navigation.getPath().canReach();
        }

        boolean isTargetBlacklisted(BlockPos blockPos) {
            return this.blacklistedTargets.contains(blockPos);
        }

        private void blacklistTarget(BlockPos blockPos) {
            this.blacklistedTargets.add(blockPos);
            while (this.blacklistedTargets.size() > 3) {
                this.blacklistedTargets.remove(0);
            }
        }

        void clearBlacklist() {
            this.blacklistedTargets.clear();
        }

        private void dropAndBlacklistAnthill() {
            if (Ant.this.anthillPos != null) {
                this.blacklistTarget(Ant.this.anthillPos);
            }
            this.dropAnthill();
        }

        private void dropAnthill() {
            Ant.this.anthillPos = null;
            Ant.this.remainingCooldownBeforeLocatingNewAnthill = 200;
        }

        private boolean hasReachedTarget(BlockPos blockPos) {
            if (Ant.this.closerThan(blockPos, 2)) {
                return true;
            }
            Path path = Ant.this.navigation.getPath();
            return path != null && path.getTarget().equals(blockPos) && path.canReach() && path.isDone();
        }
    }

    public class AntGoToKnownResourceGoal extends BaseAntGoal {
        int travellingTicks;

        AntGoToKnownResourceGoal() {
            this.travellingTicks = Ant.this.level().random.nextInt(10);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canAntUse() {
            return !Ant.this.isTame() && Ant.this.savedResourcePos != null && !Ant.this.hasRestriction() && this.wantsToGoToKnownResource() && Ant.this.isResourceValid(Ant.this.savedResourcePos) && !Ant.this.closerThan(Ant.this.savedResourcePos, 2);
        }

        @Override
        public boolean canAntContinueToUse() {

            if (Ant.this.isTame()) return false;
            return this.canAntUse();
        }

        @Override
        public void start() {
            this.travellingTicks = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.travellingTicks = 0;
            Ant.this.navigation.stop();
            Ant.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (Ant.this.savedResourcePos == null) {
                return;
            }
            ++this.travellingTicks;
            if (this.travellingTicks > this.adjustedTickDelay(600)) {
                Ant.this.savedResourcePos = null;
                return;
            }
            if (Ant.this.navigation.isInProgress()) {
                return;
            }
            if (Ant.this.isTooFarAway(Ant.this.savedResourcePos)) {
                Ant.this.savedResourcePos = null;
                return;
            }
            Ant.this.moveTo(Ant.this.savedResourcePos, 0,0);
        }

        private boolean wantsToGoToKnownResource() {
            return Ant.this.ticksWithoutResourceSinceExitingAnthill > 2400;
        }
    }

    class AntWanderGoal extends WaterAvoidingRandomStrollGoal {

        AntWanderGoal(Ant ant) {
            super(ant, 0.8, 1.0000001E-5f);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }


        @Override
        @Nullable
        protected Vec3 getPosition() {
            Vec3 vec32;
            if (Ant.this.hasAnthill() && !Ant.this.isTame()) {
                if (Ant.this.isAnthillValid() && !Ant.this.closerThan(Ant.this.anthillPos, 22)) {
                    Vec3 vec3 = Vec3.atCenterOf(Ant.this.anthillPos);
                    vec32 = vec3.subtract(Ant.this.position()).normalize();
                } else {
                    vec32 = Ant.this.getViewVector(0.0f);
                }
                Vec3 vec33 = LandRandomPos.getPosTowards(Ant.this, 8, 7, vec32);
                if (vec33 != null) {
                    return vec33;
                }
            }
            return super.getPosition();
        }
    }

    class AntHurtByOtherGoal extends HurtByTargetGoal {
        AntHurtByOtherGoal(Ant ant) {
            super(ant);
        }

        @Override
        public boolean canContinueToUse() {
            return Ant.this.isAngry() && super.canContinueToUse();
        }

        @Override
        protected void alertOther(Mob mob, LivingEntity livingEntity) {
            if (mob instanceof Ant && this.mob.hasLineOfSight(livingEntity)) {
                mob.setTarget(livingEntity);
            }
        }
    }

    static class AntBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {
        AntBecomeAngryTargetGoal(Ant ant) {
            super(ant, Player.class, 10, true, false, ant::isAngryAt);
        }

        @Override
        public boolean canUse() {
            return this.antCanTarget() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            boolean bl = this.antCanTarget();
            if (!bl || this.mob.getTarget() == null) {
                this.targetMob = null;
                return false;
            }
            return super.canContinueToUse();
        }

        private boolean antCanTarget() {
            Ant ant = (Ant)this.mob;
            return ant.isAngry();
        }
    }

    abstract class BaseAntGoal extends Goal {
        BaseAntGoal() {
        }

        public abstract boolean canAntUse();

        public abstract boolean canAntContinueToUse();

        @Override
        public boolean canUse() {
            return this.canAntUse() && !Ant.this.isAngry() && !Ant.this.isTame();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canAntContinueToUse() && !Ant.this.isAngry() && !Ant.this.isTame();
        }
    }


    @SuppressWarnings("unused")
    public static boolean canSpawn(EntityType<Ant> ant, ServerLevelAccessor world, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        return world.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(world, pos);
    }

}

