package com.hpfxd.polar.network.packet.packets.play;

import com.hpfxd.polar.event.player.PlayerInteractEvent;
import com.hpfxd.polar.event.player.PlayerPlaceBlockEvent;
import com.hpfxd.polar.network.PacketUtils;
import com.hpfxd.polar.network.PolarChannelHandler;
import com.hpfxd.polar.network.packet.Packet;
import com.hpfxd.polar.player.GameMode;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.util.*;
import com.hpfxd.polar.world.Block;
import io.netty.buffer.ByteBuf;

public class PacketInPlayerBlockPlacement extends Packet {
    private Position position;
    private byte face;

    @Override
    public void read(ByteBuf buf) {
        this.position = PacketUtils.readPosition(buf);
        this.face = buf.readByte();
    }

    @Override
    public void handle(PolarChannelHandler channelHandler) {
        Player player = channelHandler.getPlayer();
        ItemStack stack = player.getPlayerInventory().getItemInHand();
        if (this.position.getY() == 4095.0d) { // y=4095 = right click air
            new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_AIR, stack).postEvent();

            if (stack.getMaterial().isSword()) {
                player.setBlocking(true);
                player.updateStatusMetadata();
            }
        } else {
            MutableLocation location = new MutableLocation(this.position);
            BlockFace face = BlockFace.getBlockFaceById(this.face);
            face.apply(location, 1);
            Position pos = Position.from(location);

            new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, stack).postEvent();

            if (stack.getMaterial() == Material.AIR) {
                return;
            }

            PlayerPlaceBlockEvent event = new PlayerPlaceBlockEvent(player, pos);
            event.postEvent();
            if (event.isCancelled()) {
                Block previousBlock = player.getWorld().getBlock((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
                channelHandler.sendPacket(new PacketOutBlockChange(pos, previousBlock.getType() << 4 | (previousBlock.getMeta() & 15)));
            } else {
                player.getWorld().setBlock(pos, stack.getMaterial().getId(), stack.getDamage());

                if (player.getGameMode() != GameMode.CREATIVE) {
                    stack.setCount(stack.getCount() - 1);
                    if (stack.getCount() == 0) {
                        player.getPlayerInventory().setItemInHand(ItemStack.EMPTY_STACK);
                    }
                }
            }
        }
    }
}
