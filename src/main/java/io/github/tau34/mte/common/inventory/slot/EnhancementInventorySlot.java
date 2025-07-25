package io.github.tau34.mte.common.inventory.slot;

import io.github.tau34.mte.Enhancement;
import io.github.tau34.mte.common.item.ItemEnhancement;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.common.inventory.container.SelectedWindowData;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.slot.VirtualInventoryContainerSlot;
import mekanism.common.inventory.slot.BasicInventorySlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;

public class EnhancementInventorySlot extends BasicInventorySlot {
    private final Runnable onChange;

    public static EnhancementInventorySlot input(@Nullable IContentsListener listener, Set<Enhancement> supportedTypes, Runnable onChange) {
        Objects.requireNonNull(supportedTypes, "Supported types cannot be null");
        return new EnhancementInventorySlot(listener, (stack, automationType) -> {
            Item item = stack.getItem();
            if (item instanceof ItemEnhancement enhancementItem) {
                Enhancement enhancementType = enhancementItem.getEnhancementType(stack);
                return supportedTypes.contains(enhancementType);
            }
            return false;
        }, onChange);
    }

    private EnhancementInventorySlot(@Nullable IContentsListener listener, BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInsert, Runnable onChange) {
        super(manualOnly, canInsert, stack -> stack.getItem() instanceof ItemEnhancement, listener, 0, 0);
        setSlotOverlay(SlotOverlay.UPGRADE);
        this.onChange = onChange;
    }

    @NotNull
    @Override
    public VirtualInventoryContainerSlot createContainerSlot() {
        return new VirtualInventoryContainerSlot(this, new SelectedWindowData(SelectedWindowData.WindowType.UPGRADE), getSlotOverlay(), this::setStackUnchecked);
    }

    public void onChange() {
        onChange.run();
    }

    @Override
    public int getLimit(ItemStack stack) {
        return 64;
    }
}
