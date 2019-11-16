package org.jzl;

import org.jzl.function.TernaryFunction;
import org.jzl.holder.Holder;
import org.jzl.observable.*;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public abstract class Observable<T, C extends Context<C>> implements ObservableSource<T> {

    public abstract void subscribe(Observer<T> observer);

    public final <Downstream extends Context<Downstream>> Observable<T, Downstream> context(Function<C, Downstream> mapper) {
        return ObservableDownstreamContext.of(this, mapper);
    }


    public final <R> R reduce(R result, TernaryFunction<R, T, C, R> accumulator) {
        return ObservableReduce.of(this, result, accumulator, (t, c) -> t).get();
    }

    public final int count() {
        return reduce(Holder.ofInt(0), (r, t, c) -> r.increment()).getAsInt();
    }

    public final <R> Observable<R, C> map(BiFunction<T, C, R> mapper) {
        return ObservablePipeline.of(this, mapper);
    }

    public final <R> Observable<R, C> flatMap(BiFunction<T, C, ObservableSource<R>> mapper) {
        return ObservableFlatMap.of(this, mapper);
    }

    public final Observable<T, C> filter(BiPredicate<T, C> predicate) {
        return ObservableFilter.of(this, predicate);
    }

    public final Observable<T, C> peek(BiConsumer<T, C> consumer) {
        return ObservablePeek.of(this, consumer);
    }

    public final void forEach(BiConsumer<T, C> consumer) {
        subscribe(value -> {
            consumer.accept(value, getContext());
        });
    }

    public final <K, V> Observable<GroupedObservable<K, V, C>, C> groupBy(BiFunction<T, C, K> keySelector, BiFunction<T, C, V> valueSelector) {
        return ObservableGroupBy.of(this, keySelector, valueSelector);
    }

    static <T, C extends Context<C>> Observable<T, C> create(C context, ObservableOnSubscribe<T> subscribe) {
        return ObservableCreate.of(context, subscribe);
    }

    @SafeVarargs
    public static <T, C extends Context<C>> Observable<T, C> of(C context, T... values) {
        return create(context, emitter -> {
            for (T value : values) {
                emitter.launch(value);
            }
        });
    }

    public static <T, C extends Context<C>> Observable<T, C> of(C context, Collection<T> values) {
        return create(context, emitter -> {
            for (T value : values) {
                emitter.launch(value);
            }
        });
    }

    public static <T, C extends Context<C>> Observable<T, C> of(C context, ObservableSource<T> source) {
        return create(context, emitter -> {
            source.subscribe(emitter::launch);
        });
    }

    public abstract C getContext();
}
