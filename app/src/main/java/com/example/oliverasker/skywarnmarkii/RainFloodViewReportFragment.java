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

public class RainFloodViewReportFragment extends Fragment {

    TextView Rain;
    TextView PrecipRate;
    TextView RiverFlood;
    TextView Creek_StreamFlood;
    TextView StreetFlood;
    TextView LargeRiverFlood;
    TextView IceJamFlood;

    HashMap<String, String> values;

    // public static WinterViewReportFragment newInstance(HashMap<String,String> m){
    public static RainFloodViewReportFragment newInstance(HashMap<String, String> tempMap) {
        RainFloodViewReportFragment rainView = new RainFloodViewReportFragment();
        Bundle args = new Bundle();
        //args.putString("snowfall", s);
        args.putSerializable("hMap", tempMap);
        rainView.setArguments(args);
        return rainView;
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

        View view = inflater.inflate(R.layout.rain_flood_view_fragment, container, false);

        Rain = (TextView) view.findViewById(R.id.rain_field_tv);
        Rain.setText(values.get("Rain"));

        PrecipRate = (TextView) view.findViewById(R.id.precip_rate_field_tv);
        PrecipRate.setText(values.get("PrecipRate"));

        Creek_StreamFlood = (TextView) view.findViewById(R.id.creek_stream_flood_field_tv);
        Creek_StreamFlood.setText(values.get("Creek_StreamFlood"));

        StreetFlood = (TextView) view.findViewById(R.id.street_flood_field_tv);
        StreetFlood.setText(values.get("StreetFlood"));

        LargeRiverFlood = (TextView) view.findViewById(R.id.large_river_flood_field_tv);
        LargeRiverFlood.setText(values.get("LargeRiverFlood"));

        IceJamFlood = (TextView) view.findViewById(R.id.ice_jam_field_tv);
        IceJamFlood.setText(values.get("IceJamFlood"));

        return view;
    }
}
