package org.jzl.holder;

import java.util.function.DoubleSupplier;

public class DoubleHolder implements Holder<Double>, DoubleSupplier, Increment<DoubleHolder>, Decrement<DoubleHolder> {
    public double value;

    DoubleHolder(double value) {
        this.value = value;
    }

    @Override
    public double getAsDouble() {
        return value;
    }

    @Override
    public Double get() {
        return value;
    }

    @Override
    public DoubleHolder decrement() {
        value--;
        return this;
    }

    @Override
    public DoubleHolder increment() {
        value++;
        return this;
    }
}
