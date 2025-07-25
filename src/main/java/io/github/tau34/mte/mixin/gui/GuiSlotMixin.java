package io.github.tau34.mte.mixin.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.tau34.mte.common.holder.IScrollableFactoryElement;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.common.tier.FactoryTier;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = GuiSlot.class, remap = false)
public class GuiSlotMixin extends GuiElementMixin implements IScrollableFactoryElement {
    @Unique
    private int processingData = -1;

    @Unique
    private boolean visible = true;

    @Unique
    private int xMult = 0;

    @Unique
    private int indexPerPage = 0;

    @Override
    public int getProcessingIndex() {
        return processingData;
    }

    @Override
    public void setProcessingIndex(int i) {
        processingData = i;
    }

    @Override
    public boolean hasProcessingIndex() {
        return processingData != -1;
    }

    @Override
    public void setVisible(boolean b) {
        visible = b;
    }

    @Override
    protected void translateElement(PoseStack pose) {
        if (processingData != -1) {
            int xOffset = Mth.floorDiv(processingData, indexPerPage) * xMult * indexPerPage * -1;
            int yOffset = visible ? 0 : -100000;
            pose.translate(xOffset, yOffset, 0);
        }
    }

    @Override
    public void setTier(FactoryTier t) {
        xMult = t == FactoryTier.BASIC ? 38 : t == FactoryTier.ADVANCED ? 26 : 19;
        indexPerPage = t.processes;
    }
}
