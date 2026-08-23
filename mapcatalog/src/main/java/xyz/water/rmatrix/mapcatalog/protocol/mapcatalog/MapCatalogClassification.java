package xyz.water.rmatrix.mapcatalog.protocol.mapcatalog;

import java.util.List;

/** Classification metadata transported by MapCatalog. */
public record MapCatalogClassification(
        boolean hasExplorationMarker,
        List<MapCatalogBanner> banners
) {
    public MapCatalogClassification {
        banners = banners == null ? List.of() : List.copyOf(banners);
    }
}
