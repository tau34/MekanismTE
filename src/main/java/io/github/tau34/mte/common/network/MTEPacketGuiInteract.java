package io.github.tau34.mte.common.network;

import com.mojang.logging.LogUtils;
import io.github.tau34.mte.common.holder.ITileEntityMekanismHolder;
import mekanism.api.functions.TriConsumer;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class MTEPacketGuiInteract implements IMekanismPacket {
    private final MTEGuiInteraction interaction;
    private final BlockPos pos;
    private final int extra;

    public MTEPacketGuiInteract(MTEGuiInteraction interaction, BlockEntity tile) {
        this(interaction, tile.getBlockPos());
    }

    public MTEPacketGuiInteract(MTEGuiInteraction interaction, BlockEntity tile, int extra) {
        this(interaction, tile.getBlockPos(), extra);
    }

    public MTEPacketGuiInteract(MTEGuiInteraction interaction, BlockPos pos) {
        this(interaction, pos, 0);
    }

    public MTEPacketGuiInteract(MTEGuiInteraction interaction, BlockPos pos, int extra) {
        this.interaction = interaction;
        this.pos = pos;
        this.extra = extra;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        Player player = context.getSender();
        if (player != null) {
            TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, player.level(), this.pos);
            if (tile != null) {
                this.interaction.consume(tile, player, this.extra);
            }
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.interaction);
        buffer.writeBlockPos(this.pos);
        buffer.writeInt(this.extra);
    }

    public static MTEPacketGuiInteract decode(FriendlyByteBuf buffer) {
        return new MTEPacketGuiInteract(buffer.readEnum(MTEGuiInteraction.class), buffer.readBlockPos(), buffer.readInt());
    }

    public enum MTEGuiInteraction {
        CONTAINER_TRACK_ENHANCEMENTS((tile, player, extra) -> {
            if (player.containerMenu instanceof MekanismContainer container && tile instanceof ITileEntityMekanismHolder holder) {
                container.startTrackingServer(extra, holder.getEnhancementComponent());
            }
        });

        private final TriConsumer<TileEntityMekanism, Player, Integer> consumerForTile;

        MTEGuiInteraction(TriConsumer<TileEntityMekanism, Player, Integer> consumerForTile) {
            this.consumerForTile = consumerForTile;
        }

        public void consume(TileEntityMekanism tile, Player player, int extra) {
            this.consumerForTile.accept(tile, player, extra);
        }
    }
}