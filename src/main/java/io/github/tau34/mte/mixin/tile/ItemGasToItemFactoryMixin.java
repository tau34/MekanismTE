package io.github.tau34.mte.mixin.tile;

import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntityItemStackGasToItemStackFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityItemStackGasToItemStackFactory.class, remap = false)
public class ItemGasToItemFactoryMixin {
    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lmekanism/common/tier/FactoryTier;processes:I"))
    private int modifySlots(FactoryTier instance) {
        return MTEUtils.getProcesses(instance, this);
    }

    @Redirect(method = "getInitialGasTanks", at = @At(value = "FIELD", target = "Lmekanism/common/tier/FactoryTier;processes:I"))
    private int modifySlots1(FactoryTier instance) {
        return MTEUtils.getProcesses(instance, this);
    }

    @Redirect(method = "load", at = @At(value = "FIELD", target = "Lmekanism/common/tier/FactoryTier;processes:I"))
    private int modifySlots2(FactoryTier instance) {
        return MTEUtils.getProcesses(instance, this);
    }
}
