package io.github.tau34.mte.mixin.gui;

import io.github.tau34.mte.common.holder.IInventoryContainerSlotHolder;
import io.github.tau34.mte.common.inventory.slot.EnhancementInventorySlot;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.slot.BasicInventorySlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InventoryContainerSlot.class, remap = false)
public class InventoryContainerSlotMixin implements IInventoryContainerSlotHolder {
    @Shadow @Final private BasicInventorySlot slot;

    @Unique
    private int processingData = -1;

    @Inject(method = "setChanged", at = @At("TAIL"))
    private void setEnhancementSlotChanged(CallbackInfo ci) {
        if (slot instanceof EnhancementInventorySlot eis) {
            eis.onChange();
        }
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
