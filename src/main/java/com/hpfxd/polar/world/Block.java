package com.hpfxd.polar.world;

import lombok.Data;

@Data
public class Block {
    private final Chunk chunk;
    private final int x;
    private final int y;
    private final int z;
    private final int type;
    private final int meta;
}
