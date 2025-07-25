package io.github.tau34.mte.mixin.calc;

import mekanism.common.block.attribute.Attribute;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.content.blocktype.BlockTypeTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BlockType.class, remap = false)
public abstract class BlockTypeMixin {
    @Shadow
    protected abstract void setFrom(BlockTypeTile<?> tile, Class<? extends Attribute>... types);

    @Shadow
    public abstract void add(Attribute... attrs);
}
