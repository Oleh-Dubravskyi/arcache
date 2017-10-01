package com.dubravsky.arcache;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ArcacheTest {

    private static final String ANY_STRING = "LoremIpsum";

    @Test
    public void shouldPutAndGetString() {
        Arcache arcache = Arcache.createDefault();

        String key = "key_01";
        arcache.put(key, ANY_STRING);

        assertThat(arcache.get(key), is(ANY_STRING));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfNullKeyIsProvided() {
        Arcache arcache = Arcache.createDefault();

        arcache.put(null, ANY_STRING);
    }
    
}