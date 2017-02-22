package com.example.oliverasker.skywarnmarkii.Models;

import android.graphics.Bitmap;

/**
 * Created by oliverasker on 2/8/17.
 */

public class UserSubmittedImageModel {

    private String userSubmitted;
    private String dateOfEvent;
    private Bitmap photo;

    //For testing will just use photos in drawable
    private int imageResource;


    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }


    public String getUserSubmitted() {
        return userSubmitted;
    }

    public void setUserSubmitted(String userSubmitted) {
        this.userSubmitted = userSubmitted;
    }

    public String getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(String dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
