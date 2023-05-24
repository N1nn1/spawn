package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import com.ninni.spawn.client.model.TunaEggModel;
import com.ninni.spawn.entity.TunaEgg;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class TunaEggRenderer extends MobRenderer<TunaEgg, TunaEggModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/tuna/tuna_egg.png");

    public TunaEggRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new TunaEggModel(ctx.bakeLayer(SpawnEntityModelLayers.TUNA_EGG)), 0.2F);
    }

    @Override
    protected void setupRotations(TunaEgg egg, PoseStack poseStack, float f, float g, float h) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - g));
        float i = (float)(egg.level.getGameTime() - egg.lastHit) + h;
        if (i < 5.0f) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(i / 1.5f * (float)Math.PI) * 3.0f));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(TunaEgg entity) {
        return  TEXTURE;
    }
}
