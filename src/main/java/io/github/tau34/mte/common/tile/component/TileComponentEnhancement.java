package io.github.tau34.mte.common.tile.component;

import com.mojang.logging.LogUtils;
import io.github.tau34.mte.Enhancement;
import io.github.tau34.mte.common.holder.IAdditionalEnhanceable;
import io.github.tau34.mte.common.holder.IBlockStateHolder;
import io.github.tau34.mte.common.inventory.slot.EnhancementInventorySlot;
import io.github.tau34.mte.common.item.ItemEnhancement;
import mekanism.api.*;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.ISyncableData;
import mekanism.common.inventory.container.sync.SyncableItemStack;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.ITileComponent;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.interfaces.ITierUpgradable;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.NBTUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.util.thread.EffectiveSide;

import java.util.*;

public class TileComponentEnhancement implements ITileComponent, MekanismContainer.ISpecificContainerTracker {
    private final TileEntityMekanism tile;
    private final Set<Enhancement> supported;

    @ContainerSync
    private final List<EnhancementInventorySlot> enhancementSlot = new ArrayList<>();

    public TileComponentEnhancement(TileEntityMekanism tile) {
        this.tile = tile;
        Set<Upgrade> upgrades = this.tile.getSupportedUpgrade();
        List<Enhancement> enhancements = new ArrayList<>();
        if (upgrades.contains(Upgrade.ENERGY)) {
            enhancements.add(Enhancement.ECO);
            enhancements.add(Enhancement.ENERGY);
        }
        if (upgrades.contains(Upgrade.SPEED)) {
            enhancements.add(Enhancement.SPEED);
        }
        if (tile instanceof TileEntityFactory<?>) {
            enhancements.add(Enhancement.PROCESSING);
        }
        if (this.tile instanceof TileEntityFactory<?>) {
            enhancements.add(Enhancement.PROCESSING);
        }
        if (tile instanceof IAdditionalEnhanceable additional) {
            enhancements.addAll(additional.supported());
        }
        supported = new HashSet<>(enhancements);
        for (int i = 0; i < 3; i++) {
            enhancementSlot.add(EnhancementInventorySlot.input(tile, supported, this::recalculateEnhancement));
        }
        this.tile.addComponent(this);
    }

    @ComputerMethod(nameOverride = "getSupportedEnhancements")
    public Set<Enhancement> getSupportedTypes() {
        return supported;
    }

    public boolean supports(Enhancement enhancement) {
        return supported.contains(enhancement);
    }

    public boolean supports(Upgrade upgrade) {
        return switch (upgrade) {
            case SPEED -> supports(Enhancement.SPEED);
            case ENERGY -> supports(Enhancement.ENERGY) || supports(Enhancement.ECO);
            default -> false;
        };
    }

    public List<EnhancementInventorySlot> getSlots() {
        return enhancementSlot;
    }

    @Override
    public void read(CompoundTag nbtTags) {
        NBTUtils.setCompoundIfPresent(nbtTags, "componentEnhancement", enhancementNBT ->
                NBTUtils.setListIfPresent(enhancementNBT, NBTConstants.ITEMS, Tag.TAG_COMPOUND, list -> {
                    DataHandlerUtils.readContainers(getSlots(), list);
                    recalculateEnhancement();
                }));
    }

    @Override
    public void write(CompoundTag nbtTags) {
        CompoundTag enhancementNBT = new CompoundTag();
        enhancementNBT.put(NBTConstants.ITEMS, DataHandlerUtils.writeContainers(getSlots()));
        nbtTags.put("componentEnhancement", enhancementNBT);
    }

    @Override
    public void readFromUpdateTag(CompoundTag updateTag) {
        NBTUtils.setCompoundIfPresent(updateTag, "enhancementClient", enhancementNBT ->
                NBTUtils.setListIfPresent(enhancementNBT, NBTConstants.ITEMS, Tag.TAG_COMPOUND, list -> {
                    DataHandlerUtils.readContainers(getSlots(), list);
                    recalculateEnhancement();
                }));
    }

    @Override
    public void addToUpdateTag(CompoundTag updateTag) {
        CompoundTag enhancementNBT = new CompoundTag();
        enhancementNBT.put(NBTConstants.ITEMS, DataHandlerUtils.writeContainers(getSlots()));
        updateTag.put("enhancementClient", enhancementNBT);
    }

    public int getEnhancements(Enhancement enhancement) {
        int i = 0;
        for (EnhancementInventorySlot slot : enhancementSlot) {
            if (slot.getStack().getItem() instanceof ItemEnhancement upgrade) {
                if (upgrade.getEnhancementType(slot.getStack()).equals(enhancement)) {
                    i += slot.getStack().getCount();
                }
            }
        }
        return i;
    }

    @Override
    public List<ISyncableData> getSpecificSyncableData() {
        List<ISyncableData> list = new ArrayList<>();
        for (EnhancementInventorySlot slot : enhancementSlot) {
            list.add(SyncableItemStack.create(slot::getStack, slot::setStack));
        }
        return list;
    }

    public void recalculateEnhancement() {
        for (Enhancement enhancement : getSupportedTypes()) {
            switch (enhancement) {
                case ECO, ENERGY -> tile.recalculateUpgrades(Upgrade.ENERGY);
                case SPEED -> tile.recalculateUpgrades(Upgrade.SPEED);
                case PROCESSING -> this.replaceBlock();
            }
        }
    }

    public void replaceBlock() {
        if (tile.getBlockState() instanceof IBlockStateHolder holder) {
            int processingData = getEnhancements(Enhancement.PROCESSING);
            if (holder.getProcessingData(EffectiveSide.get() == LogicalSide.CLIENT) == processingData) return;
            BlockPos pos = tile.getBlockPos();
            Level world = tile.getLevel();
            if (world != null) {
                LevelChunk chunk = world.getChunkAt(pos);
                if (chunk.isEmpty()) return;
                chunk.removeBlockEntity(pos);
                world.setBlockAndUpdate(pos, holder.setProcessingData(processingData,
                        EffectiveSide.get() == LogicalSide.CLIENT));
                IUpgradeData upgradeData = tile.getUpgradeData();
                if (upgradeData != null) {
                    TileEntityMekanism upgradedTile = WorldUtils.getTileEntity(TileEntityMekanism.class, world, pos);
                    if (upgradedTile != null) {
                        upgradedTile.parseUpgradeData(upgradeData);
                        if (!tile.isRemote()) upgradedTile.sendUpdatePacket();
                        upgradedTile.setChanged();
                    }
                }
            }
        }
    }
}
