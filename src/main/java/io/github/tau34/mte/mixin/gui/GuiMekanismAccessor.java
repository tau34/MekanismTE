package io.github.tau34.mte.mixin.gui;

import mekanism.client.gui.GuiMekanism;
import mekanism.client.gui.element.GuiElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = GuiMekanism.class, remap = false)
public interface GuiMekanismAccessor {
    @Invoker(value = "addRenderableWidget")
    <T extends GuiElement> T invokeAddRenderableWidget(T element);
}