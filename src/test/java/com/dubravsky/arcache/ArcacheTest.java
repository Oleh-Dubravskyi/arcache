package com.dubravsky.arcache;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ArcacheTest {

    private static final String ANY_STRING = "LoremIpsum";
    private static final byte[] NULL_BYTE_ARRAY = null;

    private Arcache defaultArcache;

    private static String fillStringWith(int size, int value) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(String.valueOf(value));
        }
        return result.toString();
    }

    @BeforeMethod
    public void init() {
        defaultArcache = Arcache.createDefault();
    }

    @Test
    public void shouldPutAndGetString() {
        String key = "key_01";
        defaultArcache.put(key, ANY_STRING);

        assertThat(defaultArcache.get(key), is(ANY_STRING));
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void shouldThrowNpeIfNullKeyIsProvided() {
        defaultArcache.put(null, ANY_STRING);
    }

    @Test
    public void shouldRemoveOldestItemIfLimitIsExceeded() {
        String stringFilledWithZeros = fillStringWith(50, 0);
        String stringFilledWithOnes = fillStringWith(50, 1);
        String stringFilledWithTwos = fillStringWith(50, 2);

        Arcache arcache = Arcache.builder()
                .limitSize(100)
                .build();

        arcache.put("key_01", stringFilledWithZeros);
        arcache.put("key_02", stringFilledWithOnes);
        arcache.put("key_03", stringFilledWithTwos); // it tries to insert element with size 50 but
        // the cache contains two elements with total size of 100. hence, the oldes element (key_01) should be removed

        assertThat(arcache.get("key_01"), is(NULL_BYTE_ARRAY));
        assertThat(arcache.get("key_02"), is(stringFilledWithOnes));
        assertThat(arcache.get("key_03"), is(stringFilledWithTwos));
    }

}