package org.jzl.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class Pool<T> {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final LinkedList<T> objects;
    private final int maxSize;

    public Pool(int maxSize) {
        objects = new LinkedList<>();
        this.maxSize = maxSize;
    }

    protected abstract T newObject();

    public T obtain() {
        lock.readLock().lock();
        if (objects.size() > 0) {
            lock.readLock().unlock();
            try {
                lock.writeLock().lock();
                return objects.remove();
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            lock.readLock().unlock();
        }
        return newObject();

    }

    public void free(T object) {
        if (object == null) {
            return;
        }
        lock.readLock().lock();
        if (objects.size() > maxSize) {
            return;
        }
        lock.readLock().unlock();

        if (object instanceof Poolable) {
            ((Poolable) object).reset();
        }
        try {
            lock.writeLock().lock();
            objects.add(object);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SafeVarargs
    public final void freeAll(T... objects) {
        for (T object : objects) {
            free(object);
        }
    }

    public final void freeAll(Collection<T> objects) {
        objects.forEach(this::free);
    }

    public interface Poolable {
        void reset();
    }

    public static class ReflectionPool<T> extends Pool<T> {

        private Class<T> type;

        public ReflectionPool(int maxSize, Class<T> type) {
            super(maxSize);
            this.type = type;
        }

        @Override
        protected T newObject() {
            try {
                return type.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
