package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.event.player.PlayerInteractEvent;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Player;
import io.netty.buffer.ByteBuf;

public class PacketInAnimation extends Packet {
    @Override
    public void read(ByteBuf buf) {
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        Player player = channelHandler.getPlayer();
        new PlayerInteractEvent(player, PlayerInteractEvent.Action.SWING, player.getPlayerInventory().getItemInHand()).postEvent();

        player.sendPacketToViewers(new PacketOutAnimation(player.getEntityId(), PacketOutAnimation.Animation.SWING_ARM));
    }
}
