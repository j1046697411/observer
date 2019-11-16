package org.jzl.holder;

public class ShortHolder implements Holder<Short> , Increment<ShortHolder> , Decrement<ShortHolder>{

    public short value;

    ShortHolder(short value) {
        this.value = value;
    }

    @Override
    public Short get() {
        return value;
    }

    @Override
    public ShortHolder decrement() {
        value --;
        return this;
    }

    @Override
    public ShortHolder increment() {
        value ++;
        return this;
    }
}
