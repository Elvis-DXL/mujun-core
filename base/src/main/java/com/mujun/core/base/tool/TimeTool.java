package com.mujun.core.base.tool;


import com.mujun.core.base.enums.DatePattern;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public final class TimeTool {
    private TimeTool() {
        throw new AssertionError("Tool classes do not allow instantiation");
    }

    public static LocalDateTime dayStart(LocalDateTime time) {
        return time.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime dayEnd(LocalDateTime time) {
        return time.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
    }

    public static LocalDateTime monthStart(LocalDateTime time) {
        return dayStart(time.with(TemporalAdjusters.firstDayOfMonth()));
    }

    public static LocalDateTime monthEnd(LocalDateTime time) {
        return dayEnd(time.with(TemporalAdjusters.lastDayOfMonth()));
    }

    public static LocalDateTime yearStart(LocalDateTime time) {
        return dayStart(time.with(TemporalAdjusters.firstDayOfYear()));
    }

    public static LocalDateTime yearEnd(LocalDateTime time) {
        return dayEnd(time.with(TemporalAdjusters.lastDayOfYear()));
    }

    public static LocalDateTime dayStart(LocalDate time) {
        return dayStart(time.atStartOfDay());
    }

    public static LocalDateTime dayEnd(LocalDate time) {
        return dayEnd(time.atStartOfDay());
    }

    public static LocalDateTime monthStart(LocalDate time) {
        return monthStart(time.atStartOfDay());
    }

    public static LocalDateTime monthEnd(LocalDate time) {
        return monthEnd(time.atStartOfDay());
    }

    public static LocalDateTime yearStart(LocalDate time) {
        return yearStart(time.atStartOfDay());
    }

    public static LocalDateTime yearEnd(LocalDate time) {
        return yearEnd(time.atStartOfDay());
    }

    public static String formatLDT(LocalDateTime time, String pattern) {
        return null == time || EmptyTool.isEmpty(pattern) ? null : DateTimeFormatter.ofPattern(pattern).format(time);
    }

    public static String formatLDT(LocalDateTime time, DatePattern pattern) {
        return null == time || null == pattern ? null : DateTimeFormatter.ofPattern(pattern.val()).format(time);
    }

    public static String formatLD(LocalDate time, String pattern) {
        return null == time || EmptyTool.isEmpty(pattern) ? null : DateTimeFormatter.ofPattern(pattern).format(time);
    }

    public static String formatLD(LocalDate time, DatePattern pattern) {
        return null == time || null == pattern ? null : DateTimeFormatter.ofPattern(pattern.val()).format(time);
    }

    public static LocalDateTime parseLDT(String timeStr, String pattern) {
        return EmptyTool.isEmpty(timeStr) || EmptyTool.isEmpty(pattern) ?
                null : LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseLDT(String timeStr, DatePattern pattern) {
        return EmptyTool.isEmpty(timeStr) || null == pattern ?
                null : LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern.val()));
    }

    public static LocalDate parseLD(String timeStr, String pattern) {
        return EmptyTool.isEmpty(timeStr) || EmptyTool.isEmpty(pattern) ?
                null : LocalDate.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLD(String timeStr, DatePattern pattern) {
        return EmptyTool.isEmpty(timeStr) || null == pattern ?
                null : LocalDate.parse(timeStr, DateTimeFormatter.ofPattern(pattern.val()));
    }

    public static LocalDateTime dateToLocal(Date date) {
        return null != date ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }

    public static Date localToDate(LocalDateTime time) {
        return null != time ? Date.from(time.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    public static Long localToTime(LocalDateTime time) {
        return null != time ? time.toInstant(ZoneOffset.of("+8")).toEpochMilli() : null;
    }

    public static Calendar dateToCalendar(Date date) {
        if (null == date) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date addDate(Date date, int calendarType, int num) {
        Calendar calendar = dateToCalendar(date);
        calendar.add(calendarType, num);
        return calendar.getTime();
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formatDate(Date date, DatePattern pattern) {
        return formatDate(date, pattern.val());
    }

    public static Date dateStartTime(Date date) {
        Calendar calendar = dateToCalendar(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date dateEndTime(Date date) {
        Calendar calendar = dateToCalendar(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static int monthDaysByDate(Date date) {
        Calendar calendar = dateToCalendar(date);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }

    public static int monthDaysByLD(LocalDate time) {
        return monthDaysByDate(localToDate(time.atStartOfDay()));
    }

    public static int monthDaysByLDT(LocalDateTime time) {
        return monthDaysByDate(localToDate(time));
    }
}