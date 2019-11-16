package org.jzl.holder;

import java.util.function.LongSupplier;

public class LongHolder implements Holder<Long>, LongSupplier, Increment<LongHolder> , Decrement<LongHolder>{
    public long value;

    LongHolder(long value) {
        this.value = value;
    }

    @Override
    public Long get() {
        return value;
    }

    @Override
    public long getAsLong() {
        return value;
    }

    @Override
    public LongHolder increment() {
        value ++;
        return this;
    }

    @Override
    public LongHolder decrement() {
        value --;
        return this;
    }
}
