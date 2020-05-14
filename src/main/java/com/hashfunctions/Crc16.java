package com.hashfunctions;

public class Crc16 {

    public static int hash(CharSequence charSequence) {
        return hash(charSequence.toString().getBytes());
    }

    public static int hash(byte[] buffer) {
        /* Note the change here */
        int crc = 0x1D0F;
        for (int j = 0; j < buffer.length ; j++) {
            crc = ((crc  >>> 8) | (crc  << 8) )& 0xffff;
            crc ^= (buffer[j] & 0xff);//byte to int, trunc sign
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return crc;
    }
}
