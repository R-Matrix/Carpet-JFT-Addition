//#if MC >= 12104
package xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record MapSyncStartS2C(
        SyncMode syncMode,
        UUID worldSessionId,
        int highestMapId,
        int entryCount
) implements CustomPayload {
    public static final CustomPayload.Id<MapSyncStartS2C> ID =
            new CustomPayload.Id<>(Identifier.of("carpetjftaddition", "jftm_sync_start"));

    public static final PacketCodec<RegistryByteBuf, MapSyncStartS2C> CODEC = PacketCodec.tuple(
            JftMapSyncPacketCodecs.jft$SYNC_MODE_CODEC,
            MapSyncStartS2C::syncMode,
            JftMapSyncPacketCodecs.jft$UUID_CODEC,
            MapSyncStartS2C::worldSessionId,
            PacketCodecs.VAR_INT,
            MapSyncStartS2C::highestMapId,
            PacketCodecs.VAR_INT,
            MapSyncStartS2C::entryCount,
            MapSyncStartS2C::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
//#endif
