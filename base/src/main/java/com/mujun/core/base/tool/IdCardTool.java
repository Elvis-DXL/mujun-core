package com.mujun.core.base.tool;

import com.mujun.core.base.enums.DatePattern;
import com.mujun.core.base.enums.Gender;
import com.mujun.core.base.enums.Regex;

import java.time.LocalDate;

public final class IdCardTool {
    private IdCardTool() {
        throw new AssertionError("Tool classes do not allow instantiation");
    }

    private static void verifyIdCardStr(String idCardStr) {
        DSTool.trueThrow(Regex.ID_CARD.verifyFail(idCardStr), new IllegalArgumentException("idCardStr format error"));
    }

    public static Gender getSex(String idCardStr) {
        if (EmptyTool.isEmpty(idCardStr)) {
            return null;
        }
        verifyIdCardStr(idCardStr);
        return Integer.parseInt(idCardStr.substring(16, 17)) % 2 == 0 ? Gender.NV : Gender.NAN;
    }

    public static LocalDate getBirthday(String idCardStr) {
        if (EmptyTool.isEmpty(idCardStr)) {
            return null;
        }
        verifyIdCardStr(idCardStr);
        try {
            return TimeTool.parseLD(idCardStr.substring(6, 14), DatePattern.yyyyMMdd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer getCurrAge(String idCardStr) {
        if (EmptyTool.isEmpty(idCardStr)) {
            return null;
        }
        verifyIdCardStr(idCardStr);
        LocalDate birthday;
        try {
            birthday = TimeTool.parseLD(idCardStr.substring(6, 14), DatePattern.yyyyMMdd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return DSTool.birthdayToCurrAge(birthday);
    }

    public static Integer getAgeByTime(String idCardStr, LocalDate aimTime) {
        if (EmptyTool.isEmpty(idCardStr) || null == aimTime) {
            return null;
        }
        verifyIdCardStr(idCardStr);
        LocalDate birthday;
        try {
            birthday = TimeTool.parseLD(idCardStr.substring(6, 14), DatePattern.yyyyMMdd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return DSTool.birthdayToAgeByTime(birthday, aimTime);
    }
}