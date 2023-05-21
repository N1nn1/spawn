package com.ninni.spawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.spawn.entity.Snail;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import static net.minecraft.client.model.geom.PartNames.*;

@Environment(value= EnvType.CLIENT)
public class SnailModel extends EntityModel<Snail> {
    private static final String FOOT = "foot";
    private static final String WHISKERS = "whiskers";
    private static final String SHELL = "shell";

    private final ModelPart shell;
    private final ModelPart foot;
    private final ModelPart whiskers;
    private final ModelPart leftEye;
    private final ModelPart rightEye;

    public SnailModel(ModelPart root) {
        this.shell = root.getChild(SHELL);
        this.foot = root.getChild(FOOT);

        this.whiskers = foot.getChild(WHISKERS);
        this.leftEye = foot.getChild(LEFT_EYE);
        this.rightEye = foot.getChild(RIGHT_EYE);
    }

    @SuppressWarnings("unused")
    public static LayerDefinition getLayerDefinition() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        
        PartDefinition shell = modelPartData.addOrReplaceChild(
                SHELL,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, -7.0F, 0.0F, 10.0F, 10.0F, 10.0F)
                        .texOffs(0, 20)
                        .addBox(-4.0F, -2.0F, -2.0F, 8.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 19.0F, -1.0F)
        );

        PartDefinition foot = modelPartData.addOrReplaceChild(
                FOOT,
                CubeListBuilder.create()
                        .texOffs(28, 8)
                        .addBox(-3.0F, -5.0F, -6.0F, 6.0F, 5.0F, 12.0F), 
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        PartDefinition whiskers = foot.addOrReplaceChild(
                WHISKERS, 
                CubeListBuilder.create()
                        .texOffs(8, 24)
                        .addBox(-2.0F, 0.0F, -1.0F, 4.0F, 0.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, -4.0F, -6.0F, 0.7418F, 0.0F, 0.0F)
        );

        PartDefinition leftEye = foot.addOrReplaceChild(
                LEFT_EYE,
                CubeListBuilder.create()
                        .texOffs(12, 26)
                        .addBox(-0.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F)
                        .texOffs(0, 24)
                        .addBox(-1.5F, -6.0F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(2.0F, -5.0F, -4.5F)
        );

        PartDefinition rightEye = foot.addOrReplaceChild(
                RIGHT_EYE,
                CubeListBuilder.create()
                        .texOffs(12, 26)
                        .mirror()
                        .addBox(-0.5F, -3.0F, -0.5F, 1.0F, 4.0F, 1.0F)
                        .mirror(false)
                        .texOffs(0, 24)
                        .mirror()
                        .addBox(-1.5F, -6.0F, -1.5F, 3.0F, 3.0F, 3.0F)
                        .mirror(false),
                PartPose.offset(-2.0F, -5.0F, -4.5F)
        );
       
        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public void setupAnim(Snail entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        float pi = (float)Math.PI;

        float speed = 1;
        float degree = 1;
        float tilt = Math.min(limbSwingAmount * 2, 0.5f);

        shell.visible = entity.getShellGrowthTicks() == 0;

        //eye looking direction
        leftEye.xRot = headPitch * pi/180;
        leftEye.yRot = headYaw * pi/180;
        rightEye.xRot = headPitch * pi/180;
        rightEye.yRot = headYaw * pi/180;

        //random eye rotation
        leftEye.zRot = Mth.sin(ageInTicks * speed * 0.05F) * degree * 0.1F;
        leftEye.xRot += Mth.cos(ageInTicks * speed * 0.025F) * degree * 0.2F;
        leftEye.y = Mth.cos(ageInTicks * speed * 0.025F + pi/2) * degree - 5.0F;
        rightEye.zRot = Mth.sin(ageInTicks * speed * 0.05F + pi) * degree * 0.1F;
        rightEye.xRot += Mth.cos(ageInTicks * speed * 0.025F + pi) * degree * 0.2F;
        rightEye.y = Mth.cos(ageInTicks * speed * 0.025F - pi/2) * degree - 5.0F;

        //body scaling along when the snail slithers
        foot.zScale = 1 + tilt;
        leftEye.zScale = 1 - tilt;
        rightEye.zScale = 1 - tilt;

        //random whiskers rotation
        whiskers.xRot = Mth.sin(ageInTicks * speed * 0.05F + pi/2) * degree * 0.5F + pi/4;

        //retreating in its shell
        if (entity.isScared()) {
            this.foot.visible = false;
            if (entity.isBaby()) this.shell.y = 23.0F;
            else this.shell.y = 21.0F;
            this.shell.z = -4.0F;
            this.shell.zRot = 0.0F;
        } else {
            shell.zRot = Mth.sin(limbSwing * speed * 0.6F) * degree * -0.5F * limbSwingAmount;
            this.foot.visible = true;
            this.shell.y = 19.0F;
            this.shell.z = -1.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (young) {
            poseStack.pushPose();
            poseStack.translate(0.0, 1.0, 0.05);
            poseStack.scale(0.31F, 0.31F, 0.31F);
            shell.render(poseStack, vertexConsumer, light, overlay, red, green, blue, alpha);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(0.0, 1.5F, 0.0);
            foot.render(poseStack, vertexConsumer, light, overlay, red, green, blue, alpha);
            poseStack.popPose();
        } else {
            shell.render(poseStack, vertexConsumer, light, overlay, red, green, blue, alpha);
            foot.render(poseStack, vertexConsumer, light, overlay, red, green, blue, alpha);
        }

    }
}