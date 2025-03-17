package com.mujun.core.base.context;

import java.io.Serializable;
import java.util.Optional;

public class UserContextHolder implements Serializable {
    private final static String KEY = "USER_CONTEXT";

    public UserContextHolder() {
    }

    public static void setUser(UserContext user) {
        ContextHolder.set(KEY, user);
    }

    public static Optional<UserContext> getUser() {
        UserContext userContext = (UserContext) ContextHolder.get(KEY);
        return Optional.ofNullable(userContext);
    }

    public static void clear() {
        ContextHolder.remove(KEY);
    }
}