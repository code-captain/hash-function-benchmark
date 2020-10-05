package com.hashfunctions;

public final class Djb2 {
    public static long hash(String s) {
        long hash = 5381;
        for(int i = 0; i < s.length(); ++i) {
            hash = ((hash << 5) + hash) + s.charAt(i);
        }
        return hash;
    }
}
