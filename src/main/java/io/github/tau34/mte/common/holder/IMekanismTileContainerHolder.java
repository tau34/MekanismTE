package io.github.tau34.mte.common.holder;

import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;

import java.util.List;

public interface IMekanismTileContainerHolder {
    List<VirtualInventoryContainerSlot> getContainerSlots();
}
