package com.dubravsky.arcache;

import com.dubravsky.arcache.compress.Compressor;
import com.dubravsky.arcache.compress.GzipCompressor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Arcache {

    private static final int DEFAULT_LIMIT_SIZE = 16 * 1024;
    private static final int MAX_LENGTH_TO_STORE_WITHOUT_COMPRESSION = 1024;

    private final ConcurrentHashMap<String, byte[]> map = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();
    private final int limitSize;

    private LatestFirstConcurrentLinkedSet<String> accessedKeys = new LatestFirstConcurrentLinkedSet<>();
    private AtomicInteger size = new AtomicInteger();
    private Compressor compressor = new GzipCompressor();

    public Arcache(int limitSize) {
        this.limitSize = limitSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Arcache createDefault() {
        return new Arcache(DEFAULT_LIMIT_SIZE);
    }

    public String get(String key) {
        byte[] bytes = map.get(key);
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    public void put(String key, String value) {
        if (key == null) {
            throw new NullPointerException("Key in Arcache can not be null");
        }

        try {
            lock.lock();
            map.put(key, value.length() < MAX_LENGTH_TO_STORE_WITHOUT_COMPRESSION ? value.getBytes() : compress(value));
            accessedKeys.add(key);
            if (size.addAndGet(value.length()) > limitSize) {
                removeElementsWhileSizeIsExceeded();
            }
        } finally {
            lock.unlock();
        }
    }

    private byte[] compress(String value) {
        return compressor.compress(value);
    }

    private void removeElementsWhileSizeIsExceeded() {
        int currentSize;
        do {
            String key = accessedKeys.removeLast();
            byte[] value = map.remove(key);
            currentSize = size.addAndGet(-1 * value.length);
        } while (currentSize > limitSize);
    }

    public int dataSize() {
        return map.values().stream()
                .mapToInt(s -> s.length)
                .sum();
    }

    public static final class Builder {
        private int limitSize;

        private Builder() {
        }

        public Builder limitSize(int limitSize) {
            this.limitSize = limitSize;
            return this;
        }

        public Arcache build() {
            return new Arcache(limitSize);
        }
    }
}