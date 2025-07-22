package io.github.tau34.mte.mixin.nbt;

import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(CopyNbtFunction.class)
public interface CopyNbtFunctionAccessor {
    @Accessor("operations")
    @Mutable
    void setOperations(List<CopyNbtFunction.CopyOperation> ops);
}
