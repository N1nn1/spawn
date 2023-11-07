package com.ninni.spawn.block.entity;

import com.google.common.collect.Lists;
import com.ninni.spawn.block.AnthillBlock;
import com.ninni.spawn.entity.Ant;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AnthillBlockEntity extends BlockEntity {
    public static final String MIN_OCCUPATION_TICKS_KEY = "MinOccupationTicks";
    public static final String ENTITY_DATA_KEY = "EntityData";
    public static final String TICKS_IN_ANTHILL_KEY = "TicksInAnthill";
    public static final String ANTS_KEY = "Ants";
    private static final List<String> IGNORED_ANT_TAGS =
            Arrays.asList(
                    "Bees",
                    "Air",
                    "ArmorDropChances",
                    "ArmorItems",
                    "Brain",
                    "CanPickUpLoot",
                    "DeathTime",
                    "FallDistance",
                    "FallFlying",
                    "Fire",
                    "HandDropChances",
                    "HandItems",
                    "HurtByTimestamp",
                    "HurtTime",
                    "LeftHanded",
                    "Motion",
                    "NoGravity",
                    "OnGround",
                    "PortalCooldown",
                    "Pos",
                    "Rotation",
                    "CannotEnterAnthillTicks",
                    "TicksSinceGathering",
                    "AnthillPos",
                    "Passengers",
                    "Leash",
                    "UUID");
    private final List<Ant> ants = Lists.newArrayList();
    @Nullable
    private BlockPos savedResourcePos;
    private boolean hasResource;

    public AnthillBlockEntity(BlockPos pos, BlockState state) {
        super(SpawnBlockEntityTypes.ANTHILL.get(), pos, state);
    }

    public boolean hasNoAnts() {
        return this.ants.isEmpty();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isFullOfAnts() {
        return this.ants.size() == 3;
    }

    public void angerAnts(@Nullable Player player, BlockState state, AntState antState) {
        List<Entity> list = this.tryReleaseAnt(state, antState);
        if (player != null) {
            for (Entity entity : list) {
                if (!(entity instanceof com.ninni.spawn.entity.Ant ant)) continue;
                if (!(player.position().distanceToSqr(entity.position()) <= 16.0)) continue;
                ant.setTarget(player);
                ant.setStayOutOfAnthillCountdown(400);
            }
        }
    }

    public void storeAnt(CompoundTag nbt, int i, boolean bl) {
        this.ants.add(new AnthillBlockEntity.Ant(nbt, i, bl ? 2400 : 600));
    }

    private List<Entity> tryReleaseAnt(BlockState state, AntState antState) {
        ArrayList<Entity> list = Lists.newArrayList();
        this.ants.removeIf(ant -> {
            assert this.level != null;
            return AnthillBlockEntity.releaseAnt(this.level, this.worldPosition, state, ant, list, antState, this.savedResourcePos);
        });
        if (!list.isEmpty()) super.setChanged();
        return list;
    }

    public void tryEnterAnthill(Entity entity, boolean bl) {
        this.tryEnterAnthill(entity, bl, 0);
    }

    public void tryEnterAnthill(Entity entity, boolean bl, int ticksInAnthill) {
        if (this.ants.size() >= 3) {
            return;
        }
        entity.stopRiding();
        entity.ejectPassengers();
        CompoundTag nbtCompound = new CompoundTag();
        entity.save(nbtCompound);
        BlockPos blockPos = this.getBlockPos();
        this.addAnt(nbtCompound, ticksInAnthill, bl, entity.level().getRandom());
        if (this.level != null) {
            if (entity instanceof com.ninni.spawn.entity.Ant ant) {
                if (ant.hasSavedResourcePos()) {
                    this.savedResourcePos = ant.getSavedResourcePos();
                }
                this.hasResource = ant.hasResource();
            }
            this.level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SpawnSoundEvents.ANTHILL_ENTER.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, this.getBlockState()));
        }
        entity.discard();
        super.setChanged();
    }

    public void addAnt(CompoundTag nbtCompound, int ticksInAnthill, boolean bl, RandomSource random) {
        assert this.level != null;
        this.ants.add(new Ant(nbtCompound, ticksInAnthill, (bl ? 2400 : 600) + random.nextInt(600)));
    }

    private static boolean releaseAnt(Level world, BlockPos pos, BlockState state, Ant ant, @Nullable List<Entity> entities, AntState antState, BlockPos resourcePos) {
        if (world.isNight() && antState != AntState.EMERGENCY) {
            return false;
        }
        CompoundTag nbtCompound = ant.entityData.copy();
        AnthillBlockEntity.removeIrrelevantNbtKeys(nbtCompound);
        nbtCompound.put("AnthillPos", NbtUtils.writeBlockPos(pos));
        boolean bl = !world.getBlockState(pos.above()).getCollisionShape(world, pos.above()).isEmpty();

        if (bl && antState != AntState.EMERGENCY) return false;
        Entity newAnt = EntityType.loadEntityRecursive(nbtCompound, world, entity -> entity);
        if (newAnt != null) {
            if (newAnt instanceof com.ninni.spawn.entity.Ant releasedAnt) {
                if (resourcePos != null && !releasedAnt.hasSavedResourcePos()) {
                    releasedAnt.setSavedResourcePos(resourcePos);
                }
                if (antState == AntState.RESOURCE_DELIVERED) {
                    releasedAnt.setHasResource(false);
                    int i = state.getValue(AnthillBlock.RESOURCE_LEVEL);
                    if (state.getBlock() instanceof AnthillBlock && i < 3) {
                        world.setBlockAndUpdate(pos, state.setValue(AnthillBlock.RESOURCE_LEVEL, i + 1));
                    }
                }
                AnthillBlockEntity.ageAnt(Ant.ticksInAnthill, releasedAnt);
                if (entities != null) entities.add(releasedAnt);
                double x = (double)pos.getX() + 0.5;
                double y = (double)pos.getY() + 1;
                double z = (double)pos.getZ() + 0.5;
                releasedAnt.moveTo(x, y, z, releasedAnt.getYRot(), releasedAnt.getXRot());
            } else return false;
            world.playSound(null, pos, SpawnSoundEvents.ANTHILL_EXIT.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(releasedAnt, world.getBlockState(pos)));
            if (world instanceof ServerLevel serverLevel) {
                releasedAnt.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(releasedAnt.blockPosition()), MobSpawnType.EVENT, null, nbtCompound);
            }
            return world.addFreshEntity(releasedAnt);
        }
        return false;
    }

    static void removeIrrelevantNbtKeys(CompoundTag compound) {
        for (String string : IGNORED_ANT_TAGS) compound.remove(string);
    }

    private static void ageAnt(int ticks, com.ninni.spawn.entity.Ant ant) {
        int i = ant.getAge();
        if (i < 0) ant.setAge(Math.min(0, i + ticks));
        else if (i > 0) ant.setAge(Math.max(0, i - ticks));
    }

    private static void tickAnts(Level world, BlockPos pos, BlockState state, List<Ant> ants, BlockPos resourcePos, boolean hasResource) {
        boolean bl = false;
        Iterator<Ant> iterator = ants.iterator();
        while (iterator.hasNext()) {
            Ant ant = iterator.next();
            if (Ant.ticksInAnthill > ant.minOccupationTicks) {
                AntState antState = hasResource ? AntState.RESOURCE_DELIVERED : AntState.ANT_RELEASED;
                if (releaseAnt(world, pos, state, ant, null, antState, resourcePos)) {
                    bl = true;
                    iterator.remove();
                }
            }
            ++Ant.ticksInAnthill;
        }
        if (bl) AnthillBlockEntity.setChanged(world, pos, state);
    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, AnthillBlockEntity blockEntity) {
        AnthillBlockEntity.tickAnts(world, pos, state, blockEntity.ants, blockEntity.savedResourcePos, blockEntity.hasResource);
        if (!blockEntity.ants.isEmpty() && world.getRandom().nextDouble() < 0.005) {
            double d = (double)pos.getX() + 0.5;
            double e = pos.getY();
            double f = (double)pos.getZ() + 0.5;
            world.playSound(null, d, e, f, SpawnSoundEvents.ANTHILL_WORK.get(), SoundSource.BLOCKS, 0.5f, 1.0f);
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.ants.clear();
        ListTag nbtList = nbt.getList(ANTS_KEY, 10);
        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag nbtCompound = nbtList.getCompound(i);
            Ant ant = new Ant(nbtCompound.getCompound(ENTITY_DATA_KEY), nbtCompound.getInt(TICKS_IN_ANTHILL_KEY), nbtCompound.getInt(MIN_OCCUPATION_TICKS_KEY));
            this.ants.add(ant);
        }
        this.hasResource = nbt.getBoolean("HasResource");
        this.savedResourcePos = null;
        if (nbt.contains("ResourcePos")) {
            this.savedResourcePos = NbtUtils.readBlockPos(nbt.getCompound("ResourcePos"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put(ANTS_KEY, this.getAnts());
        nbt.putBoolean("HasResource", this.hasResource);
        if (this.savedResourcePos != null) {
            nbt.put("ResourcePos", NbtUtils.writeBlockPos(this.savedResourcePos));
        }
    }

    public ListTag getAnts() {
        ListTag nbtList = new ListTag();
        for (Ant ant : this.ants) {
            CompoundTag nbtCompound = ant.entityData.copy();
            nbtCompound.remove("UUID");
            CompoundTag nbtCompound2 = new CompoundTag();
            nbtCompound2.put(ENTITY_DATA_KEY, nbtCompound);
            nbtCompound2.putInt(IGNORED_ANT_TAGS.toString(), Ant.ticksInAnthill);
            nbtCompound2.putInt(MIN_OCCUPATION_TICKS_KEY, ant.minOccupationTicks);
            nbtList.add(nbtCompound2);
        }
        return nbtList;
    }

    public enum AntState {
        RESOURCE_DELIVERED,
        ANT_RELEASED,
        EMERGENCY
    }

    static class Ant {
        final CompoundTag entityData;
        static int ticksInAnthill;
        final int minOccupationTicks;

        Ant(CompoundTag entityData, int ticksInDwelling, int minOccupationTicks) {
            AnthillBlockEntity.removeIrrelevantNbtKeys(entityData);
            this.entityData = entityData;
            Ant.ticksInAnthill = ticksInDwelling;
            this.minOccupationTicks = minOccupationTicks;
        }
    }
}
