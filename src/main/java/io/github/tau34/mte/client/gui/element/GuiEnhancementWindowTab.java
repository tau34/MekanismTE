package io.github.tau34.mte.client.gui.element;

import io.github.tau34.mte.MTELang;
import mekanism.client.SpecialColors;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.tab.window.GuiWindowCreatorTab;
import mekanism.client.gui.element.window.GuiWindow;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class GuiEnhancementWindowTab extends GuiWindowCreatorTab<TileEntityMekanism, GuiEnhancementWindowTab> {

    public GuiEnhancementWindowTab(IGuiWrapper gui, TileEntityMekanism tile, Supplier<GuiEnhancementWindowTab> elementSupplier) {
        super(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "upgrade.png"), gui, tile, gui.getWidth() - 18, -20, 26, 18, false, elementSupplier);
    }

    @Override
    public void renderToolTip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderToolTip(guiGraphics, mouseX, mouseY);
        displayTooltips(guiGraphics, mouseX, mouseY, MTELang.ENHANCEMENTS.translate());
    }

    @Override
    protected void colorTab(GuiGraphics guiGraphics) {
        MekanismRenderer.color(guiGraphics, SpecialColors.TAB_UPGRADE);
    }

    @Override
    protected GuiWindow createWindow() {
        return new GuiEnhancementWindow(gui(), getGuiWidth() / 2 - 156 / 2, 15, dataSource);
    }
}