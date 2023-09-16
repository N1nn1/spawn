package com.ninni.spawn.registry;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.Set;
import java.util.stream.Stream;

public record SpawnWoodType(String name, BlockSetType setType, SoundType soundType, SoundType hangingSignSoundType, SoundEvent fenceGateClose, SoundEvent fenceGateOpen) {
    private static final Set<net.minecraft.world.level.block.state.properties.WoodType> VALUES = new ObjectArraySet<>();

    public static final WoodType ROTTEN = register(new WoodType("rotten", SpawnBlockSetType.ROTTEN, SpawnSoundEvents.ROTTEN_WOOD, SoundType.EMPTY, SpawnSoundEvents.ROTTEN_WOOD_FENCE_GATE_CLOSE, SpawnSoundEvents.ROTTEN_WOOD_FENCE_GATE_OPEN));


    private static net.minecraft.world.level.block.state.properties.WoodType register(net.minecraft.world.level.block.state.properties.WoodType woodType) {
        VALUES.add(woodType);
        return woodType;
    }

    public static Stream<net.minecraft.world.level.block.state.properties.WoodType> values() {
        return VALUES.stream();
    }
}

