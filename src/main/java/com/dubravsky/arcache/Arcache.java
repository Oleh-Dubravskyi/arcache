package com.dubravsky.arcache;

import java.util.concurrent.ConcurrentHashMap;

public class Arcache {

    private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    public static Arcache createDefault() {
        return new Arcache();
    }

    public String get(String key) {
        return map.get(key);
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

}