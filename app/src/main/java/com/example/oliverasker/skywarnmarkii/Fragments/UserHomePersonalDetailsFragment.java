package com.example.oliverasker.skywarnmarkii.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.R;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by oliverasker on 2/19/17.
 */

public class UserHomePersonalDetailsFragment extends Fragment {

    private TextView affilliationTV;
    private TextView usernameTV;
    private TextView spotterIDTV;
    private TextView callSignTV;

//    public interface dataPass{
//        void onDataPass(String[] cognitoVals);
//    }
    private static final String TAG = "PersnlDetailsFrag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_user_home_personal_details, container, false);

        Log.d(TAG, "onCreateView");
        //affilliationTV = (TextView)v.findViewById(R.id.affiliationTV);
        //usernameTV = (TextView)v.findViewById(R.id.usernameTV);
        //callSignTV = (TextView)v.findViewById(R.id.callsignTV);
        //spotterIDTV = (TextView)v.findViewById(R.id.weatherSpotterTV);

        Bundle b = getArguments();
        Map<String,String> vals = (Map<String, String>) b.get("attrMap");

        if(vals.containsKey("phone_number")){
            Log.d(TAG, "phone_number: " + vals.get("phone_number"));
        }
//        cognitoValArray value order:
//            0.SpotterId
//            1.Callsign
//            2.Affiliation
//            3.Username

        Iterator it = vals.entrySet().iterator();
        while (it.hasNext()) {
            // vals = new String[userAttr2.size()];
            Map.Entry pair = (Map.Entry) it.next();
            Log.i(TAG, "AttrMap contains: " + pair.getKey() + " = " + pair.getValue());
        }

        /// the following values are returned

//            email
//            given_name
//            custom:Affiliation
//            custom:SpotterID
//            family_name   <- last name
//            given_name   <- first name
//            phone_number

        TableLayout tableLayout = (TableLayout)v.findViewById(R.id.user_cognito_details_table_layout);

        TableRow tableRow = new TableRow(getContext());
        tableLayout.addView(tableRow);

        if(vals.containsKey("given_name") & vals.containsKey("family_name")) {
            tableLayout.addView(createTableRow("Name: ", vals.get("given_name") + " " + vals.get("family_name")));
        }

        if(vals.containsKey("custom:CallSign")) {
            tableLayout.addView(createTableRow("Call Sign: ", vals.get("custom:CallSign")));
            //UserInformationModel.getInstance().setSpotterID(vals.get("custom:CallSign"));
        }

        if(vals.containsKey("custom:SpotterID")) {
            tableLayout.addView(createTableRow("Spotter ID: ", vals.get("custom:SpotterID")));
            //UserInformationModel.getInstance().setSpotterID(vals.get("custom:SpotterID"));
        }

        if(vals.containsKey("custom:Affiliation")) {
            tableLayout.addView(createTableRow("Affiliation: " , vals.get("custom:Affiliation")));
           // UserInformationModel.getInstance().setAffiliation(vals.get("custom:Affiliation"));
        }

        if(vals.containsKey("phone_number")){
            tableLayout.addView(createTableRow("Phone Number: " , vals.get("phone_number")));
           // UserInformationModel.getInstance().setPhone(vals.get("phone_number"));
        }
        if(vals.containsKey("email")){
            tableLayout.addView(createTableRow("Email: " , vals.get("email")));
        }

        return v;
    }

    public TableRow createTableRow(String label, String value){
        TableRow TR = new TableRow(getContext());
        TextView labelTV = new TextView(getContext());
        labelTV.setText(label);
        labelTV.setTextSize(16);
        labelTV.setTextColor(Color.BLACK);
        labelTV.setPadding(15,15,15,15);


        TextView valueTV = new TextView(getContext());
        valueTV.setPadding(15,15,15,15);
        valueTV.setTextColor(Color.BLACK);
        valueTV.setText(value);
        valueTV.setTextSize(16);
        TR.addView(labelTV);
        TR.addView(valueTV);
        return TR;
    }

}
