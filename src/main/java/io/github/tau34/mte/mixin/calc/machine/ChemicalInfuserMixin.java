package io.github.tau34.mte.mixin.calc.machine;

import io.github.tau34.mte.Enhancement;
import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.api.Upgrade;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.machine.TileEntityChemicalInfuser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityChemicalInfuser.class, remap = false)
public class ChemicalInfuserMixin {
    @Redirect(method = "recalculateUpgrades", at = @At(value = "INVOKE", target = "Lmekanism/common/tile/component/TileComponentUpgrade;getUpgrades(Lmekanism/api/Upgrade;)I"))
    private int modifyUpgrades(TileComponentUpgrade instance, Upgrade upgrade) {
        return instance.getUpgrades(upgrade) + MTEUtils.getEnhancement((TileEntityChemicalInfuser)(Object) this, Enhancement.SPEED);
    }
}
