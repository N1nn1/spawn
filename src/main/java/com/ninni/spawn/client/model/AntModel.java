package com.ninni.spawn.client.model;

import com.google.common.collect.ImmutableList;
import com.ninni.spawn.entity.Ant;
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
public class AntModel<E extends Ant> extends AgeableListModel<E> {
    public static final String ABDOMEN = "abdomen";
    public static final String LEFT_ANTENNA = "left_antenna";
    public static final String RIGHT_ANTENNA = "right_antenna";

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;
    private final ModelPart abdomen;
    private final ModelPart leftForeLeg;
    private final ModelPart rightForeLeg;
    private final ModelPart leftMidLeg;
    private final ModelPart rightMidLeg;
    private final ModelPart leftBackLeg;
    private final ModelPart rightBackLeg;

    public AntModel(ModelPart root) {
        this.root = root;

        this.body = this.root.getChild(BODY);
        this.leftForeLeg = this.root.getChild(LEFT_FRONT_LEG);
        this.leftMidLeg = this.root.getChild(LEFT_MID_LEG);
        this.leftBackLeg = this.root.getChild(LEFT_HIND_LEG);
        this.rightForeLeg = this.root.getChild(RIGHT_FRONT_LEG);
        this.rightMidLeg = this.root.getChild(RIGHT_MID_LEG);
        this.rightBackLeg = this.root.getChild(RIGHT_HIND_LEG);


        this.head = this.body.getChild(HEAD);
        this.abdomen = this.body.getChild(ABDOMEN);

        this.leftAntenna = this.head.getChild(LEFT_ANTENNA);
        this.rightAntenna = this.head.getChild(RIGHT_ANTENNA);
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(14, 13)
                        .addBox(-1.5F, -1.0F, -2.5F, 3.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 20.5F, 0.5F)
        );

