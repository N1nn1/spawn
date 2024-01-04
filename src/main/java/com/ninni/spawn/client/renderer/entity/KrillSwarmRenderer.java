package com.ninni.spawn.client.renderer.entity;

import com.ninni.spawn.entity.KrillSwarm;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

@Environment(value=EnvType.CLIENT)
public class KrillSwarmRenderer<T extends LivingEntity> extends EntityRenderer<KrillSwarm> {

    public KrillSwarmRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0;
        this.shadowStrength = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(KrillSwarm entity) {
        return null;
    }


}
