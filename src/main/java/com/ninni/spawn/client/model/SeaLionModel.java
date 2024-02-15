package com.ninni.spawn.client.model;

import com.ninni.spawn.client.animation.SeaLionAnimation;
import com.ninni.spawn.entity.SeaLion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@SuppressWarnings("FieldCanBeLocal, unused")
@Environment(value= EnvType.CLIENT)
public class SeaLionModel extends AgeableHierarchicalModel<SeaLion> {

    private final ModelPart root;
    private final ModelPart everything;
    private final ModelPart hip;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart tail;
    private final ModelPart chest;
    private final ModelPart rightArm;
    private final ModelPart rightFlipper;
    private final ModelPart leftArm;
    private final ModelPart leftFlipper;
    private final ModelPart head;
    private final ModelPart leftEar;
    private final ModelPart rightEar;
    private final ModelPart leftWhiskerGroup;
    private final ModelPart rightWhiskerGroup;
    private final ModelPart leftWhisker;
    private final ModelPart rightWhisker;
    private final ModelPart leftWhisker2;
    private final ModelPart rightWhisker2;

    public SeaLionModel(ModelPart root) {
        super(0.5F, 24.0F);


        this.root = root;

        this.everything = root.getChild("everything");

        this.hip = this.everything.getChild("hip");
        this.chest = this.everything.getChild("chest");
        this.rightArm = this.everything.getChild("rightArm");
        this.leftArm = this.everything.getChild("leftArm");

        this.rightFlipper = this.rightArm.getChild("rightFlipper");
        this.leftFlipper = this.leftArm.getChild("leftFlipper");

        this.head = this.chest.getChild("head");

        this.rightLeg = this.hip.getChild("rightLeg");
        this.leftLeg = this.hip.getChild("leftLeg");
        this.tail = this.hip.getChild("tail");


        this.leftEar = this.head.getChild("leftEar");
        this.rightEar = this.head.getChild("rightEar");
        this.leftWhiskerGroup = this.head.getChild("leftWhiskerGroup");
        this.rightWhiskerGroup = this.head.getChild("rightWhiskerGroup");
        this.leftWhisker = this.leftWhiskerGroup.getChild("leftWhisker");
        this.rightWhisker = this.rightWhiskerGroup.getChild("rightWhisker");
        this.leftWhisker2 = this.leftWhiskerGroup.getChild("leftWhisker2");
        this.rightWhisker2 = this.rightWhiskerGroup.getChild("rightWhisker2");

    }

