package io.github.tau34.mte.common.item;

import io.github.tau34.mte.Enhancement;
import mekanism.api.Upgrade;
import mekanism.api.text.APILang;
import mekanism.api.text.EnumColor;
import mekanism.client.key.MekKeyHandler;
import mekanism.client.key.MekanismKeyHandler;
import mekanism.common.MekanismLang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemEnhancement extends Item {
    private final Enhancement enhancement;

    public ItemEnhancement(Enhancement type, Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
        enhancement = type;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (MekKeyHandler.isKeyPressed(MekanismKeyHandler.detailsKey)) {
            Enhancement enhancementType = getEnhancementType(stack);
            tooltip.add(enhancementType.getDescription());
        } else {
            tooltip.add(MekanismLang.HOLD_FOR_DETAILS.translateColored(EnumColor.GRAY, EnumColor.INDIGO, MekanismKeyHandler.detailsKey.getTranslatedKeyMessage()));
        }
    }

    public Enhancement getEnhancementType(ItemStack stack) {
        return enhancement;
    }
}
