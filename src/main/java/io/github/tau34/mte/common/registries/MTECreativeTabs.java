package io.github.tau34.mte.common.registries;

import io.github.tau34.mte.MTELang;
import io.github.tau34.mte.MTEMod;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;

public class MTECreativeTabs {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(MTEMod.MOD_ID);

    public static final CreativeTabRegistryObject MTE = CREATIVE_TABS.registerMain(MTELang.MTE, MTEItems.SPEED_ENHANCEMENT, builder ->
            builder.withSearchBar()
                    .displayItems((displayParameters, output) -> CreativeTabDeferredRegister.addToDisplay(MTEItems.ITEMS, output))
    );
}
