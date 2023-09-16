package com.ninni.spawn.registry;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.Set;
import java.util.stream.Stream;

public record SpawnBlockSetType(String name, boolean canOpenByHand, SoundType soundType, SoundEvent doorClose, SoundEvent doorOpen, SoundEvent trapdoorClose, SoundEvent trapdoorOpen, SoundEvent pressurePlateClickOff, SoundEvent pressurePlateClickOn, SoundEvent buttonClickOff, SoundEvent buttonClickOn) {
    private static final Set<BlockSetType> VALUES = new ObjectArraySet<>();

    public static final BlockSetType ROTTEN = register(new BlockSetType("rotten", true, SpawnSoundEvents.ROTTEN_WOOD, SpawnSoundEvents.ROTTEN_WOOD_DOOR_CLOSE, SpawnSoundEvents.ROTTEN_WOOD_DOOR_OPEN, SpawnSoundEvents.ROTTEN_WOOD_TRAPDOOR_CLOSE, SpawnSoundEvents.ROTTEN_WOOD_TRAPDOOR_OPEN, SoundEvents.EMPTY, SoundEvents.EMPTY, SoundEvents.EMPTY, SoundEvents.EMPTY));

    private static BlockSetType register(BlockSetType blockSetType) {
        VALUES.add(blockSetType);
        return blockSetType;
    }

    public static Stream<BlockSetType> values() {
        return VALUES.stream();
    }
}

