package com.hpfxd.polar.util;

import lombok.Data;

@Data
public class MutableLocation {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public MutableLocation(Position position) {
        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
        this.yaw = 0;
        this.pitch = 0;
    }

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setYawPitch(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void setLocation(MutableLocation from) {
        this.x = from.x;
        this.y = from.y;
        this.z = from.z;
        this.yaw = from.yaw;
        this.pitch = from.pitch;
    }

    public double distanceToSq(MutableLocation location) {
        return Math.pow(this.x - location.x, 2) + Math.pow(this.y - location.y, 2) + Math.pow(this.z - location.z, 2);
    }

    public byte getYawAngle() {
        float f = this.getYaw() % 360;
        return (byte) ((f / 360f) * 256f);
    }

    public byte getPitchAngle() {
        float f = this.getPitch() % 360f;
        return (byte) ((f / 360f) * 256f);
    }

    public Vector toVector() {
        return new Vector((float) this.x, (float) this.y, (float) this.z);
    }
}
