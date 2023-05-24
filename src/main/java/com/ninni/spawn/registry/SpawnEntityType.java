package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.entity.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;

public class SpawnEntityType {



    public static final EntityType<AnglerFish> ANGLER_FISH = register(
            "angler_fish",
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(AnglerFish::new)
                    .defaultAttributes(AnglerFish::createAttributes)
                    .spawnGroup(MobCategory.WATER_AMBIENT)
                    .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
                    .dimensions(EntityDimensions.scalable(0.6F, 0.6F))
                    .trackRangeChunks(10)
    );

    public static final EntityType<Tuna> TUNA = register(
            "tuna",
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(Tuna::new)
                    .defaultAttributes(Tuna::createAttributes)
                    .spawnGroup(MobCategory.WATER_CREATURE)
                    .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Tuna::checkSurfaceWaterAnimalSpawnRules)
                    .dimensions(EntityDimensions.scalable(1.2F, 0.8F))
    );

    public static final EntityType<TunaEgg> TUNA_EGG = register(
            "tuna_egg",
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(TunaEgg::new)
                    .defaultAttributes(TunaEgg::createAttributes)
                    .spawnGroup(MobCategory.MISC)
                    .dimensions(EntityDimensions.fixed(0.15f, 0.15f))
                    .trackRangeChunks(10)
    );

    public static final EntityType<Seahorse> SEAHORSE = register(
            "seahorse",
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(Seahorse::new)
                    .defaultAttributes(Seahorse::createAttributes)
                    .spawnGroup(MobCategory.WATER_AMBIENT)
                    .spawnRestriction(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
                    .dimensions(EntityDimensions.scalable(0.3F, 0.6F))
                    .trackRangeChunks(10)
    );

    public static final EntityType<Snail> SNAIL = register(
            "snail",
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(Snail::new)
                    .defaultAttributes(Snail::createAttributes)
                    .spawnGroup(MobCategory.CREATURE)
                    .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE_WG, Snail::canSpawn)
                    .dimensions(EntityDimensions.scalable(0.8F, 0.8F))
                    .trackRangeChunks(10)
    );

    public static final EntityType<Hamster> HAMSTER = register(
            "hamster",
            FabricEntityTypeBuilder.createMob()
                    .entityFactory(Hamster::new)
                    .defaultAttributes(Hamster::createAttributes)
                    .spawnGroup(MobCategory.CREATURE)
                    .spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE_WG, Hamster::canSpawn)
                    .dimensions(EntityDimensions.scalable(0.6F, 0.5F))
                    .trackRangeChunks(10)
    );

    static {
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.ANGLER_FISH_SPAWNS), MobCategory.WATER_AMBIENT, SpawnEntityType.ANGLER_FISH, 5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.TUNA_SPAWNS), MobCategory.WATER_CREATURE, SpawnEntityType.TUNA, 15, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.SEAHORSE_SPAWNS), MobCategory.WATER_AMBIENT, SpawnEntityType.SEAHORSE, 20, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.SNAIL_SPAWNS), MobCategory.CREATURE, SpawnEntityType.SNAIL, 12, 1, 3);
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Spawn.MOD_ID, id), entityType.build());
    }
}
