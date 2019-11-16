package org.jzl.observable;

import org.jzl.Context;
import org.jzl.Observable;
import org.jzl.Observer;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class ObservableFilter<T, C extends Context<C>> extends ObservableUpstream<T, T, C> {
    private BiPredicate<T, C> predicate;

    private ObservableFilter(Observable<T, C> upstreamObservable, BiPredicate<T, C> predicate) {
        super(upstreamObservable);
        this.predicate = predicate;
    }

    @Override
    public void subscribe(Observer<T> observer) {
        upstreamObservable.subscribe(new FilterObserver(observer));
    }

    public static <T, C extends Context<C>> ObservableFilter<T, C> of(Observable<T, C> upstreamObservable, BiPredicate<T, C> predicate) {
        return new ObservableFilter<>(upstreamObservable, predicate);
    }

    private class FilterObserver implements Observer<T> {

        private Observer<T> observer;

        FilterObserver(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T value) {
            if (predicate.test(value, getContext())) {
                observer.onNext(value);
            }
        }
    }
}
