package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ninni.spawn.client.model.HerringModel;
import com.ninni.spawn.entity.Herring;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class HerringRenderer extends MobRenderer<Herring, HerringModel<Herring>> {
    private static final ResourceLocation HERRING_LOCATION = new ResourceLocation(MOD_ID,"textures/entity/fish/herring.png");

    public HerringRenderer(EntityRendererProvider.Context context) {
        super(context, new HerringModel<>(context.bakeLayer(SpawnEntityModelLayers.HERRING)), 0.2f);
    }

    @Override
    public ResourceLocation getTextureLocation(Herring entity) {
        return HERRING_LOCATION;
    }

    @Override
    protected void setupRotations(Herring herring, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(herring, poseStack, f, g, h);
        float i = 4.3f * Mth.sin(0.6f * f);
        poseStack.mulPose(Axis.YP.rotationDegrees(i));
        if (!herring.isInWater()) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
        }
    }
}

