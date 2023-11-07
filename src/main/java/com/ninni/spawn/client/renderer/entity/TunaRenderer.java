package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ninni.spawn.client.model.TunaModel;
import com.ninni.spawn.entity.Tuna;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.ninni.spawn.Spawn.MOD_ID;

@OnlyIn(Dist.CLIENT)
public class TunaRenderer<T extends LivingEntity> extends MobRenderer<Tuna, TunaModel> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/entity/tuna/tuna.png");

    public TunaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new TunaModel(ctx.bakeLayer(SpawnEntityModelLayers.TUNA)), 0.8F);
    }

    @Override
    protected void setupRotations(Tuna tuna, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(tuna, poseStack, f, g, h);
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
