package org.jzl;

public interface ObservableSource<T> {
    void subscribe(Observer<T> observer);
}
