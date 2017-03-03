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

/**
 * Holds info common to all reports
 */
public class GeneralSubmitReportFragment extends Fragment implements ISetTextInFragment {

    private HashMap<String,String> reportValues;
    private TextView UsernameTV;
    private TextView WeatherEventTV;
    private TextView LongitudeTV;
    private TextView LatitudeTV;
    private TextView StreetTV;
    private TextView CityTV;
    private TextView StateTV;
    private TextView ZipTV;
    private TextView CommentsTV;
    private TextView CurrentTempTV;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        reportValues = (HashMap<String,String>)getArguments().getSerializable("hMap");
        View view = inflater.inflate(R.layout.general_info_submit_fragment, container, false);

        Bundle b= new Bundle();
        if(b!= null){
            //String s = b.getStringArrayList("winterMap","");
            HashMap<String, String> s= (HashMap<String, String>) b.getSerializable("hMap");
        }

//        UsernameTV = (TextView)view.findViewById(R.id.username_field_tv);
//        UsernameTV.setText(reportValues.get("Username"));
//
//        ZipTV = (TextView)view.findViewById(R.id.zip_field_tv);
//        ZipTV.setText(reportValues.get("ZipCode"));

        WeatherEventTV = (TextView)view.findViewById(R.id.weather_event_field_tv);
        WeatherEventTV.setText(reportValues.get("WeatherEvent"));

        LongitudeTV = (TextView)view.findViewById(R.id.longitude_field_tv);
        LongitudeTV.setText(reportValues.get("Longitude"));

        LatitudeTV = (TextView)view.findViewById(R.id.lattitude_field_tv);
        LatitudeTV.setText(reportValues.get("Latitude"));

//        StreetTV = (TextView)view.findViewById(R.id.street_field_tv);
//        StreetTV.setText(reportValues.get("Street"));

//        CityTV = (TextView)view.findViewById(R.id.city_field_tv);
//        CityTV.setText(reportValues.get("City"));

        CommentsTV = (TextView) view.findViewById(R.id.comments_field_tv);
        CommentsTV.setText(reportValues.get("Comments"));

        CurrentTempTV = (TextView) view.findViewById(R.id.current_temp_field_tv);
        CurrentTempTV.setText(reportValues.get("CurrentTemperature"));

//        StateTV = (TextView)view.findViewById(R.id.state_field_tv);
//        StateTV.setText(reportValues.get("State"));
        return view;
    }

    public static GeneralSubmitReportFragment newInstance(HashMap<String,String> tempMap){
        GeneralSubmitReportFragment generalReport = new GeneralSubmitReportFragment();
        Bundle args = new Bundle();
        //args.putString("snowfall", s);
        args.putSerializable("hMap",tempMap);
        generalReport.setArguments(args);
        return generalReport;
    }

    @Override
    public void onCreate(Bundle savedInstance){

        super.onCreate(savedInstance);
    }


    @Override
    public void showText(HashMap<String, String> textToShow) {

    }
}
