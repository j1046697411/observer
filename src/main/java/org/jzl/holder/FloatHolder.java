package org.jzl.holder;

public class FloatHolder implements Holder<Float>, Increment<FloatHolder>, Decrement<FloatHolder> {
    public float value;

    FloatHolder(float value) {
        this.value = value;
    }

    @Override
    public Float get() {
        return value;
    }

    @Override
    public FloatHolder decrement() {
        value--;
        return this;
    }

    @Override
    public FloatHolder increment() {
        value++;
        return this;
    }
}
