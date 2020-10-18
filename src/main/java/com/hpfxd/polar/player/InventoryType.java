package com.hpfxd.polar.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InventoryType {
    CHEST("minecraft:chest", true),
    CRAFTING_TABLE("minecraft:crafting_table", false),
    FURNACE("minecraft:furnace", true),
    DISPENSER("minecraft:dispenser", true),
    ENCHANTING_TABLE("minecraft:enchanting_table", false),
    BREWING_STAND("minecraft:brewing_stand", true),
    VILLAGER("minecraft:villager", false),
    BEACON("minecraft:beacon", true),
    ANVIL("minecraft:anvil", false),
    HOPPER("minecraft:hopper", true),
    DROPPER("minecraft:dropper", true),
    HORSE("EntityHorse", true)
    ;

    private final String name;
    private final boolean storage; // if false, amount of slots sent to client is 0
}
