package com.ninni.spawn.network;

import com.ninni.spawn.Spawn;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class SpawnNetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
                    new ResourceLocation(Spawn.MOD_ID, "network"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    protected static int packetID = 0;

    public SpawnNetworkHandler() {
    }

    public static void init() {
        INSTANCE.registerMessage(getPacketID(), OpenHamsterScreenPacket.class, OpenHamsterScreenPacket::write, OpenHamsterScreenPacket::read, OpenHamsterScreenPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static int getPacketID() {
        return packetID++;
    }
}
