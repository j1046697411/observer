package org.jzl.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Pools {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Map<Class<?>, Pool<?>> pools = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Pool<T> getPool(Class<T> type) {
        lock.readLock().lock();
        Pool<T> pool = (Pool<T>) pools.get(type);
        lock.readLock().unlock();
        if (pool == null) {
            pool = new Pool.ReflectionPool<>(16, type);
            lock.writeLock().lock();
            pools.put(type, pool);
            lock.writeLock().unlock();
        }
        return pool;
    }

    @SuppressWarnings("unchecked")
    public <T> void free(T object) {
        Pool<T> objectPool = (Pool<T>) getPool(object.getClass());
        objectPool.free(object);
    }

    @SafeVarargs
    public final <T> void freeAll(T... objects) {
        for (T object : objects) {
            free(object);
        }
    }

    public final <T> void freeAll(Collection<T> objects) {
        objects.forEach(this::free);
    }

    public final <T> T obtain(Class<T> type) {
        return getPool(type).obtain();
    }

    public static Pools get() {
        return Holder.SIN;
    }

    private static class Holder {
        static final Pools SIN = new Pools();
    }
}
