package com.example.oliverasker.skywarnmarkii.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by student on 10/23/16.
 */

public class SevereWeatherSubmitReportFragment extends Fragment {
    private static final String TAG = "SevereWeatherSubmitReportFragment";


    private String[] windDirectionValues = {"North", "South", "East","West", "Northeast","Northwest", "Southeast", "Southwest"};
    private String[] severeWeatherTypeValues = {"Tornado","Thunderstorm","Non-Thunderstorm","Tropical Storm", "Hurricane"};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        String[] test = getResources().getStringArray(R.array.wind_direction_values_array);//{"item1","item2"};
        //For severe weather type drop down
        View view = inflater.inflate(R.layout.fragment_severe_weather_submit,container,false);
        Spinner severeWeatherSpinner = (Spinner)view.findViewById(R.id.severe_weather_spinner);

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

        //}
        //windDirectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        windDirectionSpinner.setAdapter(windDirectionAdapter);

        return view;
    }
}