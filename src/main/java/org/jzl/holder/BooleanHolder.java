package org.jzl.holder;

import java.util.function.BooleanSupplier;

public enum BooleanHolder implements Holder<Boolean>, BooleanSupplier {
    TRUE(true), FALSE(false);

    public final boolean value;

    BooleanHolder(boolean value) {
        this.value = value;
    }

    @Override
    public boolean getAsBoolean() {
        return value;
    }

    @Override
    public Boolean get() {
        return value ? Boolean.TRUE : Boolean.FALSE;
    }

    public BooleanHolder non(){
        return this == BooleanHolder.TRUE ? BooleanHolder.FALSE : BooleanHolder.TRUE;
    }
}
