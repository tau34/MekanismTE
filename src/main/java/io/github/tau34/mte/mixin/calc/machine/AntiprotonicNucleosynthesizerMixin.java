package io.github.tau34.mte.mixin.calc.machine;

import io.github.tau34.mte.Enhancement;
import io.github.tau34.mte.common.holder.IAdditionalEnhanceable;
import mekanism.common.tile.machine.TileEntityAntiprotonicNucleosynthesizer;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Set;

@Mixin(value = TileEntityAntiprotonicNucleosynthesizer.class, remap = false)
public class AntiprotonicNucleosynthesizerMixin implements IAdditionalEnhanceable {
    @Override
    public Set<Enhancement> supported() {
        return Set.of(Enhancement.ENERGY);
    }
}
