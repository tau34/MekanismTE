package io.github.tau34.mte.mixin.tile;

import io.github.tau34.mte.common.holder.IProcessingEnhanceable;
import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.upgrade.MachineUpgradeData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = TileEntityFactory.class, remap = false)
public abstract class TileEntityFactoryMixin extends TileEntityMekanismMixin implements IProcessingEnhanceable {
    @Shadow @Final public int[] progress;

    @Shadow @Final protected List<IInventorySlot> outputSlots;

    @Shadow @Final protected List<IInventorySlot> inputSlots;

    @Shadow private EnergyInventorySlot energySlot;

    @Shadow private boolean sorting;

    @Shadow public abstract MachineEnergyContainer<TileEntityFactory<?>> getEnergyContainer();

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lmekanism/common/tier/FactoryTier;processes:I"))
    private int modifySlots(FactoryTier instance) {
        return MTEUtils.getProcesses(instance, this);
    }

    @Redirect(method = "presetVariables", at = @At(value = "FIELD", target = "Lmekanism/common/tier/FactoryTier;processes:I"))
    private int modifySlots1(FactoryTier instance) {
        return MTEUtils.getProcesses(instance, this);
    }

    @Redirect(method = "load", at = @At(value = "FIELD", target = "Lmekanism/common/tier/FactoryTier;processes:I"))
    private int modifySlots2(FactoryTier instance) {
        return MTEUtils.getProcesses(instance, this);
    }

    @Inject(method = "parseUpgradeData", at = @At(value = "HEAD"), cancellable = true)
    private void log(IUpgradeData upgradeData, CallbackInfo ci) {
        if (upgradeData instanceof MachineUpgradeData data) {
            redstone = data.redstone;
            setControlType(data.controlType);
            getEnergyContainer().setEnergy(data.energyContainer.getEnergy());
            sorting = data.sorting;
            energySlot.deserializeNBT(data.energySlot.serializeNBT());
            System.arraycopy(data.progress, 0, progress, 0, Math.min(progress.length, data.progress.length));
            for (int i = 0; i < Math.min(inputSlots.size(), data.inputSlots.size()); i++) {
                inputSlots.get(i).deserializeNBT(data.inputSlots.get(i).serializeNBT());
            }
            for (int i = 0; i < Math.min(outputSlots.size(), data.outputSlots.size()); i++) {
                outputSlots.get(i).setStack(data.outputSlots.get(i).getStack());
            }
            for (ITileComponent component : getComponents()) {
                component.read(data.components);
            }
            ci.cancel();
        }
    }
}
