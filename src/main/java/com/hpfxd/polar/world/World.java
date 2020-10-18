package com.hpfxd.polar.world;

import com.hpfxd.polar.event.EventManager;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.network.packet.packets.play.PacketOutBlockChange;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.util.Position;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class World {
    @Getter private final List<Player> players = new CopyOnWriteArrayList<>();
    @Getter private final Chunk[][] chunks;
    @Getter private final WorldBorder worldBorder = new WorldBorder();
    @Getter private final EventManager eventManager = new EventManager();

    public World(int size) {
        log.info("Creating world. Size: {} chunks.", size);

        double s = size * 16;
        this.worldBorder.setCenterX(s / 2);
        this.worldBorder.setCenterZ(s / 2);
        this.worldBorder.setRadius(s);

        this.chunks = new Chunk[size][size];
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                Chunk chunk = new Chunk(this, x, z);
                chunk.initializeSections();

                // set chunk content

                for (int y1 = 1; y1 < 5 + x + z; y1++) {
                    chunk.setBlock(0, y1, 0, 35, x & 0xF); // wool pillars for chunk marking
                }

                for (int x1 = 0; x1 < 16; x1++) {
                    for (int z1 = 0; z1 < 16; z1++) {
                        chunk.setBlock(x1, 1, z1, 7, 0);
                        chunk.setBlock(x1, 2, z1, 1, 0);
                        chunk.setBlock(x1, 3, z1, 3, 0);
                        chunk.setBlock(x1, 4, z1, 4, 0);
                    }
                }

                this.chunks[x][z] = chunk;
            }
        }
    }

    public void setBlock(int x, int y, int z, int id, int meta) {
        Chunk chunk = this.getChunkAtWorldPos(x, z);

        chunk.setBlock(x & 0xF, y, z & 0xF, id, meta);
    }

    public Block getBlock(int x, int y, int z) {
        Chunk chunk = this.getChunkAtWorldPos(x, z);

        return chunk.getBlock(x & 0xF, y, z & 0xF);
    }

    public Block getBlock(Position position) {
        return this.getBlock((int) position.getX(), (int) position.getY(), (int) position.getZ());
    }

    // set block and update players about it
    public void setBlock(Position position, int id, int meta) {
        int x = (int) position.getX();
        int y = (int) position.getY();
        int z = (int) position.getZ();

        Chunk chunk = this.getChunkAtWorldPos(x, z);

        chunk.setBlock(x & 0xF, y, z & 0xF, id, meta);

        this.setBlock((int) position.getX(), (int) position.getY(), (int) position.getZ(), id, meta);

        // update players about the change
        PacketOutBlockChange packet = new PacketOutBlockChange(position, id << 4 | (meta & 15));
        for (Player player : this.players) {
            if (player.getVisibleChunks().contains(chunk)) {
                player.getChannelHandler().sendPacket(packet);
            }
        }
    }

    public void sendPacketToPlayers(Packet packet) {
        for (Player player : this.players) {
            player.getChannelHandler().sendPacket(packet);
        }
    }

    public Player getPlayerByEntityId(int entityId) {
        for (Player player : this.players) {
            if (player.getEntityId() == entityId) return player;
        }

        return null;
    }

    public Chunk getChunkAtWorldPos(int x, int z) {
        return this.chunks[(x >> 4)][(z >> 4)];
    }
}
