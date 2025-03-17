package com.mujun.core.base.enums;


import com.mujun.core.base.tool.EmptyTool;

public enum Regex {
    ID_CARD("(^[0-9]{18}$)|(^[0-9]{17}(Ⅹ|X|x)$)", "身份证号码"),
    MOBILE_PHONE("(^1[0-9]{10}$)", "手机号"),
    LNG_LAT("(^[0-9]{1,3}$)|(^[0-9]{1,3}[\\.]{1}$)|(^[0-9]{1,3}[\\.]{1}[0-9]+$)", "经纬度");

    private final String regexStr;
    private final String desc;

    Regex(String regexStr, String desc) {
        this.regexStr = regexStr;
        this.desc = desc;
    }

    public String getRegexStr() {
        return regexStr;
    }

    public String getDesc() {
        return desc;
    }

    public static boolean verify(String aimStr, String regexStr) {
        return EmptyTool.isNotEmpty(aimStr) && EmptyTool.isNotEmpty(regexStr) && aimStr.matches(regexStr);
    }

    public static boolean verifyFail(String aimStr, String regexStr) {
        return !verify(aimStr, regexStr);
    }

    public boolean verify(String aimStr) {
        return EmptyTool.isNotEmpty(aimStr) && aimStr.matches(getRegexStr());
    }

    public boolean verifyFail(String aimStr) {
        return !verify(aimStr);
    }
}