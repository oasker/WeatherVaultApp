package com.example.oliverasker.skywarnmarkii.Fragments.WeatherEventSubmitFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.oliverasker.skywarnmarkii.R;

import java.util.HashMap;

/**
 * Created by student on 10/23/16.
 */

public class SevereWeatherSubmitReportFragment extends Fragment {
    private static final String TAG = "SevereWthrSubtReprtFrag";

    private EditText WindSpeedInput;
    private Spinner severeWeatherSpinner;
    private EditText WindGustInput;
    private EditText HailSizeInput;
    private EditText TornadoInput;
    private EditText BarometerInput;
    private EditText WindDamageInput;
    private EditText LightningInput;
    private EditText DamageCommentsInput;

    private String[] windDirectionValues = {"North", "South", "East","West", "Northeast","Northwest", "Southeast", "Southwest"};
    private String[] severeWeatherTypeValues = {"Tornado","Thunderstorm","Non-Thunderstorm","Tropical Storm", "Hurricane"};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        String[] test = getResources().getStringArray(R.array.wind_direction_values_array);//{"item1","item2"};

        View view = inflater.inflate(R.layout.fragment_severe_weather_submit,container,false);
        severeWeatherSpinner = (Spinner)view.findViewById(R.id.severe_weather_spinner);
        //ArrayAdapter severeWeatherTypeSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.severe_weather_spinner_values_array, R.layout.multiline_spinner_dropdown_item);
        ArrayAdapter<String> severeWeatherTypeSpinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, severeWeatherTypeValues);
        //ArrayAdapter<String> severeWeatherTypeSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, severeWeatherTypeValues);
        severeWeatherTypeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        severeWeatherTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        severeWeatherSpinner.setAdapter(severeWeatherTypeSpinnerAdapter);
        //For wind direction drop down
        Spinner windDirectionSpinner= (Spinner)view.findViewById(R.id.severe_wind_direction_spinner);
//        ArrayAdapter<String> windDirectionAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, windDirectionValues);
        ArrayAdapter<String> windDirectionAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, windDirectionValues);

        windDirectionSpinner.setAdapter(windDirectionAdapter);

        WindSpeedInput = (EditText)view.findViewById(R.id.wind_speed_field_tv);
        WindGustInput= (EditText)view.findViewById(R.id.wind_gust_field_tv);

        HailSizeInput= (EditText)view.findViewById(R.id.hail_size_field_tv);

        TornadoInput= (EditText)view.findViewById(R.id.tornado_field_tv);
        BarometerInput= (EditText)view.findViewById(R.id.barometer_field_tv);
        WindDamageInput= (EditText)view.findViewById(R.id.wind_damage_field_tv);
        LightningInput= (EditText)view.findViewById(R.id.lightning_damage_field_tv);
        DamageCommentsInput= (EditText)view.findViewById(R.id.damage_comments_field_tv);


        return view;
    }
    public HashMap<String,AttributeValue> getValues(HashMap<String, AttributeValue> vals){
        Log.d(TAG, "getValues");
//        HashMap<String,AttributeValue> vals = new HashMap<>();
        if(!WindSpeedInput.getText().toString().trim().equals(""))
            vals.put("WindSpeed", new AttributeValue().withN(WindSpeedInput.getText().toString()));

        if(!WindGustInput.getText().toString().trim().equals(""))
            vals.put("WindGust", new AttributeValue().withN(WindGustInput.getText().toString()));


//            vals.put("WindDirection", new AttributeValue().withN(win.getText().toString()));

        if(!HailSizeInput.getText().toString().trim().equals(""))
            vals.put("HailSize", new AttributeValue().withN(HailSizeInput.getText().toString()));

        if(!TornadoInput.getText().toString().trim().equals(""))
            vals.put("Tornado", new AttributeValue().withS(TornadoInput.getText().toString()));

        if(!BarometerInput.getText().toString().trim().equals(""))
            vals.put("Barometer", new AttributeValue().withN(BarometerInput.getText().toString()));

        if(!WindDamageInput.getText().toString().trim().equals(""))
            vals.put("WindDamage", new AttributeValue().withS(WindDamageInput.getText().toString()));

        if(!LightningInput.getText().toString().trim().equals(""))
            vals.put("LightningDamage", new AttributeValue().withS(LightningInput.getText().toString()));

        if(!DamageCommentsInput.getText().toString().trim().equals(""))
            vals.put("DamageComments", new AttributeValue().withS(DamageCommentsInput.getText().toString()));


        return vals;
    }
}