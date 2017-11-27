package com.dubravsky.arcache.compress;

public interface Compressor {

    byte[] compress(String value);

    String decompress(byte[] data);

}
