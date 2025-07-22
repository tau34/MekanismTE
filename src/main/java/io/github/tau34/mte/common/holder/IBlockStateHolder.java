package io.github.tau34.mte.common.holder;

import net.minecraft.world.level.block.state.BlockState;

public interface IBlockStateHolder {
    BlockState setProcessingData(int additional, boolean isClient);

    int getProcessingData(boolean isClient);
}
