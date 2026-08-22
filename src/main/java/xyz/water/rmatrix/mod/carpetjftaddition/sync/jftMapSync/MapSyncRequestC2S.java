package xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record MapSyncRequestC2S(
        int protocolVersion,
        UUID worldSessionId,
        int knownMaxMapId,
        boolean forceFullSync
) implements CustomPayload {
    public static final CustomPayload.Id<MapSyncRequestC2S> ID =
            new CustomPayload.Id<>(Identifier.of("carpetjftaddition", "jftm_sync_request"));

    public static final PacketCodec<RegistryByteBuf, MapSyncRequestC2S> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            MapSyncRequestC2S::protocolVersion,
            JftMapSyncPacketCodecs.jft$UUID_CODEC,
            MapSyncRequestC2S::worldSessionId,
            PacketCodecs.VAR_INT,
            MapSyncRequestC2S::knownMaxMapId,
            PacketCodecs.BOOLEAN,
            MapSyncRequestC2S::forceFullSync,
            MapSyncRequestC2S::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
