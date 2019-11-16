package org.jzl;

public interface ObservableOnSubscribe<T> {
    void subscribe(Emitter<T> emitter);
}
