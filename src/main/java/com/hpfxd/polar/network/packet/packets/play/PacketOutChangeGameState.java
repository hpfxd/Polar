package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PacketOutChangeGameState extends Packet {
    private final Action action;
    private final float value;

    public PacketOutChangeGameState(Action action, float value) {
        this.action = action;
        this.value = value;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(this.action.ordinal());
        buf.writeFloat(this.value);
    }

    public enum Action {
        INVALID_BED,
        END_RAINING,
        BEGIN_RAINING,
        CHANGE_GAME_MODE,
        ENTER_CREDITS,
        DEMO_MESSAGE,
        ARROW_HITTING_PLAYER,
        FADE_VALUE,
        FADE_TIME,
        PLAY_MOB_APPEARANCE
    }
}
