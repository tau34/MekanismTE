package io.github.tau34.mte.mixin.tile;

import io.github.tau34.mte.common.holder.IFactorySlotHolder;
import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.api.IContentsListener;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.FactoryInputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntitySawingFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = TileEntitySawingFactory.class, remap = false)
public abstract class SawingFactoryMixin {
    @Redirect(method = "addSlots", at = @At(value = "FIELD", target = "Lmekanism/common/tier/FactoryTier;processes:I"))
    private int modifySlot(FactoryTier instance) {
        return MTEUtils.getProcesses(instance, this);
    }

    @Inject(method = "addSlots", at = @At(value = "INVOKE", target = "Lmekanism/common/tile/factory/TileEntityFactory$ProcessInfo;<init>(ILmekanism/common/inventory/slot/FactoryInputInventorySlot;Lmekanism/api/inventory/IInventorySlot;Lmekanism/api/inventory/IInventorySlot;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void saveInfoToSlot(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener, CallbackInfo ci,
                                int baseX, int baseXMult, int i, int xPos, OutputInventorySlot outputSlot, OutputInventorySlot secondaryOutputSlot, FactoryInputInventorySlot inputSlot, int index) {
        if (outputSlot instanceof IFactorySlotHolder holder) {
            holder.setProcessingIndex(i);
        }
    }
}
