package com.example.oliverasker.skywarnmarkii.Fragments.SubmitMultipleReportsActivityFragments;

//import android.support.v7.app.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.oliverasker.skywarnmarkii.R;

//import android.support.v4.app.Fragment;

/**
 * Created by oliverasker on 4/9/17.
 */

public class SubmitMultipleReportsAttributes extends Fragment
{
    private static final String TAG = "SubmtMultReportsAttr";
    private CheckBox rainAttrCB;
    private CheckBox severeAttrCB;
    private CheckBox winterAttrCB;
    private CheckBox coastalAttrCB;


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
        View v = inflater.inflate(R.layout.fragment_multiple_reports_attributes,container,false);

        rainAttrCB = (CheckBox)v.findViewById(R.id.rain_attribute_CB);
        severeAttrCB = (CheckBox)v.findViewById(R.id.severe_attribute_CB);
        winterAttrCB = (CheckBox)v.findViewById(R.id.winter_attribute_CB);
        coastalAttrCB = (CheckBox)v.findViewById(R.id.coastal_attribute_CB);

        CompoundButton.OnCheckedChangeListener myCheckboxListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                switch (buttonView.getId()){
                   /// RainFloodSubmitReportFragment rainFrag = new RainFloodSubmitReportFragment();
                    case R.id.rain_attribute_CB:
                        if(isChecked) {
                            Log.d(TAG, "rain CB CHECKED");

                            //transaction.replace(R.id.rain_attribute_container, new RainFloodSubmitReportFragment());
                            //transaction.commit();
                        }
                        else {
                           // transaction.remove(getFragmentManager().findFragmentById(R.id.rain_attribute_container));
                           // transaction.commit();
                            Log.d(TAG, "rain CB NOT CHECKED");
                        }
                        break;



                    case R.id.winter_attribute_CB:
                        if(isChecked) {
                            Log.d(TAG, "winter CB CHECKED");
//                            transaction.replace(R.id.rain_attribute_container, new RainFloodSubmitReportFragment());
//                            transaction.commit();

                        }
                        else {
                            Log.d(TAG, "winter CB NOT CHECKED");
                        }
                        break;



                    case R.id.severe_attribute_CB:
                        if(isChecked) {
                            Log.d(TAG, "severe CB CHECKED");
                        }
                        else {
                            Log.d(TAG, "severe CB NOT CHECKED");
                        }
                        break;



                    case R.id.coastal_attribute_CB:
                        if(isChecked) {
                            Log.d(TAG, "coastal CB CHECKED");
                        }
                        else {
                            Log.d(TAG, "coastal CB NOT CHECKED");
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        rainAttrCB.setOnCheckedChangeListener(myCheckboxListener);
        severeAttrCB.setOnCheckedChangeListener(myCheckboxListener);
        coastalAttrCB.setOnCheckedChangeListener(myCheckboxListener);
        winterAttrCB.setOnCheckedChangeListener(myCheckboxListener);



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
}
