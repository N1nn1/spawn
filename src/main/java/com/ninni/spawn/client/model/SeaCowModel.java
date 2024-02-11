package com.ninni.spawn.client.model;

import com.google.common.collect.ImmutableList;
import com.ninni.spawn.entity.SeaCow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import static net.minecraft.client.model.geom.PartNames.*;

@SuppressWarnings("FieldCanBeLocal, unused")
@Environment(EnvType.CLIENT)
public class SeaCowModel extends AgeableListModel<SeaCow> {
    public static final String FLUKES = "flukes";

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart tail;
    private final ModelPart flukes;
    private float headXRot;

    public SeaCowModel(ModelPart root) {
        this.root = root;

        this.body = this.root.getChild(BODY);

        this.head = this.body.getChild(HEAD);
        this.leftArm = this.body.getChild(LEFT_ARM);
        this.rightArm = this.body.getChild(RIGHT_ARM);
        this.tail = this.body.getChild(TAIL);

        this.flukes = this.tail.getChild(FLUKES);
    }

    @Override
    public void setupAnim(SeaCow entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float pi = ((float)Math.PI);


        if (entity.isInWaterOrBubble()) {
            float degree = Math.min(limbDistance / 0.3f, 0.7f) + 0.3f;
            float tailDegree = Math.min(limbDistance / 0.3f, 0.8f) + 0.2f;
            float armDegree = Math.min(limbDistance / 0.3f, 0.6f);

            this.body.xRot = headPitch * (pi / 180);
            this.body.yRot = headYaw * (pi / 180);

            this.head.xRot = -(headPitch * (pi / 180)) / 2;
            this.head.yRot = -(headYaw * (pi / 180)) / 2;
            this.head.xRot += this.headXRot;

            this.head.xRot += Mth.cos(animationProgress * 0.2F + 3f) * 0.6F * degree * 0.25F;
            this.body.xRot += Mth.cos(animationProgress * 0.2F + 2f) * 0.6F * degree * 0.25F;
            this.body.y = Mth.cos(animationProgress * 0.2F) * 1.5F * degree + 17.0F;

            this.rightArm.zRot = Mth.cos(animationProgress * 0.2F + 3.5f) * 2F * 0.25F * degree - 0.8F;
            this.rightArm.yRot = Mth.sin(animationProgress * 0.2F + 3.5f) * 3F * 0.25F * armDegree;

            this.leftArm.zRot = Mth.cos(animationProgress * 0.2F + 3.5f + pi) * 2F * 0.25F * degree + 0.8F;
            this.leftArm.yRot = Mth.sin(animationProgress * 0.2F + 3.5f + pi) * 3F * 0.25F * armDegree;


            this.tail.xRot = Mth.cos(animationProgress * 0.2F + 1f) * tailDegree * 0.35F;
            this.flukes.xRot = Mth.cos(animationProgress * 0.2F - 1) * tailDegree * 0.35F;

        } else {
            limbDistance = Mth.clamp(limbDistance, -0.25F, 0.25F);
            float degree = 3f;

            this.body.xRot = 0;
            this.body.yRot = 0;

            this.head.xRot = (headPitch * (pi / 180)) / 2;
            this.head.yRot = (headYaw * (pi / 180)) / 2;

            this.body.y = Mth.cos(limbAngle + 1) * 1.4f * 2 * limbDistance + 17.0F;

            this.rightArm.zRot = 0;
            this.rightArm.yRot = Mth.cos(limbAngle) * 1.4f * degree * limbDistance + 0.4F;

            this.leftArm.zRot = 0;
            this.leftArm.yRot = Mth.cos(limbAngle + pi) * 1.4f * degree * limbDistance - 0.4F;

            this.tail.xRot = Mth.cos(limbAngle) * 0.4f * degree * limbDistance - 0.4f;
            this.flukes.xRot = Mth.cos(limbAngle) * 0.4f * degree * limbDistance + 0.3f;

        }
    }

    @Override
    public void prepareMobModel(SeaCow entity, float f, float g, float h) {
        super.prepareMobModel(entity, f, g, h);
        this.head.y = -1.0F + entity.getHeadEatPositionScale(h) * 3.0f;
        this.headXRot = entity.getHeadEatAngleScale(h);
    }


    public static LayerDefinition getLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition body = root.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-10.5F, -9.0F, -13.0F, 21.0F, 16.0F, 25.0F),
                PartPose.offset(0.0F, 17.0F, 1.0F)
        );

        PartDefinition head = body.addOrReplaceChild(
                HEAD,
                CubeListBuilder.create()
                        .texOffs(0, 41)
                        .addBox(-7.5F, -6.0F, -6.0F, 15.0F, 11.0F, 8.0F)
                        .texOffs(46, 41)
                        .addBox(-5.5F, -6.0F, -13.0F, 11.0F, 14.0F, 7.0F),
                PartPose.offset(0.0F, -1.0F, -13.0F)
        );
        PartDefinition leftArm = body.addOrReplaceChild(
                LEFT_ARM,
                CubeListBuilder.create()
                        .texOffs(67, 15)
                        .addBox(0.0F, -1.0F, -4.0F, 14.0F, 2.0F, 8.0F),
                PartPose.offset(10.5F, 6.0F, -7.0F)
        );

        PartDefinition rightArm = body.addOrReplaceChild(
                RIGHT_ARM,
                CubeListBuilder.create().texOffs(67, 15)
                        .mirror()
                        .addBox(-14.0F, -1.0F, -4.0F, 14.0F, 2.0F, 8.0F)
                        .mirror(false),
                PartPose.offset(-10.5F, 6.0F, -7.0F)
        );

        PartDefinition tail = body.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(0, 60)
                        .addBox(-6.5F, -4.5F, 0.0F, 13.0F, 9.0F, 12.0F),
                PartPose.offset(0.0F, -1.5F, 12.0F)
        );

        PartDefinition flukes = tail.addOrReplaceChild(
                FLUKES, 
                CubeListBuilder.create()
                        .texOffs(36, 67)
                        .addBox(-9.5F, -1.0F, -1.0F, 19.0F, 2.0F, 14.0F),
                PartPose.offset(0.0F, 0.5F, 8.0F)
        );


        return LayerDefinition.create(meshdefinition, 112, 96);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body);
    }

}