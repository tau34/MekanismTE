package io.github.tau34.mte.mixin.gui;

import io.github.tau34.mte.common.holder.IFactorySlotHolder;
import io.github.tau34.mte.common.holder.IInventoryContainerSlotHolder;
import io.github.tau34.mte.common.holder.IMekanismTileContainerHolder;
import io.github.tau34.mte.common.holder.ITileEntityMekanismHolder;
import io.github.tau34.mte.common.inventory.slot.EnhancementInventorySlot;
import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.slot.FactoryInputInventorySlot;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = MekanismTileContainer.class, remap = false)
public class MekanismTileContainerMixin<T extends TileEntityMekanism> implements IMekanismTileContainerHolder {
    @Shadow @Final @NotNull protected T tile;

    @Unique
    private final List<VirtualInventoryContainerSlot> slots = new ArrayList<>();

    /*@Unique
    protected @NotNull List<IInventorySlot> getSlots(TileEntityMekanism instance, @Nullable Direction side) {
        return instance.getInventorySlots(side);
    }

    @Redirect(method = "addSlots", at = @At(value = "INVOKE", target = "Lmekanism/common/tile/base/TileEntityMekanism;getInventorySlots(Lnet/minecraft/core/Direction;)Ljava/util/List;"))
    private @NotNull List<IInventorySlot> hideFactorySlots(TileEntityMekanism instance, @Nullable Direction side) {
        return getSlots(instance, side);
    }*/

    @Inject(method = "addSlots", at = @At("TAIL"))
    private void addEnhancementSlots(CallbackInfo ci) {
        if (MTEUtils.supportsEnhancement(this.tile)) {
            if (this.tile instanceof ITileEntityMekanismHolder holder) {
                for (EnhancementInventorySlot slot : holder.getEnhancementComponent().getSlots()) {
                    VirtualInventoryContainerSlot vcs = slot.createContainerSlot();
                    slots.add(vcs);
                    ((MekanismContainerMixin) this).invokeAddSlot(vcs);
                }
            }
        }
    }

    @Redirect(method = "addSlots", at = @At(value = "INVOKE", target = "Lmekanism/api/inventory/IInventorySlot;createContainerSlot()Lnet/minecraft/world/inventory/Slot;"))
    private Slot saveInfoToSlot(IInventorySlot instance) {
        Slot res = instance.createContainerSlot();
        if (instance instanceof IFactorySlotHolder fsh) {
            if (res instanceof IInventoryContainerSlotHolder holder && fsh.hasProcessingIndex()) {
                holder.setProcessingIndex(fsh.getProcessingIndex());
            }
        }
        return res;
    }

    @Override
    public List<VirtualInventoryContainerSlot> getContainerSlots() {
        return slots;
    }
}
