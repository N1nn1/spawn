package com.ninni.spawn.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;

public class TunaEggParticle  extends TextureSheetParticle {
    private final SpriteSet spriteProvider;


    TunaEggParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteProvider) {
        super(world, x, y, z);
        this.spriteProvider = spriteProvider;
        this.gravity = 0.5F;
        this.hasPhysics = true;
        this.lifetime = 60;
        this.zd = random.nextInt(2) == 1 ? random.nextFloat()/20 : -(random.nextFloat()/20);
        this.xd = random.nextInt(2) == 1 ? random.nextFloat()/20 : -(random.nextFloat()/20);
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        if (age < lifetime) {
            if (alpha > 0.1F) {
                alpha -= 0.015F;
            } else {
                this.remove();
            }
        }
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected int getLightColor(float f) {
        BlockPos blockPos = new BlockPos((int)this.x, (int)this.y, (int)this.z);
        if (this.level.hasChunkAt(blockPos)) {
            return LevelRenderer.getLightColor(this.level, blockPos);
        }
        return 0;
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteSet spriteProvider) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new TunaEggParticle(clientWorld, d, e, f, this.spriteProvider);
        }
    }
}
