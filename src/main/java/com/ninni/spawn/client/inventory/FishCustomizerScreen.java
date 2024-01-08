package com.ninni.spawn.client.inventory;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ninni.spawn.Spawn;
import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.entity.Seahorse;
import com.ninni.spawn.mixin.accessor.TropicalFishAccessor;
import com.ninni.spawn.registry.SpawnEntityType;
import com.ninni.spawn.registry.SpawnItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class FishCustomizerScreen extends AbstractContainerScreen<FishCustomizerMenu> {
    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(Spawn.MOD_ID, "textures/gui/container/fish.png");
    private final List<AbstractButton> buttons = Lists.newArrayList();
    private final FishCustomizerMenu menu;
    private final Player player;
    private final Level level;
    private Optional<Seahorse.Pattern> optional = Optional.empty();
    private double rotationY = 0;
    private double rotateY = 0;

    public FishCustomizerScreen(FishCustomizerMenu customizerMenu, Inventory inventory, Component component) {
        super(customizerMenu, inventory, component);
        this.menu = customizerMenu;
        this.player = inventory.player;
        this.level = inventory.player.level();
    }

    @Override
    public void render(GuiGraphics poseStack, int i, int j, float f) {
        this.renderBackground(poseStack);
        super.render(poseStack, i, j, f);
        this.renderTooltip(poseStack, i, j);
    }

    private <T extends AbstractWidget> void addButton(T abstractWidget) {
        this.addRenderableWidget(abstractWidget);
        this.buttons.add((AbstractButton)(abstractWidget));
    }

    @Override
    protected void init() {
        super.init();
        this.buttons.clear();
        this.addButton(new UpdateBodyPlanButton(this.leftPos + 38, this.topPos + 26));
        this.addButton(new UpdatePatternButton(this.leftPos + 38, this.topPos + 45));
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float partialTicks, int x, int y) {
        ItemStack item = menu.resultSlot.getItem();
        int imgX = (this.width - this.imageWidth) / 2;
        int imgY = (this.height - this.imageHeight) / 2;

        this.renderBackground(poseStack);
        poseStack.blit(RESOURCE_LOCATION, imgX, imgY, 0, 0, this.imageWidth, this.imageHeight);

        Slot bodyDyeSlot = this.menu.bodyDyeSlot;
        Slot patternDyeSlot = this.menu.patternDyeSlot;
        Slot bucketSlot = this.menu.bucketSlot;

        if (!bodyDyeSlot.hasItem()) poseStack.blit(RESOURCE_LOCATION, imgX + bodyDyeSlot.x, imgY + bodyDyeSlot.y, this.imageWidth, 0, 16, 16);
        if (!patternDyeSlot.hasItem()) poseStack.blit(RESOURCE_LOCATION, imgX + patternDyeSlot.x, imgY + patternDyeSlot.y, this.imageWidth, 0, 16, 16);
        if (!bucketSlot.hasItem()) poseStack.blit(RESOURCE_LOCATION, imgX + bucketSlot.x, imgY + bucketSlot.y, this.imageWidth + 16, 0, 16, 16);


        if (menu.resultSlot.hasItem() && item.is(SpawnTags.CUSTOMIZABLE_MOB_ITEMS)) {

            CompoundTag compoundTag = item.getTag();
            if (compoundTag != null && compoundTag.contains("BucketVariantTag", 3)) {
                int tag = compoundTag.getInt("BucketVariantTag");

                if (item.is(Items.TROPICAL_FISH_BUCKET)) {
                    TropicalFish tropicalFish = EntityType.TROPICAL_FISH.create(this.level);
                    TropicalFish.Variant variant = new TropicalFish.Variant(TropicalFish.getPattern(tag), TropicalFish.getBaseColor(tag), TropicalFish.getPatternColor(tag));
                    ((TropicalFishAccessor) tropicalFish).callSetPackedVariant(variant.getPackedId());

                    renderMovableEntity(poseStack, imgX + 107, imgY + 54, 40, tropicalFish, true);
                }

                if (item.is(SpawnItems.SEAHORSE_BUCKET)) {
                    int h;
                    Seahorse seahorse = SpawnEntityType.SEAHORSE.create(this.level);
                    Seahorse.Pattern pattern = this.optional.isPresent() ? this.optional.get() : Seahorse.getPattern(tag);
                    Seahorse.Variant variant = new Seahorse.Variant(pattern, Seahorse.getBaseColor(tag), Seahorse.getPatternColor(tag));
                    seahorse.setPackedVariant(variant.getPackedId());
                    if (pattern.base() == Seahorse.Base.LARGE) {
                        h = -10;
                    } else h = 0;

                    renderMovableEntity(poseStack, imgX + 107, imgY + 64 + h, 40, seahorse, false);

                }

            }
        }

    }

    public void renderMovableEntity(GuiGraphics poseStack, int x, int y, int scale, LivingEntity entity, boolean rotate) {
        rotationY += rotateY;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Quaternionf quaternionf = new Quaternionf().rotateX((float) Math.toRadians(180f));
        Quaternionf quaternionf1 = new Quaternionf().rotateY( ((float) rotationY));
        Quaternionf quaternionf2 = new Quaternionf().rotateZ((float) Math.toRadians(90f));
        Quaternionf quaternionf3 = new Quaternionf().rotateX((float) rotationY);

        if (rotate) {
            quaternionf.mul(quaternionf2);
            quaternionf.mul(quaternionf3);
        } else quaternionf.mul(quaternionf1);

        InventoryScreen.renderEntityInInventory(poseStack, x, y, scale, quaternionf, quaternionf1, entity);
    }


    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        rotateY = pDragX / 40f;
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        rotateY = 0.01;
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }


    @Environment(value=EnvType.CLIENT)
    class UpdateBodyPlanButton extends AbstractButton {
        public UpdateBodyPlanButton(int i, int j) {
            super(i, j, 16, 16, Component.translatable("container.spawn.fish_customizer.body_plan"));
        }

        @Override
        public void onPress() {
            //FishCustomizerScreen.this.minecraft.getConnection().send(new ServerboundSetBeaconPacket(Optional.ofNullable(FishCustomizerScreen.this.primary), Optional.ofNullable(FishCustomizerScreen.this.secondary)));
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            int l = 16;
            if (this.isHovered()) {
                l = 32;
            }
            this.setTooltip(Tooltip.create(Component.translatable("container.spawn.fish_customizer.body_plan"), null));
            guiGraphics.blit(RESOURCE_LOCATION, this.getX(), this.getY(), 176, l, this.width, this.height);
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }

    @Environment(value=EnvType.CLIENT)
    class UpdatePatternButton extends AbstractButton {
        public UpdatePatternButton(int i, int j) {
            super(i, j, 16, 16, Component.translatable("container.spawn.fish_customizer.pattern"));
        }

        @Override
        public void onPress() {
            ItemStack item = menu.resultSlot.getItem();
            CompoundTag compoundTag = item.getTag();
            if (compoundTag != null) {
                int tag = compoundTag.getInt("BucketVariantTag");
                Seahorse.Pattern pattern = Seahorse.getPattern(tag);
                int newId = (pattern.getPackedId() >> 8) + 1;
                int shifted = newId << 8;
                Seahorse.Pattern newPattern = Seahorse.Pattern.byId(shifted);
                if (newPattern != null) {
                    optional = Optional.of(newPattern);
                }
            }
            FishCustomizerScreen.this.menu.onButtonClick(player,false, optional);
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
            int l = 16;
            if (this.isHovered()) {
                l = 32;
            }
            this.setTooltip(Tooltip.create(Component.translatable("container.spawn.fish_customizer.pattern"), null));
            guiGraphics.blit(RESOURCE_LOCATION, this.getX(), this.getY(), 192, l, this.width, this.height);
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }
}
