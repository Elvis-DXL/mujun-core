package com.mujun.core.base.tool;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.json.JSONUtil;
import com.mujun.core.base.enums.DatePattern;
import com.mujun.core.base.exception.BizException;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;

public final class DSTool {
    private DSTool() {
        throw new AssertionError("Utility classes do not allow instantiation");
    }

    private static final double EARTH_RADIUS_METER = 6371393D;

    private static final List<String> ZERO_TO_NINE = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    public static Double lngLatMeter(Double srcLng, Double srcLat, Double aimLng, Double aimLat) {
        return null == srcLng || null == srcLat || null == aimLng || null == aimLat ? null
                : EARTH_RADIUS_METER * Math.acos(Math.cos(Math.toRadians(srcLat)) * Math.cos(Math.toRadians(aimLat))
                * Math.cos(Math.toRadians(srcLng) - Math.toRadians(aimLng))
                + Math.sin(Math.toRadians(srcLat)) * Math.sin(Math.toRadians(aimLat)));
    }

    public static Double lngLatMeter(String srcLng, String srcLat, String aimLng, String aimLat) {
        return EmptyTool.isEmpty(srcLng) || EmptyTool.isEmpty(srcLat)
                || EmptyTool.isEmpty(aimLng) || EmptyTool.isEmpty(aimLat) ? null
                : lngLatMeter(Double.parseDouble(srcLng), Double.parseDouble(srcLat),
                Double.parseDouble(aimLng), Double.parseDouble(aimLat));
    }

    public static <T, K> K newByObj(Class<K> clazz, T srcObj, String... fields) {
        return null == clazz || null == srcObj ? null : copySomeFields(srcObj, ClassTool.newInstance(clazz), fields);
    }

    public static <T, K> K copySomeFields(T src, K aim, String... fields) {
        if (null == src || null == aim) {
            return aim;
        }
        Map<String, Field> srcMap = ListTool.listToMap(ClassTool.classAllFields(src.getClass()), Field::getName, it -> it);
        Map<String, Field> aimMap = ListTool.listToMap(ClassTool.classAllFields(aim.getClass()), Field::getName, it -> it);
        List<String> fieldList = Arrays.asList(fields);
        if (EmptyTool.isEmpty(fieldList)) {
            fieldList = new ArrayList<>(srcMap.keySet());
        }
        for (String field : fieldList) {
            Field srcField = srcMap.get(field);
            Field aimField = aimMap.get(field);
            if (null == srcField || null == aimField) {
                continue;
            }
            srcField.setAccessible(true);
            aimField.setAccessible(true);
            try {
                aimField.set(aim, srcField.get(src));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                srcField.setAccessible(false);
                aimField.setAccessible(false);
            }
        }
        return aim;
    }

    public static String randomNumberStrByLength(int length) {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        while (count <= length) {
            sb.append(randomGetOne(ZERO_TO_NINE));
            count++;
        }
        return sb.toString();
    }

    public static String intToStrByLength(int num, Integer length) {
        if (null == length) {
            return num + "";
        }
        String str = num + "";
        while (str.length() < length) {
            str = "0".concat(str);
        }
        return str;
    }

    public static Double doubleFmt(Double value, Integer num) {
        if (value == null) {
            return null;
        }
        if (num == null || num < 1) {
            return value;
        }
        StringBuilder builder = new StringBuilder("#.");
        for (int i = 0; i < num; i++) {
            builder.append("0");
        }
        DecimalFormat df = new DecimalFormat(builder.toString());
        return Double.parseDouble(df.format(value));
    }

