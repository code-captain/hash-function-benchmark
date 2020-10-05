package com.hashfunctions.murmur;

public class MurmurConstants {
    /**
     * Helps convert a byte into its unsigned value
     */
    public static final int UNSIGNED_MASK = 0xff;

    /**
     * Helps convert integer to its unsigned value
     */
    public static final int UINT_MASK = 0xFFFFFFFF;

    /**
     * Helps convert long to its unsigned value
     */
    public static final long LONG_MASK = 0xFFFFFFFFFFFFFFFFL;

    public static final long SEED = 0x9747b28c;
}
