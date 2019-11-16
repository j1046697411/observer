package org.jzl.holder;

public interface Decrement<T extends Decrement<T>> {
    T decrement();
}
