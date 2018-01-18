package com.burgerdelivery.util;

public class BitFlag {
    private int flags;

    public BitFlag() {
    }

    public BitFlag(int flags) {
        this.flags = flags;
    }

    public void set(int flag) {
        flags = (flags | (1 << flag));
    }

    public void unSet(int flag) {
        flags = (flags & ~(1 << flag));
    }

    public boolean isSet(int flag) {
        return ((flags & flag) == flag);
    }

    public int getFlags() {
        return flags;
    }
}