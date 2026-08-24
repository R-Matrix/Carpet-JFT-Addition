package xyz.water.rmatrix.mapcatalog.mixin;

import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.water.rmatrix.mapcatalog.server.MapCatalogServerService;

import java.util.Objects;

/** Notifies MapCatalog when a map's persistent banner markers change. */
@Mixin(MapState.class)
public abstract class MapStateMapCatalogMixin {
    @Unique
    private MapBannerMarker mapcatalog$bannerBeforeRemoval;

    @Inject(method = "addBanner", at = @At("RETURN"))
    private void mapcatalog$afterAddBanner(
            net.minecraft.world.WorldAccess world,
            net.minecraft.util.math.BlockPos pos,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (cir.getReturnValue()) {
            MapCatalogServerService.mapcatalog$onMapStateBannerChanged((MapState) (Object) this);
        }
    }

    @Inject(method = "removeBanner", at = @At("HEAD"))
    private void mapcatalog$beforeRemoveBanner(
            net.minecraft.world.BlockView world,
            int x,
            int z,
            CallbackInfo ci
    ) {
        MapState mapState = (MapState) (Object) this;
        mapcatalog$bannerBeforeRemoval = mapState.getBanners().stream()
                .filter(banner -> banner.pos().getX() == x && banner.pos().getZ() == z)
                .findFirst()
                .orElse(null);
    }

    @Inject(method = "removeBanner", at = @At("TAIL"))
    private void mapcatalog$afterRemoveBanner(
            net.minecraft.world.BlockView world,
            int x,
            int z,
            CallbackInfo ci
    ) {
        MapState mapState = (MapState) (Object) this;
        MapBannerMarker after = mapState.getBanners().stream()
                .filter(banner -> banner.pos().getX() == x && banner.pos().getZ() == z)
                .findFirst()
                .orElse(null);
        if (!Objects.equals(mapcatalog$bannerBeforeRemoval, after)) {
            MapCatalogServerService.mapcatalog$onMapStateBannerChanged(mapState);
        }
        mapcatalog$bannerBeforeRemoval = null;
    }
}