    @Override
    public void setupAnim(SeaLion entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {

        this.everything.getAllParts().forEach(ModelPart::resetPose);

        if (entity.isInWaterOrBubble()) {

            this.animate(entity.waterIdleAnimationState, SeaLionAnimation.IDLE, ageInTicks, 1.0f);
            this.animate(entity.floppingAnimationState, SeaLionAnimation.FLOP, ageInTicks, 1.0f);
            this.animateWalk(SeaLionAnimation.SWIM, limbSwing, limbSwingAmount, 2.5f, 100.0F);

            this.everything.xRot += headPitch * (float) (Math.PI / 180);
            this.everything.yRot += headYaw * (float) (Math.PI / 180);
            this.head.xRot += -(headPitch * (float) (Math.PI / 180))/2;
            this.head.yRot += (headYaw * (float) (Math.PI / 180))/2;

        } else {
            this.animateWalk(SeaLionAnimation.WALK, limbSwing, limbSwingAmount, 5.5f, 100.0F);
            this.animate(entity.slackingAnimationState, SeaLionAnimation.SLACKING, ageInTicks, 1.0f);

            this.head.xRot += headPitch * (float) (Math.PI / 180);
            this.head.yRot += headYaw * (float) (Math.PI / 180);

            this.everything.xRot = 0;
            this.everything.yRot = 0;
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition everything = partdefinition.addOrReplaceChild("everything", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition chest = everything.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -8.0F, -12.0F, 11.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 4.0F));

        PartDefinition head = chest.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 40).addBox(-2.0F, -7.0F, -9.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-3.5F, -8.0F, -4.0F, 7.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, -8.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -7.5F, 2.0F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(-2.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, -7.5F, 2.0F));

        PartDefinition leftWhiskerGroup = head.addOrReplaceChild("leftWhiskerGroup", CubeListBuilder.create(), PartPose.offset(2.0F, -5.0F, -8.0F));

        PartDefinition leftWhisker = leftWhiskerGroup.addOrReplaceChild("leftWhisker", CubeListBuilder.create().texOffs(40, 43).addBox(0.0F, -2.0F, 0.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftWhisker2 = leftWhiskerGroup.addOrReplaceChild("leftWhisker2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, 0.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, -0.3927F, 0.0F));

        PartDefinition rightWhiskerGroup = head.addOrReplaceChild("rightWhiskerGroup", CubeListBuilder.create(), PartPose.offset(-2.0F, -5.0F, -8.0F));

        PartDefinition rightWhisker = rightWhiskerGroup.addOrReplaceChild("rightWhisker", CubeListBuilder.create().texOffs(40, 43).mirror().addBox(-5.0F, -2.0F, 0.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightWhisker2 = rightWhiskerGroup.addOrReplaceChild("rightWhisker2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.0F, -3.0F, 0.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, 0.3927F, 0.0F));

        PartDefinition hip = everything.addOrReplaceChild("hip", CubeListBuilder.create().texOffs(30, 24).addBox(-4.5F, -3.5F, 0.0F, 9.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, 4.0F));

        PartDefinition tail = hip.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(19, 24).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 7.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition rightLeg = hip.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(34, 38).mirror().addBox(-6.5F, -0.5F, -2.0F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 3.0F, 6.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition leftLeg = hip.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(34, 38).addBox(-0.5F, -0.5F, -2.0F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(4.0F, 3.0F, 6.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition rightArm = everything.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(24, 38).mirror().addBox(-2.0F, -2.0F, -3.0F, 2.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, -6.0F, -4.0F));

        PartDefinition rightFlipper = rightArm.addOrReplaceChild("rightFlipper", CubeListBuilder.create().texOffs(34, 0).mirror().addBox(-8.0F, 0.0F, -3.0F, 7.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 5.0F, 0.0F));

        PartDefinition rightArm2 = rightArm.addOrReplaceChild("rightArm2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftArm = everything.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(24, 38).addBox(0.0F, -2.0F, -3.0F, 2.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, -6.0F, -4.0F));

        PartDefinition leftFlipper = leftArm.addOrReplaceChild("leftFlipper", CubeListBuilder.create().texOffs(34, 0).addBox(1.0F, 0.0F, -3.0F, 7.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 5.0F, 0.0F));

        PartDefinition leftArm2 = leftArm.addOrReplaceChild("leftArm2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition createSwimmingBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition everything = partdefinition.addOrReplaceChild("everything", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition hip = everything.addOrReplaceChild("hip", CubeListBuilder.create().texOffs(30, 24).addBox(-4.5F, -3.5F, 0.0F, 9.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, 4.0F));

        PartDefinition rightLeg = hip.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(34, 38).mirror().addBox(-7.0F, -0.5F, -2.0F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 1.0F, 6.5F, -3.1416F, 1.1781F, 0.0F));

        PartDefinition leftLeg = hip.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(34, 38).addBox(0.0F, -0.5F, -2.0F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(2.0F, 1.0F, 6.5F, -3.1416F, -1.1781F, 0.0F));

        PartDefinition tail = hip.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(19, 24).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 7.0F));

        PartDefinition leftArm = everything.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, -3.0F, -5.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r1 = leftArm.addOrReplaceChild("leftFlipper", CubeListBuilder.create().texOffs(50, 38).addBox(-0.5F, 0.0F, -3.0F, 1.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition rightArm = everything.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, -3.0F, -5.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r2 = rightArm.addOrReplaceChild("rightFlipper", CubeListBuilder.create().texOffs(50, 38).mirror().addBox(-0.5F, 0.0F, -3.0F, 1.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition chest = everything.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -4.0F, -12.0F, 11.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 4.0F));

        PartDefinition head = chest.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 40).addBox(-2.0F, -1.0F, -13.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-3.5F, -2.0F, -8.0F, 7.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -12.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -1.5F, -2.0F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(-2.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, -1.5F, -2.0F));

        PartDefinition leftWhiskerGroup = head.addOrReplaceChild("leftWhiskerGroup", CubeListBuilder.create(), PartPose.offset(2.0F, 1.0F, -12.0F));

        PartDefinition leftWhisker = leftWhiskerGroup.addOrReplaceChild("leftWhisker", CubeListBuilder.create().texOffs(40, 43).addBox(0.0F, -2.0F, 0.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftWhisker2 = leftWhiskerGroup.addOrReplaceChild("leftWhisker2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, 0.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, -0.3927F, 0.0F));

        PartDefinition rightWhiskerGroup = head.addOrReplaceChild("rightWhiskerGroup", CubeListBuilder.create(), PartPose.offset(-2.0F, 1.0F, -12.0F));

        PartDefinition rightWhisker = rightWhiskerGroup.addOrReplaceChild("rightWhisker", CubeListBuilder.create().texOffs(40, 43).mirror().addBox(-5.0F, -2.0F, 0.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightWhisker2 = rightWhiskerGroup.addOrReplaceChild("rightWhisker2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.0F, -3.0F, 0.0F, 5.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, 0.3927F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    
    @Override
    public ModelPart root() {
        return this.root;
    }
}
