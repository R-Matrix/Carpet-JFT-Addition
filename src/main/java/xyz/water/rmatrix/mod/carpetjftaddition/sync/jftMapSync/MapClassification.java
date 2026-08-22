//#if MC >= 12104
package xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync;

import java.util.List;

public record MapClassification(
        boolean hasExplorationMarker,
        List<BannerMarker> banners
) {
    public MapClassification {
        banners = banners == null ? List.of() : List.copyOf(banners);
    }
}
//#endif
