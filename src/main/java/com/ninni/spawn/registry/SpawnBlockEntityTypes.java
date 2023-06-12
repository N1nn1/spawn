package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.block.entity.AnthillBlockEntity;
import com.ninni.spawn.block.entity.SunflowerBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SpawnBlockEntityTypes {

    public static final BlockEntityType<SunflowerBlockEntity> SUNFLOWER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Spawn.MOD_ID, "sunflower"),
            BlockEntityType.Builder.of(SunflowerBlockEntity::new,
                    SpawnBlocks.SUNFLOWER
            ).build(null)
    );

    public static final BlockEntityType<AnthillBlockEntity> ANTHILL = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Spawn.MOD_ID, "anthill"),
            BlockEntityType.Builder.of(AnthillBlockEntity::new,
                    SpawnBlocks.ANTHILL,
                    SpawnBlocks.ROTTEN_LOG_ANTHILL
            ).build(null)
    );

}
