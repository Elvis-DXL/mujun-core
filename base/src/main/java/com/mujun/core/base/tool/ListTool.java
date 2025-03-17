package com.mujun.core.base.tool;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ListTool {
    private ListTool() {
        throw new AssertionError("Tool classes do not allow instantiation");
    }

    public static <T, K, U> Map<K, U> listToMap(List<T> srcList,
                                                Function<? super T, ? extends K> keyMapper,
                                                Function<? super T, ? extends U> valueMapper) {
        return EmptyTool.isEmpty(srcList) ?
                new HashMap<>() : srcList.stream().collect(Collectors.toMap(keyMapper, valueMapper, (k1, k2) -> k1));
    }

    public static <T, K> Map<K, List<T>> listGroup(List<T> srcList, Function<? super T, ? extends K> keyMapper) {
        return EmptyTool.isEmpty(srcList) ? new HashMap<>() : srcList.stream().collect(Collectors.groupingBy(keyMapper));
    }

    public static <T, V> List<V> listMap(List<T> srcList, Function<? super T, ? extends V> mapper) {
        return EmptyTool.isEmpty(srcList) ? new ArrayList<>() : srcList.stream().map(mapper).collect(Collectors.toList());
    }

    public static <T> List<T> listFilter(List<T> srcList, Predicate<? super T> predicate) {
        return EmptyTool.isEmpty(srcList) ? new ArrayList<>() : srcList.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T> List<T> listDistinct(List<T> srcList) {
        return EmptyTool.isEmpty(srcList) ? new ArrayList<>() : srcList.stream().distinct().collect(Collectors.toList());
    }

    public static <T> List<T> listNonNull(List<T> srcList) {
        return listFilter(srcList, Objects::nonNull);
    }

    public static <T, V> List<V> listGetField(List<T> srcList, Function<? super T, ? extends V> mapper) {
        return EmptyTool.isEmpty(srcList) ? new ArrayList<>() :
                srcList.stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    public static <T, V> List<V> listGetFieldNonNull(List<T> srcList, Function<? super T, ? extends V> mapper) {
        return EmptyTool.isEmpty(srcList) ? new ArrayList<>() :
                srcList.stream().filter(Objects::nonNull).map(mapper).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static <T> List<T> listNonNullDistinct(List<T> srcList) {
        return EmptyTool.isEmpty(srcList) ? new ArrayList<>() :
                srcList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    public static <T, K> List<T> listClassChange(List<K> srcList, Class<T> clazz, String... fields) {
        srcList = listNonNull(srcList);
        if (EmptyTool.isEmpty(srcList) || null == clazz) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        Map<String, Field> srcMap = listToMap(ClassTool.classAllFields(srcList.get(0).getClass()), Field::getName, it -> it);
        Map<String, Field> aimMap = listToMap(ClassTool.classAllFields(clazz), Field::getName, it -> it);
        List<String> fieldList = Arrays.asList(fields);
        if (EmptyTool.isEmpty(fieldList)) {
            fieldList = new ArrayList<>(srcMap.keySet());
        }
        for (K src : srcList) {
            T aim = ClassTool.newInstance(clazz);
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
            result.add(aim);
        }
        return result;
    }

    public static <T> boolean containsAnyOne(List<T> srcList, List<T> aimList) {
        if (EmptyTool.isEmpty(srcList) || EmptyTool.isEmpty(aimList)) {
            return false;
        }
        for (T it : aimList) {
            if (srcList.contains(it)) {
                return true;
            }
        }
        return false;
    }
}