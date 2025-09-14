package com.sinaukoding.eventbooking.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {

    public static String formatLocalDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.of("id", "ID")));
    }

}
