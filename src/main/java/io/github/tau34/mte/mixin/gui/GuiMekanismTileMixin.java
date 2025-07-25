package io.github.tau34.mte.mixin.gui;

import io.github.tau34.mte.client.gui.element.GuiEnhancementWindowTab;
import io.github.tau34.mte.common.util.MTEUtils;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = GuiMekanismTile.class, remap = false)
public class GuiMekanismTileMixin<T extends TileEntityMekanism> extends GuiMekanismMixin {
    @Shadow @Final protected T tile;

    @Nullable @Unique private GuiEnhancementWindowTab enhancementWindowTab;

    @Inject(method = "addGenericTabs", at = @At("TAIL"))
    private void addEnhancementTabs(CallbackInfo ci) {
        if (MTEUtils.supportsEnhancement(this.tile)) {
            enhancementWindowTab = ((GuiMekanismAccessor) this).invokeAddRenderableWidget(new GuiEnhancementWindowTab(
                    ((GuiMekanismTile<?, ?>) (Object) this), this.tile, () -> enhancementWindowTab));
        }
    }
}
