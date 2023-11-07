package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.entity.AnglerFish;
import com.ninni.spawn.entity.Ant;
import com.ninni.spawn.entity.Hamster;
import com.ninni.spawn.entity.Seahorse;
import com.ninni.spawn.entity.Snail;
import com.ninni.spawn.entity.Tuna;
import com.ninni.spawn.entity.TunaEgg;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Spawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnEntityType {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Spawn.MOD_ID);

    public static final RegistryObject<EntityType<AnglerFish>> ANGLER_FISH = register(
            "angler_fish",
            EntityType.Builder
                    .of(AnglerFish::new, MobCategory.WATER_AMBIENT)
                    .sized(0.6F, 0.6F)
                    .clientTrackingRange(10)
    );

    public static final RegistryObject<EntityType<Tuna>> TUNA = register(
            "tuna",
            EntityType.Builder
                    .of(Tuna::new, MobCategory.WATER_CREATURE)
                    .sized(1.2F, 0.8F)
    );

    public static final RegistryObject<EntityType<TunaEgg>> TUNA_EGG = register(
            "tuna_egg",
            EntityType.Builder
                    .of(TunaEgg::new, MobCategory.MISC)
                    .sized(0.15f, 0.15f)
                    .clientTrackingRange(10)
    );

    public static final RegistryObject<EntityType<Seahorse>> SEAHORSE = register(
            "seahorse",
            EntityType.Builder
                    .of(Seahorse::new, MobCategory.WATER_AMBIENT)
                    .sized(0.3F, 0.6F)
                    .clientTrackingRange(10)
    );

    public static final RegistryObject<EntityType<Snail>> SNAIL = register(
            "snail",
            EntityType.Builder
                    .of(Snail::new, MobCategory.CREATURE)
                    .sized(0.8F, 0.8F)
                    .clientTrackingRange(10)
    );

    public static final RegistryObject<EntityType<Hamster>> HAMSTER = register(
            "hamster",
            EntityType.Builder
                    .of(Hamster::new, MobCategory.CREATURE)
                    .sized(0.6F, 0.5F)
                    .clientTrackingRange(10)
    );

    public static final RegistryObject<EntityType<Ant>> ANT = register(
            "ant",
            EntityType.Builder
                    .of(Ant::new, MobCategory.CREATURE)
                    .sized(0.6F, 0.5F)
                    .clientTrackingRange(10)
    );

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, EntityType.Builder<T> entityType) {
        return ENTITY_TYPES.register(id, () -> entityType.build(new ResourceLocation(Spawn.MOD_ID, id).toString()));
    }
}
