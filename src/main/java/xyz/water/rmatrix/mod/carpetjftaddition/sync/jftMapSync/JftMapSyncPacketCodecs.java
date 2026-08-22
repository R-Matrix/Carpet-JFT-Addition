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

    private static final PacketCodec<ByteBuf, Long> jft$LONG_CODEC = PacketCodec.ofStatic(
            (buf, value) -> buf.writeLong(value),
            ByteBuf::readLong
    );

    static final PacketCodec<ByteBuf, UUID> jft$UUID_CODEC = PacketCodec.tuple(
            jft$LONG_CODEC,
            UUID::getMostSignificantBits,
            jft$LONG_CODEC,
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

    //#if MC >= 12103
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
    //#else
    //$$ private static final PacketCodec<RegistryByteBuf, Jft$MapInfoPrefix> jft$MAP_INFO_PREFIX_CODEC = PacketCodec.tuple(
    //$$         PacketCodecs.VAR_INT,
    //$$         Jft$MapInfoPrefix::mapId,
    //$$         Identifier.PACKET_CODEC,
    //$$         Jft$MapInfoPrefix::dimension,
    //$$         PacketCodecs.INTEGER,
    //$$         Jft$MapInfoPrefix::centerX,
    //$$         PacketCodecs.INTEGER,
    //$$         Jft$MapInfoPrefix::centerZ,
    //$$         PacketCodecs.BYTE,
    //$$         Jft$MapInfoPrefix::scale,
    //$$         PacketCodecs.BOOL,
    //$$         Jft$MapInfoPrefix::locked,
    //$$         Jft$MapInfoPrefix::new
    //$$ );
    //$$
    //$$ static final PacketCodec<RegistryByteBuf, ServerMapInfo> jft$MAP_INFO_CODEC = PacketCodec.tuple(
    //$$         jft$MAP_INFO_PREFIX_CODEC,
    //$$         info -> new Jft$MapInfoPrefix(
    //$$                 info.mapId(),
    //$$                 info.dimension(),
    //$$                 info.centerX(),
    //$$                 info.centerZ(),
    //$$                 info.scale(),
    //$$                 info.locked()
    //$$         ),
    //$$         jft$CLASSIFICATION_CODEC,
    //$$         ServerMapInfo::classification,
    //$$         (prefix, classification) -> new ServerMapInfo(
    //$$                 prefix.mapId(),
    //$$                 prefix.dimension(),
    //$$                 prefix.centerX(),
    //$$                 prefix.centerZ(),
    //$$                 prefix.scale(),
    //$$                 prefix.locked(),
    //$$                 classification
    //$$         )
    //$$ );
    //#endif

    static final PacketCodec<RegistryByteBuf, List<ServerMapInfo>> jft$MAP_LIST_CODEC =
            PacketCodecs.collection(ArrayList::new, jft$MAP_INFO_CODEC, jft$MAX_BATCH_ENTRIES);

    private JftMapSyncPacketCodecs() {
    }

    private record Jft$MapInfoPrefix(
            int mapId,
            Identifier dimension,
            int centerX,
            int centerZ,
            byte scale,
            boolean locked
    ) {
    }
}
