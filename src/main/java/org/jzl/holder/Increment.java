package org.jzl.holder;

public interface Increment<T extends Increment<T>> {
    T increment();
}
