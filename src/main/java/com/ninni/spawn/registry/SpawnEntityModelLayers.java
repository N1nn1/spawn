package com.ninni.spawn.registry;

import com.ninni.spawn.client.model.*;
import com.ninni.spawn.client.renderer.block.ClamLauncherRenderer;
import com.ninni.spawn.client.renderer.block.PigmentShifterRenderer;
import com.ninni.spawn.client.renderer.block.WhaleUvulaRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(EnvType.CLIENT)
public interface SpawnEntityModelLayers {

    ModelLayerLocation ANGLER_FISH = main("angler_fish", AnglerFishModel::getLayerDefinition);
    ModelLayerLocation ANGLER_FISH_DEFLATED = main("angler_fish_deflated", AnglerFishModel::getDeflatedLayerDefinition);
    ModelLayerLocation TUNA = main("tuna", TunaModel::getLayerDefinition);
    ModelLayerLocation TUNA_EGG = main("tuna_egg", TunaEggModel::getLayerDefinition);
    ModelLayerLocation SEAHORSE_SMALL = main("seahorse_small", SeahorseModel::getALayerDefinition);
    ModelLayerLocation SEAHORSE_LARGE = main("seahorse_large", SeahorseModel::getBLayerDefinition);
    ModelLayerLocation SNAIL = main("snail", SnailModel::getLayerDefinition);
    ModelLayerLocation HAMSTER = main("hamster", HamsterModel::getLayerDefinition);
    ModelLayerLocation HAMSTER_STANDING = main("hamster_standing", HamsterModel::getStandingLayerDefinition);
    ModelLayerLocation ANT = main("ant", AntModel::getLayerDefinition);
    ModelLayerLocation HERRING = main("herring", HerringModel::getLayerDefinition);
    ModelLayerLocation WHALE = main("whale", WhaleModel::getLayerDefinition);
    ModelLayerLocation WHALE_UVULA = main("whale_uvula", WhaleUvulaRenderer::createBodyLayer);
    ModelLayerLocation SEA_COW = main("sea_cow", SeaCowModel::getLayerDefinition);
    ModelLayerLocation CLAM_WEDGE_SHELL = main("clam_wedge_shell", ClamModel::createWedgeShellLayer);
    ModelLayerLocation CLAM_SCALLOP = main("clam_scallop", ClamModel::createScallopLayer);
    ModelLayerLocation CLAM_GIANT_CLAM = main("clam_giant_clam", ClamModel::createGiantClamLayer);
    ModelLayerLocation PIGMENT_SHIFTER = main("pigment_shifter", PigmentShifterRenderer::createBodyLayer);
    ModelLayerLocation CLAM_LAUNCHER = main("clam_launcher", ClamLauncherRenderer::createBodyLayer);
    ModelLayerLocation OCTOPUS = main("octopus", OctopusModel::createBodyLayer);
    ModelLayerLocation OCTOPUS_LOCKING = main("octopus_locking", OctopusModel::createLockingBodyLayer);

    private static ModelLayerLocation register(String id, String name, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        ModelLayerLocation layer = new ModelLayerLocation(new ResourceLocation(MOD_ID, id), name);
        EntityModelLayerRegistry.registerModelLayer(layer, provider);
        return layer;
    }

    private static ModelLayerLocation main(String id, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        return register(id, "main", provider);
    }
}
