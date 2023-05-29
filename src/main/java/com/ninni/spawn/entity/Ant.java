package com.ninni.spawn.entity;

import com.ninni.spawn.SpawnTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class Ant extends TamableAnimal {

    public Ant(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Mth.nextInt(random, 1, 4));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Mth.nextInt(random, 1, 2));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.nextDouble(random, 0.2, 0.275));
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
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.MAX_HEALTH, 10.0);
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

    public static boolean canSpawn(EntityType<Ant> hamsterEntityType, ServerLevelAccessor world, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        return world.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(world, pos);
    }
}
