package com.ninni.spawn.entity;

import com.google.common.collect.Maps;
import com.ninni.spawn.SpawnTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Ant extends TamableAnimal {
    private static final EntityDataAccessor<Integer> DATA_ABDOMEN_COLOR = SynchedEntityData.defineId(Ant.class, EntityDataSerializers.INT);
    private static final Map<DyeColor, float[]> COLORARRAY_BY_COLOR = Maps.newEnumMap(Arrays.stream(DyeColor.values()).collect(Collectors.toMap(dyeColor -> dyeColor, Ant::createAbdomenColor)));

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
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 7f, 2.0f, false));
        this.goalSelector.addGoal(7, new TemptGoal(this, 1.2, Ingredient.of(SpawnTags.HAMSTER_TEMPTS), false));
        this.goalSelector.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 1));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));

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
        this.entityData.define(DATA_ABDOMEN_COLOR, DyeColor.RED.getId());
    }

    private static float[] createAbdomenColor(DyeColor dyeColor) {
        if (dyeColor == DyeColor.WHITE) return new float[]{0.9019608f, 0.9019608f, 0.9019608f};
        float[] fs = dyeColor.getTextureDiffuseColors();
        return new float[]{fs[0] * 0.75f, fs[1] * 0.75f, fs[2] * 0.75f};
    }
    public static float[] getColorArray(DyeColor dyeColor) {
        return COLORARRAY_BY_COLOR.get(dyeColor);
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

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putByte("AbdomenColor", (byte)this.getAbdomenColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("AbdomenColor", 99)) {
            this.setAbdomenColor(DyeColor.byId(compoundTag.getInt("AbdomenColor")));
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
}
