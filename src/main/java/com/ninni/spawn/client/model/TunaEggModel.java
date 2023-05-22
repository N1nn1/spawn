package com.ninni.spawn.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.spawn.entity.TunaEgg;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@SuppressWarnings("FieldCanBeLocal, unused")
@Environment(value= EnvType.CLIENT)
public class TunaEggModel extends EntityModel<TunaEgg> {
    public static final String MAIN = "main";

    private final ModelPart root;
    private final ModelPart main;

    public TunaEggModel(ModelPart root) {
        this.root = root;
        this.main = this.root.getChild(MAIN);
    }

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild(
                MAIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F)
                        .texOffs(0, 8)
                        .addBox(-1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 16, 16);
    }


    @Override
    public void setupAnim(TunaEgg entity, float f, float g, float h, float i, float j) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, float f, float g, float h, float k) {
        this.root.render(poseStack, vertexConsumer, i, j, f, g, h, k);
    }
}