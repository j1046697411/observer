package org.jzl.observable;

import org.jzl.Context;
import org.jzl.Observable;
import org.jzl.Observer;
import org.jzl.function.TernaryFunction;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ObservableReduce<R, T, C extends Context<C>> extends ObservablePipeline<T, T, C> implements Supplier<R>, Observer<T> {

    private TernaryFunction<R, T, C, R> accumulator;
    private R result;

    private ObservableReduce(Observable<T, C> upstreamObservable, R result, TernaryFunction<R, T, C, R> accumulator, BiFunction<T, C, T> mapper) {
        super(upstreamObservable, mapper);
        this.accumulator = accumulator;
        this.result = result;
        upstreamObservable.subscribe(this);
    }

    @Override
    public R get() {
        return result;
    }

    public static <R, T, C extends Context<C>> ObservableReduce<R, T, C> of(Observable<T, C> upstreamObservable, R result, TernaryFunction<R, T, C, R> accumulator, BiFunction<T, C, T> mapper) {
        return new ObservableReduce<>(upstreamObservable, result, accumulator, mapper);
    }

    @Override
    public void onNext(T value) {
        result = accumulator.apply(result, value, getContext());
    }
}
