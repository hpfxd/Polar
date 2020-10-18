package com.hpfxd.polar;

import com.hpfxd.polar.player.GameMode;
import com.hpfxd.polar.util.MutableLocation;
import com.hpfxd.polar.util.Position;
import lombok.Getter;

@Getter
public class PolarConfig {
    private String host;
    private int port;
    private int worldSize;
    private int viewDistance;
    private Position spawn;
    private boolean sendEntityPacketEveryTick;
    private int coreExecutorPoolSize;
    private int packetWriterExecutorPoolSize;
    private GameMode defaultGameMode;

    private transient int viewDistanceSq;
    private transient MutableLocation spawnLocation;

    public void init() {
        this.viewDistanceSq = this.viewDistance * this.viewDistance;
        this.spawnLocation = new MutableLocation(this.spawn);
    }
}
