package com.ninni.spawn.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.world.entity.monster.Spider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpiderRenderer.class)
@OnlyIn(Dist.CLIENT)
public abstract class SpiderRendererMixin<T extends Spider> extends MobRenderer<T, SpiderModel<T>> {

    public SpiderRendererMixin(EntityRendererProvider.Context context, SpiderModel<T> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Override
    protected void setupRotations(T spider, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(spider, poseStack, f, g, h);
        if (spider.isClimbing()) {
            poseStack.translate(0.0f, 0.25f, -0.25f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
        }
    }
}
