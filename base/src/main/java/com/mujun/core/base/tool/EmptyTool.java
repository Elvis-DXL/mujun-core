package com.mujun.core.base.tool;

import java.util.Collection;
import java.util.function.Consumer;

public final class EmptyTool {
    private EmptyTool() {
        throw new AssertionError("Tool classes do not allow instantiation");
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static void isNotEmpty(Collection<?> coll, Consumer<Collection<?>> consumer) {
        if (isEmpty(coll)) {
            return;
        }
        consumer.accept(coll);
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static void isNotEmpty(String str, Consumer<String> consumer) {
        if (isEmpty(str)) {
            return;
        }
        consumer.accept(str);
    }

    public static <T> void isNotNull(T obj, Consumer<T> consumer) {
        if (null == obj) {
            return;
        }
        consumer.accept(obj);
    }
}