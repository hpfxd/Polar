package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Player;
import io.netty.buffer.ByteBuf;

public class PacketInEntityAction extends Packet {
    private int entityId;
    private int actionId;
    private int actionParameter;

    @Override
    public void read(ByteBuf buf) {
        this.entityId = PacketUtils.readVarInt(buf);
        this.actionId = PacketUtils.readVarInt(buf);
        this.actionParameter = PacketUtils.readVarInt(buf);
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        Player player = channelHandler.getPlayer();
        if (this.entityId == player.getEntityId()) {
            if (this.actionId == 0) {
                player.setSneaking(true);
            } else if (this.actionId == 1) {
                player.setSneaking(false);
            } else if (this.actionId == 3) {
                player.setSprinting(true);
            } else if (this.actionId == 4) {
                player.setSprinting(false);
            }

            player.updateStatusMetadata();
        }
    }
}
