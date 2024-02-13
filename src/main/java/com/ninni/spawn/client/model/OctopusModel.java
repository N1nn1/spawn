package com.ninni.spawn.client.model;

import com.ninni.spawn.client.animation.OctopusAnimation;
import com.ninni.spawn.entity.Octopus;
import net.minecraft.client.animation.definitions.CamelAnimation;
import net.minecraft.client.animation.definitions.FrogAnimation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import static net.minecraft.client.model.geom.PartNames.*;

@SuppressWarnings("FieldCanBeLocal, unused")
public class OctopusModel extends HierarchicalModel<Octopus> {
    public static final String EVERYTHING = "everything";
    public static final String TOP = "top";
    public static final String TENTACLES = "tentacles";
    public static final String MANTLE = "mantle";
    public static final String LEFT_FORE_TENTACLE = "leftForeTentacle";
    public static final String RIGHT_FORE_TENTACLE = "rightForeTentacle";
    public static final String LEFT_MID_FORE_TENTACLE = "leftMidForeTentacle";
    public static final String RIGHT_MID_FORE_TENTACLE = "rightMidForeTentacle";
    public static final String LEFT_MID_BACK_TENTACLE = "leftMidBackTentacle";
    public static final String RIGHT_MID_BACK_TENTACLE = "rightMidBackTentacle";
    public static final String LEFT_BACK_TENTACLE = "leftBackTentacle";
    public static final String RIGHT_BACK_TENTACLE = "rightBackTentacle";

    private final ModelPart root;
    private final ModelPart everything;
    private final ModelPart top;
    private final ModelPart mantle;
    private final ModelPart head;
    private final ModelPart eyes;
    private final ModelPart leftEye;
    private final ModelPart rightEye;
    private final ModelPart tentacles;
    private final ModelPart leftForeTentacle;
    private final ModelPart rightForeTentacle;
    private final ModelPart leftMidForeTentacle;
    private final ModelPart rightMidForeTentacle;
    private final ModelPart leftMidBackTentacle;
    private final ModelPart rightMidBackTentacle;
    private final ModelPart leftBackTentacle;
    private final ModelPart rightBackTentacle;

    public OctopusModel(ModelPart root) {

        this.root = root;

        this.everything = root.getChild(EVERYTHING);

        this.top = this.everything.getChild(TOP);
        this.tentacles = this.everything.getChild(TENTACLES);

        this.mantle = this.top.getChild(MANTLE);
        this.head = this.top.getChild(HEAD);

        this.eyes = this.head.getChild(EYES);

        this.leftEye = this.eyes.getChild(LEFT_EYE);
        this.rightEye = this.eyes.getChild(RIGHT_EYE);

        this.leftForeTentacle = this.tentacles.getChild(LEFT_FORE_TENTACLE);
        this.rightForeTentacle = this.tentacles.getChild(RIGHT_FORE_TENTACLE);
        this.leftMidForeTentacle = this.tentacles.getChild(LEFT_MID_FORE_TENTACLE);
        this.rightMidForeTentacle = this.tentacles.getChild(RIGHT_MID_FORE_TENTACLE);
        this.leftMidBackTentacle = this.tentacles.getChild(LEFT_MID_BACK_TENTACLE);
        this.rightMidBackTentacle = this.tentacles.getChild(RIGHT_MID_BACK_TENTACLE);
        this.leftBackTentacle = this.tentacles.getChild(LEFT_BACK_TENTACLE);
        this.rightBackTentacle = this.tentacles.getChild(RIGHT_BACK_TENTACLE);
    }

