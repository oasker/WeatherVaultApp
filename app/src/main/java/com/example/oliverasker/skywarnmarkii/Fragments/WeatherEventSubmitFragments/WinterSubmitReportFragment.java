package com.example.oliverasker.skywarnmarkii.Fragments.WeatherEventSubmitFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.oliverasker.skywarnmarkii.R;

import java.util.ArrayList;
import java.util.HashMap;

/*
    Created 10/22/2016
    This class holds fragment view for SUBMITTING a Winter weather type.
 */

public class WinterSubmitReportFragment extends Fragment {
    private static final String TAG ="WinterSubRepFrag";
    private TextView Snowfall;
    private TextView SnowfallRate;
    private TextView SnowDepth;
    private TextView WaterEquivalent;
    private TextView FreezingRain;

    private TextView Sleet;
    private TextView BlowDrift;
    private TextView Whiteout;
    private TextView Thundersnow;
    private TextView FreezingRainAccum;

    private ArrayList<TextView> viewList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_winter_submit,container,false);
        Snowfall = (TextView)view.findViewById(R.id.snowfall_field_tv);
        SnowfallRate = (TextView)view.findViewById(R.id.snowfall_rate_field_tv);
        SnowDepth = (TextView)view.findViewById(R.id.snow_depth_field_tv);
        WaterEquivalent = (TextView)view.findViewById(R.id.water_equiv_field_tv);
        FreezingRain = (TextView)view.findViewById(R.id.freezing_rain_field_tv);
        Sleet = (TextView)view.findViewById(R.id.sleet_field_tv);
        Whiteout = (TextView)view.findViewById(R.id.whiteout_field_tv);
        BlowDrift=(TextView)view.findViewById(R.id.blowdrift_tv);
        Thundersnow=(TextView)view.findViewById(R.id.thundersnow_field_tv);
        FreezingRainAccum=(TextView)view.findViewById(R.id.freezing_rain_accumulation_tv);

        return view;
    }

    public void setThundersnow(String thundersnow) {
        Thundersnow .setText( thundersnow);
    }

    public void setSnowfall(String snowfall) {
        Snowfall.setText(snowfall);
    }

    public void setWhiteout(String whiteout) {
        Whiteout .setText( whiteout);
    }


//  Accepts hashmap where non empty values are entered
    public HashMap<String,AttributeValue> getValues(HashMap<String, AttributeValue> vals){
        Log.d(TAG, "getValues");
//        HashMap<String,AttributeValue> vals = new HashMap<>();

        if(!Snowfall.getText().toString().trim().equals(""))
            vals.put("Snowfall", new AttributeValue().withN(Snowfall.getText().toString()));
        if(!Sleet.getText().toString().trim().equals(""))
            vals.put("SnowfallSleet", new AttributeValue().withN(Sleet.getText().toString()));
        if(!SnowfallRate.getText().toString().trim().equals(""))
            vals.put("SnowfallRate", new AttributeValue().withN(SnowfallRate.getText().toString()));
        if(!SnowDepth.getText().toString().trim().equals(""))
            vals.put("SnowDepth", new AttributeValue().withN(SnowDepth.getText().toString()));
        if(!WaterEquivalent.getText().toString().trim().equals(""))
            vals.put("WaterEquivalent", new AttributeValue().withN(WaterEquivalent.getText().toString()));
        if(!FreezingRain.getText().toString().trim().equals(""))
            vals.put("FreezingRain", new AttributeValue().withS(FreezingRain.getText().toString()));
        if(!FreezingRainAccum.getText().toString().trim().equals(""))
            vals.put("FreezingRainAccum", new AttributeValue().withN(FreezingRainAccum.getText().toString()));
        if(!BlowDrift.getText().toString().trim().equals(""))
            vals.put("BlowDrift", new AttributeValue().withS(BlowDrift.getText().toString()));
        if(!Whiteout.getText().toString().trim().equals(""))
            vals.put("Whiteout", new AttributeValue().withS(Whiteout.getText().toString()));
        if(!Thundersnow.getText().toString().trim().equals(""))
            vals.put("Thundersnow", new AttributeValue().withS(Thundersnow.getText().toString()));
        return vals;
    }
}
