package io.github.tau34.mte.common.registries;

import io.github.tau34.mte.Enhancement;
import io.github.tau34.mte.MTEMod;
import io.github.tau34.mte.common.item.ItemEnhancement;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;

public class MTEItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(MTEMod.MOD_ID);

    public static final ItemRegistryObject<ItemEnhancement> SPEED_ENHANCEMENT = registerUpgrade(Enhancement.SPEED);
    public static final ItemRegistryObject<ItemEnhancement> ENERGY_ENHANCEMENT = registerUpgrade(Enhancement.ENERGY);
    public static final ItemRegistryObject<ItemEnhancement> ECO_ENHANCEMENT = registerUpgrade(Enhancement.ECO);
    public static final ItemRegistryObject<ItemEnhancement> PROCESSING_ENHANCEMENT = registerUpgrade(Enhancement.PROCESSING);

    private static ItemRegistryObject<ItemEnhancement> registerUpgrade(Enhancement type) {
        return ITEMS.register("enhancement_" + type.getRawName(), properties -> new ItemEnhancement(type, properties));
    }
}
