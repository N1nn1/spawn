package com.ninni.spawn.entity;

import com.ninni.spawn.registry.SpawnItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Octopus extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Octopus.class, EntityDataSerializers.BOOLEAN);
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
    private int idleAnimationTimeout = 0;
    public final AnimationState waterIdleAnimationState = new AnimationState();
    private int waterIdleAnimationTimeout = 0;


    public Octopus(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setMaxUpStep(1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0));
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

        if ((this.level()).isClientSide()) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {

        if (this.isInWaterOrBubble() && !this.onGround()) {
            if (this.waterIdleAnimationTimeout <= 0) {
                this.waterIdleAnimationTimeout = 20 * 8;
                this.waterIdleAnimationState.start(this.tickCount);
            } else {
                --this.waterIdleAnimationTimeout;
            }
            this.idleAnimationState.stop();
        } else {
            this.waterIdleAnimationState.stop();
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = 20 * 8;
                this.idleAnimationState.start(this.tickCount);
            } else {
                --this.idleAnimationTimeout;
            }
        }

    }

    protected PathNavigation createNavigation(Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROM_BUCKET, false);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("FromBucket", this.fromBucket());
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setFromBucket(compoundTag.getBoolean("FromBucket"));
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
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
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


    public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<Octopus> mobEntityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        int i = levelAccessor.getSeaLevel();
        int j = i - 13;
        return blockPos.getY() >= j && blockPos.getY() <= i && levelAccessor.getFluidState(blockPos.below()).is(FluidTags.WATER) && levelAccessor.getBlockState(blockPos.above()).is(Blocks.WATER);
    }

}
