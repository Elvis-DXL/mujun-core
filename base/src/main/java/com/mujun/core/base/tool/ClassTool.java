package com.mujun.core.base.tool;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ClassTool {
    private ClassTool() {
        throw new AssertionError("Utility classes do not allow instantiation");
    }

    public static <T> T newInstance(Class<T> clazz) {
        DSTool.trueThrow(null == clazz, new NullPointerException("class must not be null"));
        DSTool.trueThrow(clazz.isInterface(), new IllegalArgumentException("specified class is an interface"));
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static List<Field> classAllFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();
        do {
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length > 0) {
                result.addAll(Arrays.asList(fields));
            }
            clazz = clazz.getSuperclass();
        } while (null != clazz && !clazz.equals(Object.class));
        return result;
    }
}