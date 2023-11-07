package com.ninni.spawn.events;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.entity.AnglerFish;
import com.ninni.spawn.entity.Ant;
import com.ninni.spawn.entity.Hamster;
import com.ninni.spawn.entity.Seahorse;
import com.ninni.spawn.entity.Snail;
import com.ninni.spawn.entity.Tuna;
import com.ninni.spawn.entity.TunaEgg;
import com.ninni.spawn.registry.SpawnEntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Spawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobEvents {

    @SubscribeEvent
    public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
        event.put(SpawnEntityType.ANGLER_FISH.get(), AnglerFish.createAttributes().build());
        event.put(SpawnEntityType.TUNA.get(), Tuna.createAttributes().build());
        event.put(SpawnEntityType.TUNA_EGG.get(), TunaEgg.createAttributes().build());
        event.put(SpawnEntityType.SEAHORSE.get(), Seahorse.createAttributes().build());
        event.put(SpawnEntityType.SNAIL.get(), Snail.createAttributes().build());
        event.put(SpawnEntityType.HAMSTER.get(), Hamster.createAttributes().build());
        event.put(SpawnEntityType.ANT.get(), Ant.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(SpawnEntityType.ANGLER_FISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(SpawnEntityType.TUNA.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Tuna::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(SpawnEntityType.SEAHORSE.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(SpawnEntityType.SNAIL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE_WG, Snail::canSpawn, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(SpawnEntityType.HAMSTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE_WG, Hamster::canSpawn, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(SpawnEntityType.ANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE_WG, Ant::canSpawn, SpawnPlacementRegisterEvent.Operation.AND);
    }

}
