package xyz.water.rmatrix.mapcatalog.protocol.mapcatalog;

import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

import java.util.Optional;

/** A persistent banner decoration transported by MapCatalog. */
public record MapCatalogBanner(
        int worldX,
        int worldZ,
        DyeColor color,
        Optional<Text> name
) {
    public MapCatalogBanner {
        name = name == null ? Optional.empty() : name;
    }
}
