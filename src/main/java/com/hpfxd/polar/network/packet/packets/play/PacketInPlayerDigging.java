package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.event.player.PlayerBreakBlockEvent;
import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.util.Position;
import io.netty.buffer.ByteBuf;

public class PacketInPlayerDigging extends Packet {
    private byte status;
    private Position position;
    private byte face;

    @Override
    public void read(ByteBuf buf) {
        this.status = buf.readByte();
        this.position = PacketUtils.readPosition(buf);
        this.face = buf.readByte();
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        Player player = channelHandler.getPlayer();
        if (this.status == 0) { // started digging
            if (player.getGameMode().isInstantBreak()) {
                this.destroy(player, this.position);
            }
        } else if (this.status == 2) { // finished digging
            this.destroy(player, this.position);
        } else if (this.status == 5) { // shoot arrow / finish eating / stop blocking
            if (player.isBlocking()) {
                player.setBlocking(false);
                player.updateStatusMetadata();
            }
        }

        // todo digging animation, dropping items
    }

    private void destroy(Player player, Position position) {
        PlayerBreakBlockEvent event = new PlayerBreakBlockEvent(player, position);
        event.postEvent();
        if (!event.isCancelled()) {
            int type = player.getWorld().getBlock(position).getType();

            player.getWorld().setBlock(position, 0, 0);
            player.sendPacketToViewers(new PacketOutEffect(PacketOutEffect.Effect.BLOCK_BREAK, position, type, false));
        }
    }
}
