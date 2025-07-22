package io.github.tau34.mte.mixin.gui;

import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.client.gui.machine.GuiFactory;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntityFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GuiFactory.class, remap = false)
public class GuiFactoryMixin extends GuiMekanismTileMixin<TileEntityFactory<?>> {
    @Redirect(method = "addGuiElements", at = @At(value = "FIELD", target = "Lmekanism/common/tier/FactoryTier;processes:I"))
    private int modifySlots(FactoryTier instance) {
        return MTEUtils.getProcesses(instance, tile);
    }
}
