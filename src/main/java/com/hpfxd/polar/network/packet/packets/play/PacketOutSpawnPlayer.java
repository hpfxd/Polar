package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Player;
import io.netty.buffer.ByteBuf;

public class PacketOutSpawnPlayer extends Packet {
    private final Player player;

    public PacketOutSpawnPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.player.getEntityId());
        PacketUtils.writeUUID(buf, this.player.getUuid());
        buf.writeInt((int) (this.player.getLocation().getX() * 32));
        buf.writeInt((int) (this.player.getLocation().getY() * 32));
        buf.writeInt((int) (this.player.getLocation().getZ() * 32));
        buf.writeByte(this.player.getLocation().getYawAngle());
        buf.writeByte(this.player.getLocation().getPitchAngle());
        buf.writeShort(this.player.getPlayerInventory().getItemInHand().getItemIdOrZero());
        this.player.getMetadata().write(buf);
    }
}
