//#if MC >= 12104
package xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync;

import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

import java.util.Optional;

public record BannerMarker(
        int worldX,
        int worldZ,
        DyeColor color,
        Optional<Text> name
) {
    public BannerMarker {
        name = name == null ? Optional.empty() : name;
    }
}
//#endif
