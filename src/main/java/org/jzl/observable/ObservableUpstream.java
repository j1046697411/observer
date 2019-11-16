package org.jzl.observable;

import org.jzl.Context;
import org.jzl.Observable;
import org.jzl.ObservableSource;

import java.util.Objects;

public abstract class ObservableUpstream<Upstream, Downstream, C extends Context<C>> extends Observable<Downstream, C> implements ObservableSource<Downstream> {

    Observable<Upstream, C> upstreamObservable;

    ObservableUpstream(Observable<Upstream, C> upstreamObservable) {
        this.upstreamObservable = Objects.requireNonNull(upstreamObservable);
    }

    protected Observable<Upstream, C> getUpstreamObservable() {
        return upstreamObservable;
    }

    @Override
    public C getContext() {
        return upstreamObservable.getContext();
    }
}
