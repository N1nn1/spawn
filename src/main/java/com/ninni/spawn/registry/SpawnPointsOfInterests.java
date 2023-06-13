package com.ninni.spawn.registry;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

import static com.ninni.spawn.Spawn.MOD_ID;

public class SpawnPointsOfInterests {

    public static final PoiType ANTHILL = register("anthill", getAllStatesOf(SpawnBlocks.ANTHILL), 0, 1);
    public static final PoiType ROTTEN_LOG_ANTHILL = register("rotten_log_anthill", getAllStatesOf(SpawnBlocks.ROTTEN_LOG_ANTHILL), 0, 1);
    public static final PoiType ANT_FARM = register("ant_farm", getAllStatesOf(SpawnBlocks.ANT_FARM), 0, 1);

    private static Set<BlockState> getAllStatesOf(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

    private static PoiType register(String id, Set<BlockState> workStationStates, int ticketCount, int searchDistance) {
        return PointOfInterestHelper.register(new ResourceLocation(MOD_ID, id), ticketCount, searchDistance, workStationStates);
    }
}
