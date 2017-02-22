package com.example.oliverasker.skywarnmarkii.Models;

import android.graphics.Bitmap;

/**
 * Created by oliverasker on 2/20/17.
 */


public class SubmittedPhotoModel{
    Bitmap image;
    String dateTaken;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }
}

