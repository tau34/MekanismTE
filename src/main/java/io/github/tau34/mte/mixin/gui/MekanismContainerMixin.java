package io.github.tau34.mte.mixin.gui;

import mekanism.common.inventory.container.MekanismContainer;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = MekanismContainer.class, remap = false)
public interface MekanismContainerMixin {
    @Invoker(value = "addSlot")
    Slot invokeAddSlot(Slot slot);
}
