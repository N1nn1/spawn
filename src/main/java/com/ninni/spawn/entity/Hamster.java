package com.ninni.spawn.entity;

import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.entity.variant.HamsterVariant;
import com.ninni.spawn.registry.SpawnEntityType;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class Hamster extends TamableAnimal implements InventoryCarrier {
    public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.BYTE);
    static final Predicate<ItemEntity> ALLOWED_ITEMS = itemEntity -> !itemEntity.hasPickUpDelay() && itemEntity.isAlive();
    private final SimpleContainer inventory = new SimpleContainer(12);

    public Hamster(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new Hamster.HamsterMoveControl();
        this.setCanPickUpLoot(true);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        HamsterVariant[] variants = HamsterVariant.values();
        HamsterVariant variant = Util.getRandom(variants, serverLevelAccessor.getRandom());
        this.setVariant(variant);
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0F));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.1, 7f, 2.0f, false));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.2, Ingredient.of(SpawnTags.HAMSTER_TEMPTS), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1));
        this.goalSelector.addGoal(7, new HamsterSearchForItemsGoal());
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(11, new StandGoal());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 6.0);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(SpawnTags.HAMSTER_TEMPTS);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (this.level.isClientSide) {
            boolean bl = this.isOwnedBy(player) || this.isTame() || itemStack.is(SpawnTags.HAMSTER_FEEDS) && !this.isTame();
            return bl ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        if (this.isTame()) {
            if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) itemStack.shrink(1);
                this.heal(4f);
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
        if (!itemStack.is(SpawnTags.HAMSTER_FEEDS)) return super.mobInteract(player, interactionHand);
        if (!player.getAbilities().instabuild) itemStack.shrink(1);
        if (this.random.nextInt(3) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setOrderedToSit(true);
            this.level.broadcastEntityEvent(this, (byte)7);
            return InteractionResult.SUCCESS;
        } else {
            this.level.broadcastEntityEvent(this, (byte)6);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void aiStep() {
        if (this.isStanding()) {
                this.jumping = false;
                this.xxa = 0.0f;
                this.zza = 0.0f;
        }
        super.aiStep();
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        InventoryCarrier.pickUpItem(this, this, itemEntity);
    }


    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
            this.spawnAtLocation(itemStack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @Override
    protected int calculateFallDamage(float f, float g) {
        return super.calculateFallDamage(f, g) - 10;
    }
    
    boolean canMove() {
        return !this.isStanding() && !this.isInSittingPose() && !this.isImmobile();
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, HamsterVariant.RUSSIAN.id());
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    public void setTame(boolean bl) {
        super.setTame(bl);
        if (bl) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(12.0);
            this.setHealth(20.0f);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6.0);
        }
    }

    public boolean isStanding() {
        return this.getFlag(1);
    }

    public void setStanding(boolean bl) {
        this.setFlag(1, bl);
    }

    private void setFlag(int i, boolean bl) {
        if (bl) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) | i));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) & ~i));
        }
    }
    private boolean getFlag(int i) {
        return (this.entityData.get(DATA_FLAGS_ID) & i) != 0;
    }

    public HamsterVariant getVariant() {
        return HamsterVariant.byId(this.entityData.get(VARIANT));
    }
    public void setVariant(HamsterVariant variant) {
        this.entityData.set(VARIANT, variant.id());
    }

    class HamsterMoveControl extends MoveControl {
        public HamsterMoveControl() {
            super(Hamster.this);
        }
        @Override
        public void tick() {
            if (Hamster.this.canMove()) super.tick();
        }
    }

    class HamsterSearchForItemsGoal extends Goal {
        public HamsterSearchForItemsGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!Hamster.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) return false;
            if (Hamster.this.getTarget() != null || Hamster.this.getLastHurtByMob() != null) return false;
            if (!Hamster.this.canMove()) return false;
            if (Hamster.this.getRandom().nextInt(Hamster.HamsterSearchForItemsGoal.reducedTickDelay(10)) != 0) return false;
            List<ItemEntity> list = Hamster.this.level.getEntitiesOfClass(ItemEntity.class, Hamster.this.getBoundingBox().inflate(8.0, 8.0, 8.0), ALLOWED_ITEMS);
            return !list.isEmpty() && Hamster.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
        }

        @Override
        public void tick() {
            List<ItemEntity> list = Hamster.this.level.getEntitiesOfClass(ItemEntity.class, Hamster.this.getBoundingBox().inflate(8.0, 8.0, 8.0), ALLOWED_ITEMS);
            ItemStack itemStack = Hamster.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemStack.isEmpty() && !list.isEmpty()) {
                Hamster.this.getNavigation().moveTo(list.get(0), 1f);
            }
        }

        @Override
        public void start() {
            List<ItemEntity> list = Hamster.this.level.getEntitiesOfClass(ItemEntity.class, Hamster.this.getBoundingBox().inflate(8.0, 8.0, 8.0), ALLOWED_ITEMS);
            if (!list.isEmpty()) {
                Hamster.this.getNavigation().moveTo(list.get(0), 1.2f);
            }
        }
    }

    class StandGoal extends Goal {
        private double relX;
        private double relZ;
        private int lookTime;
        private int looksRemaining;

        public StandGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return Hamster.this.getLastHurtByMob() == null
                    && Hamster.this.getRandom().nextFloat() < 0.02f
                    && Hamster.this.getNavigation().isDone()
                    && !Hamster.this.isInSittingPose()
                    && Hamster.this.isOnGround();
        }

        @Override
        public boolean canContinueToUse() {
            return this.looksRemaining > 0;
        }

        @Override
        public void start() {
            this.resetLook();
            this.looksRemaining = 2 + Hamster.this.getRandom().nextInt(3);
            Hamster.this.setStanding(true);
            Hamster.this.getNavigation().stop();
        }

        @Override
        public void stop() {
            Hamster.this.setStanding(false);
        }

        @Override
        public void tick() {
            --this.lookTime;
            if (this.lookTime <= 0) {
                --this.looksRemaining;
                this.resetLook();
            }

            Hamster.this.getLookControl().setLookAt(Hamster.this.getX() + this.relX, Hamster.this.getEyeY(), Hamster.this.getZ() + this.relZ, Hamster.this.getMaxHeadYRot(), Hamster.this.getMaxHeadXRot());
        }

        private void resetLook() {
            double d = Math.PI * 2 * Hamster.this.getRandom().nextDouble();
            this.relX = Math.cos(d);
            this.relZ = Math.sin(d);
            this.lookTime = this.adjustedTickDelay(80 + Hamster.this.getRandom().nextInt(20));
        }
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.getVariant().id());
        compoundTag.putBoolean("Standing", this.isStanding());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setVariant(HamsterVariant.byId(compoundTag.getInt("Variant")));
        this.setStanding(compoundTag.getBoolean("Standing"));
    }

    public static boolean canSpawn(EntityType<Hamster> hamsterEntityType, ServerLevelAccessor world, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        return world.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(world, pos);
    }
    
    @Override
    @Nullable
    public Hamster getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        Hamster hamster = SpawnEntityType.HAMSTER.create(serverLevel);
        if (hamster != null && ageableMob instanceof Hamster hamster2) {
            if (this.random.nextBoolean()) {
                hamster.setVariant(this.getVariant());
            } else {
                hamster.setVariant(hamster2.getVariant());
            }
            if (this.isTame()) {
                hamster.setOwnerUUID(this.getOwnerUUID());
                hamster.setTame(true);
            }
        }
        return hamster;
    }
}
