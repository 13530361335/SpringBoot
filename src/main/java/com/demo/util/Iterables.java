package com.demo.util;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Iterable 的工具类
 */
public class Iterables {

    public static <T> void forEach(Iterable<? extends T> es, BiConsumer<Integer, ? super T> action) {
        Objects.requireNonNull(es);
        Objects.requireNonNull(action);
        int i = 0;
        for (T e : es) {
            action.accept(i++, e);
        }
    }

}