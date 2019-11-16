package org.jzl.observable;

import org.jzl.Context;
import org.jzl.Observable;

import java.util.Objects;

public abstract class GroupedObservable<K, V, C extends Context<C>> extends Observable<V, C> {
    private C context;
    private K key;

    GroupedObservable(C context, K key) {
        Objects.requireNonNull(context);
        this.context = context;
        this.key = key;
    }

    @Override
    public C getContext() {
        return context;
    }

    public K getKey() {
        return key;
    }
}
