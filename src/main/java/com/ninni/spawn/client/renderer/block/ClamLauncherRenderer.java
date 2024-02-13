package com.ninni.spawn.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ninni.spawn.Spawn;
import com.ninni.spawn.block.ClamLauncherBlock;
import com.ninni.spawn.block.entity.ClamLauncherBlockEntity;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class ClamLauncherRenderer implements BlockEntityRenderer<ClamLauncherBlockEntity> {
    public static final Material SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(Spawn.MOD_ID, "entity/clam/clam_launcher/clam_launcher"));
    private final ModelPart bottom;
    private final ModelPart top;


    public ClamLauncherRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(SpawnEntityModelLayers.CLAM_LAUNCHER);
        this.bottom = modelPart.getChild("bottom");
        this.top = modelPart.getChild("top");
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "bottom",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 14.0F, new CubeDeformation(-0.01F))
                        .texOffs(0, 32)
                        .addBox(-3.0F, -2.0F, 6.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F)),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        partdefinition.addOrReplaceChild(
                "top",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-8.0F, -2.0F, -14.0F, 16.0F, 2.0F, 14.0F),
                PartPose.offset(0.0F, 23.0F, 6.0F)
        );

        return LayerDefinition.create(meshdefinition, 64, 48);
    }

    @Override
    public void render(ClamLauncherBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        //variables
        VertexConsumer vertexConsumer = SHELL_TEXTURE.buffer(multiBufferSource, RenderType::entityCutoutNoCull);

        //position the block correctly
        poseStack.translate(0.5f, 1.5f, 0.5f);
        Direction d = blockEntity.getBlockState().getValue(ClamLauncherBlock.FACING);
        float g = blockEntity.getBlockState().getValue(ClamLauncherBlock.FACING).toYRot();
        if (d == Direction.NORTH) poseStack.mulPose(Axis.YP.rotationDegrees(0));
        else if (d == Direction.SOUTH) poseStack.mulPose(Axis.YP.rotationDegrees(180));
        else poseStack.mulPose(Axis.YP.rotationDegrees(g));
        poseStack.mulPose(new Quaternionf().rotateZ((float)Math.PI));

        //render top
        poseStack.pushPose();

        if (blockEntity.getBlockState().getValue(ClamLauncherBlock.POWERED)) {
            poseStack.translate(0.0f, 1.4f, 0.35f);
            poseStack.mulPose(new Quaternionf().rotateX(-(Mth.lerp(blockEntity.activeRotation, blockEntity.activeRotation, 1f))));
            poseStack.translate(0.0f, -1.4f, -0.35f);

        }

        this.top.render(poseStack, vertexConsumer, i, j);

        poseStack.popPose();


        //render bottom
        this.bottom.render(poseStack, vertexConsumer, i, j);
    }
}
