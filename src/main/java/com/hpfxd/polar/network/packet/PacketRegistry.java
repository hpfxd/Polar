package com.hpfxd.polar.network.packet;

import com.hpfxd.polar.network.packet.packets.handshake.PacketInHandshake;
import com.hpfxd.polar.network.packet.packets.login.PacketInLoginStart;
import com.hpfxd.polar.network.packet.packets.login.PacketOutLoginDisconnect;
import com.hpfxd.polar.network.packet.packets.login.PacketOutLoginSuccess;
import com.hpfxd.polar.network.packet.packets.play.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import lombok.extern.slf4j.Slf4j;

// i was too concerned with optimizing this class for some reason
@Slf4j
public class PacketRegistry {
    private PacketData handshakePacketData;

    // Int2ObjectMaps of Packet IDs and PacketData objects.
    private final Int2ObjectLinkedOpenHashMap<PacketData> packetMapLoginIn = new Int2ObjectLinkedOpenHashMap<>();
    private final Int2ObjectLinkedOpenHashMap<PacketData> packetMapLoginOut = new Int2ObjectLinkedOpenHashMap<>();

    private final Int2ObjectLinkedOpenHashMap<PacketData> packetMapPlayIn = new Int2ObjectLinkedOpenHashMap<>();
    private final Int2ObjectLinkedOpenHashMap<PacketData> packetMapPlayOut = new Int2ObjectLinkedOpenHashMap<>();

    private final Int2ObjectLinkedOpenHashMap<PacketData> packetMapStatusIn = new Int2ObjectLinkedOpenHashMap<>();
    private final Int2ObjectLinkedOpenHashMap<PacketData> packetMapStatusOut = new Int2ObjectLinkedOpenHashMap<>();

