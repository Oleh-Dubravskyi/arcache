package com.dubravsky.arcache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Arcache {

    private static final int DEFAULT_LIMIT_SIZE = 16 * 1024;

    private final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();
    private final int limitSize;

    private LatestFirstConcurrentLinkedSet<String> accessedKeys = new LatestFirstConcurrentLinkedSet<>();
    private AtomicInteger size = new AtomicInteger();

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
        return map.get(key);
    }

    public void put(String key, String value) {
        if (key == null) {
            throw new NullPointerException("Key in Arcache can not be null");
        }

        try {
            lock.lock();
            map.put(key, value);
            accessedKeys.add(key);
            if (size.addAndGet(value.length()) > limitSize) {
                removeElementsWhileSizeIsExceeded();
            }
        } finally {
            lock.unlock();
        }
    }

    private void removeElementsWhileSizeIsExceeded() {
        int currentSize;
        do {
            String key = accessedKeys.removeLast();
            String value = map.remove(key);
            currentSize = size.addAndGet(-1 * value.length());
        } while (currentSize > limitSize);
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