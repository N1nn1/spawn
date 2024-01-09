package com.ninni.spawn.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class SandCloudParticle extends TextureSheetParticle {

    SandCloudParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, boolean bl) {
        super(clientLevel, d, e, f);
        this.scale(3.0f);
        this.setSize(0.25f, 0.25f);
        this.lifetime = this.random.nextInt(50) + 80;
        this.gravity = -0.0005f;
        this.xd = g;
        this.yd = h + (double)(this.random.nextFloat() / 500.0f);
        this.zd = i;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime || this.alpha <= 0.0f) {
            this.remove();
            return;
        }
        this.xd += (this.random.nextFloat() / 5000.0f * (float)(this.random.nextBoolean() ? 1 : -1));
        this.zd += (this.random.nextFloat() / 5000.0f * (float)(this.random.nextBoolean() ? 1 : -1));
        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);
        if (this.age >= this.lifetime - 60 && this.alpha > 0.01f) {
            this.alpha -= 0.015f;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteSet spriteProvider) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            SandCloudParticle sandCloudParticle = new SandCloudParticle(clientWorld, d, e, f, g, h, i, false);
            sandCloudParticle.setAlpha(0.8f);
            sandCloudParticle.pickSprite(this.spriteProvider);
            return sandCloudParticle;
        }
    }
}

