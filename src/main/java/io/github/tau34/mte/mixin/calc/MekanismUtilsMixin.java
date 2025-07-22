package io.github.tau34.mte.mixin.calc;

import io.github.tau34.mte.Enhancement;
import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.api.math.MathUtils;
import mekanism.common.config.MekanismConfig;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.MekanismUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MekanismUtils.class, remap = false)
public abstract class MekanismUtilsMixin {
    @Shadow
    public static double fractionUpgrades(IUpgradeTile tile, Upgrade type) {
        return 0;
    }

    /*
    @Inject(method = "fractionUpgrades", at = @At("HEAD"), cancellable = true)
    private static void modifyUpgrades(IUpgradeTile tile, Upgrade type, CallbackInfoReturnable<Double> cir) {
        if (tile.supportsUpgrade(type)) {
            if (tile instanceof ITileEntityMekanismHolder holder) {
                //LogUtils.getLogger().debug("{}, Upgrades: {}", type.getRawName(), tile.getComponent().getUpgrades(type));
                //LogUtils.getLogger().debug("{}, Enhancements: {}", type.getRawName(), holder.getEnhancementComponent().getEnhancements(type));
                cir.setReturnValue((tile.getComponent().getUpgrades(type) +
                        holder.getEnhancementComponent().getEnhancements(type)) / (double) type.getMax());
            } else {
                cir.setReturnValue(tile.getComponent().getUpgrades(type) / (double) type.getMax());
            }
        } else {
            cir.setReturnValue(0.0D);
        }
    }
    */

    @Inject(method = "getBaseUsage", at = @At("HEAD"), cancellable = true)
    private static void modifyBaseUsage(IUpgradeTile tile, int def, CallbackInfoReturnable<Long> cir) {
        if (tile.supportsUpgrades()) {
            if (tile.supportsUpgrade(Upgrade.GAS)) {
                cir.setReturnValue(Math.round(def * Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(),
                        fractionUpgrades(tile, Upgrade.SPEED) + MTEUtils.fractionEnhancement(tile, Enhancement.SPEED)
                                - fractionUpgrades(tile, Upgrade.GAS))));
                return;
            }
        }
        cir.setReturnValue((long) def);
    }

    @Inject(method = "getTicks", at = @At("HEAD"), cancellable = true)
    private static void modifyTicks(IUpgradeTile tile, int def, CallbackInfoReturnable<Integer> cir) {
        if (tile.supportsUpgrades()) {
            cir.setReturnValue(MathUtils.clampToInt(def * Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(),
                    -fractionUpgrades(tile, Upgrade.SPEED) - MTEUtils.fractionEnhancement(tile, Enhancement.SPEED))));
            return;
        }
        cir.setReturnValue(def);
    }

    @Inject(method = "getEnergyPerTick", at = @At("HEAD"), cancellable = true)
    private static void modifyEnergyPerTick(IUpgradeTile tile, FloatingLong def, CallbackInfoReturnable<FloatingLong> cir) {
        if (tile.supportsUpgrades()) {
            cir.setReturnValue(def.multiply(Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(),
                    2 * (fractionUpgrades(tile, Upgrade.SPEED) + MTEUtils.fractionEnhancement(tile, Enhancement.SPEED))
                            - (fractionUpgrades(tile, Upgrade.ENERGY) + MTEUtils.fractionEnhancement(tile, Enhancement.ECO)))));
            return;
        }
        cir.setReturnValue(def);
    }

    @Inject(method = "getGasPerTickMeanMultiplier", at = @At("HEAD"), cancellable = true)
    private static void modifyGasPerTick(IUpgradeTile tile, CallbackInfoReturnable<Double> cir) {
        if (tile.supportsUpgrades()) {
            if (tile.supportsUpgrade(Upgrade.GAS)) {
                cir.setReturnValue(Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(),
                        2 * (fractionUpgrades(tile, Upgrade.SPEED) + MTEUtils.fractionEnhancement(tile, Enhancement.SPEED))
                                - fractionUpgrades(tile, Upgrade.GAS)));
                return;
            }
            cir.setReturnValue(Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(),
                    fractionUpgrades(tile, Upgrade.SPEED) + MTEUtils.fractionEnhancement(tile, Enhancement.SPEED)));
            return;
        }
        cir.setReturnValue(1.0D);
    }

    @Inject(method = "getMaxEnergy(Lmekanism/common/tile/interfaces/IUpgradeTile;Lmekanism/api/math/FloatingLong;)Lmekanism/api/math/FloatingLong;", at = @At("HEAD"), cancellable = true)
    private static void modifyMaxEnergy(IUpgradeTile tile, FloatingLong def, CallbackInfoReturnable<FloatingLong> cir) {
        if (tile.supportsUpgrades()) {
            cir.setReturnValue(def.multiply(Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(),
                    fractionUpgrades(tile, Upgrade.ENERGY) + MTEUtils.fractionEnhancement(tile, Enhancement.ENERGY))));
            return;
        }
        cir.setReturnValue(def);
    }
}
