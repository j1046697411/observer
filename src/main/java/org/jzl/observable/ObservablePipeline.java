package org.jzl.observable;

import org.jzl.Context;
import org.jzl.Observable;
import org.jzl.Observer;

import java.util.function.BiFunction;

public class ObservablePipeline<Upstream, Downstream, C extends Context<C>> extends ObservableUpstream<Upstream, Downstream, C> {

    private BiFunction<Upstream, C, Downstream> mapper;

    protected ObservablePipeline(Observable<Upstream, C> upstreamObservable, BiFunction<Upstream, C, Downstream> mapper) {
        super(upstreamObservable);
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Observer<Downstream> observer) {
        upstreamObservable.subscribe(new PipelineObserver(observer, mapper));
    }

    public static <R, T, C extends  Context<C>> ObservablePipeline<T, R, C> of(Observable<T, C> upstreamObservable, BiFunction<T, C, R> mapper) {
        return new ObservablePipeline<>(upstreamObservable, mapper);
    }

    private class PipelineObserver implements Observer<Upstream> {

        private Observer<Downstream> downstreamObserver;
        private BiFunction<Upstream, C, Downstream> mapper;

        private PipelineObserver(Observer<Downstream> downstreamObserver, BiFunction<Upstream, C, Downstream> mapper) {
            this.downstreamObserver = downstreamObserver;
            this.mapper = mapper;
        }

        @Override
        public void onNext(Upstream upstream) {
            downstreamObserver.onNext(mapper.apply(upstream, getContext()));
        }
    }
}
