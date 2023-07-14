package com.example.zivsfitnessapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Utils {

    public static int getDayIndexFromMillis(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static long getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar newCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        newCalendar.setTimeInMillis(0L);
        newCalendar.set(Calendar.YEAR, year);
        newCalendar.set(Calendar.MONTH, month);
        newCalendar.set(Calendar.DAY_OF_MONTH, day);

        return newCalendar.getTimeInMillis();
    }

    public static String dateToStr(long date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
}
