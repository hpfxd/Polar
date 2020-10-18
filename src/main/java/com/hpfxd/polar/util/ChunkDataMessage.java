package com.hpfxd.polar.util;

import lombok.Data;

@Data
public class ChunkDataMessage {
    private final int x, z;
    private final boolean continuous;
    private final int primaryMask;
    private final byte[] data;

    public static ChunkDataMessage empty(int x, int z) {
        return new ChunkDataMessage(x, z, true, 0, new byte[0]);
    }
}
