package com.ninni.spawn.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;

public class KrillParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;


    KrillParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteProvider) {
        super(world, x, y, z);
        this.hasPhysics = true;
        this.spriteProvider = spriteProvider;
        this.lifetime = world.random.nextInt(40) + 80;
        float amount = 40;
        this.quadSize *= 0.5f + random.nextInt(4)/2f;
        this.yd = random.nextInt(2) == 1 ? random.nextFloat()/amount : -(random.nextFloat()/amount);
        this.zd = random.nextInt(2) == 1 ? random.nextFloat()/amount : -(random.nextFloat()/amount);
        this.xd = random.nextInt(2) == 1 ? random.nextFloat()/amount : -(random.nextFloat()/amount);
        this.pickSprite(spriteProvider);
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

        if (!this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
            this.remove();
        }
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
            return new KrillParticle(clientWorld, d, e, f, this.spriteProvider);
        }
    }
}
