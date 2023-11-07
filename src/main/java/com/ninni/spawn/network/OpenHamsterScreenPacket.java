package com.ninni.spawn.network;

import com.ninni.spawn.client.ClientEventsHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenHamsterScreenPacket {
    private final int containerId;
    private final int size;
    private final int entityId;

    public OpenHamsterScreenPacket(int id, int size, int entityId) {
        this.containerId = id;
        this.size = size;
        this.entityId = entityId;
    }

    public static OpenHamsterScreenPacket read(FriendlyByteBuf buf) {
        int containerId = buf.readUnsignedByte();
        int size = buf.readVarInt();
        int entityId = buf.readInt();
        return new OpenHamsterScreenPacket(containerId, size, entityId);
    }

    public static void write(OpenHamsterScreenPacket packet, FriendlyByteBuf buf) {
        buf.writeByte(packet.containerId);
        buf.writeVarInt(packet.size);
        buf.writeInt(packet.entityId);
    }

    public static void handle(OpenHamsterScreenPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientEventsHandler.openHamsterInventoryScreen(packet));
        ctx.get().setPacketHandled(true);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public int getSize() {
        return this.size;
    }

    public int getEntityId() {
        return this.entityId;
    }
}
