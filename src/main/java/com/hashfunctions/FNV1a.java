package com.hashfunctions;

public class FNV1a {
    private static final int FNV1_32_INIT = 0x811c9dc5;
    private static final int FNV1_PRIME_32 = 16777619;
    private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV1_PRIME_64 = 1099511628211L;


    public static int hash32(CharSequence charSequence) {
        return hash32(charSequence.toString().getBytes());
    }

    public static int hash32(byte[] data) {
        return hash32(data, data.length);
    }

    public static int hash32(byte[] data, int length) {
        int hash = FNV1_32_INIT;
        for (int i = 0; i < length; i++) {
            hash ^= (data[i] & 0xff);
            hash *= FNV1_PRIME_32;
        }

        return hash;
    }

    public static long hash64(CharSequence charSequence) {
        return hash64(charSequence.toString().getBytes());
    }

    public static long hash64(byte[] data) {
        return hash64(data, data.length);
    }

    public static long hash64(byte[] data, int length) {
        long hash = FNV1_64_INIT;
        for (int i = 0; i < length; i++) {
            hash ^= (data[i] & 0xff);
            hash *= FNV1_PRIME_64;
        }

        return hash;
    }
}