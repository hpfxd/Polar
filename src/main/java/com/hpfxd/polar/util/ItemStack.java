package com.hpfxd.polar.util;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

// https://wiki.vg/index.php?title=Slot_Data&oldid=7093
@Getter
public class ItemStack {
    public static final ItemStack EMPTY_STACK = new ItemStack(Material.AIR, -1);

    private final Material material;
    @Setter private int count;
    @Setter private int damage;

    public ItemStack(Material material) {
        this(material, 1);
    }

    public ItemStack(Material material, int count) {
        this(material, count, 0);
    }

    public ItemStack(Material material, int count, int damage) {
        this.material = material;
        this.count = count;
        this.damage = damage;
    }

    public void write(ByteBuf buf) {
        buf.writeShort(this.material.getId());

        if (this.material != Material.AIR) {
            buf.writeByte(this.count);
            buf.writeShort(this.damage);
            buf.writeByte(0);
        }
    }

    public int getItemIdOrZero() { // used in some packets
        return this.material == Material.AIR ? 0 : this.material.getId();
    }
}
