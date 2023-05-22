package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ninni.spawn.client.model.SpawnEntityModelLayers;
import com.ninni.spawn.client.model.TunaModel;
import com.ninni.spawn.entity.Tuna;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(value=EnvType.CLIENT)
public class TunaRenderer<T extends LivingEntity> extends MobRenderer<Tuna, TunaModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/tuna/tuna.png");

    public TunaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new TunaModel(ctx.bakeLayer(SpawnEntityModelLayers.TUNA)), 0.8F);
    }

    @Override
    protected void setupRotations(Tuna tuna, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(tuna, poseStack, f, g, h);
        float i = 1.0f;
        float j = 1.0f;
        if (!tuna.isInWater()) {
            i = 1.3f;
            j = 1.7f;
        }
        //TODO
        float k = i * 4.3f * Mth.sin(j * 0.6f * f);
        poseStack.mulPose(Axis.YP.rotationDegrees(k));
        if (!tuna.isInWater()) {
            poseStack.translate(0.2f, 0.1f, 0.0f);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Tuna entity) {
        return  TEXTURE;
    }
}
