package org.jzl.observable;

import org.jzl.Context;
import org.jzl.Emitter;
import org.jzl.Observable;
import org.jzl.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ObservableGroupBy<K, V, T, C extends Context<C>> extends ObservableUpstream<T, GroupedObservable<K, V, C>, C> {

    private BiFunction<T, C, K> keySelector;
    private BiFunction<T, C, V> valueSelector;


    private ObservableGroupBy(Observable<T, C> upstreamObservable, BiFunction<T, C, K> keySelector, BiFunction<T, C, V> valueSelector) {
        super(upstreamObservable);
        this.keySelector = keySelector;
        this.valueSelector = valueSelector;
    }

    @Override
    public void subscribe(Observer<GroupedObservable<K, V, C>> observer) {
        upstreamObservable.subscribe(new GroupByObserver(observer));
    }

    private class GroupByObserver implements Observer<T> {

        private Observer<GroupedObservable<K, V, C>> observer;
        private Map<K, Emitter<V>> groups;

        private GroupByObserver(Observer<GroupedObservable<K, V, C>> observer) {
            this.observer = observer;
            groups = new HashMap<>();
        }

        @Override
        public void onNext(T value) {
            K key = keySelector.apply(value, getContext());
            Emitter<V> groupEmitter = groups.get(key);
            if (groupEmitter == null) {
                Grouped<K, V, C> grouped = new Grouped<>(getContext(), key);
                this.groups.put(key, grouped);
                observer.onNext(grouped);
                groupEmitter = grouped;
            }
            groupEmitter.launch(valueSelector.apply(value, getContext()));
        }
    }

    private static class Grouped<K, V, C extends Context<C>> extends GroupedObservable<K, V, C> implements Emitter<V> {

        private Observer<V> observer;
        private List<V> queue;

        Grouped(C context, K key) {
            super(context, key);
            queue = new ArrayList<>();
        }

        @Override
        public void subscribe(Observer<V> observer) {
            this.observer = observer;
            drain();
        }

        @Override
        public void launch(V value) {
            queue.add(value);
            drain();
        }

        private void drain() {
            if (observer == null) {
                return;
            }
            for (V v : queue) {
                observer.onNext(v);
            }
            queue.clear();
        }
    }

    public static <K, V, T, C extends Context<C>> ObservableGroupBy<K, V, T, C> of(Observable<T, C> upstreamObservable, BiFunction<T, C, K> keySelector, BiFunction<T, C, V> valueSelector){
        return new ObservableGroupBy<>(upstreamObservable, keySelector, valueSelector);
    }
}
