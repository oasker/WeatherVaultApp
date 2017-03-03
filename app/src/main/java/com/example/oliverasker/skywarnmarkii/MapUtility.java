package com.example.oliverasker.skywarnmarkii;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

/**
 * Created by oliverasker on 3/1/17.
 */

public class MapUtility {
    private static final String TAG = "MapUtility";

    public static LatLng getLocationFromAddress(Context mContext, String strAddress) {
        Geocoder coder = new Geocoder(mContext, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;
        try {
            // first string is address, second is number of locations the address matches
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null | address.size() == 0) {
                return null;
            }
            android.location.Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            if(p1==null)
                Log.d(TAG,"LatLong == null");
            Log.d(TAG,p1.toString());
            return p1;
        } catch (Exception e) {
            e.printStackTrace();
        }
         Log.d(TAG, " getLocationFromAddress() --> Lat: " + p1.latitude + " Long: " + p1.longitude);
        return p1;
    }

    public static double[] getLatLongFromAddress(Context mContext, String strAddress) {
        Log.i(TAG, "getLatLongFromAddress: address: " +strAddress );
        Geocoder coder = new Geocoder(mContext, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;
        double[] latLong = new double[2];
        try {
            // first string is address, second is number of locations the address matches
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null | address.size() == 0) {
                return null;
            }
            android.location.Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            if(p1==null)
                Log.d(TAG,"p1 == null");
            //Log.d(TAG,p1.toString());

            //Log.d(TAG, "getLatLongCounty: " +location);
            latLong[0] = location.getLatitude();
            latLong[1] = location.getLongitude();
            //latLong[2] = location.getC

            return latLong;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latLong;
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String county;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    county = bundle.getString("address");
                    break;
                default:
                    county = null;
            }
           // latLongTV.setText(locationAddress);
            Log.i(TAG, "handleMessag() : county = " + county);
        }
    }
}


