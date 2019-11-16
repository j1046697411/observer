package org.jzl.holder;

import java.util.Objects;
import java.util.function.*;

public class EntityHolder<T> implements Holder<T> {

    private static final EntityHolder<?> EMPTY = new EntityHolder<>(null);

    private T value;

    private EntityHolder(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    public <R> EntityHolder<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (value == null) {
            return empty();
        } else {
            return EntityHolder.ofNullable(mapper.apply(value));
        }
    }

    public <R, U> EntityHolder<R> map(U u, BiFunction<T, U, R> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (value == null || u == null) {
            return empty();
        } else {
            return EntityHolder.ofNullable(mapper.apply(value, u));
        }
    }

    public <R, U> EntityHolder<R> flatMap(U u, BiFunction<T, U, EntityHolder<R>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (value == null || u == null) {
            return empty();
        } else {
            return Objects.requireNonNull(mapper.apply(value, u));
        }
    }

    public <R> EntityHolder<R> flatMap(Function<T, R> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (value == null) {
            return empty();
        } else {
            return EntityHolder.of(mapper.apply(value));
        }
    }

    public EntityHolder<T> filter(Predicate<T> filter) {
        Objects.requireNonNull(filter, "filter is null");
        if (value != null && filter.test(value)) {
            return this;
        } else {
            return empty();
        }
    }

    public <U> EntityHolder<T> filter(U u, BiPredicate<T, U> filter) {
        Objects.requireNonNull(filter, "filter is null");
        if (value != null && u != null && filter.test(value, u)) {
            return this;
        } else {
            return empty();
        }
    }

    public EntityHolder<T> def(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        if (value == null) {
            return ofNullable(supplier.get());
        } else {
            return this;
        }
    }

    public EntityHolder<T> def(T def) {
        if (value == null) {
            return ofNullable(def);
        } else {
            return this;
        }
    }

    public boolean isPresent() {
        return value != null;
    }

    public static <T> EntityHolder<T> of(T value) {
        return new EntityHolder<>(Objects.requireNonNull(value));
    }

    @SuppressWarnings("unchecked")
    public static <T> EntityHolder<T> empty() {
        return (EntityHolder<T>) EMPTY;
    }

    public static <T> EntityHolder<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }
}
