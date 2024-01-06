package com.ninni.spawn.mixin;

import com.mojang.datafixers.util.Pair;
import com.ninni.spawn.registry.SpawnBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
public abstract class OverworldBiomeBuilderMixin {

    @Shadow @Final private Climate.Parameter[] temperatures;
    @Shadow @Final private Climate.Parameter[] humidities;
    @Shadow protected abstract void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter parameter, Climate.Parameter parameter2, Climate.Parameter parameter3, Climate.Parameter parameter4, Climate.Parameter parameter5, float f, ResourceKey<Biome> resourceKey);
    @Shadow @Final private Climate.Parameter[] erosions;
    @Shadow @Final private Climate.Parameter nearInlandContinentalness;
    @Shadow @Final private Climate.Parameter FULL_RANGE;
    private final Climate.Parameter seagrassMeadowTemperature = Climate.Parameter.span(0.24f, 0.35f);
    private final Climate.Parameter seagrassMeadowContinentalness = Climate.Parameter.span(-0.355f, -0.17f);

    @Inject(at = @At("TAIL"), method = "addOffCoastBiomes")
    private void S$addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, CallbackInfo ci) {
        //TODO why the FUCK does this not work
        //this.addSurfaceBiome(consumer, seagrassMeadowTemperature, FULL_RANGE, seagrassMeadowContinentalness, FULL_RANGE, FULL_RANGE, 0.0f, SpawnBiomes.SEAGRASS_MEADOW);
    }

    @Inject(at = @At("RETURN"), method = "addMidSlice")
    private void S$addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter parameter, CallbackInfo ci) {
        for (int i = 0; i < this.temperatures.length; i++) {
            Climate.Parameter parameter2 = this.temperatures[i];
            for (int j = 0; j < this.humidities.length; j++) {
                Climate.Parameter parameter3 = this.humidities[j];
                if (i == 3 && j == 2) {
                    ResourceKey<Biome> resourceKey = SpawnBiomes.ANT_GARDENS;
                    this.addSurfaceBiome(consumer, parameter2, parameter3, this.nearInlandContinentalness, this.erosions[3], parameter, 0.0F, resourceKey);
                }
            }
        }
    }

}
