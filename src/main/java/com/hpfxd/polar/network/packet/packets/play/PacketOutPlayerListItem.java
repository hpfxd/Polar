package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.util.MojangProperty;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class PacketOutPlayerListItem extends Packet {
    private final int action;
    private final List<Player> players;

    public PacketOutPlayerListItem(int action, List<Player> players) {
        this.action = action;
        this.players = players;
    }

    @Override
    public void write(ByteBuf buf) {
        PacketUtils.writeVarInt(buf, this.action);
        PacketUtils.writeVarInt(buf, this.players.size());
        for (Player player : this.players) {
            PacketUtils.writeUUID(buf, player.getUuid());

            if (this.action == 0) { // add player
                PacketUtils.writeString(buf, player.getName());
                PacketUtils.writeVarInt(buf, player.getMojangProperties().size());
                for (MojangProperty property : player.getMojangProperties()) {
                    PacketUtils.writeString(buf, property.getName());
                    PacketUtils.writeString(buf, property.getValue());
                    buf.writeBoolean(property.getSignature() != null);
                    if (property.getSignature() != null) {
                        PacketUtils.writeString(buf, property.getSignature());
                    }
                }
                PacketUtils.writeVarInt(buf, player.getGameMode().ordinal());
                PacketUtils.writeVarInt(buf, 1); // ping
                buf.writeBoolean(false); // has display name
            }
        }
    }
}
