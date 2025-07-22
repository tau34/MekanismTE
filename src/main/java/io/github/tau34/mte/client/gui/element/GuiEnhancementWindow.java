package io.github.tau34.mte.client.gui.element;

import io.github.tau34.mte.MTELang;
import io.github.tau34.mte.common.holder.IMekanismTileContainerHolder;
import io.github.tau34.mte.common.holder.ITileEntityMekanismHolder;
import io.github.tau34.mte.common.network.MTEPacketGuiInteract;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.slot.GuiVirtualSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.gui.element.window.GuiWindow;
import mekanism.common.Mekanism;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.SelectedWindowData;
import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.network.to_server.PacketGuiInteract;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.client.gui.GuiGraphics;

public class GuiEnhancementWindow extends GuiWindow {
    private final TileEntityMekanism tile;

    public GuiEnhancementWindow(IGuiWrapper gui, int x, int y, TileEntityMekanism tile) {
        super(gui, x, y, 156, 100, SelectedWindowData.WindowType.UPGRADE);
        this.tile = tile;
        MekanismTileContainer<?> container = (MekanismTileContainer<?>) ((GuiMekanism<?>) gui()).getMenu();
        if (container instanceof IMekanismTileContainerHolder holder) {
            int index = 0;
            for (VirtualInventoryContainerSlot slot : holder.getContainerSlots()) {
                addChild(new GuiVirtualSlot(this, SlotType.NORMAL, gui, relativeX + 18 + 18 * index++, relativeY + 18, slot));
            }
        }
        if (tile instanceof ITileEntityMekanismHolder holder) {
            container.startTracking(4, holder.getEnhancementComponent());
            Mekanism.packetHandler().sendToServer(new MTEPacketGuiInteract(MTEPacketGuiInteract.MTEGuiInteraction.CONTAINER_TRACK_ENHANCEMENTS, tile, 4));
        }
    }

    @Override
    public void close() {
        super.close();
        if (tile instanceof ITileEntityMekanismHolder) {
            Mekanism.packetHandler().sendToServer(new PacketGuiInteract(PacketGuiInteract.GuiInteraction.CONTAINER_STOP_TRACKING, tile, 4));
            ((MekanismContainer) ((GuiMekanism<?>) gui()).getMenu()).stopTracking(4);
        }
    }

    @Override
    public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderForeground(guiGraphics, mouseX, mouseY);
        drawTitleText(guiGraphics, MTELang.ENHANCEMENTS.translate(), 5);
    }
}
