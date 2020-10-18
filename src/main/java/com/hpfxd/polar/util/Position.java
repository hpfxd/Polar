package com.hpfxd.polar.util;

import lombok.Data;

@Data
public class Position {
    private final double x;
    private final double y;
    private final double z;

    public static Position from(MutableLocation location) {
        return new Position(location.getX(), location.getY(), location.getZ());
    }
}
