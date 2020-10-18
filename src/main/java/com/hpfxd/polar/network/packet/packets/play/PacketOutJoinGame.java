package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutJoinGame extends Packet {
    private final int entityId;
    private final byte gamemode;
    private final int dimension;
    private final byte difficulty;
    private final byte maxPlayers;
    private final String levelType;
    private final boolean reducedDebugInfo;

    public PacketOutJoinGame(int entityId, byte gamemode, int dimension, byte difficulty, byte maxPlayers, String levelType, boolean reducedDebugInfo) {
        this.entityId = entityId;
        this.gamemode = gamemode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.levelType = levelType;
        this.reducedDebugInfo = reducedDebugInfo;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte(this.gamemode);
        buf.writeByte(this.dimension);
        buf.writeByte(this.difficulty);
        buf.writeByte(this.maxPlayers);
        PacketUtils.writeString(buf, this.levelType);
        buf.writeBoolean(this.reducedDebugInfo);
    }
}
