package io.github.tau34.mte.mixin.gui;

import io.github.tau34.mte.common.holder.IMekanismTileContainerHolder;
import io.github.tau34.mte.common.holder.ITileEntityMekanismHolder;
import io.github.tau34.mte.common.inventory.slot.EnhancementInventorySlot;
import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = MekanismTileContainer.class, remap = false)
public class MekanismTileContainerMixin<T extends TileEntityMekanism> implements IMekanismTileContainerHolder {
    @Shadow @Final @NotNull protected T tile;

    @Unique
    private final List<VirtualInventoryContainerSlot> slots = new ArrayList<>();

    @Inject(method = "addSlots", at = @At("TAIL"))
    private void addEnhancementSlots(CallbackInfo ci) {
        if (this.tile.supportsUpgrades()) {
            if (this.tile instanceof ITileEntityMekanismHolder holder) {
                for (EnhancementInventorySlot slot : holder.getEnhancementComponent().getSlots()) {
                    VirtualInventoryContainerSlot vcs = slot.createContainerSlot();
                    slots.add(vcs);
                    ((MekanismContainerMixin) this).invokeAddSlot(vcs);
                }
            }
        }
    }

    @Override
    public List<VirtualInventoryContainerSlot> getContainerSlots() {
        return slots;
    }
}
