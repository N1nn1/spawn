package com.ninni.spawn.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record AnthillConfig(float anthill_chance) implements FeatureConfiguration {
    public static final Codec<AnthillConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("anthill_chance").forGetter(anthillConfig -> anthillConfig.anthill_chance)).apply(instance, AnthillConfig::new));
}
