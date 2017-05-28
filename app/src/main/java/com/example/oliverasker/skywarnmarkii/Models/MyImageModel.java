package com.example.oliverasker.skywarnmarkii.Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by oliverasker on 4/5/17.
 * https://junjunguo.com/blog/android-take-photo-show-in-list-view-b/
 */

public class MyImageModel {
    protected SimpleDateFormat df = new SimpleDateFormat("MMMM d, yy  h:mm");
    private String title, description, path;
    private Calendar datetime;
    private long datetimeLong;

    /**
     * Gets title.
     *
     * @return Value of title.
     */
    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets new datetimeLong.
     *
     * @param datetimeLong New value of datetimeLong.
     */
    public void setDatetime(long datetimeLong) {
        this.datetimeLong = datetimeLong;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(datetimeLong);
        this.datetime = cal;
    }

    /**
     * Sets new description.
     *
     * @param description New value of description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets path.
     *
     * @return Value of path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets new path.
     *
     * @param path New value of path.
     */
    public void setPath(String path) {
        this.path = path;
    }

    @Override public String toString() {
        return "Title:" + title + "   " + df.format(datetime.getTime()) +
                "\nDescription:" + description + "\nPath:" + path;
    }
}