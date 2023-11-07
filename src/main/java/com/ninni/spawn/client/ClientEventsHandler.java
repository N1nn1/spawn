package com.ninni.spawn.client;

import com.ninni.spawn.client.inventory.HamsterInventoryMenu;
import com.ninni.spawn.client.inventory.HamsterInventoryScreen;
import com.ninni.spawn.entity.Hamster;
import com.ninni.spawn.network.OpenHamsterScreenPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

public class ClientEventsHandler {

    public static void openHamsterInventoryScreen(OpenHamsterScreenPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer clientPlayer = minecraft.player;
        Entity entity = null;
        if (clientPlayer != null) {
            entity = clientPlayer.level().getEntity(packet.getEntityId());
        }
        if (entity instanceof Hamster hamster) {
            SimpleContainer inventory = new SimpleContainer(packet.getSize());
            Inventory playerInventory = clientPlayer.getInventory();
            HamsterInventoryMenu reindeerContainer = new HamsterInventoryMenu(packet.getContainerId(), playerInventory, inventory, hamster);
            clientPlayer.containerMenu = reindeerContainer;
            HamsterInventoryScreen reindeerScreen = new HamsterInventoryScreen(reindeerContainer, playerInventory, hamster);
            minecraft.setScreen(reindeerScreen);
        }
    }

}
