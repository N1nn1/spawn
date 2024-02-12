package com.ninni.spawn.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.spawn.Spawn;
import com.ninni.spawn.block.PigmentShifterBlock;
import com.ninni.spawn.block.entity.PigmentShifterBlockEntity;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.minecraft.client.Camera;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PigmentShifterRenderer implements BlockEntityRenderer<PigmentShifterBlockEntity> {
    public static final Material SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(Spawn.MOD_ID, "entity/clam/pigment_shifter/pigment_shifter"));
    private final ModelPart bottom;
    private final ModelPart top;
    private final ModelPart eye;
    private final BlockEntityRenderDispatcher renderer;


    public PigmentShifterRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(SpawnEntityModelLayers.PIGMENT_SHIFTER);
        this.renderer = context.getBlockEntityRenderDispatcher();
        this.bottom = modelPart.getChild("bottom");
        this.top = modelPart.getChild("top");
        this.eye = modelPart.getChild("eye");
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "bottom",
                CubeListBuilder.create()
                        .texOffs(0, 42)
                        .addBox(-8.0F, -6.0F, -8.0F, 16.0F, 6.0F, 16.0F)
                        .texOffs(0, 17)
                        .addBox(-5.0F, -10.0F, -5.0F, 10.0F, 4.0F, 10.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        partdefinition.addOrReplaceChild(
                "eye",
                CubeListBuilder.create()
                        .texOffs(0, 31)
                        .addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F),
                PartPose.offset(0.0F, 13.0F, 0.0F)
        );

        partdefinition.addOrReplaceChild(
                "top",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, -7.0F, -5.0F, 10.0F, 7.0F, 10.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void render(PigmentShifterBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        //variables
        VertexConsumer vertexConsumer = SHELL_TEXTURE.buffer(multiBufferSource, RenderType::entityCutoutNoCull);
        float g = (float)blockEntity.tickCount + f;
        float h = blockEntity.getActiveRotation(f) * 57.295776f;
        float k = Mth.sin(g * 0.1f) / 2.0f + 0.3f;
        k = k * k + k;
        float l = Mth.cos(g * 0.1f) / 2.0f + 0.3f;
        l = l * l + l;
        Camera camera = this.renderer.camera;

        //position the block correctly
        poseStack.translate(0.5f, 1.5f, 0.5f);
        poseStack.mulPose(new Quaternionf().rotateZ((float)Math.PI));

        //render top
        poseStack.pushPose();
        if (blockEntity.getBlockState().getValue(PigmentShifterBlock.WATERLOGGED)) {
            poseStack.translate(0.0f, -0.6f + k * 0.1f, 0.0f);
            Vector3f vector3f = new Vector3f(0.0f, 1.0f, 0.0f).normalize();
            poseStack.mulPose(new Quaternionf().rotationAxis(h * ((float)Math.PI / 180), vector3f));
        }
        this.top.render(poseStack, vertexConsumer, i, j);
        poseStack.popPose();

        //render eye
        if (blockEntity.getBlockState().getValue(PigmentShifterBlock.WATERLOGGED)) {
            poseStack.pushPose();
            poseStack.scale(0.7f, 0.7f, 0.7f);
            poseStack.translate(0.0f, 1f + l * 0.1f, 0.0f);
            poseStack.mulPose(new Quaternionf().rotationYXZ(camera.getYRot() * ((float) Math.PI / 180), -camera.getXRot() * ((float) Math.PI / 180), 0));
            poseStack.translate(0.0f, -0.85f, 0.0f);
            this.eye.render(poseStack, vertexConsumer, i, j);
            poseStack.popPose();
        }

        //render bottom
        this.bottom.render(poseStack, vertexConsumer, i, j);
    }
}
