package com.ninni.spawn.entity;

import com.google.common.collect.Lists;
import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.block.entity.AnthillBlockEntity;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import com.ninni.spawn.registry.SpawnBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ant extends TamableAnimal implements NeutralMob {
    private static final EntityDataAccessor<Integer> DATA_ABDOMEN_COLOR = SynchedEntityData.defineId(Ant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANGER = SynchedEntityData.defineId(Ant.class, EntityDataSerializers.INT);
    private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    @Nullable
    private UUID angryAt;
    private int cannotEnterAnthillTicks;
    int ticksLeftToFindDwelling;
    @Nullable
    BlockPos anthillPos;
    MoveToAnthillGoal moveToAnthillGoal;

    public Ant(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Mth.nextInt(random, 4, 8));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Mth.nextInt(random, 1, 3));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.nextDouble(random, 0.225, 0.3));
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new FindAnthillGoal());
        this.goalSelector.addGoal(6, new EnterAnthillGoal());
        this.moveToAnthillGoal = new MoveToAnthillGoal();
        this.goalSelector.addGoal(7, this.moveToAnthillGoal);
        this.goalSelector.addGoal(9, new TemptGoal(this, 1.2, Ingredient.of(SpawnTags.HAMSTER_TEMPTS), false));
        this.goalSelector.addGoal(10, new AntComeBackToAnthillGoal());
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 6));
        this.goalSelector.addGoal(13, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANGER, 0);
        this.entityData.define(DATA_ABDOMEN_COLOR, DyeColor.RED.getId());
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
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        Item item = itemStack.getItem();

        if (this.isTame()) {
            if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
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

    boolean canEnterDwelling() {
        if (this.cannotEnterAnthillTicks <= 0 && this.getTarget() == null) return this.level().isRaining() || this.level().isNight();
        else return false;
    }

    public void setCannotEnterAnthillTicks(int cannotEnterAnthillTicks) {
        this.cannotEnterAnthillTicks = cannotEnterAnthillTicks;
    }

    private boolean doesAnthillHaveSpace(BlockPos pos) {
        BlockEntity blockEntity = this.level().getBlockEntity(pos);
        if (blockEntity instanceof AnthillBlockEntity) {
            return !((AnthillBlockEntity)blockEntity).isFullOfAnts();
        } else {
            return false;
        }
    }

    boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.closerThan(this.blockPosition(), distance);
    }

    boolean isAnthillValid() {
        if (!this.hasAnthill()) {
            return false;
        } else {
            BlockEntity blockEntity = this.level().getBlockEntity(this.anthillPos);
            return blockEntity != null && blockEntity.getType() == SpawnBlockEntityTypes.ANTHILL;
        }
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
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putByte("AbdomenColor", (byte)this.getAbdomenColor().getId());
        compoundTag.putInt("CannotEnterAnthillTicks", this.cannotEnterAnthillTicks);
        if (this.hasAnthill()) {
            assert this.getAnthillPos() != null;
            compoundTag.put("AnthillPos", NbtUtils.writeBlockPos(this.getAnthillPos()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("AbdomenColor", 99)) {
            this.setAbdomenColor(DyeColor.byId(compoundTag.getInt("AbdomenColor")));
        }
        this.cannotEnterAnthillTicks = compoundTag.getInt("CannotEnterAnthillTicks");
        if (compoundTag.contains("AnthillPos")) {
            this.anthillPos = NbtUtils.readBlockPos(compoundTag.getCompound("AnthillPos"));
        }
    }


    class AntComeBackToAnthillGoal extends Goal {

        AntComeBackToAnthillGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (Ant.this.onGround()) return Ant.this.navigation.isDone() && Ant.this.random.nextInt(10) == 0;
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return Ant.this.navigation.isInProgress();
        }

        @Override
        public void start() {
            Vec3 vec3d = this.getRandomLocation();
            if (vec3d != null) {
                Ant.this.navigation.moveTo(Ant.this.navigation.createPath(new BlockPos((int)vec3d.x, (int)vec3d.y, (int)vec3d.z), 1), 1.0);
            }
        }

        @Nullable
        private Vec3 getRandomLocation() {
            Vec3 vec3d2;
            assert Ant.this.anthillPos != null;
            if (Ant.this.isAnthillValid() && !Ant.this.isWithinDistance(Ant.this.anthillPos, 22)) {
                Vec3 vec3d = Vec3.atCenterOf(Ant.this.anthillPos);
                vec3d2 = vec3d.subtract(Ant.this.position()).normalize();
            } else {
                vec3d2 = Ant.this.getViewVector(0.0F);
            }

            Vec3 vec3d3 = HoverRandomPos.getPos(Ant.this, 12, 5, vec3d2.x, vec3d2.z, 1.5707964F, 3, 1);
            return vec3d3 != null ? vec3d3 : AirAndWaterRandomPos.getPos(Ant.this, 12, 2, -2, vec3d2.x, vec3d2.z, 1.5707963705062866);
        }
    }
    class EnterAnthillGoal extends Ant.NotAngryGoal {
        EnterAnthillGoal() {
            super();
        }

        @Override
        public boolean canBirtStart() {
            if (Ant.this.hasAnthill() && Ant.this.canEnterDwelling()) {
                assert Ant.this.anthillPos != null;
                if (Ant.this.anthillPos.closerToCenterThan(Ant.this.position(), 2.0)) {
                    BlockEntity blockEntity = Ant.this.level().getBlockEntity(Ant.this.anthillPos);
                    if (blockEntity instanceof AnthillBlockEntity blockEntity1) {
                        if (!blockEntity1.isFullOfAnts()) {
                            return true;
                        }

                        Ant.this.anthillPos = null;
                    }
                }
            }

            return false;
        }

        @Override
        public boolean canBirtContinue() {
            return false;
        }

        @Override
        public void start() {
            BlockEntity blockEntity = Ant.this.level().getBlockEntity(Ant.this.anthillPos);
            if (blockEntity instanceof AnthillBlockEntity birtDwellingBlockEntity) {
                birtDwellingBlockEntity.tryEnterAnthill(Ant.this);
            }

        }
    }

    private class FindAnthillGoal extends Ant.NotAngryGoal {
        FindAnthillGoal() {
            super();
        }

        @Override
        public boolean canBirtStart() {
            return Ant.this.ticksLeftToFindDwelling == 0 && !Ant.this.hasAnthill() && Ant.this.canEnterDwelling();
        }

        @Override
        public boolean canBirtContinue() {
            return false;
        }

        @Override
        public void start() {
            Ant.this.ticksLeftToFindDwelling = 200;
            List<BlockPos> list = this.getNearbyFreeDwellings();
            if (!list.isEmpty()) {
                Iterator<BlockPos> var2 = list.iterator();

                BlockPos blockPos;
                do {
                    if (!var2.hasNext()) {
                        Ant.this.moveToAnthillGoal.clearPossibleDwellings();
                        Ant.this.anthillPos = list.get(0);
                        return;
                    }

                    blockPos = var2.next();
                } while(Ant.this.moveToAnthillGoal.isPossibleDwelling(blockPos));

                Ant.this.anthillPos = blockPos;
            }
        }

        private List<BlockPos> getNearbyFreeDwellings() {
            BlockPos blockPos = Ant.this.blockPosition();
            PoiManager pointOfInterestStorage = ((ServerLevel)Ant.this.level()).getPoiManager();
            Stream<PoiRecord> stream = pointOfInterestStorage.getInRange((poiType) -> poiType.is(SpawnTags.ANT_HOME), blockPos, 20, PoiManager.Occupancy.ANY);
            return stream.map(PoiRecord::getPos).filter(Ant.this::doesAnthillHaveSpace).sorted(Comparator.comparingDouble((blockPos2) -> blockPos2.distSqr(blockPos))).collect(Collectors.toList());
        }
    }

    @VisibleForDebug
    public class MoveToAnthillGoal extends Ant.NotAngryGoal {
        int ticks;
        final List<BlockPos> possibleDwellings;
        @Nullable
        private Path path;
        private int ticksUntilLost;

        MoveToAnthillGoal() {
            super();
            this.ticks = Ant.this.level().random.nextInt(10);
            this.possibleDwellings = Lists.newArrayList();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canBirtStart() {
            return Ant.this.anthillPos != null && !Ant.this.hasRestriction() && Ant.this.canEnterDwelling() && !this.isCloseEnough(Ant.this.anthillPos) && Ant.this.level().getBlockState(Ant.this.anthillPos).is(SpawnBlocks.ANTHILL);
        }

        @Override
        public boolean canBirtContinue() {
            return this.canBirtStart();
        }

        @Override
        public void start() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            Ant.this.navigation.stop();
            Ant.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (Ant.this.anthillPos != null) {
                ++this.ticks;
                if (this.ticks > this.adjustedTickDelay(600)) {
                    this.makeChosenDwellingPossibleDwelling();
                } else if (!Ant.this.navigation.isInProgress()) {
                    if (!Ant.this.isWithinDistance(Ant.this.anthillPos, 16)) {
                        if (Ant.this.isTooFar(Ant.this.anthillPos)) {
                            this.setLost();
                        } else {
                            Ant.this.startMovingTo(Ant.this.anthillPos);
                        }
                    } else {
                        boolean bl = this.startMovingToFar(Ant.this.anthillPos);
                        if (!bl) {
                            this.makeChosenDwellingPossibleDwelling();
                        } else if (this.path != null && Objects.requireNonNull(Ant.this.navigation.getPath()).sameAs(this.path)) {
                            ++this.ticksUntilLost;
                            if (this.ticksUntilLost > 60) {
                                this.setLost();
                                this.ticksUntilLost = 0;
                            }
                        } else {
                            this.path = Ant.this.navigation.getPath();
                        }

                    }
                }
            }
        }

        private boolean startMovingToFar(BlockPos pos) {
            Ant.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
            Ant.this.navigation.moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0);
            return Ant.this.navigation.getPath() != null && Ant.this.navigation.getPath().canReach();
        }

        boolean isPossibleDwelling(BlockPos pos) {
            return this.possibleDwellings.contains(pos);
        }

        private void addPossibleDwelling(BlockPos pos) {
            this.possibleDwellings.add(pos);

            while(this.possibleDwellings.size() > 3) {
                this.possibleDwellings.remove(0);
            }

        }

        void clearPossibleDwellings() {
            this.possibleDwellings.clear();
        }

        private void makeChosenDwellingPossibleDwelling() {
            if (Ant.this.anthillPos != null) {
                this.addPossibleDwelling(Ant.this.anthillPos);
            }

            this.setLost();
        }

        private void setLost() {
            Ant.this.anthillPos = null;
            Ant.this.ticksLeftToFindDwelling = 200;
        }

        private boolean isCloseEnough(BlockPos pos) {
            if (Ant.this.isWithinDistance(pos, 2)) {
                return true;
            } else {
                Path path = Ant.this.navigation.getPath();
                return path != null && path.getTarget().equals(pos) && path.canReach() && path.isDone();
            }
        }
    }

    boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
    }

    void startMovingTo(BlockPos pos) {
        Vec3 vec3d = Vec3.atBottomCenterOf(pos);
        int i = 0;
        BlockPos blockPos = this.blockPosition();
        int j = (int)vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.distManhattan(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3 vec3d2 = AirRandomPos.getPosTowards(this, k, l, i, vec3d, 0.3141592741012573);
        if (vec3d2 != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0);
        }
    }

    private abstract class NotAngryGoal extends Goal {
        NotAngryGoal() {
        }

        public abstract boolean canBirtStart();

        public abstract boolean canBirtContinue();

        @Override
        public boolean canUse() {
            return this.canBirtStart() && !Ant.this.isAngry();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canBirtContinue() && !Ant.this.isAngry();
        }
    }


    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    public static boolean canSpawn(EntityType<Ant> ant, ServerLevelAccessor world, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        return world.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(world, pos);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(ANGER);
    }

    @Override
    public void setRemainingPersistentAngerTime(int angerTime) {
        this.entityData.set(ANGER, angerTime);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }
}
