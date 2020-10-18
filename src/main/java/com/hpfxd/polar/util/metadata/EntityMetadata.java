package com.hpfxd.polar.util.metadata;

import io.netty.buffer.ByteBuf;

public class EntityMetadata {
    private final Object[] values = new Object[0x1F];

    public void set(int index, Object value) {
        values[index] = value;
    }

    public void setByte(int index, int value) {
        set(index, (byte) value);
    }

    public void setInt(int index, int value) {
        set(index, value);
    }

    public void setFloat(int index, float value) {
        set(index, value);
    }

    public void setString(int index, String value) {
        set(index, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(int index) {
        return (T) values[index];
    }

    public int getByte(int index) {
        return this.<Byte>get(index);
    }

    public int getInt(int index) {
        return this.<Integer>get(index);
    }

    public String getString(int index) {
        return this.get(index);
    }

    public void write(ByteBuf buf) {
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            if (value == null) {
                continue;
            }
            MetadataType type = typeOf(value);

            int item = i | type.ordinal() << 5;
            buf.writeByte(item);
            type.getWriteHandler().accept(buf, value);
        }
        buf.writeByte(0x7F);
    }

    private MetadataType typeOf(Object value) {
        if (value instanceof Byte) {
            return MetadataType.BYTE;
        } else if (value instanceof Short) {
            return MetadataType.SHORT;
        } else if (value instanceof Integer) {
            return MetadataType.INT;
        } else if (value instanceof Float) {
            return MetadataType.FLOAT;
        } else if (value instanceof String) {
            return MetadataType.STRING;
        }
        return null;
    }
}
