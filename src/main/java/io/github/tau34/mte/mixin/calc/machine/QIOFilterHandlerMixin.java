package io.github.tau34.mte.mixin.calc.machine;

import io.github.tau34.mte.Enhancement;
import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.api.Upgrade;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.qio.TileEntityQIOFilterHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityQIOFilterHandler.class, remap = false)
public class QIOFilterHandlerMixin {
    @Redirect(method = "getMaxTransitTypes", at = @At(value = "INVOKE", target = "Lmekanism/common/tile/component/TileComponentUpgrade;getUpgrades(Lmekanism/api/Upgrade;)I"))
    private int modifyMaxTransitTypes(TileComponentUpgrade instance, Upgrade upgrade) {
        return instance.getUpgrades(upgrade) + MTEUtils.getEnhancement((TileEntityQIOFilterHandler)(Object) this, Enhancement.SPEED);
    }

    @Redirect(method = "getMaxTransitCount", at = @At(value = "INVOKE", target = "Lmekanism/common/tile/component/TileComponentUpgrade;getUpgrades(Lmekanism/api/Upgrade;)I"))
    private int modifyMaxTransitCount(TileComponentUpgrade instance, Upgrade upgrade) {
        return instance.getUpgrades(upgrade) + MTEUtils.getEnhancement((TileEntityQIOFilterHandler)(Object) this, Enhancement.SPEED);
    }
}
