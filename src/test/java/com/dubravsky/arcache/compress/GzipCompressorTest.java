package com.dubravsky.arcache.compress;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class GzipCompressorTest {

    private static final String INPUT = "Sed pulvinar cursus diam, et vestibulum lectus molestie et.";

    private GzipCompressor compressor;

    @BeforeMethod
    public void init() {
        compressor = new GzipCompressor();
    }

    @Test
    public void shouldCompressAndDecompress() throws IOException {
        byte[] compressedData = compressor.compress(INPUT);
        String decompressedString = compressor.decompress(compressedData);

        assertThat(decompressedString, is(INPUT));
    }

    @Test
    public void shouldCompressAndDecompressNull() throws IOException {
        byte[] compressedData = compressor.compress(null);
        String decompressedString = compressor.decompress(compressedData);

        assertThat(decompressedString, is(nullValue()));
    }

    @Test
    public void shouldCompressAndDecompressEmptyString() throws IOException {
        byte[] compressedData = compressor.compress("");
        String decompressedString = compressor.decompress(compressedData);

        assertThat(decompressedString, is(""));
    }

}
