package org.jzl;

import java.util.function.BiPredicate;

public interface Context<C extends Context<C>> extends Emitter<Object> {
    <T> void register(Class<T> type, BiPredicate<T, C> receiver);
}
