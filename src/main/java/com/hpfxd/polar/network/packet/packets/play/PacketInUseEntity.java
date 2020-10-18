package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.event.player.PlayerInteractAtEntityEvent;
import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.util.Vector;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.TimeUnit;

public class PacketInUseEntity extends Packet {
    private int targetId;
    private PlayerInteractAtEntityEvent.Action action;

    @Override
    public void read(ByteBuf buf) {
        this.targetId = PacketUtils.readVarInt(buf);
        this.action = PlayerInteractAtEntityEvent.Action.values()[PacketUtils.readVarInt(buf)];
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        Player player = channelHandler.getPlayer();
        PlayerInteractAtEntityEvent event = new PlayerInteractAtEntityEvent(player, this.targetId, this.action);
        event.postEvent();

        if (!event.isCancelled()) {
            Player target = player.getWorld().getPlayerByEntityId(this.targetId);

            if (target != null) {
                if (player.getLocation().distanceToSq(target.getLocation()) > (6 * 6)) {
                    // do nothing if more than 6 blocks away
                    return;
                }

                if (this.action == PlayerInteractAtEntityEvent.Action.ATTACK) {
                    long l = System.nanoTime() - target.getLastHit();
                    long ms = TimeUnit.NANOSECONDS.toMillis(l);

                    if (500 > ms) {
                        // player was already hit in the last 500ms, cancel
                        return;
                    }

                    target.setLastHit(System.nanoTime());
                    Vector pv = player.getLocation().toVector();
                    Vector tv = target.getLocation().toVector();

                    Vector unitVector = tv.subtract(pv).normalize();
                    unitVector.multiply(1.1);
                    unitVector.setY(0.8f);
                    target.setVelocity(unitVector);
                    target.setHealth(target.getHealth() - 2); // todo calc damage by weapon
                }
            }
        }
    }
}
