package io.github.tau34.mte.mixin.gui;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.tau34.mte.MTELang;
import io.github.tau34.mte.common.holder.IGuiFactoryHolder;
import io.github.tau34.mte.common.holder.IScrollableFactoryElement;
import io.github.tau34.mte.common.holder.IGuiSlotHolder;
import io.github.tau34.mte.common.holder.IInventoryContainerSlotHolder;
import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.client.SpecialColors;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.button.TranslationButton;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.machine.GuiFactory;
import mekanism.client.render.IFancyFontRenderer;
import mekanism.common.inventory.container.slot.InventoryContainerSlot;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntityFactory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = GuiFactory.class, remap = false)
public abstract class GuiFactoryMixin extends GuiMekanismTileMixin<TileEntityFactory<?>> implements IGuiFactoryHolder {
    @Shadow protected abstract GuiProgress addProgress(GuiProgress progressBar);

    @Unique
    private int pageIndex = 0;

    @Unique
    private int maxPage = 1;

    @Unique
    private int indexPerPage = 0;

    @Unique
    private List<IScrollableFactoryElement> slots = new ArrayList<>();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void saveProcesses(MekanismTileContainer<?> container, Inventory inv, Component title, CallbackInfo ci) {
        indexPerPage = switch (tile.tier) {
            case BASIC -> 3;
            case ADVANCED -> 5;
            case ELITE -> 7;
            case ULTIMATE -> 9;
        };
        maxPage = (int) Math.ceil(MTEUtils.getProcesses(tile.tier, tile) / (double) indexPerPage);
    }

    @Redirect(method = "addGuiElements", at = @At(value = "FIELD", target = "Lmekanism/common/tier/FactoryTier;processes:I"))
    private int modifySlots(FactoryTier instance) {
        return MTEUtils.getProcesses(instance, tile);
    }

    @Inject(method = "addGuiElements", at = @At("HEAD"))
    private void addPageButton(CallbackInfo ci) {
        ((GuiMekanismAccessor) this).invokeAddRenderableWidget(new TranslationButton(((IGuiWrapper) this), 0, -20, 16, 16, MTELang.FACTORY_PAGE_PREVIOUS, () -> update(-1)));
        ((GuiMekanismAccessor) this).invokeAddRenderableWidget(new TranslationButton(((IGuiWrapper) this), 100, -20, 16, 16, MTELang.FACTORY_PAGE_NEXT, () -> update(1)));
    }

    @Redirect(method = "addGuiElements", at = @At(value = "INVOKE", target = "Lmekanism/client/gui/machine/GuiFactory;addProgress(Lmekanism/client/gui/element/progress/GuiProgress;)Lmekanism/client/gui/element/progress/GuiProgress;"))
    private GuiProgress modifyProgresses(GuiFactory instance, GuiProgress progressBar, @Local(ordinal = 3) int cacheIndex) {
        if (progressBar instanceof IScrollableFactoryElement holder) {
            holder.setProcessingIndex(cacheIndex);
            holder.setTier(tile.tier);
            slots.add(holder);
        }
        return addProgress(progressBar);
    }

    @Inject(method = "drawForegroundText", at = @At(value = "INVOKE", target = "Lmekanism/client/gui/machine/GuiFactory;drawString(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/network/chat/Component;III)I"))
    private void addPageDisplay(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        ((IFancyFontRenderer) this).drawString(guiGraphics, MTELang.FACTORY_PAGE.translate(pageIndex + 1, maxPage), 42, -12, SpecialColors.TAB_ITEM_CONFIG.argb());
    }

    @Override
    protected void onAddSlot(InventoryContainerSlot slot, GuiSlot gui) {
        if (slot instanceof IInventoryContainerSlotHolder holder && holder.hasProcessingIndex()) {
            if (gui instanceof IScrollableFactoryElement sfe) {
                sfe.setProcessingIndex(holder.getProcessingIndex());
                sfe.setTier(tile.tier);
                slots.add(sfe);
            }
        }
    }

    @Unique
    private void update(int i) {
        pageIndex = (pageIndex + i) % maxPage;
        for (IScrollableFactoryElement holder : slots) {
            if (holder.hasProcessingIndex()) {
                int index = holder.getProcessingIndex();
                holder.setVisible(index >= pageIndex * indexPerPage && index < (pageIndex + 1) * indexPerPage);
            }
        }
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public int getMaxPage() {
        return maxPage;
    }
}
