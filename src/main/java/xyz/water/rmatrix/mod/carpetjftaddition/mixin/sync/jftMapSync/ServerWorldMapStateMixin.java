//#if MC >= 12104
package xyz.water.rmatrix.mod.carpetjftaddition.mixin.sync.jftMapSync;

import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;
import xyz.water.rmatrix.mod.carpetjftaddition.sync.jftMapSync.JftMapSyncService;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMapStateMixin {
    @Inject(method = "putMapState", at = @At("TAIL"))
    private void jft$afterPutMapState(MapIdComponent mapId, MapState mapState, CallbackInfo ci) {
        if(CarpetJFTSettings.jftMapSyncProtocol) {
            JftMapSyncService.jft$onMapStatePut((ServerWorld) (Object) this, mapId, mapState);
        }
    }
}
//#endif
