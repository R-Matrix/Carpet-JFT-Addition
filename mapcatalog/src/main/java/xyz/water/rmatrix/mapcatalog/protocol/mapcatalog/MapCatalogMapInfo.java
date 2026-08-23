package xyz.water.rmatrix.mapcatalog.protocol.mapcatalog;

import net.minecraft.util.Identifier;

/** Immutable map metadata transported by MapCatalog. */
public record MapCatalogMapInfo(
        int mapId,
        Identifier dimension,
        int centerX,
        int centerZ,
        byte scale,
        boolean locked,
        MapCatalogClassification classification
) {
    public MapCatalogMapInfo {
        classification = classification == null
                ? new MapCatalogClassification(false, java.util.List.of())
                : classification;
    }
}
