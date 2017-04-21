package com.example.oliverasker.skywarnmarkii.Utility;

import android.icu.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
