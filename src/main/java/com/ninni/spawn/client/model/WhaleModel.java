package com.ninni.spawn.client.model;

import com.google.common.collect.ImmutableList;
import com.ninni.spawn.entity.Tuna;
import com.ninni.spawn.entity.Whale;
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
public class WhaleModel extends AgeableListModel<Whale> {
    public static final String FLUKES = "flukes";

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart body;
    private final ModelPart leftFin;
    private final ModelPart rightFin;
    private final ModelPart tail;
    private final ModelPart flukes;

    public WhaleModel(ModelPart root) {
        this.root = root;

        this.head = this.root.getChild(HEAD);

        this.jaw = this.head.getChild(JAW);
        this.body = this.head.getChild(BODY);

        this.leftFin = this.body.getChild(LEFT_FIN);
        this.rightFin = this.body.getChild(RIGHT_FIN);
        this.tail = this.body.getChild(TAIL);

        this.flukes = this.tail.getChild(FLUKES);
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild(
                HEAD,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-12.0F, -4.0F, -38.0F, 24.0F, 15.0F, 36.0F)
                        .texOffs(28, 109)
                        .addBox(-12.0F, -4.0F, -38.0F, 24.0F, 15.0F, 36.0F, new CubeDeformation(-0.2F)),
                PartPose.offset(0.0F, 12.0F, -5.0F)
        );

        PartDefinition jaw = head.addOrReplaceChild(
                JAW, 
                CubeListBuilder.create()
                        .texOffs(0, 57)
                        .addBox(-12.0F, -8.0F, -35.0F, 24.0F, 15.0F, 36.0F, new CubeDeformation(0.5F))
                        .texOffs(-37, 108)
                        .addBox(-12.5F, 4.35F, -35.5F, 25.0F, 0.0F, 37.0F),
                PartPose.offset(0.0F, 4.0F, -3.0F)
        );

        PartDefinition body = head.addOrReplaceChild(
                BODY,
                CubeListBuilder.create()
                        .texOffs(84, 52)
                        .addBox(-8.0F, -5.0F, -2.0F, 16.0F, 16.0F, 18.0F)
                        .texOffs(0, 0)
                        .addBox(-1.0F, -8.0F, 3.0F, 2.0F, 3.0F, 10.0F),
                PartPose.offset(0.0F, 1.0F, 0.0F)
        );

        PartDefinition leftFin = body.addOrReplaceChild(
                LEFT_FIN,
                CubeListBuilder.create()
                        .texOffs(84, 17)
                        .addBox(0.0F, -1.0F, -4.0F, 18.0F, 2.0F, 12.0F),
                PartPose.offset(8.0F, 6.0F, 4.0F)
        );

        PartDefinition rightFin = body.addOrReplaceChild(
                RIGHT_FIN,
                CubeListBuilder.create()
                        .texOffs(84, 17)
                        .mirror()
                        .addBox(-18.0F, -1.0F, -4.0F, 18.0F, 2.0F, 12.0F)
                        .mirror(false),
                PartPose.offset(-8.0F, 6.0F, 4.0F)
        );

        PartDefinition tail = body.addOrReplaceChild(
                TAIL,
                CubeListBuilder.create()
                        .texOffs(116, 88)
                        .addBox(-5.0F, -4.25F, 0.0F, 10.0F, 8.0F, 20.0F),
                PartPose.offset(0.0F, 3.25F, 16.0F)
        );

        PartDefinition flukes = tail.addOrReplaceChild(
                FLUKES,
                CubeListBuilder.create()
                        .texOffs(84, 0)
                        .addBox(-14.0F, -1.5F, 0.0F, 28.0F, 3.0F, 14.0F),
                PartPose.offset(0.0F, 0.25F, 17.0F)
        );

        return LayerDefinition.create(meshdefinition, 176, 160);
    }

    @Override
    public void setupAnim(Whale entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float speed = 0.4f;
        float degree = 1f;
        float pi = ((float)Math.PI);
        //this.head.xRot = Mth.lerp(this.head.xRot, headPitch * (pi/180), 0.1f);
        //this.head.yRot = Mth.lerp(this.head.yRot, headYaw * (pi/180), 0.1f);
        this.head.xRot = headPitch * (pi/180);
        this.head.yRot = headYaw * (pi/180) ;

        if (entity.isInWater()) {
            this.head.y = Mth.cos(animationProgress * speed * 0.2F) * degree * 1.5F * 0.5F + 12.0F;
            this.head.xRot += Mth.cos(animationProgress * speed * 0.2F + 2f) * degree * 0.6F * 0.25F;

            this.jaw.xRot = Mth.cos(animationProgress * speed * 0.1F) * degree * 0.2F * 0.25F + 0.1F;

            this.body.xRot = Mth.cos(animationProgress * speed * 0.2F + 1f) * degree * 0.1F * 0.25F;

            this.rightFin.zRot = Mth.cos(animationProgress * speed * 0.2F + 3f) * degree * 3F * 0.25F - 0.4F;
            this.rightFin.yRot = 0.4F;

            this.leftFin.zRot = Mth.cos(animationProgress * speed * 0.2F + 3f + pi) * degree * 3F * 0.25F + 0.4F;
            this.leftFin.yRot = -0.4F;

            this.tail.xRot = Mth.cos(animationProgress * speed * 0.2F + 0.5f) * degree * 1F * 0.25F;

            this.flukes.xRot = Mth.cos(animationProgress * speed * 0.2F) * degree * 2F * 0.25F;
        }
    }
    
    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.head);
    }

}