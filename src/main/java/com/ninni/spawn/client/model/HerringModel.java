package com.ninni.spawn.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import static net.minecraft.client.model.geom.PartNames.*;

@SuppressWarnings("FieldCanBeLocal, unused")
@Environment(EnvType.CLIENT)
public class HerringModel<T extends Entity>
        extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart leftFin;
    private final ModelPart rightFin;
    private final ModelPart tail;

    public HerringModel(ModelPart modelPart) {
        this.root = modelPart;

        this.body = modelPart.getChild(BODY);

        this.leftFin = this.body.getChild(LEFT_FIN);
        this.rightFin = this.body.getChild(RIGHT_FIN);
        this.tail = this.body.getChild(TAIL);
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 7.0F)
                        .texOffs(0, -2)
                        .addBox(0.0F, -2.0F, 0.0F, 0.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 23.0F, -1.0F)
        );

        PartDefinition leftFin = body.addOrReplaceChild(
                LEFT_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(1.0F, 0.5F, 0.0F, 0.0F, 0.0F, -0.7854F)
        );

        PartDefinition rightFin = body.addOrReplaceChild(
                RIGHT_FIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F)
                        .mirror(false),
                PartPose.offsetAndRotation(-1.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.7854F)
        );

        PartDefinition tail = body.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(0, 5)
                        .addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, -0.5F, 4.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 16);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float pi = ((float)Math.PI);
        this.body.xRot = headPitch * (pi/180);
        this.body.yRot = headYaw * (pi/180);

        this.root.y = Mth.cos(animationProgress + 3) * 0.4F * 0.25F;
        this.root.yRot = Mth.cos(animationProgress + 1) * 0.4F * 0.25F;
        this.tail.yRot = Mth.cos(animationProgress + 2) * 2.8F * 0.25F;
        this.rightFin.zRot = Mth.cos(animationProgress + 1f + pi) * 2 * 0.25F + 0.6F;
        this.leftFin.zRot = Mth.cos(animationProgress + 1.5f) * 2F * 0.25F - 0.6F;
    }
}

