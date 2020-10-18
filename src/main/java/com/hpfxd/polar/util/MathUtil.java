package com.hpfxd.polar.util;

public class MathUtil {
    public static double distanceToSq(int x1, int z1, int x2, int z2) {
        return Math.pow(x1 - x2, 2) + Math.pow(z1 - z2, 2);
    }
}
