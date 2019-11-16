package org.jzl.holder;

public class ByteHolder implements Holder<Byte>, Increment<ByteHolder>, Decrement<ByteHolder> {

    public byte value;

    ByteHolder(byte value) {
        this.value = value;
    }

    @Override
    public Byte get() {
        return value;
    }

    @Override
    public ByteHolder decrement() {
        value --;
        return this;
    }

    @Override
    public ByteHolder increment() {
        value ++;
        return this;
    }
}
