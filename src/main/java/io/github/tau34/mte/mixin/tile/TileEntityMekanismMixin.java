package io.github.tau34.mte.mixin.tile;

import io.github.tau34.mte.common.holder.IProcessingEnhanceable;
import io.github.tau34.mte.common.holder.ITileEntityMekanismHolder;
import io.github.tau34.mte.common.tile.component.TileComponentEnhancement;
import io.github.tau34.mte.mixin.data.CapabilityTileEntityMixin;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.interfaces.IRedstoneControl;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = TileEntityMekanism.class, remap = false)
public abstract class TileEntityMekanismMixin extends CapabilityTileEntityMixin implements ITileEntityMekanismHolder {
    @Unique protected TileComponentEnhancement enhancementComponent;

    @Unique private boolean isLoadedServer = false;

    @Unique private boolean isLoadedClient = false;

    @Shadow public abstract boolean supportsUpgrades();

    @Shadow @Final private List<ITileComponent> components;

    @Shadow protected boolean redstone;

    @Shadow protected abstract List<ITileComponent> getComponents();

    @Shadow public abstract void setControlType(@NotNull IRedstoneControl.RedstoneControl type);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addEnhancements(IBlockProvider blockProvider, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (supportsUpgrades()) {
            components.add(enhancementComponent = new TileComponentEnhancement((TileEntityMekanism) (Object) this));
        }
    }

    @Inject(method = "blockRemoved", at = @At("RETURN"))
    private void onBlockRemoved(CallbackInfo ci) {
        removeProcessingData();
    }

    @Inject(method = "onUpdateServer", at = @At("TAIL"))
    private void reloadEnhancementServer(CallbackInfo ci) {
        if (!isLoadedServer && this instanceof IProcessingEnhanceable holder) {
            holder.getEnhancementComponent().replaceBlock();
            isLoadedServer = true;
        }
    }

    @Inject(method = "onUpdateClient", at = @At("TAIL"))
    private void reloadEnhancementClient(CallbackInfo ci) {
        if (!isLoadedClient && this instanceof IProcessingEnhanceable holder) {
            holder.getEnhancementComponent().replaceBlock();
            isLoadedClient = true;
        }
    }

    @Override
    public TileComponentEnhancement getEnhancementComponent() {
        return enhancementComponent;
    }
}