    public static String UUID36() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString();
    }

    public static <T> T randomGetOne(List<T> srcList) {
        return EmptyTool.isEmpty(srcList) ? null : srcList.get(ThreadLocalRandom.current().nextInt(srcList.size()));
    }

    public static <T, V> V objGet(T srcObj, Function<? super T, ? extends V> function) {
        return null == srcObj ? null : function.apply(srcObj);
    }

    public static <T, K> T mapGet(Map<K, T> srcMap, K aimKey) {
        return null == srcMap || srcMap.isEmpty() ? null : srcMap.get(aimKey);
    }

    public static <T, K, V> V mapObjGetField(Map<K, T> srcMap, K aimKey, Function<? super T, ? extends V> function) {
        return null == srcMap || srcMap.isEmpty() || null == srcMap.get(aimKey) ? null : function.apply(srcMap.get(aimKey));
    }

    public static String hypStr(String aimStr, int start, int mid, int end) {
        if (EmptyTool.isEmpty(aimStr) || aimStr.length() < Math.max(start, end)) {
            return aimStr;
        }
        String midStr = "";
        while (midStr.length() < mid) {
            midStr = midStr.concat("*");
        }
        return aimStr.substring(0, start) + midStr + aimStr.substring(aimStr.length() - end);
    }

    public static String rsaDecryptStr(String aimStr, String pubKey, String priKey) {
        try {
            return SecureUtil.rsa(priKey, pubKey).decryptStr(aimStr, KeyType.PrivateKey);
        } catch (Exception e) {
            throw new BizException("解密失败", e);
        }
    }

    public static <T> void judgeAdd(List<T> aimList, T aimObj) {
        trueThrow(null == aimList, new NullPointerException("aimList must not be null"));
        if (null == aimObj || aimList.contains(aimObj)) {
            return;
        }
        aimList.add(aimObj);
    }

    public static <T> void judgeAddAll(List<T> aimList, List<T> aims) {
        trueThrow(null == aimList, new NullPointerException("aimList must not be null"));
        if (EmptyTool.isEmpty(aims)) {
            return;
        }
        for (T item : aims) {
            if (null == item || aimList.contains(item)) {
                continue;
            }
            aimList.add(item);
        }
    }

    public static Integer minLackOrNext(List<Integer> srcList) {
        srcList = ListTool.listNonNull(srcList);
        if (EmptyTool.isEmpty(srcList)) {
            return 1;
        }
        srcList.sort(Integer::compare);
        Integer max = srcList.get(srcList.size() - 1);
        for (int idx = 1; idx <= max; idx++) {
            if (srcList.contains(idx)) {
                continue;
            }
            return idx;
        }
        return max + 1;
    }

    public static <T extends RuntimeException> void trueThrow(boolean flag, T ex) {
        if (!flag) {
            return;
        }
        throw ex;
    }

    public static <T> void trueDo(boolean flag, T obj, Consumer<T> consumer) {
        if (!flag) {
            return;
        }
        consumer.accept(obj);
    }

    public static <T> void judgeDo(boolean flag, T obj, Consumer<T> trueConsumer, Consumer<T> falseConsumer) {
        if (flag) {
            trueConsumer.accept(obj);
        } else {
            falseConsumer.accept(obj);
        }
    }

    public static Integer birthdayToCurrAge(LocalDate birthday) {
        return birthdayToAgeByTime(birthday, LocalDate.now());
    }

    public static Integer birthdayToAgeByTime(LocalDate birthday, LocalDate aimTime) {
        if (null == birthday || null == aimTime) {
            return null;
        }
        return Math.max(aimTime.getMonthValue() < birthday.getMonthValue() ? aimTime.getYear() - birthday.getYear() - 1
                : (aimTime.getMonthValue() > birthday.getMonthValue() ? aimTime.getYear() - birthday.getYear()
                : (aimTime.getDayOfMonth() < birthday.getDayOfMonth() ?
                aimTime.getYear() - birthday.getYear() - 1 : aimTime.getYear() - birthday.getYear())), 0);
    }

    public static Integer birthdayStrToCurrAge(String birthdayStr) {
        return birthdayStrToAgeByTime(birthdayStr, LocalDate.now());
    }

    public static Integer birthdayStrToAgeByTime(String birthdayStr, LocalDate aimTime) {
        if (EmptyTool.isEmpty(birthdayStr)) {
            return null;
        }
        LocalDate birthday;
        try {
            birthday = TimeTool.parseLD(birthdayStr, DatePattern.yyyy_MM_dd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return birthdayToAgeByTime(birthday, aimTime);
    }

    public static <T> String toJson(T aim) {
        return null == aim ? null : JSONUtil.toJsonStr(aim);
    }

    public static <T> T parseObj(String aimStr, Class<T> clazz) {
        return EmptyTool.isEmpty(aimStr) || null == clazz ? null : JSONUtil.toBean(aimStr, clazz);
    }

    public static <T> List<T> parseList(String aimStr, Class<T> clazz) {
        return EmptyTool.isEmpty(aimStr) || null == clazz ?
                new ArrayList<>() : JSONUtil.toList(JSONUtil.parseArray(aimStr), clazz);
    }

    public static String fileExtName(String fileName) {
        if (EmptyTool.isEmpty(fileName)) {
            return "";
        }
        int indexOf = fileName.lastIndexOf(".");
        return indexOf == -1 ? "" : fileName.substring(indexOf + 1);
    }
}