package com.hashfunctions;

public final class Sdbm {
    public static long hash(String s) {
        long hash = 0;
        for(int i = 0; i < s.length(); ++i) {
            hash = s.charAt(i) + (hash << 6) + (hash << 16) - hash;
        }
        return hash;
    }
}
