package io.github.tau34.mte.mixin.calc;

import io.github.tau34.mte.common.holder.ITileEntityMekanismHolder;
import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.MekanismUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MachineEnergyContainer.class, remap = false)
public abstract class MachineEnergyContainerMixin<T extends TileEntityMekanism> {
    @Shadow public abstract void setEnergyPerTick(FloatingLong energyPerTick);

    @Shadow public abstract FloatingLong getBaseEnergyPerTick();

    @Shadow @Final protected T tile;

    @Redirect(method = "updateMaxEnergy", at = @At(value = "INVOKE", target = "Lmekanism/common/tile/base/TileEntityMekanism;supportsUpgrade(Lmekanism/api/Upgrade;)Z"))
    private boolean modifyMaxEnergy(TileEntityMekanism instance, Upgrade upgrade) {
        return MTEUtils.supportsUpgrade(instance, upgrade);
    }

    @Inject(method = "updateEnergyPerTick", at = @At(value = "HEAD"), cancellable = true)
    private void modifyEnergyPerTick(CallbackInfo ci) {
        if (MTEUtils.supportsEnhancement(tile)) {
            if (MTEUtils.supportsUpgrade(tile, Upgrade.ENERGY) || MTEUtils.supportsUpgrade(tile, Upgrade.SPEED)) {
                setEnergyPerTick(MekanismUtils.getEnergyPerTick(tile, getBaseEnergyPerTick()));
            }
        }
        ci.cancel();
    }
}
