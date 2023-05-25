package com.ninni.spawn.client.inventory;

import com.ninni.spawn.entity.Hamster;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class HamsterInventoryMenu extends AbstractContainerMenu {
    private final Container hamster;

    public HamsterInventoryMenu(int i, Inventory inventory, Container container, final Hamster hamster) {
        super(null, i);

        this.hamster = container;
        container.startOpen(inventory.player);
        int l;
        int m;
        for (m = 0; m < 2; m++) {
            for (l = 0; l < 3; l++) {
                this.addSlot(new Slot(container, l + m * 3, 8 + l * 18, 36 + m * 18));
            }
        }
        for (m = 0; m < 2; m++) {
            for (l = 0; l < 3; l++) {
                this.addSlot(new Slot(container, l + m * 3 + 6, 116 + l * 18, 36 + m * 18));
            }
        }
        for (l = 0; l < 3; ++l) {
            for (m = 0; m < 9; ++m) {
                this.addSlot(new Slot(inventory, m + l * 9 + 9, 8 + m * 18, 102 + l * 18 - 18));
            }
        }
        for (l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack;
        Slot slot = this.slots.get(i);
        ItemStack itemStack2 = slot.getItem();
        itemStack = itemStack2.copy();
        if (slot.hasItem()) {
            if (i > 11 && !this.moveItemStackTo(itemStack2, 0, 12, false)) {
                return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(itemStack2, 12, 48, true)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }
            slot.setChanged();
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemStack2);
            this.broadcastChanges();
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.hamster.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.hamster.stopOpen(player);
    }
}
