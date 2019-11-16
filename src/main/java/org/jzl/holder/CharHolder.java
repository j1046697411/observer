package org.jzl.holder;

public class CharHolder implements Holder<Character> , Increment<CharHolder>, Decrement<CharHolder>{

    public char value;

    CharHolder(char value) {
        this.value = value;
    }

    @Override
    public Character get() {
        return value;
    }

    @Override
    public CharHolder decrement() {
        value --;
        return this;
    }

    @Override
    public CharHolder increment() {
        value ++;
        return this;
    }
}
