package com.hpfxd.polar.util;

import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;

@AllArgsConstructor
public enum BlockFace {
    BOTTOM((pos, i) -> pos.setY(pos.getY() - i)),
    TOP((pos, i) -> pos.setY(pos.getY() + i)),
    SOUTH((pos, i) -> pos.setZ(pos.getZ() - i)),
    NORTH((pos, i) -> pos.setZ(pos.getZ() + i)),
    WEST((pos, i) -> pos.setX(pos.getX() - i)),
    EAST((pos, i) -> pos.setX(pos.getX() + i))
    ;

    private final BiConsumer<MutableLocation, Integer> applyConsumer;

    public void apply(MutableLocation location, int amount) {
        this.applyConsumer.accept(location, amount);
    }

    public static BlockFace getBlockFaceById(int id) {
        return values()[id];
    }
}
