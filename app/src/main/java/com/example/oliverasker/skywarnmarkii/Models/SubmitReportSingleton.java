package com.example.oliverasker.skywarnmarkii.Models;

import android.util.Log;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.util.Map;


/**
 * Created by oliverasker on 2/15/17.
 * Singleton for submitting report
 */

public class SubmitReportSingleton {
    private static String TAG = "SubmitReportSingleton";
    static private Map<String, AttributeValue> mapHolder ;
    private static SubmitReportSingleton submitReportSingleton = new SubmitReportSingleton();
    private SubmitReportSingleton(){}

    public static SubmitReportSingleton getInstance(){
        if(submitReportSingleton == null){
            return new SubmitReportSingleton();
        }
        else
            return submitReportSingleton;
    }

    public Map<String, AttributeValue> getMapHolder() {
        Log.d(TAG, "-----------Start || getMapHolder()-----------");
        Utility.printMap(mapHolder);
        Log.d(TAG, "-----------End || getMapHolder()-----------");

        return mapHolder;
    }

    public void setMapHolder(Map<String, AttributeValue> mapHolder) {
        Log.d(TAG, "-----------Start || setMapHolder()-----------");
        Utility.printMap(mapHolder);
        Log.d(TAG, "-----------End  ||  setMapHolder()-----------");
        SubmitReportSingleton.mapHolder = mapHolder;
    }
}
