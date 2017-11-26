package com.dubravsky.arcache.compress;

import java.io.IOException;

public interface Compressor {

    byte[] compress(String value) throws IOException;

    String decompress(byte[] data) throws IOException;

}
