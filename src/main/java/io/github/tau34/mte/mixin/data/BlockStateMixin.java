package io.github.tau34.mte.mixin.data;

import io.github.tau34.mte.common.holder.IBlockStateHolder;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockState.class)
public class BlockStateMixin implements IBlockStateHolder {
    @Unique
    private int processingData = 0;
    @Unique
    private int clientProcessingData = 0;

    @Override
    public BlockState setProcessingData(int additional, boolean isClient) {
        if (isClient) {
            clientProcessingData = additional;
        } else {
            processingData = additional;
        }
        return (BlockState) (Object) this;
    }

    @Override
    public int getProcessingData(boolean isClient) {
        return isClient ? clientProcessingData : processingData;
    }
}
