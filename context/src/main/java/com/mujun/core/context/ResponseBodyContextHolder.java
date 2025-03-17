package com.mujun.core.context;

import java.io.Serializable;

public class ResponseBodyContextHolder implements Serializable {
    private final static String KEY = "RESPONSE_BODY";

    public ResponseBodyContextHolder() {
    }

    public static void set(String info) {
        ContextHolder.set(KEY, info);
    }

    public static String get() {
        return (String) ContextHolder.get(KEY);
    }

    public static void clear() {
        ContextHolder.remove(KEY);
    }
}