package io.github.tau34.mte.mixin.gui;

import io.github.tau34.mte.common.holder.IFactorySlotHolder;
import mekanism.common.inventory.slot.OutputInventorySlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = OutputInventorySlot.class)
public class OutputInventorySlotMixin implements IFactorySlotHolder {
    @Unique
    private int processingData = -1;

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
