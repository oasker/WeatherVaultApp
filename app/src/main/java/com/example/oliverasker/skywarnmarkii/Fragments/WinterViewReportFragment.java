package com.example.oliverasker.skywarnmarkii.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.ISetTextInFragment;
import com.example.oliverasker.skywarnmarkii.R;

import java.util.HashMap;

/*
    Created 10/22/2016
    This class holds fragment view for SUBMITTING a Winter weather type.
 */

public class WinterViewReportFragment extends Fragment implements ISetTextInFragment {

    private TextView Snowfall;
    private TextView SnowfallRate;
    private TextView SnowDepth;
    private TextView WaterEquivalent;
    private TextView FreezingRain;

    private TextView Sleet;
    private TextView BlowDrift;
    private TextView Whiteout;
    private TextView Thundersnow;

    private String snowfallString;
    private String waterEquivalent;
    private String  snowFallString;
    private String  snowDepthString;
    private String  freezingRainString;
    private String  sleetString;
    private String  whiteoutString;
    private String thunderSnowString;
    private HashMap<String,String> reportValues;

   // public static WinterViewReportFragment newInstance(HashMap<String,String> m){
   public static WinterViewReportFragment newInstance(HashMap<String,String> tempMap){
        WinterViewReportFragment winterView = new WinterViewReportFragment();
        Bundle args = new Bundle();
        //args.putString("snowfall", s);
        args.putSerializable("hMap",tempMap);
        winterView.setArguments(args);
        return winterView;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        //get arguments
       // snowfallString = getArguments().getString("snowfall");
        reportValues = (HashMap<String,String>)getArguments().getSerializable("hMap");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){

        Bundle b =this.getArguments();
        if(b!= null){
           //String s = b.getStringArrayList("winterMap","");
            HashMap<String, String> s= (HashMap<String, String>) b.getSerializable("hMap");
        }

        View view = inflater.inflate(R.layout.winter_view_fragment,container,false);

        Snowfall = (TextView)view.findViewById(R.id.snowfall_field_tv);
       // Snowfall.setText(snowfallString);
        Snowfall.setText(reportValues.get("Snowfall"));

        SnowfallRate = (TextView)view.findViewById(R.id.snowfall_rate_field_tv);
        SnowfallRate.setText(reportValues.get("SnowfallRate"));

        SnowDepth = (TextView)view.findViewById(R.id.snow_depth_field_tv);
        SnowDepth.setText(reportValues.get("SnowDepth"));

        WaterEquivalent = (TextView)view.findViewById(R.id.water_equiv_field_tv);
        WaterEquivalent.setText(reportValues.get("WaterEquivalent"));

        FreezingRain = (TextView)view.findViewById(R.id.freezing_rain_field_tv);
        FreezingRain.setText(reportValues.get("FreezingRain"));

        Sleet = (TextView)view.findViewById(R.id.sleet_field_tv);
        Sleet.setText(reportValues.get("Sleet"));

        Whiteout = (TextView)view.findViewById(R.id.whiteout_field_tv);
        Whiteout.setText(reportValues.get("Whiteout"));

        BlowDrift=(TextView)view.findViewById(R.id.blowdrift_tv);
        BlowDrift.setText(reportValues.get("BlowDrift"));

        Thundersnow=(TextView)view.findViewById(R.id.thundersnow_field_tv);
        Thundersnow.setText(reportValues.get("Thundersnow"));

        return view;
    }
    //
    @Override
    public void showText(HashMap<String, String> hmap) {
        if(hmap.containsKey("Snowfall"))
            Snowfall.setText(hmap.get("Snowfall"));
        if(hmap.containsKey("SnowDepth"))
            Snowfall.setText(hmap.get("SnowDepth"));
        if(hmap.containsKey("SnowfallRate"))
            Snowfall.setText(hmap.get("SnowfallRate"));

        if(hmap.containsKey("WaterEquivalent"))
            Snowfall.setText(hmap.get("WaterEquivalent"));
        if(hmap.containsKey("FreezingRain"))
            Snowfall.setText(hmap.get("FreezingRain"));
        if(hmap.containsKey("Sleet"))
            Snowfall.setText(hmap.get("Sleet"));

        if(hmap.containsKey("Blowdrift"))
            Snowfall.setText(hmap.get("Blowdrift"));
        if(hmap.containsKey("Whiteout"))
            Snowfall.setText(hmap.get("Whiteout"));
        if(hmap.containsKey("Thundersnow"))
            Snowfall.setText(hmap.get("Thundersnow"));
    }
}
