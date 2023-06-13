package com.ninni.spawn.mixin;

import com.ninni.spawn.SpawnClient;
import com.ninni.spawn.client.inventory.HamsterInventoryMenu;
import com.ninni.spawn.entity.Hamster;
import com.ninni.spawn.entity.HamsterOpenContainer;
import com.ninni.spawn.registry.SpawnVanillaIntegration;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundHorseScreenOpenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements HamsterOpenContainer {
    @Shadow protected abstract void nextContainerCounter();

    @Shadow protected abstract void initMenu(AbstractContainerMenu abstractContainerMenu);

    @Shadow private int containerCounter;

    @Override
    public void openHamsterInventory(Hamster hamster, Container container) {
        ServerPlayer $this = (ServerPlayer) (Object) this;
        if ($this.containerMenu != $this.inventoryMenu) {
            $this.closeContainer();
        }
        this.nextContainerCounter();
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(hamster.getId());
        buf.writeInt(container.getContainerSize());
        buf.writeInt(this.containerCounter);
        ServerPlayNetworking.send($this, SpawnVanillaIntegration.OPEN_HAMSTER_SCREEN, buf);
        $this.connection.send(new ClientboundHorseScreenOpenPacket(this.containerCounter, container.getContainerSize(), hamster.getId()));
        $this.containerMenu = new HamsterInventoryMenu(this.containerCounter, $this.getInventory(), container, hamster);
        this.initMenu($this.containerMenu);
    }
}