    public void populateRegistry() {
        log.info("Populating packet registry.");

        // HANDSHAKE
        this.registerPacket(ProtocolState.HANDSHAKE, ProtocolDirection.IN, 0x00, PacketInHandshake.class);

        // LOGIN
        this.registerPacket(ProtocolState.LOGIN, ProtocolDirection.IN, 0x00, PacketInLoginStart.class);

        this.registerPacket(ProtocolState.LOGIN, ProtocolDirection.OUT, 0x00, PacketOutLoginDisconnect.class);
        this.registerPacket(ProtocolState.LOGIN, ProtocolDirection.OUT, 0x02, PacketOutLoginSuccess.class);

        // PLAY
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x00, PacketInKeepAlive.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x01, PacketInChatMessage.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x02, PacketInUseEntity.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x03, PacketInPlayer.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x04, PacketInPlayerPosition.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x05, PacketInPlayerLook.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x06, PacketInPlayerPositionAndLook.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x07, PacketInPlayerDigging.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x08, PacketInPlayerBlockPlacement.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x09, PacketInHeldItemChange.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x0A, PacketInAnimation.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x0B, PacketInEntityAction.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x0E, PacketInClickWindow.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.IN, 0x15, PacketInClientSettings.class);

        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x00, PacketOutKeepAlive.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x01, PacketOutJoinGame.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x02, PacketOutChatMessage.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x04, PacketOutEntityEquipment.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x05, PacketOutSpawnPosition.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x06, PacketOutUpdateHealth.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x08, PacketOutPlayerPositionAndLook.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x09, PacketOutHeldItemChange.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x0B, PacketOutAnimation.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x0C, PacketOutSpawnPlayer.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x12, PacketOutEntityVelocity.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x13, PacketOutDestroyEntities.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x14, PacketOutEntity.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x15, PacketOutEntityRelativeMove.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x16, PacketOutEntityLook.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x17, PacketOutEntityLookAndRelativeMove.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x18, PacketOutEntityTeleport.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x19, PacketOutEntityHeadLook.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x1C, PacketOutEntityMetadata.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x21, PacketOutChunkData.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x23, PacketOutBlockChange.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x25, PacketOutBlockBreakAnimation.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x26, PacketOutChunkBulk.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x28, PacketOutEffect.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x2B, PacketOutChangeGameState.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x2E, PacketOutCloseWindow.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x2F, PacketOutSetSlot.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x30, PacketOutWindowItems.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x32, PacketOutConfirmTransaction.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x38, PacketOutPlayerListItem.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x40, PacketOutDisconnect.class);
        this.registerPacket(ProtocolState.PLAY, ProtocolDirection.OUT, 0x44, PacketOutWorldBorder.class);
    }

    private void registerPacket(ProtocolState state, ProtocolDirection direction, int id, Class<? extends Packet> clazz) {
        PacketData data = new PacketData(id, state, direction, clazz);

        if (state == ProtocolState.HANDSHAKE && direction == ProtocolDirection.IN && id == 0x00) {
            this.handshakePacketData = data;
        } else if (state == ProtocolState.LOGIN) {
            if (direction == ProtocolDirection.IN) {
                this.packetMapLoginIn.put(id, data);
            } else {
                this.packetMapLoginOut.put(id, data);
            }
        } else if (state == ProtocolState.PLAY) {
            if (direction == ProtocolDirection.IN) {
                this.packetMapPlayIn.put(id, data);
            } else {
                this.packetMapPlayOut.put(id, data);
            }
        } else if (state == ProtocolState.STATUS) {
            if (direction == ProtocolDirection.IN) {
                this.packetMapStatusIn.put(id, data);
            } else {
                this.packetMapStatusOut.put(id, data);
            }
        }
    }

    public PacketData getPacketDataByClass(ProtocolState state, ProtocolDirection direction, Class<? extends Packet> clazz) {
        if (state == ProtocolState.HANDSHAKE) {
            if (direction == ProtocolDirection.IN && clazz == this.handshakePacketData.getClazz()) {
                return this.handshakePacketData;
            }

            return null;
        } else if (state == ProtocolState.LOGIN) {
            if (direction == ProtocolDirection.IN) {
                return this.findPacketDataByClass(this.packetMapLoginIn, clazz);
            }

            return this.findPacketDataByClass(this.packetMapLoginOut, clazz);
        } else if (state == ProtocolState.PLAY) {
            if (direction == ProtocolDirection.IN) {
                return this.findPacketDataByClass(this.packetMapPlayIn, clazz);
            }

            return this.findPacketDataByClass(this.packetMapPlayOut, clazz);
        } else if (state == ProtocolState.STATUS) {
            if (direction == ProtocolDirection.IN) {
                return this.findPacketDataByClass(this.packetMapStatusIn, clazz);
            }

            return this.findPacketDataByClass(this.packetMapStatusOut, clazz);
        }

        return null;
    }

    private PacketData findPacketDataByClass(Int2ObjectMap<PacketData> map, Class<? extends Packet> clazz) {
        ObjectIterator<Int2ObjectMap.Entry<PacketData>> it = Int2ObjectMaps.fastIterator(map);

        while (it.hasNext()) {
            Int2ObjectMap.Entry<PacketData> entry = it.next();
            PacketData data = entry.getValue();

            if (data.getClazz() == clazz) return data;
        }

        return null;
    }

    public PacketData getPacketDataById(ProtocolState state, ProtocolDirection direction, int id) {
        if (state == ProtocolState.HANDSHAKE) {
            if (direction == ProtocolDirection.IN && id == 0x00) {
                return this.handshakePacketData;
            }

            return null;
        } else if (state == ProtocolState.LOGIN) {
            if (direction == ProtocolDirection.IN) {
                return this.packetMapLoginIn.get(id);
            }

            return this.packetMapLoginOut.get(id);
        } else if (state == ProtocolState.PLAY) {
            if (direction == ProtocolDirection.IN) {
                return this.packetMapPlayIn.get(id);
            }

            return this.packetMapPlayOut.get(id);
        } else if (state == ProtocolState.STATUS) {
            if (direction == ProtocolDirection.IN) {
                return this.packetMapStatusIn.get(id);
            }

            return this.packetMapStatusOut.get(id);
        }

        return null;
    }
}
