package xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record MapSyncEndS2C(
        UUID worldSessionId,
        int highestMapId
) implements CustomPayload {
    public static final CustomPayload.Id<MapSyncEndS2C> ID =
            new CustomPayload.Id<>(Identifier.of("carpetjftaddition", "jftm_sync_end"));

    public static final PacketCodec<RegistryByteBuf, MapSyncEndS2C> CODEC = PacketCodec.tuple(
            JftMapSyncPacketCodecs.jft$UUID_CODEC,
            MapSyncEndS2C::worldSessionId,
            PacketCodecs.VAR_INT,
            MapSyncEndS2C::highestMapId,
            MapSyncEndS2C::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
