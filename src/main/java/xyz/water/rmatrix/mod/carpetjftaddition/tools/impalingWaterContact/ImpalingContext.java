package xyz.water.rmatrix.mod.carpetjftaddition.tools.impalingWaterContact;

import net.minecraft.entity.Entity;

public final class ImpalingContext {
    public static final ThreadLocal<Boolean> IS_IMPALING = new ThreadLocal<>();
    public static final ThreadLocal<Entity> USER = new ThreadLocal<>();
}
