package org.jzl.event;

import org.jzl.util.Pool;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

public abstract class AbstractEventSource<L> implements EventSource<L> {

    private Collection<L> listeners;
    private List<Operation<L>> operations = new ArrayList<>();
    private final Pool<Operation<L>> pool = new Pool<Operation<L>>(16) {
        @Override
        protected Operation<L> newObject() {
            return new Operation<>();
        }
    };
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public AbstractEventSource(Collection<L> listeners) {
        this.listeners = Objects.requireNonNull(listeners, "listeners is null");
    }

    protected AbstractEventSource() {
        this.listeners = new LinkedList<>();
    }

    @Override
    public void addEventListener(L listener) {
        addOperation(OperationType.ADD, listener);
    }

    @Override
    public void removeEventListener(L listener) {
        addOperation(OperationType.REMOVE, listener);
    }

    private void addOperation(OperationType type, L listener) {
        Operation<L> operation = pool.obtain();
        operation.type = type;
        operation.listener = listener;
        lock.writeLock().lock();
        operations.add(operation);
        lock.readLock().unlock();
    }

    @Override
    public void clearAllListeners() {
        addOperation(OperationType.REMOVE_ALL, null);
    }

    protected void forEachEventListeners(Predicate<L> predicate) {
        Objects.requireNonNull(predicate);
        applyOperations();
        for (L listener : listeners) {
            if (predicate.test(listener)) {
                return;
            }
        }
    }

    private void applyOperations() {
        lock.readLock().lock();
        operations.forEach(operation -> {
            switch (operation.type) {
                case ADD: {
                    listeners.add(operation.listener);
                    break;
                }
                case REMOVE: {
                    listeners.remove(operation.listener);
                    break;
                }
                case REMOVE_ALL: {
                    listeners.clear();
                    break;
                }
            }
            pool.free(operation);
        });
        lock.readLock().unlock();

        lock.writeLock().lock();
        operations.clear();
        lock.writeLock().unlock();
    }

    static class Operation<L> {
        OperationType type;
        L listener;
    }

    enum OperationType {
        REMOVE, ADD, REMOVE_ALL
    }
}
