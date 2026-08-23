package xyz.water.rmatrix.mapcatalog.mixin;

import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.water.rmatrix.mapcatalog.server.MapCatalogServerService;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMapCatalogMixin {
    @Inject(method = "putMapState", at = @At("TAIL"))
    private void mapcatalog$onPutMapState(MapIdComponent mapId, MapState mapState, CallbackInfo ci) {
        MapCatalogServerService.mapcatalog$onMapStatePut((ServerWorld) (Object) this, mapId, mapState);
    }
}
