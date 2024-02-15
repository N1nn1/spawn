package com.ninni.spawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.spawn.entity.Seahorse;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import static net.minecraft.client.model.geom.PartNames.*;

@SuppressWarnings("FieldCanBeLocal, unused")
@Environment(EnvType.CLIENT)
public class SeahorseModel<E extends Seahorse> extends ColorableHierarchicalModel<E> {
    public static final String FIN = "fin";
    public static final String TAIL_BASE = "tail_base";
    public static final String SNOUT = "snout";
    public static final String CREST = "crest";

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tailBase;
    private final ModelPart tail;
    private final ModelPart leftFin;
    private final ModelPart rightFin;
    private final ModelPart fin;
    private final ModelPart head;
    private final ModelPart snout;
    private final ModelPart crest;

    public SeahorseModel(ModelPart root) {
        this.root = root;
        
        this.body = this.root.getChild(BODY);
        
        this.tailBase = this.body.getChild(TAIL_BASE);
        this.leftFin = this.body.getChild(LEFT_FIN);
        this.rightFin = this.body.getChild(RIGHT_FIN);
        this.head = this.body.getChild(HEAD);
        this.fin = this.body.getChild(FIN);
        
        this.snout = this.head.getChild(SNOUT);
        this.crest = this.head.getChild(CREST);
        
        this.tail = this.tailBase.getChild(TAIL);
    }

    @Override
    public void setupAnim(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float speed = 2.0f;
        float degree = 1.0f;
        if (entity.isInWaterOrBubble()) {
            this.head.xRot = headPitch * (float) (Math.PI / 180);
            this.head.yRot = headYaw * (float) (Math.PI / 180);
        } else {
            this.head.xRot = 0;
            this.head.yRot = 0;
        }
        this.fin.yRot = Mth.cos(limbAngle * speed * 1.8F) * degree * 1.5F * limbDistance;
        this.body.y = Mth.cos(animationProgress * speed * 0.1F) * degree * 0.5F * 0.25F + 17.0F;
        this.leftFin.yRot = Mth.cos(animationProgress * speed * 0.4F) * degree * -1.8F * 0.25F + 0.6F;
        this.rightFin.yRot = Mth.cos(animationProgress * speed * 0.4F) * degree * 1.8F * 0.25F - 0.6F;
        this.tailBase.xRot = Mth.cos(-1.0F + animationProgress * speed * 0.1F) * degree * 0.6F * 0.25F;
        this.tail.xRot = Mth.cos(-2.0F + animationProgress * speed * 0.1F) * degree * 0.6F * 0.25F;
        this.body.xRot = Mth.cos(animationProgress * speed * 0.1F) * degree * 0.4F * 0.25F;
    }

    public static LayerDefinition getALayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F),
                PartPose.offset(0.0F, 18.0F, 1.5F)
        );

        PartDefinition tailBase = body.addOrReplaceChild(
                TAIL_BASE,
                CubeListBuilder.create()
                        .texOffs(22, 1)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 2.0F, 0.5F)
        );

        PartDefinition tail = tailBase.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(-1.0F, 0.0F, -3.0F, 2.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, 1.0F, 0.0F)
        );

        PartDefinition leftFin = body.addOrReplaceChild(
                LEFT_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(1.5F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F)
        );

        PartDefinition rightFin = body.addOrReplaceChild(
                RIGHT_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 1.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(-1.5F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F)
        );

        PartDefinition head = body.addOrReplaceChild(
                HEAD,
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(-2.0F, -3.0F, -2.5F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F)
        );

        PartDefinition snout = head.addOrReplaceChild(
                SNOUT,
                CubeListBuilder.create()
                        .texOffs(16, 17)
                        .addBox(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, -0.5F, -2.5F)
        );

        PartDefinition crest = head.addOrReplaceChild(
                CREST,
                CubeListBuilder.create()
                        .texOffs(0, 3)
                        .addBox(0.0F, -5.0F, -5.0F, 0.0F, 5.0F, 6.0F),
                PartPose.offset(0.0F, 0.0F, 1.5F)
        );

        PartDefinition fin = body.addOrReplaceChild(
                FIN,
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -0.0F, -0.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public static LayerDefinition getBLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(12, 4)
                        .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(0.0F, 14.0F, 1.5F)
        );

        PartDefinition fin = body.addOrReplaceChild(
                FIN,
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 3.0F),
                PartPose.offset(0.0F, 2.5F, 1.5F)
        );

        PartDefinition tailBase = body.addOrReplaceChild(
                TAIL_BASE,
                CubeListBuilder.create()
                        .texOffs(14, 12)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 5.0F, 0.5F)
        );

        PartDefinition tail = tailBase.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(0, 7)
                        .addBox(-1.0F, 0.0F, -3.0F, 2.0F, 4.0F, 4.0F),
                PartPose.offset(0.0F, 1.0F, 0.0F)
        );

        PartDefinition head = body.addOrReplaceChild(
                HEAD,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -3.0F, -2.5F, 3.0F, 3.0F, 4.0F)
                        .texOffs(10, 0)
                        .addBox(-1.5F, -3.0F, 1.5F, 3.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        PartDefinition snout = head.addOrReplaceChild(
                SNOUT,
                CubeListBuilder.create()
                        .texOffs(8, 12)
                        .addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, -0.5F, -2.5F)
        );

        PartDefinition crest = head.addOrReplaceChild(
                CREST,
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -0.0F, -0.0F)
        );
        PartDefinition leftFin = body.addOrReplaceChild(
                LEFT_FIN,
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -0.0F, -0.0F)
        );
        PartDefinition rightFin = body.addOrReplaceChild(
                RIGHT_FIN,
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -0.0F, -0.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
