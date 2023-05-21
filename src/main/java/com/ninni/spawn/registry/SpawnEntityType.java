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
import net.minecraft.world.level.levelgen.Heightmap;

public class SpawnEntityType {

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

    static {
        BiomeModifications.addSpawn(BiomeSelectors.tag(SpawnTags.SNAIL_SPAWNS), MobCategory.CREATURE, SpawnEntityType.SNAIL, 12, 1, 3);
    }

    private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Spawn.MOD_ID, id), entityType.build());
    }
}
