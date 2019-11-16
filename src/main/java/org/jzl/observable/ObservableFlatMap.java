package org.jzl.observable;

import org.jzl.Context;
import org.jzl.Observable;
import org.jzl.ObservableSource;
import org.jzl.Observer;

import java.util.function.BiFunction;

public class ObservableFlatMap<Upstream, Downstream, C extends Context<C>> extends ObservableUpstream<Upstream, Downstream, C> {

    private BiFunction<Upstream, C, ObservableSource<Downstream>> mapper;

    private ObservableFlatMap(Observable<Upstream, C> upstreamObservable, BiFunction<Upstream, C, ObservableSource<Downstream>> mapper) {
        super(upstreamObservable);
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Observer<Downstream> observer) {
        upstreamObservable.subscribe(new FlatMapObserver(observer));
    }

    public static <R, T, C extends Context<C>> ObservableFlatMap<T, R, C> of(Observable<T, C> upstreamObservable, BiFunction<T, C, ObservableSource<R>> mapper) {
        return new ObservableFlatMap<>(upstreamObservable, mapper);
    }

    private class FlatMapObserver implements Observer<Upstream> {

        private Observer<Downstream> downstreamObserver;

        private FlatMapObserver(Observer<Downstream> downstreamObserver) {
            this.downstreamObserver = downstreamObserver;
        }

        @Override
        public void onNext(Upstream value) {
            mapper.apply(value, getContext()).subscribe(downstreamObserver);
        }
    }
}
