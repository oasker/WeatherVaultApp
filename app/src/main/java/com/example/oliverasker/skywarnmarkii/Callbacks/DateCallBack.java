package com.example.oliverasker.skywarnmarkii.Callbacks;

import java.util.Calendar;

/**
 * Created by oliverasker on 4/21/17.
 */

public interface DateCallBack {
    //    For single date report attributes query the startDate is set as the single day to query
    void startDateChanged(Calendar startDateCalendar);

    void endDateChanged(Calendar endDateCalendar);

    void setSingleDateToQuery(Calendar singleDateCalendar);
}
