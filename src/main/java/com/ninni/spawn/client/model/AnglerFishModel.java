package com.ninni.spawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.spawn.entity.AnglerFish;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import static net.minecraft.client.model.geom.PartNames.*;

@Environment(EnvType.CLIENT)
public class AnglerFishModel<E extends AnglerFish> extends EntityModel<E> {
    public static final String LANTERN = "lantern";

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart lantern;
    private final ModelPart jaw;
    private final ModelPart leftFin;
    private final ModelPart rightFin;

    public AnglerFishModel(ModelPart root) {
        this.root = root;

        this.body = this.root.getChild(BODY);

        this.tail = this.body.getChild(TAIL);
        this.lantern = this.body.getChild(LANTERN);
        this.jaw = this.body.getChild(JAW);
        this.leftFin = this.body.getChild(LEFT_FIN);
        this.rightFin = this.body.getChild(RIGHT_FIN);
    }

    @SuppressWarnings("unused")
    public static LayerDefinition getLayerDefinition() {
        MeshDefinition data = new MeshDefinition();
        PartDefinition root = data.getRoot();

        PartDefinition body = root.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -4.0F, -4.5F, 7.0F, 7.0F, 9.0F),
                PartPose.offset(0.5F, 19.0F, -1.0F)
        );

        PartDefinition tail = body.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 4.0F),
                PartPose.offset(-0.5F, 0.5F, 4.5F)
        );

        PartDefinition lantern = body.addOrReplaceChild(
                LANTERN,
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(0.0F, -3.0F, -1.0F, 0.0F, 3.0F, 2.0F)
                        .texOffs(22, 16)
                        .addBox(-0.5F, -5.0F, -1.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-0.5F, -3.5F, -2.5F, -0.3927F, 0.0F, 0.0F)
        );

        PartDefinition jaw = body.addOrReplaceChild(
                JAW,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-3.5F, -3.0F, -7.5F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.25F)),
                PartPose.offsetAndRotation(-0.5F, 1.0F, 3.0F, 0.1745F, 0.0F, 0.0F)
        );

        PartDefinition leftFin = body.addOrReplaceChild(
                LEFT_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 0.0F, 1.5F, 0.0F, 0.3927F, 0.0F)
        );

        PartDefinition rightFin = body.addOrReplaceChild(
                RIGHT_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 0.0F, 1.5F, 0.0F, -0.3927F, 0.0F)
        );

        return LayerDefinition.create(data, 32, 32);
    }

    @SuppressWarnings("unused")
    public static LayerDefinition getDeflatedLayerDefinition() {
        MeshDefinition data = new MeshDefinition();
        PartDefinition root = data.getRoot();

        PartDefinition body = root.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(0, 2)
                        .addBox(-4.0F, -2.0F, -4.5F, 7.0F, 5.0F, 9.0F),
                PartPose.offset(0.5F, 21.0F, -1.0F)
        );

        PartDefinition tail = body.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 4.0F),
                PartPose.offset(-0.5F, 0.5F, 4.5F)
        );

        PartDefinition lantern = body.addOrReplaceChild(
                LANTERN,
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(0.0F, -3.0F, -1.0F, 0.0F, 3.0F, 2.0F)
                        .texOffs(22, 16)
                        .addBox(-0.5F, -5.0F, -1.0F, 1.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-0.5F, -1.5F, -2.5F, -0.8727F, 0.0F, 0.0F)
        );

        PartDefinition jaw = body.addOrReplaceChild(
                JAW,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-3.5F, -3.0F, -7.5F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.25F)),
                PartPose.offset(-0.5F, 1.0F, 3.0F)
        );

        PartDefinition leftFin = body.addOrReplaceChild(
                LEFT_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 1.0F, 0.0F, 0.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(3.0F, 0.0F, 1.5F, 0.0F, 0.3927F, 0.0F)
        );

        PartDefinition rightFin = body.addOrReplaceChild(
                RIGHT_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 1.0F, 0.0F, 0.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-4.0F, 0.0F, 1.5F, 0.0F, -0.3927F, 0.0F)
        );

        return LayerDefinition.create(data, 32, 32);
    }

    @Override
    public void setupAnim(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float speed = 2.5f;
        float degree = 1.5f;
        this.body.yRot = headYaw * 0.01F;
        this.tail.yRot = Mth.cos(animationProgress * speed * 0.2F) * degree * 1.4F * 0.25F;
        this.leftFin.yRot = Mth.cos(-1.0F + animationProgress * speed * 0.2F) * degree * 0.8F * 0.25F + 0.8F;
        this.rightFin.yRot = Mth.cos(2.0F + animationProgress * speed * 0.2F) * degree * 0.8F * 0.25F - 0.8F;

        if (entity.isDeflated()) return;
        this.body.xRot = headPitch * 0.015F;
        this.body.yRot += Mth.cos(animationProgress * speed * 0.2F) * degree * 0.4F * 0.25F;
        this.jaw.xRot = Mth.cos(limbAngle * speed * 0.2F) * degree * 0.2F * limbDistance + 0.25F;
        this.tail.yRot = Mth.cos(animationProgress * speed * 0.4F) * degree * 0.8F * 0.25F;
        this.leftFin.yRot = Mth.cos(animationProgress * speed * 0.4F) * degree * 0.8F * 0.25F + 0.8F;
        this.rightFin.yRot = Mth.cos(3.0F + animationProgress * speed * 0.4F) * degree * 0.8F * 0.25F - 0.8F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, float f, float g, float h, float k) {
        this.root.render(poseStack, vertexConsumer, i, j, f, g, h, k);
    }
}
