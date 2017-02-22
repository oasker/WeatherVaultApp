package com.example.oliverasker.skywarnmarkii.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.R;

import java.util.HashMap;

/**
 * Created by student on 10/27/16.
 */

public class SevereViewReportFragment extends Fragment {
    TextView SevereType;
    TextView WindSpeed;
    TextView WindGust;
    TextView WindDirection;
    TextView HailSize;
    TextView Tornado;
    TextView Barometer;
    TextView DownedTrees;
    TextView DownedLimbs;
    TextView DiameterLimbs;
    TextView DownedPoles;
    TextView DownedWires;
    TextView WindDamage;
    TextView LightningDamage;
    TextView DamageComments;


    private HashMap<String,String> values;

    public static SevereViewReportFragment newInstance(HashMap<String,String> tempMap){
        SevereViewReportFragment severeView = new SevereViewReportFragment();
        Bundle args = new Bundle();
        //args.putString("snowfall", s);
        args.putSerializable("hMap",tempMap);
        severeView.setArguments(args);
        return severeView;
    }

    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        //get arguments
        // snowfallString = getArguments().getString("snowfall");
        values = (HashMap<String,String>)getArguments().getSerializable("hMap");

        //System.out.println("ARGMENT TEST: "+values.get("snowfall"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){

        Bundle b =this.getArguments();
        if(b!= null){
            //String s = b.getStringArrayList("winterMap","");
            HashMap<String, String> s= (HashMap<String, String>) b.getSerializable("hMap");
        }

        View view = inflater.inflate(R.layout.severe_view_fragment,container,false);

        SevereType = (TextView)view.findViewById(R.id.severe_type_field_tv);
        SevereType.setText(values.get("SevereType"));

        WindSpeed = (TextView)view.findViewById(R.id.wind_speed_field_tv);
        WindSpeed.setText(values.get("WindSpeed"));

        WindGust = (TextView)view.findViewById(R.id.wind_gust_field_tv);
        WindGust.setText(values.get("WindGust"));

        WindDirection = (TextView)view.findViewById(R.id.wind_direction_field_tv);
        WindDirection.setText(values.get("WindDirection"));

        HailSize = (TextView)view.findViewById(R.id.hail_size_field_tv);
        HailSize.setText(values.get("HailSize"));

        Tornado = (TextView)view.findViewById(R.id.tornado_field_tv);
        Tornado.setText(values.get("Tornado"));

        Barometer = (TextView)view.findViewById(R.id.barometer_field_tv);
        Barometer.setText(values.get("Barometer"));

        WindDamage= (TextView)view.findViewById(R.id.wind_damage_field_tv);
        WindDamage.setText(values.get("WindDamage"));

        LightningDamage= (TextView)view.findViewById(R.id.lightning_damage_field_tv);
        LightningDamage.setText(values.get("LightningDamage"));

        DamageComments= (TextView)view.findViewById(R.id.damage_comments_field_tv);
        DamageComments.setText(values.get("DamageComments"));

        return view;
    }
}

//Icons provided by: https://icons8.com/web-app/for/android/next