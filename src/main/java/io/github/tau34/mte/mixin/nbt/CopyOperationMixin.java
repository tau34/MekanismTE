package io.github.tau34.mte.mixin.nbt;

import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CopyNbtFunction.CopyOperation.class)
public interface CopyOperationMixin {
    @Accessor("sourcePathText")
    String getSource();
}
