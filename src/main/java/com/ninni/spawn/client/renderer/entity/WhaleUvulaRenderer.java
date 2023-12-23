package com.ninni.spawn.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.spawn.block.entity.WhaleUvulaBlockEntity;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(EnvType.CLIENT)
public class WhaleUvulaRenderer implements BlockEntityRenderer<WhaleUvulaBlockEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "entity/whale/uvula");
    public static final Material UVULA_RESOURCE_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, TEXTURE);
    private static final String UVULA_BODY = "uvula";
    private final ModelPart uvula;

    public WhaleUvulaRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(SpawnEntityModelLayers.WHALE_UVULA);
        this.uvula = modelPart.getChild(UVULA_BODY);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "uvula",
                CubeListBuilder.create()
                        .texOffs(0, 10)
                        .addBox(-6.0F, 6.0F, -6.0F, 12.0F, 10.0F, 12.0F)
                        .texOffs(0, 0)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 48, 32);
    }

    public void render(WhaleUvulaBlockEntity uvulaBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        this.uvula.x = 8;
        this.uvula.z = 8;
        this.uvula.y = 16;
        this.uvula.xRot = Mth.PI;
        this.uvula.zRot = 0;

        float g = (float)uvulaBlockEntity.ticks + f;
        float h = 0.0F;
        float k = 0.0F;
        if (uvulaBlockEntity.shaking) {
            float l = Mth.sin(g / 3.1415927F) / (4.0F + g / 3.0F);
            if (uvulaBlockEntity.clickDirection == Direction.NORTH) {
                h = -l;
            } else if (uvulaBlockEntity.clickDirection == Direction.SOUTH) {
                h = l;
            } else if (uvulaBlockEntity.clickDirection == Direction.EAST) {
                k = -l;
            } else if (uvulaBlockEntity.clickDirection == Direction.WEST) {
                k = l;
            }
        }

        this.uvula.xRot += h;
        this.uvula.zRot += k;
        VertexConsumer vertexConsumer = UVULA_RESOURCE_LOCATION.buffer(multiBufferSource, RenderType::entitySolid);
        this.uvula.render(poseStack, vertexConsumer, i, j);
    }
}
