package com.mujun.core.base.context;

import java.io.Serializable;

public class RequestBodyContextHolder implements Serializable {
    private final static String KEY = "REQUEST_BODY";

    public RequestBodyContextHolder() {
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