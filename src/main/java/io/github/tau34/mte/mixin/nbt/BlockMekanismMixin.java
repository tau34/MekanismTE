package io.github.tau34.mte.mixin.nbt;

import io.github.tau34.mte.common.holder.ITileEntityMekanismHolder;
import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.common.block.BlockMekanism;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockMekanism.class, remap = false)
public class BlockMekanismMixin {
    @Inject(method = "setPlacedBy", at = @At(value = "TAIL"))
    private void modifyRead(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, world, pos);
        if (tile != null && MTEUtils.supportsEnhancement(tile) && tile instanceof ITileEntityMekanismHolder holder) {
            CompoundTag dataMap = ItemDataUtils.getDataMapIfPresent(stack);
            if (dataMap == null) {
                dataMap = new CompoundTag();
            }
            holder.getEnhancementComponent().read(dataMap);
        }
    }
}
