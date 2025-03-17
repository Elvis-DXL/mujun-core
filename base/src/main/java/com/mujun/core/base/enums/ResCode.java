package com.mujun.core.base.enums;

public enum ResCode {
    SUCCESS(200, "操作成功"),
    FAILURE(412, "业务异常"),
    UN_AUTHORIZED(401, "认证错误/失效"),

    ;
    private final int code;
    private final String message;

    ResCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}