package com.ninni.spawn.level;

import com.mojang.serialization.Codec;
import com.ninni.spawn.registry.SpawnBiomeModifiers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import java.util.List;

public record FeatureBasedBiomeModifier(HolderSet<PlacedFeature> oldFeatures, HolderSet<PlacedFeature> newFeatures) implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.MODIFY) {
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            BiomeGenerationSettings biomeGenerationSettings = generationSettings.build();
            List<HolderSet<PlacedFeature>> features = biomeGenerationSettings.features();

            for (HolderSet<PlacedFeature> featureSuppliers : features) {
                for (Holder<PlacedFeature> featureSupplier : featureSuppliers) {
                    if (this.oldFeatures.contains(featureSupplier)) {
                        generationSettings.getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION).removeIf(this.oldFeatures::contains);
                        this.newFeatures.forEach(placedFeatureHolder -> {
                            generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placedFeatureHolder);
                        });
                    }
                }
            }

        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SpawnBiomeModifiers.FEATURE_BASED_BIOME_MODIFIER.get();
    }
}
