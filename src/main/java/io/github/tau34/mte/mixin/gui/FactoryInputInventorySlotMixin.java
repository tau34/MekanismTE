package io.github.tau34.mte.mixin.gui;

import io.github.tau34.mte.common.holder.IFactorySlotHolder;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.FactoryInputInventorySlot;
import mekanism.common.tile.factory.TileEntityFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FactoryInputInventorySlot.class, remap = false)
public class FactoryInputInventorySlotMixin implements IFactorySlotHolder {
    @Unique
    private int processingData = -1;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void saveInfoToSlot(TileEntityFactory<?> factory, int process, IInventorySlot outputSlot, IInventorySlot secondaryOutputSlot, IContentsListener listener, int x, int y, CallbackInfo ci) {
        processingData = process;
    }

    @Override
    public int getProcessingIndex() {
        return processingData;
    }

    @Override
    public void setProcessingIndex(int i) {
        processingData = i;
    }

    @Override
    public boolean hasProcessingIndex() {
        return processingData != -1;
    }
}
