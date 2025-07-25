package io.github.tau34.mte;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum MTELang implements ILangEntry {
    MTE("constants", "mod_name"),
    ENHANCEMENTS("gui", "enhancements"),
    UPGRADE_PROCESSING("upgrade", "processing"),
    UPGRADE_PROCESSING_DESCRIPTION("upgrade", "processing.description"),
    FACTORY_PAGE_PREVIOUS("gui", "factory.page.previous"),
    FACTORY_PAGE_NEXT("gui", "factory.page.next"),
    FACTORY_PAGE("gui", "factory.page");

    private final String key;

    MTELang(String type, String path) {
        this.key = Util.makeDescriptionId(type, MTEMod.rl(path));
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
