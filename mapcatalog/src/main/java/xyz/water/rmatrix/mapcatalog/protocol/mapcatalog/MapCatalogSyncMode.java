package xyz.water.rmatrix.mapcatalog.protocol.mapcatalog;

/** Wire-order-sensitive synchronization modes defined by MapCatalog. */
public enum MapCatalogSyncMode {
    FULL,
    DELTA,
    NO_CHANGE,
    DENIED
}
