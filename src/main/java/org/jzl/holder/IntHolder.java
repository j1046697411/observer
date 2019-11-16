package org.jzl.holder;

import java.util.function.IntSupplier;

public class IntHolder implements Holder<Integer>, IntSupplier, Increment<IntHolder>, Decrement<IntHolder> {

    public int value;

    IntHolder(int value) {
        this.value = value;
    }

    @Override
    public int getAsInt() {
        return value;
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public IntHolder decrement() {
        value --;
        return this;
    }

    @Override
    public IntHolder increment() {
        value ++;
        return this;
    }
}
