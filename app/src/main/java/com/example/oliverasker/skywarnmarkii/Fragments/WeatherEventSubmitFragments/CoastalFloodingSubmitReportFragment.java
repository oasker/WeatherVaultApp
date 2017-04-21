package com.example.oliverasker.skywarnmarkii.Fragments.WeatherEventSubmitFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.oliverasker.skywarnmarkii.R;

import java.util.HashMap;

/**
 * Created by student on 10/22/16.
 */

public class CoastalFloodingSubmitReportFragment extends Fragment {
    private static final String TAG = "CoastalFloodSbmtRptFrag";
    private Context mContext;

    private EditText StormSurgeInput;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_coastal_flooding_submit,container, false);

        StormSurgeInput = (EditText)view.findViewById(R.id.storm_surge_field_tv);

        return view;
    }
    public HashMap<String,AttributeValue> getValues(HashMap<String, AttributeValue> vals){
        Log.d(TAG, "getValues");
        if(!StormSurgeInput.getText().toString().trim().equals(""))
            vals.put("StormSurge", new AttributeValue().withN(StormSurgeInput.getText().toString()));
        return vals;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
