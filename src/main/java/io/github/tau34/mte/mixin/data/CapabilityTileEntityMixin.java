package io.github.tau34.mte.mixin.data;

import io.github.tau34.mte.common.holder.IBlockStateHolder;
import io.github.tau34.mte.common.holder.ICapabilityTileEntityHolder;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.CapabilityTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CapabilityTileEntity.class, remap = false)
public class CapabilityTileEntityMixin implements ICapabilityTileEntityHolder {
    @Unique
    private IBlockStateHolder h;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addEnhancements(TileEntityTypeRegistryObject<?> type, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (state instanceof IBlockStateHolder holder) {
            h = holder;
        }
    }

    @Override
    public int getProcessingData() {
        return h == null ? 0 : h.getProcessingData(EffectiveSide.get() == LogicalSide.CLIENT);
    }

    protected void removeProcessingData() {
        if (h == null) return;
        h.setProcessingData(0, true);
        h.setProcessingData(0, false);
    }
}
