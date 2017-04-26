package com.example.oliverasker.skywarnmarkii.Utility;

import android.icu.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by oliverasker on 4/17/17.
 */

public class DateUtility {

    public static String getTodaysDateString() {
        final Calendar c;
        StringBuilder returnDate = new StringBuilder();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            c = Calendar.getInstance();

            returnDate.append(String.valueOf(c.get(Calendar.MONTH) + 1) + "/" + (c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR)));
        }
        return returnDate.toString();
    }

    public static String getYerdaysDateString() {
        final Calendar c;
        StringBuilder returnDate = new StringBuilder();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            returnDate.append(String.valueOf(c.get(Calendar.MONTH) + 1) + "/" + (c.get(Calendar.DAY_OF_MONTH) - 1) + "/" + c.get(Calendar.YEAR));
        }
        return returnDate.toString();
    }

    public static long getTodaysEpoch() {
        final Calendar c;
        long epoch = 0;
        String dateString = getTodaysDateString();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date date = df.parse(dateString);
            epoch = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return epoch;
    }

    public static long getYesterdaysEpoch() {
        final Calendar c;
        long epoch = 0;
        String dateString = getYerdaysDateString();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date date = df.parse(dateString);
            epoch = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return epoch;
    }
    public static long getNumberDaysBetweenTwoCalendars(java.util.Calendar startCalendar, java.util.Calendar endCalendar){
        Date stardDate =startCalendar.getTime();
        Date endDate = endCalendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        long differenceMilis =  endDate.getTime() -stardDate.getTime();
        long differenceInDays = TimeUnit.DAYS.convert(differenceMilis,TimeUnit.MILLISECONDS);

        return differenceInDays;
    }


    public static String CalendarToString(java.util.Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateOfEvent = sdf.format(c.getTime());
//        Log.d(TAG, "startDateString: " + dateOfEvent);
        return dateOfEvent;
    }

    public static String DateToString(Date date){
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return CalendarToString(c);
    }

    public static Date AddOneDayToDate(Date date){
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        c.add(java.util.Calendar.DAY_OF_MONTH,1);
        date = c.getTime();
        return date;
    }

    public static Date getYesterdaysDate() {
        final Calendar c;
        long epoch = 0;
        Date yesterdaysDate = null;
        String dateString = getYerdaysDateString();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date date = df.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yesterdaysDate;
    }
}
