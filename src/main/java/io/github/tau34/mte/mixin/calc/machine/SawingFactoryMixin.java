package io.github.tau34.mte.mixin.calc.machine;

import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntitySawingFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntitySawingFactory.class, remap = false)
public abstract class SawingFactoryMixin {
    @Redirect(method = "addSlots", at = @At(value = "FIELD", target = "Lmekanism/common/tier/FactoryTier;processes:I"))
    private int modifySlot(FactoryTier instance) {
        return MTEUtils.getProcesses(instance, this);
    }
}
