package com.ninni.spawn.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ninni.spawn.Spawn;
import com.ninni.spawn.level.FeatureBasedBiomeModifier;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Spawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnBiomeModifiers {

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Spawn.MOD_ID);

    public static final RegistryObject<Codec<FeatureBasedBiomeModifier>> FEATURE_BASED_BIOME_MODIFIER = BIOME_MODIFIERS.register("feature_based_biome_modifier", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    PlacedFeature.LIST_CODEC.fieldOf("old_features").forGetter(FeatureBasedBiomeModifier::oldFeatures),
                    PlacedFeature.LIST_CODEC.fieldOf("new_features").forGetter(FeatureBasedBiomeModifier::newFeatures)
            ).apply(builder, FeatureBasedBiomeModifier::new))
    );

}
