package org.jzl;

public interface Observer<T> {
    void onNext(T value);
}
