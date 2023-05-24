package com.ninni.spawn.client.model;

import com.google.common.collect.ImmutableList;
import com.ninni.spawn.entity.Tuna;
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
public class TunaModel extends AgeableListModel<Tuna> {
    public static final String LEFT_PECTORAL_FIN = "left_pectoral_fin";
    public static final String RIGHT_PECTORAL_FIN = "right_pectoral_fin";
    public static final String DORSAL_FIN = "dorsal_fin";
    public static final String DORSAL_FIN2 = "dorsal_fin2";
    public static final String PELVIC_FIN = "pelvic_fin";

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart dorsalFin;
    private final ModelPart dorsalFin2;
    private final ModelPart leftPectoralFin;
    private final ModelPart rightPectoralFin;
    private final ModelPart pelvicFin;
    private final ModelPart mouth;
    private final ModelPart leftFin;
    private final ModelPart rightFin;
    private final ModelPart tailFin;

    public TunaModel(ModelPart root) {
        this.root = root;

        this.body = this.root.getChild(BODY);

        this.tail = this.body.getChild(TAIL);
        this.dorsalFin = this.body.getChild(DORSAL_FIN);
        this.dorsalFin2 = this.body.getChild(DORSAL_FIN2);
        this.leftPectoralFin = this.body.getChild(LEFT_PECTORAL_FIN);
        this.rightPectoralFin = this.body.getChild(RIGHT_PECTORAL_FIN);
        this.pelvicFin = this.body.getChild(PELVIC_FIN);
        this.mouth = this.body.getChild(MOUTH);
        this.leftFin = this.body.getChild(LEFT_FIN);
        this.rightFin = this.body.getChild(RIGHT_FIN);

        this.tailFin = this.tail.getChild(TAIL_FIN);
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.5F, -6.0F, -12.0F, 7.0F, 10.0F, 22.0F),
                PartPose.offset(0.0F, 20.0F, -1.0F)
        );

        PartDefinition tail = body.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(0, 32)
                        .addBox(-2.0F, -3.0F, 0.0F, 4.0F, 6.0F, 6.0F)
                        .texOffs(0, 0)
                        .addBox(0.0F, 3.0F, 0.0F, 0.0F, 1.0F, 5.0F)
                        .texOffs(0, 1)
                        .addBox(0.0F, -4.0F, 0.0F, 0.0F, 1.0F, 5.0F), 
                PartPose.offset(0.0F, -1.0F, 10.0F)
        );

        PartDefinition tailFin = tail.addOrReplaceChild(
                TAIL_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, -9.0F, -1.0F, 0.0F, 15.0F, 7.0F),
                PartPose.offset(0.0F, 0.0F, 6.0F)
        );

        PartDefinition dorsalFin = body.addOrReplaceChild(
                DORSAL_FIN,
                CubeListBuilder.create()
                        .texOffs(14, 25)
                        .addBox(0.0F, -4.0F, -3.5F, 0.0F, 4.0F, 7.0F),
                PartPose.offset(0.0F, -6.0F, -2.5F)
        );

        PartDefinition dorsalFin2 = body.addOrReplaceChild(
                DORSAL_FIN2,
                CubeListBuilder.create()
                        .texOffs(10, 0)
                        .addBox(0.0F, -2.0F, -2.0F, 0.0F, 2.0F, 4.0F), 
                PartPose.offset(0.0F, -6.0F, 6.0F)
        );

        PartDefinition leftPectoralFin = body.addOrReplaceChild(
                LEFT_PECTORAL_FIN,
                CubeListBuilder.create()
                        .texOffs(14, 3)
                        .addBox(0.0F, 0.0F, -1.5F, 0.0F, 3.0F, 3.0F),
                PartPose.offset(1.5F, 4.0F, 0.5F)
        );

        PartDefinition rightPectoralFin = body.addOrReplaceChild(
                RIGHT_PECTORAL_FIN,
                CubeListBuilder.create()
                        .texOffs(14, 3)
                        .addBox(0.0F, 0.0F, -1.5F, 0.0F, 3.0F, 3.0F),
                PartPose.offset(-1.5F, 4.0F, 0.5F)
        );

        PartDefinition pelvicFin = body.addOrReplaceChild(
                PELVIC_FIN,
                CubeListBuilder.create()
                        .texOffs(14, 6)
                        .addBox(0.0F, 0.0F, -1.5F, 0.0F, 2.0F, 3.0F), 
                PartPose.offset(0.0F, 4.0F, 6.5F)
        );

        PartDefinition mouth = body.addOrReplaceChild(
                MOUTH,
                CubeListBuilder.create()
                        .texOffs(28, 32)
                        .addBox(-2.5F, -8.0F, -1.0F, 5.0F, 8.0F, 1.0F),
                PartPose.offset(0.0F, 3.0F, -12.0F)
        );

        PartDefinition leftFin = body.addOrReplaceChild(
                LEFT_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, -2.0F, 5.0F, 0.0F, 4.0F).mirror(false),
                PartPose.offset(3.5F, 2.0F, -5.0F)
        );

        PartDefinition rightFin = body.addOrReplaceChild(
                RIGHT_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, 0.0F, -2.0F, 5.0F, 0.0F, 4.0F),
                PartPose.offset(-3.5F, 2.0F, -5.0F)
        );

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Tuna entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float speed = 1.2f;
        float degree = 1.5f;
        float pi = ((float)Math.PI);
        this.body.xRot = headPitch * (pi/180);
        this.body.yRot = headYaw * (pi/180);

        this.body.y = Mth.cos(animationProgress * speed * 0.2F) * degree * 1.5F * 0.25F + 20.0F;
        this.body.yRot += Mth.cos(animationProgress * speed * 0.4F + 2f) * degree * 0.4F * 0.25F;
        this.body.xRot += Mth.cos(animationProgress * speed * 0.2F + 2f) * degree * 0.1F * 0.25F;
        this.tail.yRot = Mth.cos(animationProgress * speed * 0.4F + 1f) * degree * 0.8F * 0.25F;
        this.tailFin.yRot = Mth.cos(animationProgress * speed * 0.4F) * degree * 1.6F * 0.25F;
        this.mouth.xRot = Mth.cos(animationProgress * speed * 0.1F) * degree * 0.1F * 0.25F + 0.05F;

        this.rightFin.zRot = Mth.cos(animationProgress * speed * 0.4F + 1f) * degree * 0.8F * 0.25F - 0.4F;
        this.rightFin.yRot = 0.4F;
        this.leftFin.zRot = Mth.cos(animationProgress * speed * 0.4F + 1f + pi) * degree * 0.8F * 0.25F + 0.4F;
        this.leftFin.yRot = -0.4F;
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