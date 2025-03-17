package com.mujun.core.base.enums;


import com.mujun.core.base.tool.EmptyTool;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Symbol {
    DH(","), FH(";"), XH("*"), SX("|"), JH("#"), BL("~"), WH("?"),
    DY(">"), XY("<"), DENGY("="), JIAH("+"), XHX("_"), ZHX("-"),
    ZKH("("), YKH(")"), YWD("."), ZWZKH("（"), ZWYKH("）"), BFH("%"),
    MYF("$"), RMB("￥"), ADF("@"), ZXX("/"), HAT("^"), ZDA("&"),
    ZZKH("["), YZKH("]"), ZDKH("{"), YDKH("}"), ZWZZKH("【"), ZWYZKH("】");

    private final String val;

    Symbol(String val) {
        this.val = val;
    }

    public String val() {
        return val;
    }

    public static String likeSQL(String aimStr) {
        return BFH.val() + aimStr + BFH.val();
    }

    public static String likeSQL(String aimStr, Symbol symbol) {
        return likeSQL(symbol.val() + aimStr + symbol.val());
    }

    public static String leftLikeSQL(String aimStr) {
        return aimStr + BFH.val();
    }

    public static String rightLikeSQL(String aimStr) {
        return BFH.val() + aimStr;
    }

    public Boolean included(String aimStr) {
        return EmptyTool.isNotEmpty(aimStr) && aimStr.contains(val());
    }

    public Boolean notIncluded(String aimStr) {
        return !included(aimStr);
    }

    public Boolean spliceSrcContainAim(String spliceSrc, String aim) {
        return EmptyTool.isNotEmpty(spliceSrc) && EmptyTool.isNotEmpty(aim) && spliceSrc.contains(val() + aim + val());
    }

    public Boolean spliceSrcNotContainAim(String spliceSrc, String aim) {
        return !spliceSrcContainAim(spliceSrc, aim);
    }

    public List<String> split(String aimStr) {
        if (EmptyTool.isEmpty(aimStr)) {
            return new ArrayList<>();
        }
        StringTokenizer st = new StringTokenizer(aimStr, val());
        List<String> result = new ArrayList<>();
        while (st.hasMoreTokens()) {
            result.add(st.nextToken());
        }
        return result;
    }

    public List<Long> splitToLong(String aimStr) {
        if (EmptyTool.isEmpty(aimStr)) {
            return new ArrayList<>();
        }
        StringTokenizer st = new StringTokenizer(aimStr, val());
        List<Long> result = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String temp = st.nextToken();
            result.add(Long.valueOf(temp));
        }
        return result;
    }

    public List<Integer> splitToInteger(String aimStr) {
        if (EmptyTool.isEmpty(aimStr)) {
            return new ArrayList<>();
        }
        StringTokenizer st = new StringTokenizer(aimStr, val());
        List<Integer> result = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String temp = st.nextToken();
            result.add(Integer.valueOf(temp));
        }
        return result;
    }

    public <T extends Serializable> String bothSidesAdd(T aim) {
        return null == aim ? null : (val() + aim + val());
    }

    public String join(Collection<String> srcList) {
        return join(srcList, true);
    }

    public <T, K> String join(Collection<T> srcList, Function<? super T, ? extends K> function) {
        return join(srcList, function, true);
    }

    public String join(Collection<String> srcList, boolean includeStartAndEnd) {
        if (EmptyTool.isEmpty(srcList)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String str : srcList) {
            sb.append(str);
            sb.append(val());
        }
        String result = sb.toString();
        result = result.substring(0, result.length() - val().length());
        if (includeStartAndEnd) {
            result = val() + result + val();
        }
        return result;
    }

    public <T, K> String join(Collection<T> srcList, Function<? super T, ? extends K> function, boolean includeStartAndEnd) {
        return EmptyTool.isEmpty(srcList) ? null :
                join(srcList.stream().filter(Objects::nonNull).map(function).filter(Objects::nonNull).map(Object::toString)
                        .collect(Collectors.toList()), includeStartAndEnd);
    }
}