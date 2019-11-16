package org.jzl.event;

public interface EventSource<L> {

    void addEventListener(L listener);

    void removeEventListener(L listener);

    void clearAllListeners();
}
