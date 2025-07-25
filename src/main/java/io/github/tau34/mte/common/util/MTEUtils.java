package io.github.tau34.mte.common.util;

import io.github.tau34.mte.Enhancement;
import io.github.tau34.mte.common.holder.IAdditionalEnhanceable;
import io.github.tau34.mte.common.holder.ICapabilityTileEntityHolder;
import io.github.tau34.mte.common.holder.ITileEntityMekanismHolder;
import mekanism.api.Upgrade;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.IUpgradeTile;

public class MTEUtils {
    public static double fractionEnhancement(IUpgradeTile tile, Enhancement enhancement) {
        return getEnhancement(tile, enhancement) / 8.0D;
    }

    public static int getEnhancement(IUpgradeTile tile, Enhancement enhancement) {
        if (tile instanceof ITileEntityMekanismHolder holder) {
            return holder.getEnhancementComponent().getEnhancements(enhancement);
        }
        return 0;
    }

    public static int getProcesses(FactoryTier tier, Object tile) {
        int slots = tier.processes;
        if (tile instanceof ICapabilityTileEntityHolder holder) {
            slots += holder.getProcessingData();
        }
        return slots;
    }

    public static boolean supportsEnhancement(IUpgradeTile tile) {
        return tile.supportsUpgrades() || tile instanceof IAdditionalEnhanceable;
    }

    public static boolean supportsUpgrade(TileEntityMekanism instance, Upgrade upgrade) {
        return instance.supportsUpgrade(upgrade) || (instance instanceof ITileEntityMekanismHolder holder && holder.getEnhancementComponent().supports(upgrade));
    }
}
