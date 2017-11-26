package com.dubravsky.arcache.compress;

import com.dubravsky.arcache.compress.exception.CompressorException;
import com.dubravsky.arcache.utils.InputStreamUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompressor implements Compressor {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final String EMPTY_STRING = "";

    @Override
    public byte[] compress(String value) {
        if (value == null) {
            return null;
        }

        if (value.isEmpty()) {
            return EMPTY_BYTE_ARRAY;
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
                gzos.write(value.getBytes());
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new CompressorException(e);
        }
    }

    @Override
    public String decompress(byte[] data) {
        if (data == null) {
            return null;
        }

        if (data.length == 0) {
            return EMPTY_STRING;
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             GZIPInputStream gzis = new GZIPInputStream(bais)) {
            byte[] bytes = InputStreamUtils.readFromInputStream(gzis);
            return new String(bytes);
        } catch (IOException e) {
            throw new CompressorException(e);
        }
    }

}
