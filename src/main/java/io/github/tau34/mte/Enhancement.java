package io.github.tau34.mte;

import mekanism.api.text.APILang;
import mekanism.api.text.IHasTranslationKey;
import mekanism.api.text.ILangEntry;
import net.minecraft.network.chat.Component;

public enum Enhancement implements IHasTranslationKey {
    SPEED("speed", APILang.UPGRADE_SPEED, APILang.UPGRADE_SPEED_DESCRIPTION),
    ENERGY("energy", APILang.UPGRADE_ENERGY, APILang.UPGRADE_ENERGY_DESCRIPTION),
    ECO("eco", APILang.UPGRADE_ENERGY, APILang.UPGRADE_ENERGY_DESCRIPTION),
    PROCESSING("processing", MTELang.UPGRADE_PROCESSING, MTELang.UPGRADE_PROCESSING_DESCRIPTION);

    private final String name;
    private final ILangEntry langKey;
    private final ILangEntry descLangKey;

    Enhancement(String name, ILangEntry langKey, ILangEntry descLangKey) {
        this.name = name;
        this.langKey = langKey;
        this.descLangKey = descLangKey;
    }

    public String getRawName() {
        return name;
    }

    @Override
    public String getTranslationKey() {
        return langKey.getTranslationKey();
    }

    public Component getDescription() {
        return descLangKey.translate();
    }
}
