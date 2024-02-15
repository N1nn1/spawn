package com.ninni.spawn.entity;

import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.entity.ai.goal.EatSeagrassGoal;
import com.ninni.spawn.registry.SpawnBlocks;
import com.ninni.spawn.registry.SpawnParticleTypes;
import com.ninni.spawn.registry.SpawnSoundEvents;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import static com.ninni.spawn.Spawn.MOD_ID;

public class SeaCow extends WaterAnimal implements Shearable {
    public static final ResourceLocation LOOT_COMMON = new ResourceLocation(MOD_ID, "archaeology/sea_cow_common");
    public static final ResourceLocation LOOT_RARE = new ResourceLocation(MOD_ID, "archaeology/sea_cow_rare");
    private static final EntityDataAccessor<Integer> ALGAE = SynchedEntityData.defineId(SeaCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FULLNESS = SynchedEntityData.defineId(SeaCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MUNCHING_COOLDOWN = SynchedEntityData.defineId(SeaCow.class, EntityDataSerializers.INT);
    private EatSeagrassGoal eatBlockGoal;
    private int eatAnimationTick;
    public int maxFullness = 40;
    public int maxAlgaeAmount = 20 * 60 * 40;

    public SeaCow(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02f, 0.1f, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setMaxUpStep(1);
    }

    @Override
    protected void registerGoals() {
        this.eatBlockGoal = new EatSeagrassGoal(this);
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(0, this.eatBlockGoal);
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Guardian.class, 8.0f, 1.0, 1.0));
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1.0, 10));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(5, new MoveToSeagrassGoal(1.2f, 12, 1));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 1.2f).add(Attributes.KNOCKBACK_RESISTANCE, 0.6f);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (itemStack.is(Items.BUCKET) && this.isInWaterOrBubble()) {
            player.playSound(SoundEvents.COW_MILK, 1.0f, 1.0f);
            ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, player, Items.MILK_BUCKET.getDefaultInstance());
            player.setItemInHand(interactionHand, itemStack2);
            this.setPersistenceRequired();
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if ((itemStack.is(SpawnTags.SEA_COW_LIKES) || itemStack.is(SpawnTags.SEA_COW_LOVES)) && this.isInWaterOrBubble() && this.getFullness() < maxFullness) {
            this.feed(itemStack, player);
            this.setPersistenceRequired();
            if (this.getFullness() > maxFullness) this.setFullness(maxFullness);

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (itemStack.is(Items.SHEARS)) {
            if (!this.level().isClientSide && this.readyForShearing()) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, player);
                itemStack.hurtAndBreak(1, player, player2 -> player2.broadcastBreakEvent(interactionHand));
                this.setPersistenceRequired();
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        return super.mobInteract(player, interactionHand);
    }

    @Override
    public void aiStep() {

        if (this.getMunchingCooldown() > 0) this.addMunchingCooldown(-1);
        if (this.getAlgaeAmount() < maxAlgaeAmount && this.isInWaterOrBubble()) this.setAlgaeAmount(this.getAlgaeAmount() + 1);
        if (this.getAlgaeAmount() > maxAlgaeAmount) this.setAlgaeAmount(this.maxAlgaeAmount);

        if (this.level().isClientSide) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }

        if (this.eatAnimationTick > 0) {

            if (this.getMunchingCooldown() > 0) this.eatAnimationTick = 0;


            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005f, 0.0));

            if (this.random.nextFloat() <= 0.4f) {
                this.level().addParticle(
                        new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState()),
                        this.getHeadPos(true, 0.4f).x,
                        this.getHeadBlockPos().getY() + 0.5f,
                        this.getHeadPos(true, 0.4f).z,
                        0.0, 0.0, 0.0
                );
                this.level().addParticle(
                        SpawnParticleTypes.SAND_CLOUD,
                        this.getHeadPos(true, 0.8f).x,
                        this.getHeadBlockPos().getY() + 0.5f,
                        this.getHeadPos(true, 0.8f).z,
                        0.0, 0.0, 0.0
                );
            }
        }

        super.aiStep();
    }

    @Override
    public void ate() {
        super.ate();

        if (this.level() instanceof ServerLevel serverLevel) {
            LootParams.Builder builder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, this.getHeadPos(false, 0)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY);
            LootParams lootParams = builder.withParameter(LootContextParams.BLOCK_STATE, serverLevel.getBlockState(this.getHeadBlockPos())).create(LootContextParamSets.BLOCK);

            if (this.getFullness() >= this.maxFullness / 2 && this.getFullness() < this.maxFullness) {
                LootTable lootTable = lootParams.getLevel().getServer().getLootData().getLootTable(LOOT_COMMON);
                ObjectArrayList<ItemStack> list = lootTable.getRandomItems(lootParams);
                for (ItemStack stack : list) {
                    this.spawnAtHeadLocation(stack);
                }
                this.addMunchingCooldown(this.random.nextInt(300) + 200);
                this.setFullness(0);
            } else if (this.getFullness() == this.maxFullness) {
                LootTable lootTable = lootParams.getLevel().getServer().getLootData().getLootTable(LOOT_RARE);
                ObjectArrayList<ItemStack> list = lootTable.getRandomItems(lootParams);
                for (ItemStack stack : list) {
                    this.spawnAtHeadLocation(stack);
                }
                this.addMunchingCooldown(this.random.nextInt(300) + 400);
                this.setFullness(0);
            }
        }
    }

    public void feed(ItemStack food, Player player) {
        int amount = 0;

        if (food.is(SpawnTags.SEA_COW_LIKES)) amount = 1;
        else if (food.is(SpawnTags.SEA_COW_LOVES)) amount = 5;

        player.playSound(food.getEatingSound(), 1.0f, 1.0f);

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, food),
                    this.getHeadPos(false, 0).x,
                    this.getHeadPos(false, 0).y,
                    this.getHeadPos(false, 0).z,
                    10,
                    this.getBbWidth() / 4.0f,
                    this.getBbHeight() / 4.0f,
                    this.getBbWidth() / 4.0f,
                    0.05);
        }
        this.addMunchingCooldown(20);
        this.setFullness(this.getFullness() + amount);

        food.shrink(1);
    }

    @Override
    public void shear(SoundSource soundSource) {
        this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0f, 1.0f);
        if (this.getAlgaeAmount() > 35000) {
            int i = this.random.nextInt(10) + 10;
            for (int j = 0; j < i; ++j) {
                ItemEntity itemEntity = this.spawnAtLocation(Items.SEAGRASS, 1);
                if (itemEntity == null) continue;
                itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
            }
        }
        this.setAlgaeAmount(0);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ALGAE, 0);
        this.entityData.define(FULLNESS, 0);
        this.entityData.define(MUNCHING_COOLDOWN, this.random.nextInt(200) + 30);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("AlgaeAmount", this.getAlgaeAmount());
        compoundTag.putInt("Fullness", this.getFullness());
        compoundTag.putInt("MunchingCooldown", this.getMunchingCooldown());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setAlgaeAmount(compoundTag.getInt("AlgaeAmount"));
        this.setFullness(compoundTag.getInt("Fullness"));
        this.setMunchingCooldown(compoundTag.getInt("MunchingCooldown"));
    }

    public int getAlgaeAmount() {
        return this.entityData.get(ALGAE);
    }

    public void setAlgaeAmount(int amount) {
        this.entityData.set(ALGAE, amount);
    }

    public int getMunchingCooldown() {
        return this.entityData.get(MUNCHING_COOLDOWN);
    }

    public void addMunchingCooldown(int amount) {
        this.entityData.set(MUNCHING_COOLDOWN, this.getMunchingCooldown() + amount);
    }

    public void setMunchingCooldown(int amount) {
        this.entityData.set(MUNCHING_COOLDOWN, amount);
    }

    public int getFullness() {
        return this.entityData.get(FULLNESS);
    }

    public void setFullness(int amount) {
        this.entityData.set(FULLNESS, amount);
    }

    @Override
    public boolean readyForShearing() {
        return this.getAlgaeAmount() > 14000;
    }

    @Override
    protected void customServerAiStep() {
        this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
        super.customServerAiStep();
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 10) {
            this.eatAnimationTick = 40;
        } else {
            super.handleEntityEvent(b);
        }
    }

    public float getHeadEatPositionScale(float f) {
        if (this.eatAnimationTick <= 0) {
            return 0.0f;
        }
        if (this.eatAnimationTick >= 4 && this.eatAnimationTick <= 36) {
            return 1.0f;
        }
        if (this.eatAnimationTick < 4) {
            return ((float)this.eatAnimationTick - f) / 4.0f;
        }
        return -((float)(this.eatAnimationTick - 40) - f) / 4.0f;
    }

    public float getHeadEatAngleScale(float f) {
        if (this.eatAnimationTick > 4 && this.eatAnimationTick <= 36) {
            float g = ((float)(this.eatAnimationTick - 4) - f) / 32.0f;
            return 0.62831855f + 0.21991149f * Mth.sin(g * 28.7f);
        }
        if (this.eatAnimationTick > 0) {
            return 0.62831855f;
        }
        return this.getXRot() * ((float)Math.PI / 180);
    }

    public void spawnAtHeadLocation(ItemStack itemStack) {
        if (!itemStack.isEmpty() && !this.level().isClientSide) {
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getHeadPos(false, 0).x, this.getHeadPos(false, 0).y, this.getHeadPos(false, 0).z, itemStack);
            itemEntity.setDefaultPickUpDelay();
            this.level().addFreshEntity(itemEntity);
        }
    }

    public Vec3 getHeadPos(boolean random, float randomValue) {
        final float angle = (0.0174532925F * SeaCow.this.yBodyRot);
        final float headX = 1.5F * getScale() * Mth.sin(Mth.PI + angle);
        final float headZ = 1.5F * getScale() * Mth.cos(angle);

        if (random) {
            return new Vec3(SeaCow.this.getRandomX(randomValue) + headX, SeaCow.this.getRandomY(), SeaCow.this.getRandomZ(randomValue) + headZ);
        } else return new Vec3(SeaCow.this.getX() + headX, SeaCow.this.getBlockY(), SeaCow.this.getZ() + headZ);
    }

    public BlockPos getHeadBlockPos() {
        final float angle = (0.0174532925F * SeaCow.this.yBodyRot);
        final double headX = 1.5F * getScale() * Mth.sin(Mth.PI + angle);
        final double headZ = 1.5F * getScale() * Mth.cos(angle);

        return new BlockPos((int)(SeaCow.this.getX() + headX), SeaCow.this.getBlockY(), (int)(SeaCow.this.getZ() + headZ));
    }

    @Override
    protected void handleAirSupply(int i) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(i - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0f);
            }
        } else {
            this.setAirSupply(20 * 60 * 4);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return !this.isPersistenceRequired();
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return true;
    }

    public class MoveToSeagrassGoal extends MoveToBlockGoal {
        public MoveToSeagrassGoal(double d, int i, int j) {
            super(SeaCow.this, d, i, j);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && SeaCow.this.getMunchingCooldown() == 0;
        }

        @Override
        protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
            BlockPos blockPos1 = SeaCow.this.getHeadBlockPos();
            return levelReader.getBlockState(blockPos1).is(Blocks.TALL_SEAGRASS) && levelReader.getBlockState(blockPos1.below()).is(SpawnBlocks.ALGAL_SAND);
        }
    }

    @SuppressWarnings("unused, deprecation")
    public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<Mob> mobEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        int i = serverLevelAccessor.getSeaLevel();
        int j = i - 13;
        return blockPos.getY() >= j && blockPos.getY() <= i && serverLevelAccessor.getFluidState(blockPos.below()).is(FluidTags.WATER) && serverLevelAccessor.getBlockState(blockPos.above()).is(Blocks.WATER);
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SpawnSoundEvents.BIG_FISH_SWIM;
    }
}
