package com.example.oliverasker.skywarnmarkii.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.R;

/*
    Created 10/22/2016
    This class holds fragment view for SUBMITTING a Winter weather type.
 */

public class WinterSubmitReportFragment extends Fragment {

    private TextView Snowfall;
    private TextView SnowfallRate;
    private TextView SnowDepth;
    private TextView WaterEquivalent;
    private TextView FreezingRain;


    private TextView Sleet;
    private TextView BlowDrift;
    private TextView Whiteout;
    private TextView Thundersnow;

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
        return view;
    }

    public void setThundersnow(String thundersnow) {
        Thundersnow .setText( thundersnow);
    }

    public void setSnowfall(String snowfall) {
        Snowfall.setText(snowfall);
    }

    public void setSnowfallRate(String snowfallRate) {
        SnowfallRate .setText( snowfallRate);
    }

    public void setSnowDepth(String snowDepth) {
        SnowDepth .setText( snowDepth);
    }

    public void setWaterEquivalent(String waterEquivalent) {
        WaterEquivalent.setText(waterEquivalent);
    }

    public void setFreezingRain(String freezingRain) {
        FreezingRain .setText( freezingRain);
    }

    public void setSleet(String sleet) {
        Sleet .setText( sleet);
    }

    public void setBlowDrift(String blowDrift) {
        BlowDrift .setText( blowDrift);
    }

    public void setWhiteout(String whiteout) {
        Whiteout .setText( whiteout);
    }

}
