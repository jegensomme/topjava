package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean timeIsBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static boolean dateIsBetweenHalfOpen(LocalDate lt, LocalDate startDate, LocalDate endDate) {
        return lt.compareTo(startDate) >= 0 && lt.compareTo(endDate) < 0;
    }

    public static boolean dateTimeIsBetweenHalfOpen(LocalDateTime lt, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return lt.compareTo(startDateTime) >= 0 && lt.compareTo(endDateTime) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

