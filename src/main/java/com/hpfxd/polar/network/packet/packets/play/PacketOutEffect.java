package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.util.Position;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PacketOutEffect extends Packet {
    private final Effect effect;
    private final Position position;
    private final int data;
    private final boolean disableRelativeVolume;

    public PacketOutEffect(Effect effect, Position position, int data, boolean disableRelativeVolume) {
        this.effect = effect;
        this.position = position;
        this.data = data;
        this.disableRelativeVolume = disableRelativeVolume;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(this.effect.getEffectId());
        PacketUtils.writePosition(buf, this.position);
        buf.writeInt(this.data);
        buf.writeBoolean(this.disableRelativeVolume);
    }

    @AllArgsConstructor
    @Getter
    public enum Effect {
        SOUND_CLICK(1000),
        SOUND_CLICK2(1001),
        SOUND_BOW(1002),
        SOUND_DOOR(1003),
        SOUND_FIZZ(1004),
        SOUND_MUSIC_DISC(1005),
        SOUND_GHAST_CHARGE(1007),
        SOUND_GHAST_FIREBALL(1008),
        SOUND_ZOMBIE_WOOD(1010),
        SOUND_ZOMBIE_METAL(1011),
        SOUND_ZOMBIE_WOOD_BREAK(1012),
        SOUND_WITHER_SPAWN(1013),
        SOUND_WITHER_SHOOT(1014),
        SOUND_BAT_TAKEOFF(1015),
        SOUND_ZOMBIE_INFECT(1016),
        SOUND_ZOMBIE_UNFECT(1017),
        SOUND_ENDERDRAGON_END(1018),
        SOUND_ANVIL_BREAK(1020),
        SOUND_ANVIL_USE(1021),
        SOUND_ANVIL_LAND(1022),

        SMOKE(2000),
        BLOCK_BREAK(2001),
        SPLASH_POTION(2002),
        EYE_OF_ENDER_BREAK(2003),
        MOB_SPAWN_PARTICLE_EFFECT(2004),
        HAPPY_VILLAGER(2005)
        ;
        private final int effectId;
    }
}
