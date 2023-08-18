package com.ninni.spawn.block.entity;

import com.google.common.collect.Lists;
import com.ninni.spawn.block.AnthillBlock;
import com.ninni.spawn.entity.Ant;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import com.ninni.spawn.registry.SpawnBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
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
    private static final List<String> IRRELEVANT_ANT_NBT_KEYS = Arrays.asList("Air", "Bees", "ArmorDropChances", "ArmorItems", "Brain", "CanPickUpLoot", "DeathTime", "FallDistance", "FallFlying", "Fire", "HandDropChances", "HandItems", "HurtByTimestamp", "HurtTime", "LeftHanded", "Motion", "NoGravity", "OnGround", "PortalCooldown", "Pos", "Rotation", "CannotEnterDwellingTicks", "CannotEnterHiveTicks", "TicksSincePollination", "CropsGrownSincePollination", "DwellingPos", "HivePos", "Passengers", "Leash", "UUID");
    private final List<Ant> ants = Lists.newArrayList();
    private int day = -1;

    public AnthillBlockEntity(BlockPos pos, BlockState state) {
        super(SpawnBlockEntityTypes.ANTHILL, pos, state);
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
                ant.setCannotEnterAnthillTicks(400);
            }
        }
    }

    public void storeAnt(CompoundTag compoundTag, int i, boolean bl) {
        this.ants.add(new AnthillBlockEntity.Ant(compoundTag, i, bl ? 2400 : 600));
    }

    private List<Entity> tryReleaseAnt(BlockState state, AntState antState) {
        ArrayList<Entity> list = Lists.newArrayList();
        this.ants.removeIf(ant -> {
            assert this.level != null;
            return AnthillBlockEntity.releaseAnt(this.level, this.worldPosition, state, ant, list, antState);
        });
        if (!list.isEmpty()) super.setChanged();
        return list;
    }

    public void tryEnterAnthill(Entity entity) {
        this.tryEnterAnthill(entity, 0);
    }

    public void tryEnterAnthill(Entity entity, int ticksInAnthill) {
        if (this.ants.size() >= 3) {
            return;
        }
        entity.stopRiding();
        entity.ejectPassengers();
        CompoundTag nbtCompound = new CompoundTag();
        entity.save(nbtCompound);
        BlockPos blockPos = this.getBlockPos();
        this.addAnt(nbtCompound, ticksInAnthill);
        if (this.level != null) {
            //this.level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SpeciesSoundEvents.BLOCK_BIRT_DWELLING_ENTER, SoundSource.BLOCKS, 1.0f, 1.0f);
            this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, this.getBlockState()));
        }
        entity.discard();
        super.setChanged();
    }

    public void addAnt(CompoundTag nbtCompound, int ticksInDwelling) {
        assert this.level != null;
        this.ants.add(new Ant(nbtCompound, ticksInDwelling, 1200));
    }

    private static boolean releaseAnt(Level world, BlockPos pos, BlockState state, Ant ant, @Nullable List<Entity> entities, AntState antState) {
        if ((world.isNight() || world.isRaining()) && antState != AntState.EMERGENCY) {
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
                if (antState == AntState.RESOURCE_DELIVERED) {
                    ((com.ninni.spawn.entity.Ant) newAnt).setHasResource(false);
                    int i = state.getValue(AnthillBlock.RESOURCE_LEVEL);
                    if (state.getBlock() instanceof AnthillBlock && i < 5) {
                        world.setBlockAndUpdate(pos, state.setValue(AnthillBlock.RESOURCE_LEVEL, i + 1));
                    }
                }
                AnthillBlockEntity.ageAnt(Ant.ticksInAnthill, releasedAnt);
                if (entities != null) entities.add(releasedAnt);
                double x = (double)pos.getX() + 0.5;
                double y = (double)pos.getY() + 1;
                double z = (double)pos.getZ() + 0.5;
                newAnt.moveTo(x, y, z, newAnt.getYRot(), newAnt.getXRot());
            } else return false;
            //world.playSound(null, pos, SpeciesSoundEvents.BLOCK_BIRT_DWELLING_EXIT, SoundSource.BLOCKS, 1.0f, 1.0f);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newAnt, world.getBlockState(pos)));
            if (world instanceof ServerLevel serverLevel) {
                releasedAnt.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(releasedAnt.blockPosition()), MobSpawnType.EVENT, null, nbtCompound);
            }
            return world.addFreshEntity(newAnt);
        }
        return false;
    }

    static void removeIrrelevantNbtKeys(CompoundTag compound) {
        for (String string : IRRELEVANT_ANT_NBT_KEYS) compound.remove(string);
    }

    private static void ageAnt(int ticks, com.ninni.spawn.entity.Ant ant) {
        int i = ant.getAge();
        if (i < 0) ant.setAge(Math.min(0, i + ticks));
        else if (i > 0) ant.setAge(Math.max(0, i - ticks));
    }

    private static void tickAnts(Level world, BlockPos pos, BlockState state, List<Ant> ants) {
        boolean bl = false;
        Iterator<Ant> iterator = ants.iterator();
        while (iterator.hasNext()) {
            Ant ant = iterator.next();
            if (Ant.ticksInAnthill > ant.minOccupationTicks) {
                ItemStack compoundStack = ItemStack.of(ant.entityData.getCompound("Inventory"));
                AntState antState = compoundStack != ItemStack.EMPTY ? AntState.RESOURCE_DELIVERED : AntState.ANT_RELEASED;
                if (AnthillBlockEntity.releaseAnt(world, pos, state, ant, null, antState)) {
                    bl = true;
                    iterator.remove();
                }
            }
            ++Ant.ticksInAnthill;
        }
        if (bl) AnthillBlockEntity.setChanged(world, pos, state);
    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, AnthillBlockEntity blockEntity) {
        AnthillBlockEntity.tickAnts(world, pos, state, blockEntity.ants);
        if (!blockEntity.ants.isEmpty() && world.getRandom().nextDouble() < 0.005) {
            double d = (double)pos.getX() + 0.5;
            double e = pos.getY();
            double f = (double)pos.getZ() + 0.5;
            //world.playSound(null, d, e, f, SpeciesSoundEvents.BLOCK_BIRT_DWELLING_WORK, SoundSource.BLOCKS, 1.0f, 1.0f);
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
        this.day = nbt.getInt("Day");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put(ANTS_KEY, this.getAnts());
        nbt.putInt("Day", this.day);
    }

    public ListTag getAnts() {
        ListTag nbtList = new ListTag();
        for (Ant ant : this.ants) {
            CompoundTag nbtCompound = ant.entityData.copy();
            nbtCompound.remove("UUID");
            CompoundTag nbtCompound2 = new CompoundTag();
            nbtCompound2.put(ENTITY_DATA_KEY, nbtCompound);
            nbtCompound2.putInt(IRRELEVANT_ANT_NBT_KEYS.toString(), Ant.ticksInAnthill);
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
