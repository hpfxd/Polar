package com.hpfxd.polar.player;

import com.hpfxd.polar.util.ItemStack;
import lombok.Getter;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

// https://wiki.vg/index.php?title=Inventory&oldid=7185

// todo
@Getter
public class Inventory {
    private static final AtomicInteger windowIdCounter = new AtomicInteger();

    private final int windowId;
    private final InventoryType type;
    private final String name;
    protected final ItemStack[] items;

    public Inventory(int windowId, InventoryType type, String name, int size) {
        this.windowId = windowId != -1 ? windowId : (windowIdCounter.getAndIncrement() % 255) + 1;
        this.type = type;
        this.name = name;
        this.items = new ItemStack[size];
        Arrays.fill(this.items, ItemStack.EMPTY_STACK);
    }

    public void setItem(int slot, ItemStack stack) {
        synchronized (this) {
            this.items[slot] = stack;
        }
    }

    public void clear() {
        for (int i = 0; i < this.items.length; i++) {
            this.setItem(i, ItemStack.EMPTY_STACK);
        }
    }
}
