package org.jzl.observable;

import org.jzl.*;

import java.util.Objects;

public final class ObservableCreate<T, C extends Context<C>> extends Observable<T, C> {

    private ObservableOnSubscribe<T> subscribe;
    private C context;

    private ObservableCreate(C context, ObservableOnSubscribe<T> subscribe) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(subscribe);

        this.context = context;
        this.subscribe = subscribe;
    }

    @Override
    public void subscribe(Observer<T> observer) {
        subscribe.subscribe(new ObservableEmitter(observer));
    }

    @Override
    public C getContext() {
        return context;
    }

    public static <T, C extends Context<C>> ObservableCreate<T, C> of(C context, ObservableOnSubscribe<T> subscribe) {
        return new ObservableCreate<>(context, subscribe);
    }

    class ObservableEmitter implements Emitter<T> {

        private Observer<T> observer;

        ObservableEmitter(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void launch(T value) {
            observer.onNext(value);
        }
    }
}
