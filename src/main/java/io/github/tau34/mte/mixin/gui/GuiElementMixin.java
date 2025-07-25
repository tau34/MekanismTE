package io.github.tau34.mte.mixin.gui;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.client.gui.element.GuiElement;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiElement.class, remap = false)
public class GuiElementMixin {
    @Unique
    protected void translateElement(PoseStack pose) {
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void translate(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci, @Local(ordinal = 0) PoseStack pose) {
        translateElement(pose);
    }
}
