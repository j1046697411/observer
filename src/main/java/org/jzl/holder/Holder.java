package org.jzl.holder;

import java.util.function.Supplier;

public interface Holder<T> extends Supplier<T> {

    @Override
    T get();

    static IntHolder ofInt(int value) {
        return new IntHolder(value);
    }

    static ShortHolder ofShort(short value) {
        return new ShortHolder(value);
    }

    static LongHolder ofLong(long value) {
        return new LongHolder(value);
    }

    static ByteHolder ofByte(byte value) {
        return new ByteHolder(value);
    }

    static BooleanHolder ofBoolean(boolean value) {
        return value ? BooleanHolder.TRUE : BooleanHolder.FALSE;
    }

    static FloatHolder ofFloat(float value) {
        return new FloatHolder(value);
    }

    static DoubleHolder ofDouble(double value) {
        return new DoubleHolder(value);
    }

    static CharHolder ofChar(char value) {
        return new CharHolder(value);
    }

    static <T> EntityHolder<T> ofNullable(T value) {
        return EntityHolder.ofNullable(value);
    }
}
