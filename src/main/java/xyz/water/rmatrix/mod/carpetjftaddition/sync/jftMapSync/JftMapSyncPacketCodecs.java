//#if MC >= 12104
package xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class JftMapSyncPacketCodecs {
    public static final int jft$MAX_BATCH_ENTRIES = 256;
    private static final int jft$MAX_BANNERS_PER_MAP = 256;

    static final PacketCodec<ByteBuf, UUID> jft$UUID_CODEC = PacketCodec.tuple(
            PacketCodecs.LONG,
            UUID::getMostSignificantBits,
            PacketCodecs.LONG,
            UUID::getLeastSignificantBits,
            UUID::new
    );

    static final PacketCodec<ByteBuf, SyncMode> jft$SYNC_MODE_CODEC = PacketCodecs.indexed(
            index -> SyncMode.values()[index],
            SyncMode::ordinal
    );

    static final PacketCodec<RegistryByteBuf, BannerMarker> jft$BANNER_MARKER_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER,
            BannerMarker::worldX,
            PacketCodecs.INTEGER,
            BannerMarker::worldZ,
            DyeColor.PACKET_CODEC,
            BannerMarker::color,
            TextCodecs.OPTIONAL_PACKET_CODEC,
            BannerMarker::name,
            BannerMarker::new
    );

    static final PacketCodec<RegistryByteBuf, MapClassification> jft$CLASSIFICATION_CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN,
            MapClassification::hasExplorationMarker,
            PacketCodecs.collection(ArrayList::new, jft$BANNER_MARKER_CODEC, jft$MAX_BANNERS_PER_MAP),
            MapClassification::banners,
            MapClassification::new
    );

    static final PacketCodec<RegistryByteBuf, ServerMapInfo> jft$MAP_INFO_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            ServerMapInfo::mapId,
            Identifier.PACKET_CODEC,
            ServerMapInfo::dimension,
            PacketCodecs.INTEGER,
            ServerMapInfo::centerX,
            PacketCodecs.INTEGER,
            ServerMapInfo::centerZ,
            PacketCodecs.BYTE,
            ServerMapInfo::scale,
            PacketCodecs.BOOLEAN,
            ServerMapInfo::locked,
            jft$CLASSIFICATION_CODEC,
            ServerMapInfo::classification,
            ServerMapInfo::new
    );

    static final PacketCodec<RegistryByteBuf, List<ServerMapInfo>> jft$MAP_LIST_CODEC =
            PacketCodecs.collection(ArrayList::new, jft$MAP_INFO_CODEC, jft$MAX_BATCH_ENTRIES);

    private JftMapSyncPacketCodecs() {
    }
}
//#endif
