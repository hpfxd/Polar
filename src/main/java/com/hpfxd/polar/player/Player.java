package com.hpfxd.polar.player;

import com.hpfxd.polar.Polar;
import com.hpfxd.polar.event.player.PlayerDeathEvent;
import com.hpfxd.polar.event.player.PlayerMoveEvent;
import com.hpfxd.polar.event.player.PlayerSpawnEvent;
import com.hpfxd.polar.network.PacketWriterUtil;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.network.packet.packets.play.*;
import com.hpfxd.polar.util.*;
import com.hpfxd.polar.util.metadata.EntityMetadata;
import com.hpfxd.polar.world.Chunk;
import com.hpfxd.polar.world.World;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Player {
    @Getter private final PolarChannelHandler channelHandler;
    @Getter private final UUID uuid;
    @Getter private final String name;
    @Getter private World world;
    @Getter private final int entityId;
    @Getter private Chunk chunk;
    @Getter private final MutableLocation location = new MutableLocation(Polar.getPolar().getConfig().getSpawn());
    @Getter private final MutableLocation lastLocation = new MutableLocation(Polar.getPolar().getConfig().getSpawn());
    @Getter @Setter private boolean onGround;

    @Getter private GameMode gameMode = Polar.getPolar().getConfig().getDefaultGameMode();
    @Getter private final List<MojangProperty> mojangProperties;

    @Getter private final PlayerInventory playerInventory = new PlayerInventory(this);
    @Getter private Inventory openedInventory;

    @Getter private final EntityMetadata metadata = new EntityMetadata();
    @Getter @Setter private boolean onFire;
    @Getter @Setter private boolean sprinting;
    @Getter @Setter private boolean sneaking;
    @Getter @Setter private boolean blocking;
    @Getter private float health = 20;
    @Getter @Setter private long lastHit;

    private int movementUpdates = 0;

    private final List<Player> viewers = new CopyOnWriteArrayList<>();
    @Getter private final List<Chunk> visibleChunks = new ArrayList<>();

    private final ScheduledFuture<?> keepAliveTask;
    //private final java.util.concurrent.ScheduledFuture<?> trackTask;

    public Player(PolarChannelHandler channelHandler, UUID uuid, String name, World world, List<MojangProperty> mojangProperties) {
        this.channelHandler = channelHandler;
        this.uuid = uuid;
        this.name = name;
        this.world = world;
        this.mojangProperties = mojangProperties;
        this.entityId = Polar.ENTITY_ID_COUNTER.getAndIncrement();
        this.metadata.setByte(10, 0xFF);
        log.info("UUID of {} is {}.", this.name, this.uuid);

        log.info("Scheduling tasks.");
        this.keepAliveTask = this.channelHandler.getChannel().eventLoop().scheduleAtFixedRate(
                () -> {
                    this.channelHandler.sendPacket(new PacketOutKeepAlive(Polar.KEEPALIVE_ID_COUNTER.getAndIncrement()));
                    this.channelHandler.setLastKeepAliveSent(System.nanoTime());
                }, 5, 10, TimeUnit.SECONDS); // task to send a keep alive packet every 10 seconds

        this.channelHandler.sendPacket(new PacketOutJoinGame(this.entityId, (byte) this.gameMode.ordinal(), 0, (byte) 1, (byte) 60, "flat", false));
        this.channelHandler.sendPacket(new PacketOutSpawnPosition(Polar.getPolar().getConfig().getSpawn()));
        this.channelHandler.sendPacket(new PacketOutWorldBorder(PacketOutWorldBorder.Action.SET_SIZE, this.world.getWorldBorder()));
        this.channelHandler.sendPacket(new PacketOutWorldBorder(PacketOutWorldBorder.Action.SET_CENTER, this.world.getWorldBorder()));

        this.playerInventory.setHotbarPositionAndSendToClient(0);
        this.playerInventory.setItem(PlayerInventory.HOTBAR_START_OFFSET, new ItemStack(Material.STONE, 5));
        this.playerInventory.setItem(PlayerInventory.HOTBAR_START_OFFSET + 1, new ItemStack(Material.GRASS, 16));
        this.playerInventory.setItem(PlayerInventory.HOTBAR_START_OFFSET + 2, new ItemStack(Material.COBBLESTONE, 64));
        this.playerInventory.setItem(PlayerInventory.HOTBAR_START_OFFSET + 3, new ItemStack(Material.WOOL, 1, 5));
        this.playerInventory.setItem(PlayerInventory.HOTBAR_START_OFFSET + 4, new ItemStack(Material.DIAMOND_SWORD));
        this.channelHandler.sendPacket(new PacketOutWindowItems(this.playerInventory));
        this.playerInventory.setCanUpdate(true);

        this.setHealth(20);

        // tell client about online players
        List<Player> tabUpdateList = new ArrayList<>(this.world.getPlayers());
        tabUpdateList.add(this);
        this.channelHandler.sendPacket(new PacketOutPlayerListItem(0, tabUpdateList));

        // tell online players about client
        PacketOutPlayerListItem playerListUpdate = new PacketOutPlayerListItem(0, Collections.singletonList(this));
        this.world.sendPacketToPlayers(playerListUpdate);

        Polar.getPolar().getPlayers().add(this);
        this.world.getPlayers().add(this);

        this.teleportToSpawn();

        this.updateVisibleChunks();
        this.teleportToSpawn();

        this.world.sendPacketToPlayers(new PacketOutChatMessage(new ComponentBuilder(this.name).color(ChatColor.WHITE)
                .append(" joined the game.").color(ChatColor.YELLOW)
                .create()));
    }

    public void setHealth(float health) {
        if (health <= 0) {
            this.setHealth(20);
            this.teleportToSpawn();
            this.getPlayerInventory().clear();

            new PlayerDeathEvent(this).postEvent();
        } else {
            if (health < this.health) {
                this.sendPacketToViewers(new PacketOutAnimation(this.entityId, PacketOutAnimation.Animation.TAKE_DAMAGE));
            }

            this.health = health;
            this.channelHandler.sendPacket(new PacketOutUpdateHealth(this.health, 20, 5));
        }
    }

    public int getPing() {
        long l = this.channelHandler.getLastKeepAliveReceived() - this.channelHandler.getLastKeepAliveSent();
        long ms = TimeUnit.NANOSECONDS.toMillis(l);
        return (int) ms;
    }

    public void setVelocity(Vector vector) {
        this.channelHandler.sendPacket(new PacketOutEntityVelocity(this.entityId, vector));
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        this.channelHandler.sendPacket(new PacketOutChangeGameState(PacketOutChangeGameState.Action.CHANGE_GAME_MODE, this.gameMode.ordinal()));
    }

    public void teleportToSpawn() {
        PlayerSpawnEvent event = new PlayerSpawnEvent(this, Polar.getPolar().getConfig().getSpawn());
        event.postEvent();
        this.teleport(new MutableLocation(event.getPosition()));
    }

    public void teleport(MutableLocation location) {
        this.location.setLocation(location);
        this.lastLocation.setLocation(location);
        this.channelHandler.sendPacket(new PacketOutPlayerPositionAndLook(location));
    }

    public void addViewer(Player player, boolean sendPacket) {
        if (player != this && !this.viewers.contains(player)) {
            this.viewers.add(player);
            if (sendPacket) {
                player.channelHandler.sendPacket(new PacketOutSpawnPlayer(this));
                player.channelHandler.sendPacket(new PacketOutEntity(this.getEntityId(), this.onGround));
            }
        }
    }

    public void removeViewer(Player player, boolean sendPacket) {
        if (this.viewers.contains(player)) {
            this.viewers.remove(player);
            if (sendPacket) player.channelHandler.sendPacket(new PacketOutDestroyEntities(new int[] { this.entityId }));
        }
    }

    public void sendPacketToViewers(Packet packet) {
        PacketWriterUtil.writeAndSend(this.viewers, packet);
    }

    public void sendPacketToViewersAndPlayer(Packet packet) {
        this.sendPacketToViewers(packet);
        this.channelHandler.sendPacket(packet);
    }

    public void updateStatusMetadata() {
        byte b = 0;

        if (this.onFire) {
            b |= 0x01;
        }

        if (this.sneaking) {
            b |= 0x02;
        }

        if (this.sprinting) {
            b |= 0x08;
        }

        if (this.blocking) {
            b |= 0x10;
        }

        this.metadata.setByte(0, b);

        this.sendPacketToViewers(new PacketOutEntityMetadata(this.entityId, this.metadata));
    }

    public void onMovementUpdate(boolean position, boolean look) {
        // i have no idea why the minecraft movement system is designed like this but here we are

        double distMoved = this.location.distanceToSq(this.lastLocation);

        PlayerMoveEvent event = new PlayerMoveEvent(this, this.lastLocation, this.location, distMoved);

        if (distMoved > 6) {
            event.setCancelled(true);
        }

        event.postEvent();
        if (event.isCancelled()) {
            this.teleport(this.lastLocation);
            //this.location.setLocation(this.lastLocation);
            return;
        }

        // sends teleport packet every second, need to find out why it gets out of sync
        Packet packet = null;
        if ((this.movementUpdates++ % 20 != 0) && distMoved < (4 * 4)) {
            if (position && look) {
                double deltaX = this.location.getX() - this.lastLocation.getX();
                double deltaY = this.location.getY() - this.lastLocation.getY();
                double deltaZ = this.location.getZ() - this.lastLocation.getZ();

                packet = new PacketOutEntityLookAndRelativeMove(this.entityId, (byte) (deltaX * 32d), (byte) (deltaY * 32d), (byte) (deltaZ * 32d), this.location.getYawAngle(), this.location.getPitchAngle(), this.onGround);
            } else if (position) {
                double deltaX = this.location.getX() - this.lastLocation.getX();
                double deltaY = this.location.getY() - this.lastLocation.getY();
                double deltaZ = this.location.getZ() - this.lastLocation.getZ();

                packet = new PacketOutEntityRelativeMove(this.entityId, (byte) (deltaX * 32d), (byte) (deltaY * 32d), (byte) (deltaZ * 32d), this.onGround);
            } else if (look) {
                packet = new PacketOutEntityLook(this.entityId, this.location.getYawAngle(), this.location.getPitchAngle(), this.onGround);
            } else {
                if (Polar.getPolar().getConfig().isSendEntityPacketEveryTick()) {
                    packet = new PacketOutEntity(this.entityId, this.onGround);
                }
            }
        } else {
            // entity moved more than 4 blocks since last update so entity teleport packet has to be used
            packet = new PacketOutEntityTeleport(this.entityId, (int) (this.location.getX() * 32), (int) (this.location.getY() * 32), (int) (this.location.getZ() * 32), this.location.getYawAngle(), this.location.getPitchAngle(), this.onGround);
        }

        if (position) {
            Chunk chunk;
            try {
                chunk = this.world.getChunkAtWorldPos((int) this.location.getX(), (int) this.location.getZ());
            } catch (ArrayIndexOutOfBoundsException e) {
                this.teleportToSpawn();
                return;
            }

            if (this.chunk != chunk) {
                this.chunk = chunk;

                this.updateVisibleChunks();
            }
        }

        if (packet != null) {
            this.sendPacketToViewers(packet);
            if (look) {
                this.sendPacketToViewers(new PacketOutEntityHeadLook(this.entityId, this.location.getYawAngle()));
            }
        }

        this.lastLocation.setLocation(this.location);
    }

    private void updateVisibleChunks() {
        log.info("Updating visible chunks");
        this.channelHandler.setAutoflush(false);
        int viewDistanceSq = Polar.getPolar().getConfig().getViewDistanceSq();

        List<Chunk> toLoad = new ArrayList<>();
        List<Chunk> toUnload = new ArrayList<>();
        List<Player> viewersToAdd = new ArrayList<>();
        List<Player> viewersToRemove = new ArrayList<>();

        for (Chunk[] tab : this.world.getChunks()) {
            for (Chunk chunk : tab) {
                double distanceSq = MathUtil.distanceToSq((int) this.location.getX() / 16, (int) this.location.getZ() / 16, chunk.getX(), chunk.getZ());

                if (distanceSq >= viewDistanceSq) {
                    if (this.visibleChunks.contains(chunk)) {
                        toUnload.add(chunk);
                        for (Player player : this.world.getPlayers()) {
                            if (player.chunk == chunk && player != this) {
                                viewersToRemove.add(player);
                            }
                        }
                    }
                } else {
                    if (!this.visibleChunks.contains(chunk)) {
                        toLoad.add(chunk);
                        for (Player player : this.world.getPlayers()) {
                            if (player.chunk == chunk && player != this) {
                                viewersToAdd.add(player);
                            }
                        }
                    }
                }
            }
        }

        // update visiblechunks array
        this.visibleChunks.removeAll(toUnload);
        this.visibleChunks.addAll(toLoad);

        List<ChunkDataMessage> messages = new ArrayList<>(toLoad.size());
        for (Chunk chunk : toLoad) {
            //this.channelHandler.sendPacket(new PacketOutChunkData(chunk.toMessage(true, true, 0)));
            messages.add(chunk.toMessage(true));
        }

        if (messages.size() > 0) {
            this.channelHandler.sendPacket(new PacketOutChunkBulk(messages, true));
        }

        for (Chunk chunk : toUnload) {
            // chunk bulk packet doesn't seem to work to unload chunks
            this.channelHandler.sendPacket(new PacketOutChunkData(ChunkDataMessage.empty(chunk.getX(), chunk.getZ())));
            //messages.add(ChunkDataMessage.empty(chunk.getX(), chunk.getZ()));
        }

        if (viewersToAdd.size() > 0) {
            for (Player player : viewersToAdd) {
                this.addViewer(player, true);
                player.addViewer(this, true);
            }
        }

        if (viewersToRemove.size() > 0) {
            int[] entityIds = new int[viewersToRemove.size()];
            for (int i = 0; i < viewersToRemove.size(); i++) {
                Player player = viewersToRemove.get(i);
                entityIds[i] = player.getEntityId();

                this.removeViewer(player, true);
                player.removeViewer(this, false);
            }

            this.channelHandler.sendPacket(new PacketOutDestroyEntities(entityIds));
        }

        this.channelHandler.setAutoflush(true);
        this.channelHandler.getChannel().flush();
    }

    public void destroy() {
        this.channelHandler.destroyConnection();
        this.keepAliveTask.cancel(false);
        //this.trackTask.cancel(false);

        for (Player viewer : this.viewers) {
            this.removeViewer(viewer, true);
        }

        Polar.getPolar().getPlayers().remove(this);
        this.world.getPlayers().remove(this);

        // remove from player list
        PacketOutPlayerListItem playerListUpdate = new PacketOutPlayerListItem(4, Collections.singletonList(this));
        this.world.sendPacketToPlayers(playerListUpdate);

        this.world.sendPacketToPlayers(new PacketOutChatMessage(new ComponentBuilder(this.name).color(ChatColor.WHITE)
                .append(" left the game.").color(ChatColor.YELLOW)
                .create()));
    }

    // called when the player sends a message
    public void chat(String message) {
        Packet packet = new PacketOutChatMessage(new ComponentBuilder()
                .append(this.name).color(ChatColor.WHITE)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to message.")
                        .create()))
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.name + " "))
                .append(": ").color(ChatColor.GRAY)
                .event((ClickEvent) null)
                .event((HoverEvent) null)
                .append(message).color(ChatColor.GRAY)
                .create());

        log.info("[CHAT] {}: {}", this.name, message);
        this.world.sendPacketToPlayers(packet);
    }

    public void sendMessage(String message) {
    }
}
