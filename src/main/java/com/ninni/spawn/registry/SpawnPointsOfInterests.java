package com.ninni.spawn.registry;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

import static com.ninni.spawn.Spawn.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnPointsOfInterests {

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, MOD_ID);

    public static final RegistryObject<PoiType> ANTHILL = POI_TYPES.register("anthill", () -> new PoiType(getAllStatesOf(SpawnBlocks.ANTHILL.get()), 0, 1));
    public static final RegistryObject<PoiType> ROTTEN_LOG_ANTHILL = POI_TYPES.register("rotten_log_anthill", () -> new PoiType(getAllStatesOf(SpawnBlocks.ROTTEN_LOG_ANTHILL.get()), 0, 1));
    public static final RegistryObject<PoiType> ANT_FARM = POI_TYPES.register("ant_farm", () -> new PoiType(getAllStatesOf(SpawnBlocks.ANT_FARM.get()), 0, 1));

    private static Set<BlockState> getAllStatesOf(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

}
