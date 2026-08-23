package xyz.water.rmatrix.mod.carpetjftaddition.integration;

import xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings;

import java.util.function.BooleanSupplier;

/**
 * Optional Fabric entrypoint consumed by MapCatalog when Carpet JFT Addition is loaded.
 */
public final class MapCatalogCarpetRuleEntrypoint implements BooleanSupplier {
    @Override
    public boolean getAsBoolean() {
        return CarpetJFTSettings.jftMapSyncProtocol;
    }
}
