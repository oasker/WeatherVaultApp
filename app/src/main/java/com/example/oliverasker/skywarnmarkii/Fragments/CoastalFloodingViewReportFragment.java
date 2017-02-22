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

public class CoastalFloodingViewReportFragment extends Fragment {
    TextView CoastalComments;
    TextView CoastalInjuries;
    TextView CoastalFatalities;


    TextView StormSurge;

    private HashMap<String, String> values;

    // public static WinterViewReportFragment newInstance(HashMap<String,String> m){
    public static CoastalFloodingViewReportFragment newInstance(HashMap<String, String> tempMap) {
        CoastalFloodingViewReportFragment coastalView = new CoastalFloodingViewReportFragment();
        Bundle args = new Bundle();
        //args.putString("snowfall", s);
        args.putSerializable("hMap", tempMap);
        coastalView.setArguments(args);
        return coastalView;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        //get arguments
        values = (HashMap<String, String>) getArguments().getSerializable("hMap");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        Bundle b = this.getArguments();
        if (b != null) {
            //String s = b.getStringArrayList("winterMap","");
            HashMap<String, String> s = (HashMap<String, String>) b.getSerializable("hMap");
        }

        View view = inflater.inflate(R.layout.fragment_coastal_view, container, false);

        CoastalComments= (TextView) view.findViewById(R.id.coastal_commentsTV);
        if(values.get("CoastalEventComments") != null)
            CoastalComments.setText(values.get("CoastalEventComments"));

        CoastalFatalities = (TextView) view.findViewById(R.id.coastal_fatalities_field_tv_);
        if(values.get("NumberOfFatalities")!=null){
            if(values.get("FatalityComments") != null){
                CoastalFatalities.setText(values.get("NumberOfFatalities") + " Fatalities: " + values.get("FatalityComments"));
            }
            else
                CoastalFatalities.setText(values.get("NumberOfFatalities" + " Fatalities"));
        }


        CoastalInjuries = (TextView) view.findViewById(R.id.coast_injuries_field_tv);
        if(values.get("NumberOfInjuries")!=null) {
            if(values.get("InjuryComments") !=null)
                CoastalInjuries.setText(values.get("NumberOfInjuries") + " Injuries: " + values.get("InjuryComments"));
            else
                CoastalInjuries.setText(values.get("NumberOfInjuries") + " Injuries");

        }

        StormSurge = (TextView) view.findViewById(R.id.storm_surge_field_tv);
        if(values.get("StormSurge") !=null)
            StormSurge.setText(values.get("StormSurge"));

        return view;
    }
}