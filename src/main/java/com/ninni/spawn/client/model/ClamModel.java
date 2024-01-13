package com.ninni.spawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.spawn.entity.Clam;
import com.ninni.spawn.entity.variant.ClamVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

@SuppressWarnings("FieldCanBeLocal, unused")
@Environment(value= EnvType.CLIENT)
public class ClamModel extends EntityModel<Clam> {
    public static final String BOTTOM = "bottom";
    public static final String TOP = "top";

    private final ModelPart root;
    private final ModelPart bottom;
    private final ModelPart top;

    public ClamModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.bottom = this.root.getChild(BOTTOM);
        this.top = this.bottom.getChild(TOP);
    }

    @Override
    public void setupAnim(Clam entity, float f, float g, float h, float i, float j) {
        //if (entity.getBaseColor().base() == ClamVariant.Base.GIANT_CLAM && entity.getBlockStateOn().is(Blocks.SAND) && entity.isInWaterOrBubble()) {
        //    this.bottom.y = 24F;
        //    this.top.xRot = - (Mth.HALF_PI / 2);
        //    this.bottom.xRot = -Mth.HALF_PI + (Mth.HALF_PI / 2);
        //} else {
        //    this.bottom.y = 23.5F;
        //    this.bottom.xRot = 0;
        //    this.top.xRot = 0;
        //}
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, float f, float g, float h, float k) {
        this.root.render(poseStack, vertexConsumer, i, j, f, g, h, k);
    }

    public static LayerDefinition createWedgeShellLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bottom = partdefinition.addOrReplaceChild(
                BOTTOM,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-3.5F, -0.5F, -3.0F, 7.0F, 1.0F, 3.0F)
                        .texOffs(0, 4)
                        .addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, 23.5F, 1.0F)
        );

        PartDefinition top = bottom.addOrReplaceChild(TOP, CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 16);
    }

    public static LayerDefinition createScallopLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bottom = partdefinition.addOrReplaceChild(
                BOTTOM,
                CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F)
                        .texOffs(0, 0)
                        .addBox(-4.5F, -1.0F, -7.0F, 9.0F, 2.0F, 7.0F),
                PartPose.offset(0.0F, 23.0F, 2.5F)
        );

        PartDefinition top = bottom.addOrReplaceChild(TOP, CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 16);
    }

    public static LayerDefinition createGiantClamLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bottom = partdefinition.addOrReplaceChild(
                BOTTOM,
                CubeListBuilder.create()
                        .texOffs(0, 22)
                        .addBox(-10.0F, 0.0F, -15.0F, 20.0F, 7.0F, 15.0F)
                        .texOffs(0, 65)
                        .addBox(-10.0F, -2.0F, -15.0F, 20.0F, 6.0F, 15.0F, new CubeDeformation(0.45F)),
                PartPose.offset(0.0F, 17.0F, 7.5F)
        );

        PartDefinition top = bottom.addOrReplaceChild(
                TOP,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-10.0F, -7.0F, -15.0F, 20.0F, 7.0F, 15.0F)
                        .texOffs(0, 44)
                        .addBox(-10.0F, -4.0F, -15.0F, 20.0F, 6.0F, 15.0F, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 80, 97);
    }
}