    @Override
    public void setupAnim(Octopus entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        float pi = (float)Math.PI;
        this.everything.getAllParts().forEach(ModelPart::resetPose);
        if (!entity.isLocking()) {

            this.leftEye.xRot = headPitch * ((float)Math.PI / 180);
            this.leftEye.yRot = headYaw * ((float)Math.PI / 180);
            this.rightEye.xRot = headPitch * ((float)Math.PI / 180);
            this.rightEye.yRot = headYaw * ((float)Math.PI / 180);

            this.animate(entity.waterIdleAnimationState, OctopusAnimation.WATER_IDLE, ageInTicks, 1.0f);
            this.animate(entity.idleAnimationState, OctopusAnimation.IDLE, ageInTicks, 1.0f);

            if (entity.isInWaterOrBubble() && !entity.onGround()) {
                this.animateWalk(OctopusAnimation.SWIM, limbSwing, limbSwingAmount, 1.5f, 8.0f);
            } else {
                this.animateWalk(OctopusAnimation.WALK, limbSwing, limbSwingAmount, 4.5f, 8.0f);
            }
        } else {
            this.leftEye.xRot = 0;
            this.leftEye.yRot = 0;
            this.rightEye.xRot = 0;
            this.rightEye.yRot = 0;
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition everything = partdefinition.addOrReplaceChild(
                EVERYTHING,
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, -2.0F)
        );

        PartDefinition top = everything.addOrReplaceChild(
                TOP,
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -2.0F, 1.0F)
        );

        PartDefinition mantle = top.addOrReplaceChild(
                MANTLE,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-6.5F, -5.0F, 0.0F, 13.0F, 7.0F, 9.0F, new CubeDeformation(-0.02F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        PartDefinition head = top.addOrReplaceChild(
                HEAD,
                CubeListBuilder.create()
                        .texOffs(20, 16)
                        .addBox(-3.5F, -1.0F, -3.0F, 7.0F, 3.0F, 4.0F, new CubeDeformation(0.01F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        PartDefinition eyes = head.addOrReplaceChild(
                EYES,
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(-1.5F, -6.0F, -1.0F, 3.0F, 6.0F, 2.0F),
                PartPose.offset(0.0F, -1.0F, -1.0F)
        );

        PartDefinition leftEye = eyes.addOrReplaceChild(
                LEFT_EYE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-1.0F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(1.5F, -6.0F, 0.0F)
        );

        PartDefinition rightEye = eyes.addOrReplaceChild(
                RIGHT_EYE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .mirror()
                        .addBox(-2.0F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F)
                        .mirror(false),
                PartPose.offset(-1.5F, -6.0F, 0.0F)
        );

        PartDefinition tentacles = everything.addOrReplaceChild(
                TENTACLES,
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        PartDefinition leftForeTentacle = tentacles.addOrReplaceChild(
                LEFT_FORE_TENTACLE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 16.0F),
                PartPose.offsetAndRotation(2.5F, -1.0F, -1.5F, 0.0F, -0.7854F, 0.0F)
        );

        PartDefinition rightForeTentacle = tentacles.addOrReplaceChild(
                RIGHT_FORE_TENTACLE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .mirror()
                        .addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 16.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(-2.5F, -1.0F, -1.5F, 0.0F, 0.7854F, 0.0F)
        );

        PartDefinition leftMidForeTentacle = tentacles.addOrReplaceChild(
                LEFT_MID_FORE_TENTACLE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(-0.01F)),
                PartPose.offsetAndRotation(3.0F, -1.0F, -1.0F, 0.0F, -1.1781F, 0.0F)
        );

        PartDefinition rightMidForeTentacle = tentacles.addOrReplaceChild(
                RIGHT_MID_FORE_TENTACLE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .mirror()
                        .addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(-0.01F))
                        .mirror(false),
                PartPose.offsetAndRotation(-3.0F, -1.0F, -1.0F, 0.0F, 1.1781F, 0.0F)
        );

        PartDefinition leftMidBackTentacle = tentacles.addOrReplaceChild(
                LEFT_MID_BACK_TENTACLE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 16.0F),
                PartPose.offsetAndRotation(3.0F, -1.0F, 0.5F, 0.0F, -1.9635F, 0.0F)
        );

        PartDefinition rightMidBackTentacle = tentacles.addOrReplaceChild(
                RIGHT_MID_BACK_TENTACLE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .mirror()
                        .addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 16.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(-3.0F, -1.0F, 0.5F, 0.0F, 1.9635F, 0.0F)
        );

        PartDefinition leftBackTentacle = tentacles.addOrReplaceChild(
                LEFT_BACK_TENTACLE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(-0.01F)),
                PartPose.offsetAndRotation(2.5F, -1.0F, 1.5F, 0.0F, -2.3562F, 0.0F)
        );

        PartDefinition rightBackTentacle = tentacles.addOrReplaceChild(
                RIGHT_BACK_TENTACLE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .mirror()
                        .addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(-0.01F))
                        .mirror(false),
                PartPose.offsetAndRotation(-2.5F, -1.0F, 1.5F, 0.0F, 2.3562F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 48, 48);
    }

