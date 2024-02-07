package com.ninni.spawn.entity;

import com.ninni.spawn.entity.variant.ClamVariant;
import com.ninni.spawn.item.ClamCaseItem;
import com.ninni.spawn.registry.SpawnItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Clam extends Mob implements VariantHolder<ClamVariant.Variant> {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(Clam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_ITEM = SynchedEntityData.defineId(Clam.class, EntityDataSerializers.BOOLEAN);

    public Clam(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        this.setMaxUpStep(1);
        this.fixupDimensions();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 1.2f).add(Attributes.KNOCKBACK_RESISTANCE, 0.8f);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        SpawnGroupData spawnGroupData2 = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        if (mobSpawnType == MobSpawnType.MOB_SUMMONED && compoundTag != null && compoundTag.contains("ItemVariantTag", 3)) {
            this.setPackedVariant(compoundTag.getInt("ItemVariantTag"));
        } else {
            RandomSource randomSource = serverLevelAccessor.getRandom();
            ClamVariant.Variant variant;
            ClamVariant.BaseColor[] baseColors = ClamVariant.BaseColor.values();
            ClamVariant.Pattern[] patterns = ClamVariant.Pattern.values();
            DyeColor[] dyeColors = DyeColor.values();

            ClamVariant.BaseColor baseColor = Util.getRandom(baseColors, randomSource);
            ClamVariant.Pattern pattern = Util.getRandom(patterns, randomSource);
            DyeColor dyeColor = Util.getRandom(dyeColors, randomSource);

            variant = new ClamVariant.Variant(baseColor, pattern, dyeColor);

            //this.yHeadRot = this.getYRot();
            //this.yBodyRot = this.getYRot();

            this.setPackedVariant(variant.getPackedId());
        }

        this.refreshDimensions();
        return spawnGroupData2;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if ((itemStack.isEmpty() || itemStack.getItem() instanceof ClamCaseItem) && this.isAlive()) {
            //TODO sound
            this.playSound(SoundEvents.EMPTY, 1.0f, 1.0f);
            ItemStack itemStack2 = SpawnItems.CLAM.getDefaultInstance();
            saveToBucketTag(itemStack2);
            if (itemStack.getItem() instanceof ClamCaseItem && ClamCaseItem.getContentWeight(itemStack) < 16) {
                ClamCaseItem.add(itemStack, itemStack2);
                this.discard();
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else if (itemStack.isEmpty()) {
               ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
               player.setItemInHand(interactionHand, itemStack3);
               this.discard();
               return InteractionResult.sidedSuccess(this.level().isClientSide);

            }
        }

        return super.mobInteract(player, interactionHand);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        if (this.getBaseColor().base() == ClamVariant.Base.WEDGE_SHELL) return super.getDimensions(pose).scale(1, 1);
        else if (this.getBaseColor().base() == ClamVariant.Base.SCALLOP) return super.getDimensions(pose).scale(1.8f, 1);
        else if (this.getBaseColor().base() == ClamVariant.Base.GIANT_CLAM) return super.getDimensions(pose).scale(3, 4.5f);
        else return super.getDimensions(pose);
    }

    @Override
    public void refreshDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.refreshDimensions();
        this.setPos(d, e, f);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    protected float tickHeadTurn(float f, float g) {
        this.yBodyRotO = this.yRotO;
        this.yBodyRot = this.getYRot();
        return 0.0f;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01f, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.05, 0.0));
        } else {
            super.travel(vec3);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        this.refreshDimensions();
        super.onSyncedDataUpdated(entityDataAccessor);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(FROM_ITEM, false);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.getPackedVariant());
        compoundTag.putBoolean("FromItem", this.fromItem());
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        this.refreshDimensions();
        super.readAdditionalSaveData(compoundTag);
        this.setPackedVariant(compoundTag.getInt("Variant"));
        this.setFromItem(compoundTag.getBoolean("FromItem"));
    }


    public void saveToBucketTag(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (this.hasCustomName()) itemStack.setHoverName(this.getCustomName());
        if (this.isNoAi()) compoundTag.putBoolean("NoAI", this.isNoAi());
        if (this.isSilent()) compoundTag.putBoolean("Silent", this.isSilent());
        if (this.isNoGravity()) compoundTag.putBoolean("NoGravity", this.isNoGravity());
        if (this.hasGlowingTag()) compoundTag.putBoolean("Glowing", this.hasGlowingTag());
        if (this.isInvulnerable()) compoundTag.putBoolean("Invulnerable", this.isInvulnerable());
        compoundTag.putInt("ItemVariantTag", this.getPackedVariant());
        compoundTag.putFloat("Health", this.getHealth());
    }

    public static void loadFromBucketTag(Mob mob, CompoundTag compoundTag) {
        if (compoundTag.contains("NoAI")) mob.setNoAi(compoundTag.getBoolean("NoAI"));
        if (compoundTag.contains("Silent")) mob.setSilent(compoundTag.getBoolean("Silent"));
        if (compoundTag.contains("NoGravity")) mob.setNoGravity(compoundTag.getBoolean("NoGravity"));
        if (compoundTag.contains("Glowing")) mob.setGlowingTag(compoundTag.getBoolean("Glowing"));
        if (compoundTag.contains("Invulnerable")) mob.setInvulnerable(compoundTag.getBoolean("Invulnerable"));
        if (compoundTag.contains("Health", 99)) mob.setHealth(compoundTag.getFloat("Health"));
    }

    public boolean fromItem() {
        return this.entityData.get(FROM_ITEM);
    }

    public void setFromItem(boolean bl) {
        this.entityData.set(FROM_ITEM, bl);
    }

    public void setPackedVariant(int i) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, i);
    }

    public int getPackedVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromItem();
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
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return !this.fromItem() && !this.hasCustomName();
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    @Override
    public void setVariant(ClamVariant.Variant variant) {
        this.setPackedVariant(packVariant(variant.baseColor(), variant.pattern(), variant.dyeColor()));
    }
    @Override
    public ClamVariant.Variant getVariant() {
        return new ClamVariant.Variant(this.getBaseColor(), this.getPattern(), this.getDyeColor());
    }

    public static int packVariant(ClamVariant.BaseColor baseColor, ClamVariant.Pattern pattern, DyeColor dyeColor) {
        return (baseColor.getId() << 8) | (pattern.getId() << 4) | dyeColor.getId();
    }

    public ClamVariant.BaseColor getBaseColor() {
        return getBaseColor(this.getPackedVariant());
    }
    public ClamVariant.Pattern getPattern() {
        return getPattern(this.getPackedVariant());
    }
    public DyeColor getDyeColor() {
        return getDyeColor(this.getPackedVariant());
    }


    public static ClamVariant.BaseColor getBaseColor(int i) {
        return ClamVariant.BaseColor.byId((i >> 8) & 0xF);
    }
    public static ClamVariant.Pattern getPattern(int i) {
        return ClamVariant.Pattern.byId((i >> 4) & 0xF);
    }
    public static DyeColor getDyeColor(int i) {
        return DyeColor.byId(i & 0xF);
    }

    public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<Clam> clamEntityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        int i = levelAccessor.getSeaLevel();
        int j = levelAccessor.getSeaLevel() - 35;
        return blockPos.getY() >= j && blockPos.getY() <= i && levelAccessor.getFluidState(blockPos).is(FluidTags.WATER) && levelAccessor.getBlockState(blockPos.below()).isSolid();
    }
}
