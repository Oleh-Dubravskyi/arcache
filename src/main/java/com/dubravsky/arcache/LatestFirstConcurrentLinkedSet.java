package com.dubravsky.arcache;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class LatestFirstConcurrentLinkedSet<T> {

    private LinkedList<T> queue = new LinkedList<>();
    private Lock lock = new ReentrantLock();

    LatestFirstConcurrentLinkedSet() {
    }

    public void add(T value) {
        try {
            lock.lock();
            queue.remove(value);
            queue.addFirst(value);
        } finally {
            lock.unlock();
        }
    }

    public T removeLast() {
        try {
            lock.lock();
            return queue.removeLast();
        } finally {
            lock.unlock();
        }
    }

}