    public static LayerDefinition createLockingBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition everything = partdefinition.addOrReplaceChild(
                EVERYTHING,
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, -2.0F)
        );

        PartDefinition top = everything.addOrReplaceChild(
                TOP,
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -2.0F, 1.0F)
        );

        PartDefinition head = top.addOrReplaceChild(
                HEAD, 
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -2.5F, 2.0F)
        );

        PartDefinition mantle = top.addOrReplaceChild(
                MANTLE,
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -2.5F, 2.0F)
        );

        PartDefinition eyes = head.addOrReplaceChild(
                EYES, 
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(-1.5F, -6.0F, -1.0F, 3.0F, 6.0F, 2.0F),
                PartPose.offset(0.0F, -1.0F, -1.0F)
        );

        PartDefinition leftEye = eyes.addOrReplaceChild(
                LEFT_EYE, 
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-1.0F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(1.5F, -6.0F, 0.0F)
        );

        PartDefinition rightEye = eyes.addOrReplaceChild(
                RIGHT_EYE,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .mirror()
                        .addBox(-2.0F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F)
                        .mirror(false),
                PartPose.offset(-1.5F, -6.0F, 0.0F)
        );

        PartDefinition tentacles = everything.addOrReplaceChild(
                TENTACLES,
                CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition leftForeTentacle = tentacles.addOrReplaceChild(
                LEFT_FORE_TENTACLE,
                CubeListBuilder.create().texOffs(8, 24).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(13, 2).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -9.5F, -5.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition rightForeTentacle = tentacles.addOrReplaceChild(
                RIGHT_FORE_TENTACLE,
                CubeListBuilder.create().texOffs(8, 24).mirror().addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(13, 2).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -9.5F, -5.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition leftMidForeTentacle = tentacles.addOrReplaceChild(
                LEFT_MID_FORE_TENTACLE,
                CubeListBuilder.create().texOffs(8, 24).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(-0.01F))
                .texOffs(13, 6).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(7.0F, -9.5F, -1.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition rightMidForeTentacle = tentacles.addOrReplaceChild(
                RIGHT_MID_FORE_TENTACLE,
                CubeListBuilder.create().texOffs(8, 24).mirror().addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(-0.01F)).mirror(false)
                .texOffs(13, 6).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offsetAndRotation(-7.0F, -9.5F, -1.0F, 0.0F, 1.5708F, -1.5708F));

        PartDefinition leftMidBackTentacle = tentacles.addOrReplaceChild(LEFT_MID_BACK_TENTACLE,
                CubeListBuilder.create().texOffs(8, 24).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(13, 6).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -9.5F, 5.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition rightMidBackTentacle = tentacles.addOrReplaceChild(RIGHT_MID_BACK_TENTACLE,
                CubeListBuilder.create().texOffs(8, 24).mirror().addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(13, 6).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.0F, -9.5F, 5.0F, 0.0F, 1.5708F, -1.5708F));

        PartDefinition leftBackTentacle = tentacles.addOrReplaceChild(LEFT_BACK_TENTACLE,
                CubeListBuilder.create().texOffs(8, 24).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(-0.01F))
                .texOffs(13, 2).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, -9.5F, 9.0F, 1.5708F, 3.1416F, 0.0F));

        PartDefinition rightBackTentacle = tentacles.addOrReplaceChild(RIGHT_BACK_TENTACLE,
                CubeListBuilder.create().texOffs(8, 24).mirror().addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(-0.01F)).mirror(false)
                .texOffs(13, 2).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -9.5F, 9.0F, 1.5708F, -3.1416F, 0.0F));

        return LayerDefinition.create(meshdefinition, 48, 48);
    }
    
    @Override
    public ModelPart root() {
        return root;
    }
}
