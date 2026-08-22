package xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

public record MapSyncBatchS2C(
        List<ServerMapInfo> maps
) implements CustomPayload {
    public static final CustomPayload.Id<MapSyncBatchS2C> ID =
            new CustomPayload.Id<>(Identifier.of("carpetjftaddition", "jftm_sync_batch"));

    public static final PacketCodec<RegistryByteBuf, MapSyncBatchS2C> CODEC = PacketCodec.tuple(
            JftMapSyncPacketCodecs.jft$MAP_LIST_CODEC,
            MapSyncBatchS2C::maps,
            MapSyncBatchS2C::new
    );

    public MapSyncBatchS2C {
        maps = maps == null ? List.of() : List.copyOf(maps);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