        PartDefinition head = body.addOrReplaceChild(
                HEAD,
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-2.5F, -3.0F, -3.0F, 5.0F, 4.0F, 4.0F)
                        .texOffs(15, 0)
                        .addBox(-1.5F, 0.0F, -4.0F, 3.0F, 1.0F, 1.0F), 
                PartPose.offset(0.0F, -2.0F, -1.5F)
        );

        PartDefinition leftAntenna = head.addOrReplaceChild(
                LEFT_ANTENNA,
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(0.0F, -4.0F, -3.5F, 0.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(1.0F, -3.0F, -1.5F, 0.0F, -0.3927F, 0.0F)
        );

        PartDefinition rightAntenna = head.addOrReplaceChild(
                RIGHT_ANTENNA,
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(0.0F, -4.0F, -3.5F, 0.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, -3.0F, -1.5F, 0.0F, 0.3927F, 0.0F)
        );

        PartDefinition abdomen = body.addOrReplaceChild(
                ABDOMEN, 
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.5F, -3.0F, -1.0F, 5.0F, 4.0F, 5.0F),
                PartPose.offset(0.0F, -1.0F, 1.5F)
        );

        PartDefinition leftForeLeg = partdefinition.addOrReplaceChild(
                LEFT_FRONT_LEG, 
                CubeListBuilder.create()
                        .texOffs(-1, 22)
                        .addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 21.5F, -1.0F, 0.0F, 0.3927F, 0.7854F)
        );

        PartDefinition rightForeLeg = partdefinition.addOrReplaceChild(
                RIGHT_FRONT_LEG, 
                CubeListBuilder.create()
                        .texOffs(-1, 22)
                        .mirror()
                        .addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(-1.0F, 21.5F, -1.0F, 0.0F, -0.3927F, -0.7854F)
        );

        PartDefinition leftMidLeg = partdefinition.addOrReplaceChild(
                LEFT_MID_LEG, 
                CubeListBuilder.create()
                        .texOffs(-1, 22)
                        .addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 21.5F, 0.0F, 0.0F, 0.0F, 0.7854F)
        );

        PartDefinition rightMidLeg = partdefinition.addOrReplaceChild(
                RIGHT_MID_LEG, 
                CubeListBuilder.create()
                        .texOffs(-1, 22)
                        .mirror()
                        .addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(-1.0F, 21.5F, 0.0F, 0.0F, 0.0F, -0.7854F)
        );

        PartDefinition leftBackLeg = partdefinition.addOrReplaceChild(
                LEFT_HIND_LEG, 
                CubeListBuilder.create()
                        .texOffs(-1, 22)
                        .addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 21.5F, 1.0F, 0.0F, -0.3927F, 0.7854F)
        );

        PartDefinition rightBackLeg = partdefinition.addOrReplaceChild(
                RIGHT_HIND_LEG, 
                CubeListBuilder.create()
                        .texOffs(-1, 22)
                        .mirror()
                        .addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(-1.0F, 21.5F, 1.0F, 0.0F, 0.3927F, -0.7854F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        limbDistance = Mth.clamp(limbDistance, -0.45F, 0.45F);

        float speed = 0.5F;
        float degree = 1.5F;

        head.xRot = headPitch * ((float) Math.PI / 180f);
        head.yRot = headYaw * ((float) Math.PI / 180f);

        leftAntenna.zRot = Mth.sin(animationProgress * speed * 0.1F) * degree * 0.4F * 0.25F;
        leftAntenna.xRot = Mth.cos(animationProgress * speed * 0.05F) * degree * 0.75F * 0.25F;

        rightAntenna.zRot = Mth.sin(animationProgress * speed * 0.1F + (float)Math.PI) * degree * 0.4F * 0.25F;
        rightAntenna.xRot = Mth.cos(animationProgress * speed * 0.05F + (float)Math.PI) * degree * 0.75F * 0.25F;

        abdomen.zRot = Mth.sin(limbAngle * speed * 0.3F) * degree * 0.2F * limbDistance;

        leftForeLeg.zRot = Mth.cos(limbAngle * speed * 1.8F) * degree * 0.25F * limbDistance + 0.7854F;
        leftForeLeg.yRot = Mth.sin(limbAngle * speed * 1.8F) * degree * 1F * limbDistance + 0.3927F;

        rightForeLeg.zRot = Mth.cos(limbAngle * speed * 1.8F) * degree * 0.25F * limbDistance - 0.7854F;
        rightForeLeg.yRot = Mth.sin(limbAngle * speed * 1.8F) * degree * 1F * limbDistance - 0.3927F;


        leftMidLeg.zRot = Mth.sin(limbAngle * speed * 1.8F) * degree * 0.25F * limbDistance + 0.7854F;
        leftMidLeg.yRot = Mth.cos(limbAngle * speed * 1.8F) * degree * 1F * limbDistance;

        rightMidLeg.zRot = Mth.sin(limbAngle * speed * 1.8F) * degree * 0.25F * limbDistance - 0.7854F;
        rightMidLeg.yRot = Mth.cos(limbAngle * speed * 1.8F) * degree * 1F * limbDistance;


        leftBackLeg.zRot = Mth.cos(limbAngle * speed * 1.8F + (float)Math.PI) * degree * 0.25F * limbDistance + 0.7854F;
        leftBackLeg.yRot = Mth.sin(limbAngle * speed * 1.8F + (float)Math.PI) * degree * 1F * limbDistance - 0.3927F;

        rightBackLeg.zRot = Mth.cos(limbAngle * speed * 1.8F + (float)Math.PI) * degree * 0.25F * limbDistance -0.7854F;
        rightBackLeg.yRot = Mth.sin(animationProgress * speed * 1.8F + (float)Math.PI) * degree * 1F * limbDistance + 0.3927F;

        if (entity.isInSittingPose()) {
            body.y = 22.5F;
            leftForeLeg.y =23.5F;
            leftMidLeg.y = 23.5F;
            leftBackLeg.y =23.5F;
            rightForeLeg.y =23.5F;
            rightMidLeg.y = 23.5F;
            rightBackLeg.y =23.5F;
            leftForeLeg.zRot =0;
            leftMidLeg.zRot = 0;
            leftBackLeg.zRot =0;
            rightForeLeg.zRot =0;
            rightMidLeg.zRot = 0;
            rightBackLeg.zRot =0;

        } else {
            body.y = 20.5F;
            leftForeLeg.y =21.5F;
            leftMidLeg.y = 21.5F;
            leftBackLeg.y =21.5F;
            rightForeLeg.y =21.5F;
            rightMidLeg.y = 21.5F;
            rightBackLeg.y =21.5F;
            leftForeLeg.zRot = 0.7854F;
            leftMidLeg.zRot = 0.7854F;
            leftBackLeg.zRot = 0.7854F;
            rightForeLeg.zRot = -0.7854F;
            rightMidLeg.zRot = -0.7854F;
            rightBackLeg.zRot = -0.7854F;
        }
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.root);
    }
}
