package com.hashfunctions;

public final class LoseLose {
    public static long hash(String s) {
        long hash = 0;
        for(int i = 0; i < s.length(); ++i) {
            hash += s.charAt(i);
        }
        return hash;
    }
}
