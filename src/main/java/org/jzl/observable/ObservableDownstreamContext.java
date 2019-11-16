package org.jzl.observable;

import org.jzl.Context;
import org.jzl.Observable;
import org.jzl.Observer;

import java.util.function.Function;

public final class ObservableDownstreamContext<T, Upstream extends Context<Upstream>, Downstream extends Context<Downstream>> extends Observable<T, Downstream> {

    private Downstream context;
    private Observable<T, Upstream> upstreamObservable;

    private ObservableDownstreamContext(Observable<T, Upstream> upstreamObservable, Function<Upstream, Downstream> mapper) {
        this.context = mapper.apply(upstreamObservable.getContext());
        this.upstreamObservable = upstreamObservable;
    }

    @Override
    public void subscribe(Observer<T> observer) {
        upstreamObservable.subscribe(observer);
    }

    @Override
    public Downstream getContext() {
        return context;
    }

    public static <T, Upstream extends Context<Upstream>, Downstream extends Context<Downstream>> ObservableDownstreamContext<T, Upstream, Downstream> of(Observable<T, Upstream> upstreamObservable, Function<Upstream, Downstream> mapper){
        return new ObservableDownstreamContext<>(upstreamObservable, mapper);
    }

}
