package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.block.entity.AnthillBlockEntity;
import com.ninni.spawn.block.entity.SunflowerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Spawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Spawn.MOD_ID);

    public static final RegistryObject<BlockEntityType<SunflowerBlockEntity>> SUNFLOWER = BLOCK_ENTITY_TYPES.register("sunflower", () -> BlockEntityType.Builder.of(SunflowerBlockEntity::new, SpawnBlocks.SUNFLOWER.get()).build(null));
    public static final RegistryObject<BlockEntityType<AnthillBlockEntity>> ANTHILL = BLOCK_ENTITY_TYPES.register("anthill", () -> BlockEntityType.Builder.of(AnthillBlockEntity::new, SpawnBlocks.ANTHILL.get(), SpawnBlocks.ANT_FARM.get(), SpawnBlocks.ROTTEN_LOG_ANTHILL.get()).build(null));


}
