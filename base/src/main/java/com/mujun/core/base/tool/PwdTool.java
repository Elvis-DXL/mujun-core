package com.mujun.core.base.tool;

import java.util.Random;
import java.util.regex.Pattern;

public final class PwdTool {
    private PwdTool() {
        throw new AssertionError("Tool classes do not allow instantiation");
    }

    private final static String P = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[#?!@$%^&*-]).{8,20}$";

    private final static String[] WORDS = {
            "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "n", "p",
            "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "2", "3", "4", "5", "6", "7", "8", "9", "#", "?", "!", "@", "$", "%", "^", "&", "*", "-"
    };

    public static String obtainRandomPwd(int len) {
        String pwd = createPwd(len);
        while (!checkPwd(pwd)) {
            pwd = createPwd(len);
        }
        return pwd;
    }

    private static String createPwd(int len) {
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int x = 0; x < len; ++x) {
            sb.append(WORDS[r.nextInt(WORDS.length)]);
        }
        return sb.toString();
    }

    public static boolean checkPwd(String pwd) {
        return Pattern.compile(P).matcher(pwd).find();
    }

    public static boolean checkPwd(String pwd, String pattern) {
        return Pattern.compile(pattern).matcher(pwd).find();
    }
}