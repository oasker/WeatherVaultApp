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
import android.widget.AdapterView;
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
    private Spinner StateSpinnerInput;
    private Context mContext;

    private String state;

    //    private CheckBox useAsDefaultLocationCB;
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
        ZipInput = (EditText) v.findViewById(R.id.zip_input_text_multiple_reports);
        CityInput = (EditText)v.findViewById(R.id.city_input_multiple_reports_tv);

//        Setup state spinner
        StateSpinnerInput = (Spinner) v.findViewById(R.id.state_spinner_multiple_reports);
        StateSpinnerInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "StateSpinnerInput: onItemSelected(): " + parent.getItemAtPosition(position).toString());
                state = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        useCurrentLocationCB = (CheckBox) v.findViewById(R.id.use_current_location_checkbox);
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
            vals.put("Street", new AttributeValue().withS(StreetInput.getText().toString()));
        if(!CityInput.getText().toString().trim().equals(""))
            vals.put("City", new AttributeValue().withS(CityInput.getText().toString()));
        if(!ZipInput.getText().toString().trim().equals(""))
            vals.put("ZipCode", new AttributeValue().withS(ZipInput.getText().toString()));

        if (state != null)
            vals.put("State", new AttributeValue().withS(state));
        return vals;
    }


    public String getCity() {
        return CityInput.getText().toString();
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return ZipInput.getText().toString();
    }

    public String getStreet() {
        return StreetInput.getText().toString();
    }


    public boolean areAllRequiredFieldsEntered() {
//        Ensures an address is entered, at least state, zip and city must be entered
        return state != null && !ZipInput.getText().toString().matches("") && !CityInput.getText().toString().matches("");
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }


}
