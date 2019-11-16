package org.jzl.function;

public interface TernaryFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
