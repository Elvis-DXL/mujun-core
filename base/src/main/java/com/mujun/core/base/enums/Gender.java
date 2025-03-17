package com.mujun.core.base.enums;

public enum Gender {
    NV(0, "女"), NAN(1, "男");

    private final Integer val;
    private final String desc;

    Gender(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public Integer val() {
        return val;
    }

    public String desc() {
        return desc;
    }
}