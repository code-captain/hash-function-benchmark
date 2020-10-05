package com.hashfunctions.murmur;

public class Murmur2 extends MurmurConstants {

    public static long hash(final byte[] data, int length, long seed) {
        final long m = 0x5bd1e995l;
        final int r = 24;

        // Initialize the hash to a 'random' value
        long hash = ((seed ^ length) & UINT_MASK);

        // Mix 4 bytes at a time into the hash
        int length4 = length >>> 2;

        for (int i = 0; i < length4; i++) {
            final int i4 = i << 2;

            long k = (data[i4] & UNSIGNED_MASK);
            k |= (data[i4 + 1] & UNSIGNED_MASK) << 8;
            k |= (data[i4 + 2] & UNSIGNED_MASK) << 16;
            k |= (data[i4 + 3] & UNSIGNED_MASK) << 24;

            k = ((k * m) & UINT_MASK);
            k ^= ((k >>> r) & UINT_MASK);
            k = ((k * m) & UINT_MASK);

            hash = ((hash * m) & UINT_MASK);
            hash = ((hash ^ k) & UINT_MASK);
        }

        int offset = length4 << 2;
        switch (length & 3) {
            case 3:
                hash ^= ((data[offset + 2] & UNSIGNED_MASK) << 16);
            case 2:
                hash ^= ((data[offset + 1] & UNSIGNED_MASK) << 8);
            case 1:
                hash ^= (data[offset] & UNSIGNED_MASK);
                hash = ((hash * m) & UINT_MASK);
        }

        hash ^= ((hash >>> 13) & UINT_MASK);
        hash = ((hash * m) & UINT_MASK);
        hash ^= hash >>> 15;
        return hash;
    }

    public static long hash(String data) {
        final long seed = 0x9747b28c;
        final long m = 0x5bd1e995l;
        final int r = 24;

        // Initialize the hash to a 'random' value
        int length = data.length();
        long hash = ((seed ^ length) & UINT_MASK);

        // Mix 4 bytes at a time into the hash
        int length4 = length >>> 2;

        for (int i = 0; i < length4; i++) {
            final int i4 = i << 2;

            long k = (data.charAt(i4) & UNSIGNED_MASK);
            k |= (data.charAt(i4 + 1) & UNSIGNED_MASK) << 8;
            k |= (data.charAt(i4 + 2) & UNSIGNED_MASK) << 16;
            k |= (data.charAt(i4 + 3) & UNSIGNED_MASK) << 24;

            k = ((k * m) & UINT_MASK);
            k ^= ((k >>> r) & UINT_MASK);
            k = ((k * m) & UINT_MASK);

            hash = ((hash * m) & UINT_MASK);
            hash = ((hash ^ k) & UINT_MASK);
        }

        int offset = length4 << 2;
        switch (length & 3) {
            case 3:
                hash ^= ((data.charAt(offset + 2) & UNSIGNED_MASK) << 16);
            case 2:
                hash ^= ((data.charAt(offset + 1) & UNSIGNED_MASK) << 8);
            case 1:
                hash ^= (data.charAt(offset) & UNSIGNED_MASK);
                hash = ((hash * m) & UINT_MASK);
        }

        hash ^= ((hash >>> 13) & UINT_MASK);
        hash = ((hash * m) & UINT_MASK);
        hash ^= hash >>> 15;

        return hash;
    }

    public static long hash64(String data, long seed) {
        final long m = 0xc6a4a7935bd1e995L;
        final int r = 47;
        int length = data.length();
        long h = (seed & UINT_MASK) ^ (length * m);

        int length8 = length >> 3;

        for (int i = 0; i < length8; i++) {
            final int i8 = i << 3;

            long k =  ((long)data.charAt(i8)&0xff) +(((long)data.charAt(i8+1)&0xff)<<8)
                    +(((long)data.charAt(i8+2)&0xff)<<16) +(((long)data.charAt(i8+3)&0xff)<<24)
                    +(((long)data.charAt(i8+4)&0xff)<<32) +(((long)data.charAt(i8+5)&0xff)<<40)
                    +(((long)data.charAt(i8+6)&0xff)<<48) +(((long)data.charAt(i8+7)&0xff)<<56);

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        switch (length & 7) {
            case 7:
                h ^= (long) (data.charAt((length & ~7) + 6) & 0xff) << 48;

            case 6:
                h ^= (long) (data.charAt((length & ~7) + 5) & 0xff) << 40;

            case 5:
                h ^= (long) (data.charAt((length & ~7) + 4) & 0xff) << 32;

            case 4:
                h ^= (long) (data.charAt((length & ~7) + 3) & 0xff) << 24;

            case 3:
                h ^= (long) (data.charAt((length & ~7) + 2) & 0xff) << 16;

            case 2:
                h ^= (long) (data.charAt((length & ~7) + 1) & 0xff) << 8;

            case 1:
                h ^= (long) (data.charAt(length & ~7) & 0xff);
                h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        return h;
    }

    public static long hash64(final byte[] data, int length, long seed) {
        final long m = 0xc6a4a7935bd1e995L;
        final int r = 47;

        long h = (seed & UINT_MASK) ^ (length * m);

        int length8 = length >> 3;

        for (int i = 0; i < length8; i++) {
            final int i8 = i << 3;

            long k =  ((long)data[i8]&0xff) +(((long)data[i8+1]&0xff)<<8)
                    +(((long)data[i8+2]&0xff)<<16) +(((long)data[i8+3]&0xff)<<24)
                    +(((long)data[i8+4]&0xff)<<32) +(((long)data[i8+5]&0xff)<<40)
                    +(((long)data[i8+6]&0xff)<<48) +(((long)data[i8+7]&0xff)<<56);

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        switch (length & 7) {
            case 7:
                h ^= (long) (data[(length & ~7) + 6] & 0xff) << 48;

            case 6:
                h ^= (long) (data[(length & ~7) + 5] & 0xff) << 40;

            case 5:
                h ^= (long) (data[(length & ~7) + 4] & 0xff) << 32;

            case 4:
                h ^= (long) (data[(length & ~7) + 3] & 0xff) << 24;

            case 3:
                h ^= (long) (data[(length & ~7) + 2] & 0xff) << 16;

            case 2:
                h ^= (long) (data[(length & ~7) + 1] & 0xff) << 8;

            case 1:
                h ^= (long) (data[length & ~7] & 0xff);
                h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        return h;
    }
}
