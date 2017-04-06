package com.example.oliverasker.skywarnmarkii.Callbacks;

import java.util.ArrayList;

/**
 * Created by oliverasker on 3/9/17.
 */

public interface StringCallback {
    void onProcessComplete(String county, String lat, String lng);
    void onProcessError(String error);
    void onProcessComplete(ArrayList<String> items);
}
