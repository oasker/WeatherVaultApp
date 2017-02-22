package com.example.oliverasker.skywarnmarkii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 2/19/17.
 */

public class UserHomePersonalDetailsFragment extends Fragment {

    private TextView affilliationTV;
    private TextView usernameTV;
    private TextView spotterIDTV;
    private TextView callSignTV;

    public interface dataPass{
        void onDataPass(String[] cognitoVals);
    }
    private static final String TAG = "PersnlDetailsFrag";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_user_home_personal_details, container, false);

        Log.d(TAG, "onCreateView");
        affilliationTV = (TextView)v.findViewById(R.id.affiliationTV);
        usernameTV = (TextView)v.findViewById(R.id.usernameTV);
        callSignTV = (TextView)v.findViewById(R.id.callsignTV);
        spotterIDTV = (TextView)v.findViewById(R.id.weatherSpotterTV);

        Bundle b = getArguments();
        String[] cognitoValArray = b.getStringArray("cognitoVals");
        for(String s: cognitoValArray)
                Log.i(TAG, s);
//        cognitoValArray value order:
//            0.SpotterId
//            1.Callsign
//            2.Affiliation
//            3.Username



        affilliationTV.setText(cognitoValArray[2]);
        usernameTV.setText(cognitoValArray[3]);
        callSignTV.setText(cognitoValArray[1]);
        spotterIDTV.setText(cognitoValArray[0]);

        return v;
    }

}
