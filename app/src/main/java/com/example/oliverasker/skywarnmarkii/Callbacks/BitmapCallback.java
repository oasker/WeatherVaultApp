package com.example.oliverasker.skywarnmarkii.Callbacks;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by oliverasker on 2/20/17.
 */

public interface BitmapCallback {
    void processFinish(ArrayList<Bitmap> result);
    void processFinish(Bitmap result, String path);
    void processFinish(Bitmap result, ArrayList<String> path);

    void processFinish(Bitmap result);
    void processFinish(Uri result);

}
