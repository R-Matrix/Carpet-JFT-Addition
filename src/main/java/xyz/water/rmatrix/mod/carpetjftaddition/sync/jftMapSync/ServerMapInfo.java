package xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync;

import net.minecraft.util.Identifier;

public record ServerMapInfo(
        int mapId,
        Identifier dimension,
        int centerX,
        int centerZ,
        byte scale,
        boolean locked,
        MapClassification classification
) {
    public ServerMapInfo {
        classification = classification == null
                ? new MapClassification(false, java.util.List.of())
                : classification;
    }
}
