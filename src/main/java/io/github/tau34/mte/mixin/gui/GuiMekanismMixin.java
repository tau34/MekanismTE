package io.github.tau34.mte.mixin.gui;

import com.llamalad7.mixinextras.sugar.Local;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = GuiMekanism.class, remap = false)
public class GuiMekanismMixin {
    @Unique
    protected void onAddSlot(InventoryContainerSlot slot, GuiSlot gui) {
    }

    @Inject(method = "addSlots", at = @At(value = "INVOKE", target = "Lmekanism/client/gui/GuiMekanism;addRenderableWidget(Lmekanism/client/gui/element/GuiElement;)Lmekanism/client/gui/element/GuiElement;", ordinal = 0))
    private void modifySlots(CallbackInfo ci, @Local(ordinal = 0) InventoryContainerSlot containerSlot, @Local(ordinal = 0) GuiSlot guiSlot) {
        onAddSlot(containerSlot, guiSlot);
    }
}