package com.ninni.spawn.client.inventory;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.entity.Hamster;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class HamsterInventoryScreen extends AbstractContainerScreen<HamsterInventoryMenu> {
    private static final ResourceLocation HAMSTER_INVENTORY_LOCATION = new ResourceLocation(Spawn.MOD_ID, "textures/gui/container/hamster.png");

    public HamsterInventoryScreen(HamsterInventoryMenu abstractContainerMenu, Inventory inventory, Hamster hamster) {
        super(abstractContainerMenu, inventory, hamster.getDisplayName());
        //this.passEvents = false;
    }

    @Override
    public void render(GuiGraphics poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        this.renderTooltip(poseStack, i, j);
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float f, int i, int j) {
        this.renderBackground(poseStack);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        poseStack.blit(HAMSTER_INVENTORY_LOCATION, k, l, 0, 0, this.imageWidth, this.imageHeight);
    }
}
