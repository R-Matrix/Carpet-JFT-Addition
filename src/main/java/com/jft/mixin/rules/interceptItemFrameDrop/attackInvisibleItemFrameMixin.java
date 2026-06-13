package com.jft.mixin.rules.interceptItemFrameDrop;

import com.jft.toolsMager.canInterceptItemFrameDropBlock.isOffsetSpacialBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static com.jft.CarpetJFTSettings.interceptItemFrameDrop;

@Mixin(ItemFrameEntity.class)
public abstract class attackInvisibleItemFrameMixin extends AbstractDecorationEntity implements isOffsetSpacialBlock {

    protected attackInvisibleItemFrameMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/AbstractDecorationEntity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", ordinal = 1), cancellable = true)
    private void interceptItemFrameDrop(ServerWorld world, DamageSource source, float amount, @NotNull CallbackInfoReturnable<Boolean> cir){
        if(interceptItemFrameDrop.equals("false")) return;
        BlockState blockState = this.getWorld().getBlockState(this.attachedBlockPos.offset(this.facing.getOpposite()));
        if(!Jft$canInterceptItemFrameDropBlock.contains(blockState)) return;
        if(source.getAttacker() instanceof PlayerEntity player){
            if(player.isInCreativeMode()) return;
            if(Jft$canInterceptItemFrameDropItem(player.getMainHandStack().getItem())) return;
            cir.cancel();
        }
    }

    @Unique
    private static List<BlockState> Jft$canInterceptItemFrameDropBlock = new ArrayList<>(
            List.of(
                    Blocks.DRIED_KELP_BLOCK.getDefaultState(),
                    Blocks.GRAY_CONCRETE.getDefaultState()
            )
    );

    @Unique
    private static boolean Jft$canInterceptItemFrameDropItem(Item item){
        boolean bl1 = item.equals(Items.ITEM_FRAME) || item.equals(Items.GLOW_ITEM_FRAME);
        boolean bl2 = item.equals(Items.AIR) && interceptItemFrameDrop.equals("allowEmptyHand");
        boolean bl3 = item.getDefaultStack().isIn(ItemTags.PICKAXES) || item.getDefaultStack().isIn(ItemTags.AXES);
        return bl1 || bl2 || bl3;
    }

    @Override
    public boolean Jft$isOffsetSpacialBlock(){
        BlockState blockState = this.getWorld().getBlockState(this.attachedBlockPos.offset(this.facing.getOpposite()));
        return Jft$canInterceptItemFrameDropBlock.contains(blockState);
    }


}
