package org.jzl;

import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public class Text {
    public static void main(String[] args) throws NoSuchMethodException {
        Observable.<String, Context>create(new Context() {
            @Override
            public void register(Class type, BiConsumer receiver) {

            }

            @Override
            public void launch(Object value) {
            }
        }, emitter -> {
            emitter.launch("123456");
            emitter.launch("string");
            emitter.launch("123456789");
        }).groupBy((s, context) -> {
            return Pattern.matches("[0-9]*", s) ? Key.NUMBER : Key.SERING;
        }, (s, context) -> s).peek((keyStringContextGroupedObservable, context) -> {
            System.out.println(keyStringContextGroupedObservable.getKey());
        }).count();
    }

    public enum Key {
        SERING, NUMBER
    }
}
