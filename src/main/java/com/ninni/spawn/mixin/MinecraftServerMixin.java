package com.ninni.spawn.mixin;

import com.ninni.spawn.mixin.accessor.NoiseGeneratorSettingsAccessor;
import com.ninni.spawn.registry.SpawnSurfaceRules;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow public abstract RegistryAccess.Frozen registryAccess();

    @Inject(method = "createLevels", at = @At("TAIL"))
    private void addSurfaceRules(ChunkProgressListener listener, CallbackInfo ci) {
        LevelStem levelStem = this.registryAccess().registryOrThrow(Registries.LEVEL_STEM).get(LevelStem.OVERWORLD);
        if (levelStem == null) {
            throw new NullPointerException("Overworld stem is null");
        }
        ChunkGenerator chunkGenerator = levelStem.generator();
        if (chunkGenerator instanceof NoiseBasedChunkGenerator noiseBasedChunkGenerator) {
            NoiseGeneratorSettings noiseGeneratorSettings = noiseBasedChunkGenerator.generatorSettings().value();
            ((NoiseGeneratorSettingsAccessor)(Object)noiseGeneratorSettings).setSurfaceRule(SurfaceRules.sequence(SpawnSurfaceRules.makeRules(), noiseGeneratorSettings.surfaceRule()));
        }
    }

}