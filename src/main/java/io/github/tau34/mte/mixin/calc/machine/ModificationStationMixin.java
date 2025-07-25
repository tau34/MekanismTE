package io.github.tau34.mte.mixin.calc.machine;

import io.github.tau34.mte.Enhancement;
import io.github.tau34.mte.common.holder.IAdditionalEnhanceable;
import io.github.tau34.mte.mixin.tile.TileEntityMekanismMixin;
import mekanism.api.Upgrade;
import mekanism.common.tile.TileEntityModificationStation;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.MekanismUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(value = TileEntityModificationStation.class, remap = false)
public abstract class ModificationStationMixin extends TileEntityMekanismMixin implements IAdditionalEnhanceable {
    @Shadow public int ticksRequired;

    @Shadow @Final private static int BASE_TICKS_REQUIRED;

    @Override
    protected void recalculateAdditional(Upgrade upgrade) {
        ticksRequired = Math.max(1, MekanismUtils.getTicks((IUpgradeTile) this, BASE_TICKS_REQUIRED));
    }

    @Override
    public Set<Enhancement> supported() {
        return Set.of(Enhancement.ENERGY, Enhancement.ECO, Enhancement.SPEED);
    }
}
