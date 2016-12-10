package com.example.oliverasker.skywarnmarkii;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by student on 10/27/16.
 */

public class CoastalFloodingViewReportFragment extends Fragment {
    TextView CoastalArea;
    TextView FirstFloorFlood;
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

        //System.out.println("ARGMENT TEST: "+values.get("snowfall"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        Bundle b = this.getArguments();
        if (b != null) {
            //String s = b.getStringArrayList("winterMap","");
            HashMap<String, String> s = (HashMap<String, String>) b.getSerializable("hMap");
        }

        View view = inflater.inflate(R.layout.coastal_view_fragment, container, false);

        CoastalArea = (TextView) view.findViewById(R.id.coastal_area_field_tv);
        CoastalArea.setText(values.get("CoastalArea"));

        FirstFloorFlood = (TextView) view.findViewById(R.id.first_floor_flood_field_tv);
        FirstFloorFlood.setText(values.get("FirstFloorFlood"));

        StormSurge = (TextView) view.findViewById(R.id.storm_surge_field_tv);
        StormSurge.setText(values.get("StormSurge"));

        return view;
    }
}