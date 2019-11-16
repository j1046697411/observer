package org.jzl.observable;

import org.jzl.Context;
import org.jzl.Observable;
import org.jzl.Observer;

import java.util.function.BiConsumer;

public class ObservablePeek<T, C extends Context<C>> extends ObservableUpstream<T, T, C> {

    private BiConsumer<T, C> consumer;

    private ObservablePeek(Observable<T, C> upstreamObservable, BiConsumer<T, C> consumer) {
        super(upstreamObservable);
        this.consumer = consumer;
    }

    @Override
    public void subscribe(Observer<T> observer) {
        upstreamObservable.subscribe(new PeekObserver(observer));
    }

    public static <T, C extends Context<C>> ObservablePeek<T, C> of(Observable<T, C> upstreamObservable, BiConsumer<T, C> consumer){
        return new ObservablePeek<>(upstreamObservable, consumer);
    }

    private class PeekObserver implements Observer<T> {

        private Observer<T> observer;

        PeekObserver(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T value) {
            consumer.accept(value, getContext());
            observer.onNext(value);
        }
    }
}
