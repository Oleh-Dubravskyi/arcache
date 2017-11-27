package com.dubravsky.arcache.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamUtils {

    private InputStreamUtils() {
    }

    public static byte[] readFromInputStream(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int numberReadBytes;
            byte[] array = new byte[1024];
            while ((numberReadBytes = inputStream.read(array, 0, array.length)) != -1) {
                buffer.write(array, 0, numberReadBytes);
            }
            buffer.flush();
            return buffer.toByteArray();
        }
    }

}
