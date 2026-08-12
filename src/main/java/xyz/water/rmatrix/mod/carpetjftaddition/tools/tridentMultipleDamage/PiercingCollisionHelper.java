package xyz.water.rmatrix.mod.carpetjftaddition.tools.tridentMultipleDamage;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class PiercingCollisionHelper {
    public static float getToleranceMargin(Entity entity) {
        return Math.max(0.0F, Math.min(0.3F, (float) (entity.age - 2) / 20.0F));
    }

    public static Collection<EntityHitResult> collect(World world, Entity entity, Vec3d from, Vec3d to, Box box, Predicate<Entity> hitPredicate) {
        return collect(world, entity, from, to, box, hitPredicate, getToleranceMargin(entity));
    }

    public static Collection<EntityHitResult> collect(World world, Entity entity, Vec3d from, Vec3d to, Box box, Predicate<Entity> hitPredicate, float hitboxMargin) {
        List<EntityHitResult> list = new ArrayList<>();

        for (Entity entity2 : world.getOtherEntities(entity, box, hitPredicate)) {
            Box box2 = entity2.getBoundingBox().expand(hitboxMargin);
            if (box2.contains(from)) {
                list.add(new EntityHitResult(entity2, from));
            } else {
                Optional<Vec3d> optional = box2.raycast(from, to);
                optional.ifPresent(pos -> list.add(new EntityHitResult(entity2, pos)));
            }
        }

        return list;
    }
}
