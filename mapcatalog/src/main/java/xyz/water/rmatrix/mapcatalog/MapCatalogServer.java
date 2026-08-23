package xyz.water.rmatrix.mapcatalog;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.water.rmatrix.mapcatalog.server.MapCatalogServerService;

public final class MapCatalogServer implements ModInitializer {
    public static final String MOD_ID = "mapcatalog";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MapCatalogServerService.initialize();
        LOGGER.info("MapCatalog server protocol initialized");
    }
}
