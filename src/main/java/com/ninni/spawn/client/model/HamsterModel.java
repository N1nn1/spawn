package com.ninni.spawn.client.model;

import com.google.common.collect.ImmutableList;
import com.ninni.spawn.entity.Hamster;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import static net.minecraft.client.model.geom.PartNames.*;

@SuppressWarnings("FieldCanBeLocal, unused")
@Environment(value= EnvType.CLIENT)
public class HamsterModel<E extends Hamster> extends AgeableListModel<E> {
    public static final String RIGHT_CHEEK = "right_cheek";
    public static final String LEFT_CHEEK = "left_cheek";

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart rightArm;
    private final ModelPart rightCheek;
    private final ModelPart rightEar;
    private final ModelPart leftLeg;
    private final ModelPart leftArm;
    private final ModelPart leftCheek;
    private final ModelPart leftEar;
    private final ModelPart tail;

    public HamsterModel(ModelPart root) {
        this.root = root;

        this.body = this.root.getChild(BODY);
        this.rightLeg = this.root.getChild(RIGHT_LEG);
        this.rightArm = this.root.getChild(RIGHT_ARM);
        this.leftLeg = this.root.getChild(LEFT_LEG);
        this.leftArm = this.root.getChild(LEFT_ARM);

        this.tail = this.body.getChild(TAIL);
        this.rightCheek = this.body.getChild(RIGHT_CHEEK);
        this.rightEar = this.body.getChild(RIGHT_EAR);
        this.leftCheek = this.body.getChild(LEFT_CHEEK);
        this.leftEar = this.body.getChild(LEFT_EAR);
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.75F, -3.0F, -4.5F, 7.0F, 6.0F, 9.0F),
                PartPose.offset(0.25F, 20.0F, -0.5F)
        );

        PartDefinition leftCheek = body.addOrReplaceChild(
                LEFT_CHEEK,
                CubeListBuilder.create()
                        .texOffs(23, 6)
                        .addBox(0.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(1.15F, 2.4F, -3.4F)
        );

        PartDefinition rightCheek = body.addOrReplaceChild(
                RIGHT_CHEEK,
                CubeListBuilder.create()
                        .texOffs(23, 6)
                        .mirror().addBox(-2.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F)
                        .mirror(false),
                PartPose.offset(-1.65F, 2.4F, -3.4F)
        );

        PartDefinition tail = body.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offset(-0.25F, 1.0F, 4.5F)
        );

        PartDefinition rightEar = body.addOrReplaceChild(
                RIGHT_EAR,
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        .addBox(-2.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F),
                PartPose.offsetAndRotation(-3.75F, -2.5F, -3.5F, 0.0F, 0.7854F, 0.0F)
        );

        PartDefinition leftEar = body.addOrReplaceChild(
                LEFT_EAR,
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        .mirror()
                        .addBox(0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(3.25F, -2.5F, -3.5F, 0.0F, -0.7854F, 0.0F)
        );

        PartDefinition rightLeg = partdefinition.addOrReplaceChild(
                RIGHT_LEG,
                CubeListBuilder.create()
                        .texOffs(0, 3)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(-1.5F, 23.0F, 2.0F)
        );

        PartDefinition rightArm = partdefinition.addOrReplaceChild(
                RIGHT_ARM,
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F),
                PartPose.offset(-1.5F, 23.0F, -2.0F)
        );

        PartDefinition leftLeg = partdefinition.addOrReplaceChild(
                LEFT_LEG,
                CubeListBuilder.create()
                        .texOffs(0, 3)
                        .mirror()
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F)
                        .mirror(false),
                PartPose.offset(1.5F, 23.0F, 2.0F)
        );

        PartDefinition leftArm = partdefinition.addOrReplaceChild(
                LEFT_ARM,
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .mirror()
                        .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F)
                        .mirror(false),
                PartPose.offset(1.5F, 23.0F, -2.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public static LayerDefinition getStandingLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-3.75F, -7.0F, -1.5F, 7.0F, 10.0F, 6.0F),
                PartPose.offset(0.25F, 21.0F, -0.5F)
        );

        PartDefinition leftCheek = body.addOrReplaceChild(
                LEFT_CHEEK,
                CubeListBuilder.create()
                        .texOffs(23, 6)
                        .addBox(0.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(1.15F, -1.6F, -0.4F)
        );

        PartDefinition rightCheek = body.addOrReplaceChild(
                RIGHT_CHEEK,
                CubeListBuilder.create()
                        .texOffs(23, 6)
                        .mirror().addBox(-2.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F)
                        .mirror(false),
                PartPose.offset(-1.65F, -1.6F, -0.4F)
        );

        PartDefinition tail = body.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 1.0F),
                PartPose.offset(-0.25F, 1.0F, 4.5F)
        );

        PartDefinition rightEar = body.addOrReplaceChild(
                RIGHT_EAR,
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        .addBox(-2.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F),
                PartPose.offsetAndRotation(-3.75F, -6.5F, -0.5F, 0.0F, 0.7854F, 0.0F)
        );

        PartDefinition leftEar = body.addOrReplaceChild(
                LEFT_EAR,
                CubeListBuilder.create()
                        .texOffs(0, 6)
                        .mirror()
                        .addBox(0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(3.25F, -6.5F, -0.5F, 0.0F, -0.7854F, 0.0F)
        );

        PartDefinition rightLeg = partdefinition.addOrReplaceChild(
                RIGHT_LEG,
                CubeListBuilder.create()
                        .texOffs(0, 3)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-3.5F, 23.0F, -2.0F, 0.0F, 0.7854F, 0.0F)
        );

        PartDefinition rightArm = partdefinition.addOrReplaceChild(
                RIGHT_ARM,
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F),
                PartPose.offsetAndRotation(-2.5F, 20.0F, -2.0F, -0.3927F, 0.0F, 0.0F)
        );

        PartDefinition leftLeg = partdefinition.addOrReplaceChild(
                LEFT_LEG,
                CubeListBuilder.create()
                        .texOffs(0, 3)
                        .mirror()
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(3.5F, 23.0F, -2.0F, 0.0F, -0.7854F, 0.0F)
        );

        PartDefinition leftArm = partdefinition.addOrReplaceChild(
                LEFT_ARM,
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .mirror()
                        .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(2.5F, 20.0F, -2.0F, -0.3927F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float speed = 1.0f;
        float degree = 1.0f;
        float pi = ((float)Math.PI);
        float q = entity.getPuffTicks();

        this.leftCheek.xScale = 2f * q;
        this.leftCheek.zScale = 2f * q;
        this.leftCheek.yScale = 2f * q;
        this.rightCheek.xScale = 2f * q;
        this.rightCheek.zScale = 2f * q;
        this.rightCheek.yScale = 2f * q;


        this.leftEar.yRot = Mth.cos(animationProgress * speed * 0.2F) * degree * 0.6F * 0.25F - 0.6F;
        this.rightEar.yRot = Mth.cos(animationProgress * speed * 0.2F + pi) * degree * 0.6F * 0.25F + 0.6F;

        this.body.y = Mth.cos(2 + limbAngle * 0.6f * speed) * 0.6f * limbDistance + (entity.isStanding() ? 21.0F : (entity.isInSittingPose() ? 21F : 20F));
        this.body.yRot = Mth.cos(1 + limbAngle * 1 * speed) * 0.2f * limbDistance;
        this.rightLeg.xRot = Mth.cos(limbAngle * 0.6f * speed) * 1.4f * limbDistance;
        this.leftLeg.xRot = Mth.cos(limbAngle * 0.6f * speed + pi) * 1.4f * limbDistance;
        this.rightArm.xRot = Mth.cos(limbAngle * 0.6f * speed + pi) * 1.4f * limbDistance + (entity.isStanding() ? -0.3927F : 0F);
        this.leftArm.xRot = Mth.cos(limbAngle * 0.6f * speed) * 1.4f * limbDistance + (entity.isStanding() ? -0.3927F : 0F);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.leftArm, this.leftLeg, this.rightArm, this.rightLeg);
    }
}
