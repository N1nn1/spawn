package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.block.entity.*;
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
                    SpawnBlocks.ANT_FARM,
                    SpawnBlocks.ROTTEN_LOG_ANTHILL
            ).build(null)
    );

    public static final BlockEntityType<WhaleUvulaBlockEntity> WHALE_UVULA = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Spawn.MOD_ID, "whale_uvula"),
            BlockEntityType.Builder.of(WhaleUvulaBlockEntity::new,
                    SpawnBlocks.WHALE_UVULA
            ).build(null)
    );

    public static final BlockEntityType<PigmentShifterBlockEntity> PIGMENT_SHIFTER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Spawn.MOD_ID, "pigment_shifter"),
            BlockEntityType.Builder.of(PigmentShifterBlockEntity::new,
                    SpawnBlocks.PIGMENT_SHIFTER
            ).build(null)
    );

    public static final BlockEntityType<ClamLauncherBlockEntity> CLAM_LAUNCHER = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Spawn.MOD_ID, "clam_launcher"),
            BlockEntityType.Builder.of(ClamLauncherBlockEntity::new,
                    SpawnBlocks.CLAM_LAUNCHER
            ).build(null)
    );
}
