package io.github.tau34.mte.common.holder;

import mekanism.common.tier.FactoryTier;

public interface IGuiSlotHolder {
    int getProcessingIndex();

    void setProcessingIndex(int i);

    boolean hasProcessingIndex();

    void setVisible(boolean b);

    void setTier(FactoryTier t);
}
