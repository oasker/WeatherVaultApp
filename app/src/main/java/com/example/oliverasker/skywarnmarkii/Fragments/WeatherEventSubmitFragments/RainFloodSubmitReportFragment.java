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

/*
 * Created on 10/22/16.
 *  One of four fragments that that will be dynamically added to submit report form
 */

public class RainFloodSubmitReportFragment extends Fragment {
    private static final String TAG = "RainFloodSubmtRprtFrag";
    private Context mContext;

    private EditText RainInput;
    private EditText PrecipRateInput;
    private EditText FloodComments;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_rain_flood_submit, container, false);
        RainInput=(EditText)view.findViewById(R.id.rain_field_tv);
        PrecipRateInput = (EditText)view.findViewById(R.id.precip_rate_field_tv);
        FloodComments =(EditText)view.findViewById(R.id.flood_comments_input_tv);
        return view;
    }
    public HashMap<String,AttributeValue> getValues(HashMap<String, AttributeValue> vals){
        Log.d(TAG, "getValues()");
        if (!RainInput.getText().toString().equals(""))
            vals.put("Rain", new AttributeValue().withN(RainInput.getText().toString()));
        if (!PrecipRateInput.getText().toString().trim().equals(""))
            vals.put("PrecipRate", new AttributeValue().withN(PrecipRateInput.getText().toString()));
        if (!FloodComments.getText().toString().equals(""))
            vals.put("FloodComments", new AttributeValue().withS(FloodComments.getText().toString()));
        return vals;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
