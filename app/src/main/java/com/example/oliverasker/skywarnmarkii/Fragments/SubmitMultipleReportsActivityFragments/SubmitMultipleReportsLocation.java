package com.example.oliverasker.skywarnmarkii.Fragments.SubmitMultipleReportsActivityFragments;

//import android.app.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.oliverasker.skywarnmarkii.R;

import java.util.HashMap;

//import android.support.v4.app.Fragment;

/**
 * Created by oliverasker on 4/9/17.
 */

public class SubmitMultipleReportsLocation extends Fragment {
    private static final String TAG = "SubtMultReportsLocation";

    private EditText StreetInput;
    private EditText ZipInput;
    private EditText CityInput;
    private Spinner StateInput;


    private CheckBox useAsDefaultLocationCB;
    private CheckBox useCurrentLocationCB;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_multiple_report_location_select,container,false);

        StreetInput = (EditText)v.findViewById(R.id.street_input_field_multiple_reports);
        ZipInput = (EditText)v.findViewById(R.id.zip_input_field);
        CityInput = (EditText)v.findViewById(R.id.city_input_multiple_reports_tv);
        StateInput = (Spinner)v.findViewById(R.id.state_spinner);

        useAsDefaultLocationCB = (CheckBox)v.findViewById(R.id.use_as_default_location_checkbox);
        useCurrentLocationCB = (CheckBox)v.findViewById(R.id.use_current_location_checkbox);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    public HashMap<String,AttributeValue> getValues(HashMap<String, AttributeValue> vals){
        Log.d(TAG, "getValues");
        if(!StreetInput.getText().toString().trim().equals(""))
            vals.put("Street",new AttributeValue().withS(StreetInput.toString()));

        if(!CityInput.getText().toString().trim().equals(""))
            vals.put("City",new AttributeValue().withS(CityInput.toString()));

        if(!ZipInput.getText().toString().trim().equals(""))
            vals.put("ZipCode",new AttributeValue().withS(ZipInput.toString()));

        return vals;
    }
}
