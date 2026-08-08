package xyz.water.rmatrix.mod.carpetjftaddition.mixin.rules.spawnBabyEntityOnYellowGlass;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings.spawnEggGreenGlassLargeVariant;
import static xyz.water.rmatrix.mod.carpetjftaddition.CarpetJFTSettings.spawnEggYellowGlassSmallVariant;

@Mixin(SpawnEggItem.class)
public abstract class SpawnEggItemMixin {

    @WrapOperation(method = "useOnBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/EntityType;spawnFromItemStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"))
    private Entity spawnEggUseVariant(EntityType<? extends Entity> instance,
                      ServerWorld world,
                      ItemStack stack,
                      PlayerEntity player,
                      BlockPos pos, SpawnReason spawnReason,
                      boolean alignPosition, boolean invertY,
                      Operation<Entity> original,
                      ItemUsageContext context) {

        Entity entity = original.call(instance, world, stack, player, pos, spawnReason, alignPosition, invertY);
        if (entity == null) {
            return null;
        }

        NbtComponent entityData = stack.get(DataComponentTypes.ENTITY_DATA);
        boolean hasCustomNbt = entityData != null && !entityData.isEmpty();
        if (hasCustomNbt) {
            return entity;
        }

        BlockState clickedBlock = world.getBlockState(context.getBlockPos());
        boolean small = spawnEggYellowGlassSmallVariant
                && clickedBlock.isOf(Blocks.YELLOW_STAINED_GLASS);
        boolean large = spawnEggGreenGlassLargeVariant
                && clickedBlock.isOf(Blocks.GREEN_STAINED_GLASS);
        if (!small && !large) {
            return entity;
        }

        if (entity instanceof SlimeEntity slime) {
            slime.setSize(small ? SlimeEntity.MIN_SIZE : 4, true);
        } else if (entity instanceof MobEntity mob) {
            mob.setBaby(small);
        }
        return entity;
    }
}
