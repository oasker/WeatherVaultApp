package com.example.oliverasker.skywarnmarkii.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.oliverasker.skywarnmarkii.Callbacks.StringCallback;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/*
*  This class just collects the data applicable to any report then gets user to enter
*  which events they want to report on. The data is maintained through states using a static class
*  that is assigned corresponding fields in
*
*  10/22/16
*  -First draft of submit GUI Complete
*  Colects
*      date
*      location
*      event
*  -Attempting to maintain state of activity when it is left and returned to
*      -Resource: https://inthecheesefactory.com/blog/fragment-state-saving-best-practices/en
*/
public class SubmitReportActivity extends AppCompatActivity implements StringCallback{
    private final String TAG = "SubmitReportActivity";
    //  Used for determining which fragments are shown in next activity (instance bools initialized to 0 fyi)
    private Boolean severeWeatherBool = false;
    private Boolean winterWeatherBool = false;
    private Boolean rainAndFloodBool = false;
    private Boolean coastalFloodingBool = false;
    private Button proceedButton;
    private CheckBox severeWeatherCB, winterWeatherCB, rainFloodCB, coastalFloodCB;

    private EditText State;
    private EditText Street;
    private EditText Zip;
    private EditText Town;


    private TimePicker timePicker;
    private DatePicker datePicker;
    private SkywarnWSDBMapper reportToSubmit;

    //private Map<String, AttributeValue> report;
    HashMap<String, Boolean> weatherEventsBoolsMap = new HashMap<String, Boolean>();

    private String dateSubmittedString;
    private String dateSubmittedEpoch;

    HashMap<String, AttributeValue> report;

    StateHolder sH;

    @Override
    public void onCreate(Bundle b) {
       // Log.d(TAG, "ONCREATE: " + UserInformationModel.getInstance().getAffiliation());
        super.onCreate(b);
        Intent i = getIntent();
        setContentView(R.layout.activity_submit_report);
        reportToSubmit = new SkywarnWSDBMapper();
        //Setup toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        report = new HashMap();
        //SubmitReportSingleton.getInstance().setMapHolder(new HashMap<String,AttributeValue>());

       // if (sH == null) {
            //Add Bools to hashmap so we know what fragments to place on next screen
            weatherEventsBoolsMap.put("severeBool", false);
            weatherEventsBoolsMap.put("winterBool", false);
            weatherEventsBoolsMap.put("coastalFloodBool", false);
            weatherEventsBoolsMap.put("rainFloodBool", false);

            Town = (EditText) findViewById(R.id.submit_activity_town_text_field);
            State = (EditText) findViewById(R.id.submit_activity_state_text_field);
            Zip = (EditText) findViewById(R.id.submit_activity_zip_text_field);
            Street = (EditText) findViewById(R.id.submit_activity_street_text_field);

       // }
            proceedButton = (Button) findViewById(R.id.submit_activity_button_to_next_screen);
            proceedButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!Street.getText().toString().equals(""))
                        report.put("Street", new AttributeValue().withS(Street.getText().toString()));
                    if(!Zip.getText().toString().equals(""))
                        report.put("ZipCode", new AttributeValue().withS(Zip.getText().toString()));
                    if(!State.getText().toString().equals(""))
                        report.put("State", new AttributeValue().withS(State.getText().toString()));
                    if(!Town.getText().toString().equals(""))
                        report.put("City", new AttributeValue().withS(Town.getText().toString()));
//
                    Log.d(TAG,"OnProceedButtonCLicked()");
                    LaunchSubmitReportDetails();
                }
            });

            // CheckBoxes determining which type of event is being reported
            severeWeatherCB = (CheckBox) findViewById(R.id.submit_activity_severe_weather_checkbox);
            severeWeatherCB.setOnClickListener(cbListener);

            winterWeatherCB = (CheckBox) findViewById(R.id.submit_activity_winter_checkbox);
            winterWeatherCB.setOnClickListener(cbListener);

            rainFloodCB = (CheckBox) findViewById(R.id.submit_activity_rainfall_flooding_checkbox);
            rainFloodCB.setOnClickListener(cbListener);

            coastalFloodCB = (CheckBox) findViewById(R.id.submit_activity_coastal_flooding_checkbox);
            coastalFloodCB.setOnClickListener(cbListener);

            datePicker = (DatePicker) findViewById(R.id.submit_report_date_picker);
            timePicker = (TimePicker) findViewById(R.id.timePicker);
        }

    private OnClickListener cbListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //Gather Report Type data
            severeWeatherBool = severeWeatherCB.isChecked();
            coastalFloodingBool = coastalFloodCB.isChecked();
            rainAndFloodBool = rainFloodCB.isChecked();
            winterWeatherBool = winterWeatherCB.isChecked();

            weatherEventsBoolsMap.put("severeBool", severeWeatherBool);
            weatherEventsBoolsMap.put("winterBool", winterWeatherBool);
            weatherEventsBoolsMap.put("rainFloodBool", rainAndFloodBool);
            weatherEventsBoolsMap.put("coastalFloodBool", coastalFloodingBool);

            //Add elements to row object and pass to next activity
            datePicker = (DatePicker) findViewById(R.id.submit_report_date_picker);
            String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
//            if(!Street.getText().toString().equals(""))
//                report.put("Street", new AttributeValue().withS(Street.getText().toString()));
//            if(!Zip.getText().toString().equals(""))
//                report.put("ZipCode", new AttributeValue().withS(Zip.getText().toString()));
//            if(!State.getText().toString().equals(""))
//                report.put("State", new AttributeValue().withS(State.getText().toString()));
//            if(!Town.getText().toString().equals(""))
//                report.put("Town", new AttributeValue().withS(Town.getText().toString()));
//
//
//            double latitude = MapUtility.getLatLongFromAddress(
//                    getApplicationContext(),
//                    Street.getText().toString()
//                            + " " + Town.getText().toString()
//                            +" " + State.getText().toString()
//                            + " " + Zip.getText().toString())[0];
//
//            double longitude = MapUtility.getLatLongFromAddress(
//                    getApplicationContext(),
//                    Street.getText().toString()
//                            + " " + Town.getText().toString()
//                            +" " + State.getText().toString()
//                            + " " + Zip.getText().toString())[1];
//
//
//            //String[] county = MapUtility.getCountyFromLatLong(this, getResources());
//            // Log.i(TAG, "county: " + count)
//
//            GetReportCountyTask countyTask = new GetReportCountyTask();
//            countyTask.setmContext(getApplicationContext());
//            countyTask.setRes(getResources());
//            countyTask.setCallback(SubmitReportActivity.this);
//            countyTask.execute();
//
//
//            report.put("Latitude", new AttributeValue().withN(String.valueOf(latitude)));
//            report.put("Longitude", new AttributeValue().withN(String.valueOf(longitude)));
//            //report.put("County", new AttributeValue().withS(county));
//
//           // Log.i(TAG, "lat/long: " + latitude + ", " + longitude + "  county: " + county);

            if (sH == null) {
                //Add values to stateholder
                sH = new StateHolder();
                sH.dateP = datePicker;
                sH.street = Street;
                sH.town = Town;
                sH.zip = Zip;
                sH.WeatherEventsBoolsMap = weatherEventsBoolsMap;
                sH.state = State;
            }
        }
    };

    private static long getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    public boolean validateInputs() {
        if ((Street.getText().toString() == ""
                | State.getText().toString() == ""
                | Zip.getText().toString() == ""
                | Town.getText().toString() == "")){
            Log.d(TAG, "validateInputs() true");
        return false;
    }
        else{
            Log.d(TAG, "validateInputs() true");
            return true;
        }
    }

    //Launches activity where reporter enters specific weather info
    private void LaunchSubmitReportDetails() {
        Log.d(TAG, "LaunchSubmitReportDetails()");
        String streetString = Street.getText().toString();
        String zipString = Zip.getText().toString();
        String townString = Town.getText().toString();
        String stateString = State.getText().toString();
        GetReportCountyTask countyTask = new GetReportCountyTask();
        countyTask.setCallback(this);
        countyTask.setmContext(getApplicationContext());
        countyTask.setRes(getResources());
        countyTask.setCallback(SubmitReportActivity.this);
        countyTask.setStreet(streetString);
        countyTask.setTown(townString);
        countyTask.setState(stateString);
        countyTask.setZip(zipString);
        countyTask.execute();


//        //datePicker = (DatePicker) findViewById(R.id.submit_report_date_picker);
//        report.put("DateOfEvent", new AttributeValue().withN(String.valueOf(getDateFromDatePicker(datePicker))));
//        Log.d(TAG, "DATEOFEVENT: " + String.valueOf(getDateFromDatePicker(datePicker)));
////            if(!Street.getText().toString().equals(""))
////                report.put("Street", new AttributeValue().withS(Street.getText().toString()));
////            if(!Zip.getText().toString().equals(""))
////                report.put("ZipCode", new AttributeValue().withS(Zip.getText().toString()));
////            if(!State.getText().toString().equals(""))
////                report.put("State", new AttributeValue().withS(State.getText().toString()));
////            if(!Town.getText().toString().equals(""))
////                report.put("Town", new AttributeValue().withS(Town.getText().toString()));
////
////
////            double latitude = MapUtility.getLatLongFromAddress(
////                    this,
////                    Street.getText().toString()
////                    + " " + Town.getText().toString()
////                    +" " + State.getText().toString()
////                    + " " + Zip.getText().toString())[0];
////
////            double longitude = MapUtility.getLatLongFromAddress(
////                    this,
////                    Street.getText().toString()
////                    + " " + Town.getText().toString()
////                    +" " + State.getText().toString()
////                    + " " + Zip.getText().toString())[1];
////
////
////            String[] county = MapUtility.getCountyFromLatLong(this, getResources());
////           // Log.i(TAG, "county: " + count)
////
////
////            report.put("Latitude", new AttributeValue().withN(String.valueOf(latitude)));
////            report.put("Longitude", new AttributeValue().withN(String.valueOf(longitude)));
////            report.put("County", new AttributeValue().withS(county));
////
////            Log.i(TAG, "lat/long: " + latitude + ", " + longitude + "  county: " + county);
//
//
//        //Enter user details retreived from cognito
//        report.put("Username", new AttributeValue().withS(UserInformationModel.getInstance().getUsername()));
//        report.put("FirstName", new AttributeValue().withS(UserInformationModel.getFirstName()));
//        report.put("LastName", new AttributeValue().withS(UserInformationModel.getLastName()));
//
//        report.put("NumberOfImages", new AttributeValue().withN(String.valueOf(UserInformationModel.getNumberOfImages())));
//        report.put("NumberOfVideos", new AttributeValue().withN(String.valueOf(UserInformationModel.getNumberOfVideos())));
//
//        report.put("UpVote", new AttributeValue().withN(String.valueOf(0)));
//        report.put("NetVote", new AttributeValue().withN(String.valueOf(0)));
//        report.put("DownVote", new AttributeValue().withN(String.valueOf(0)));
//
//        if (!UserInformationModel.getInstance().getAffiliation().equals("")) {
//            report.put("Affilliation", new AttributeValue().withS(UserInformationModel.getInstance().getAffiliation()));
//        } else
//            report.put("Affilliation", new AttributeValue().withS("General"));
//
//        if (!UserInformationModel.getInstance().getSpotterID().equals(""))
//            report.put("SpotterID", new AttributeValue().withS(UserInformationModel.getInstance().getSpotterID()));
//        else
//            report.put("SpotterID", new AttributeValue().withS("|"));
//
//        if (UserInformationModel.getInstance().getCallsign() != null && !UserInformationModel.getInstance().getCallsign().equals(""))
//            report.put("CallSign", new AttributeValue().withS(UserInformationModel.getInstance().getCallsign()));
//        else
//            report.put("CallSign", new AttributeValue().withS("|"));
//
////        Log.i(TAG, "Callsign::::: " + UserInformationModel.getInstance().getCallsign()
////                + " SpotterID:::: " + UserInformationModel.getInstance().getSpotterID()
////                + " Affiliation::::  " + UserInformationModel.getInstance().getAffiliation());
//
//        ///reportToSubmit.setDateSubmittedString(Utility.epochToDateTimeString(reportToSubmit.getDateSubmittedEpoch() /1000));
//        /////////////////////////////////////////////////////////////////////////////
//        ////              VALIDATE LOCATION WHEN SUBMITTING REPORTs             /////
//        /////////////////////////////////////////////////////////////////////////////
//        //        Here we check to make sure that enough info is provided for the location
//        //            1. Street, City, State != null | = ""
//        //                A. Run the address through the geocoder and and show user what will be submitted
//        //                   i. User can select one of the addresses or reenter address
//        //            2. Lat/Long are filled out (only numbers)
//        //                A. (same as 1.A)
//
//        // DateOfEvent Stuff
//        long epoch = getDateFromDatePicker(datePicker);
//        report.put("DateOfEvent", new AttributeValue().withN(String.valueOf(epoch/1000)));
////        Log.i(TAG, "Date of Event:: " + report.get("DateOfEvent"));
//
//
//        String[] keyArray = Utility.AttributeHashMapKeyToArray(report);
//        AttributeValue[] attrArray = Utility.AttributeHashMapValuesToArray(report);
//
//        Bundle b = new Bundle();
//        Intent intent = new Intent(this, SubmitReportDetailsActivity.class);
//
//        intent.putExtra("keyArray", keyArray);
//        intent.putExtra("attrArray", attrArray);
//
//        intent.putExtras(b);
//        intent.putExtra("weatherEventBoolsMap", weatherEventsBoolsMap);
//        startActivity(intent);
//        HashMap<String, AttributeValue> report = new HashMap();
//        if(validateInputs()) {
//           // HashMap<String, AttributeValue> report = new HashMap();
//
//        datePicker = (DatePicker) findViewById(R.id.submit_report_date_picker);
//        report.put("DateOfEvent", new AttributeValue().withN(String.valueOf(getDateFromDatePicker(datePicker))));
//
////            if(!Street.getText().toString().equals(""))
////                report.put("Street", new AttributeValue().withS(Street.getText().toString()));
////            if(!Zip.getText().toString().equals(""))
////                report.put("ZipCode", new AttributeValue().withS(Zip.getText().toString()));
////            if(!State.getText().toString().equals(""))
////                report.put("State", new AttributeValue().withS(State.getText().toString()));
////            if(!Town.getText().toString().equals(""))
////                report.put("Town", new AttributeValue().withS(Town.getText().toString()));
////
////
////            double latitude = MapUtility.getLatLongFromAddress(
////                    this,
////                    Street.getText().toString()
////                    + " " + Town.getText().toString()
////                    +" " + State.getText().toString()
////                    + " " + Zip.getText().toString())[0];
////
////            double longitude = MapUtility.getLatLongFromAddress(
////                    this,
////                    Street.getText().toString()
////                    + " " + Town.getText().toString()
////                    +" " + State.getText().toString()
////                    + " " + Zip.getText().toString())[1];
////
////
////            String[] county = MapUtility.getCountyFromLatLong(this, getResources());
////           // Log.i(TAG, "county: " + count)
////
////
////            report.put("Latitude", new AttributeValue().withN(String.valueOf(latitude)));
////            report.put("Longitude", new AttributeValue().withN(String.valueOf(longitude)));
////            report.put("County", new AttributeValue().withS(county));
////
////            Log.i(TAG, "lat/long: " + latitude + ", " + longitude + "  county: " + county);
//
//
//        //Enter user details retreived from cognito
//        report.put("Username", new AttributeValue().withS(UserInformationModel.getInstance().getUsername()));
//        report.put("FirstName", new AttributeValue().withS(UserInformationModel.getFirstName()));
//        report.put("LastName", new AttributeValue().withS(UserInformationModel.getLastName()));
//
//
//        report.put("NumberOfImages", new AttributeValue().withN(String.valueOf(UserInformationModel.getNumberOfImages())));
//        report.put("NumberOfVideos", new AttributeValue().withN(String.valueOf(UserInformationModel.getNumberOfVideos())));
//
//
//        report.put("UpVote", new AttributeValue().withN(String.valueOf(0)));
//        report.put("NetVote", new AttributeValue().withN(String.valueOf(0)));
//        report.put("DownVote", new AttributeValue().withN(String.valueOf(0)));
//
//        if(!UserInformationModel.getInstance().getAffiliation().equals(""))
//            report.put("Affiliation", new AttributeValue().withS(UserInformationModel.getInstance().getAffiliation()));
//
//        if(!UserInformationModel.getInstance().getSpotterID().equals(""))
//            report.put("SpotterID", new AttributeValue().withS(UserInformationModel.getInstance().getSpotterID()));
//
//        if(UserInformationModel.getInstance().getCallsign()!=null && !UserInformationModel.getInstance().getCallsign().equals(""))
//            report.put("CallSign", new AttributeValue().withS(UserInformationModel.getInstance().getCallsign()));
//       // else(report.put("Callsign", new AttributeValue().withS("|")));
//
//            Log.i(TAG, "Callsign::::: " + UserInformationModel.getInstance().getCallsign()
//                    +" SpotterID:::: " +  UserInformationModel.getInstance().getSpotterID()
//                    +" Affiliation::::  " +  UserInformationModel.getInstance().getAffiliation());
//
//            ///reportToSubmit.setDateSubmittedString(Utility.epochToDateTimeString(reportToSubmit.getDateSubmittedEpoch() /1000));
//    /////////////////////////////////////////////////////////////////////////////
//    ////              VALIDATE LOCATION WHEN SUBMITTING REPORTs             /////
//    /////////////////////////////////////////////////////////////////////////////
//    //        Here we check to make sure that enough info is provided for the location
//    //            1. Street, City, State != null | = ""
//    //                A. Run the address through the geocoder and and show user what will be submitted
//    //                   i. User can select one of the addresses or reenter address
//    //            2. Lat/Long are filled out (only numbers)
//    //                A. (same as 1.A)
//
//            // DateOfEvent Stuff
//            long epoch = getDateFromDatePicker(datePicker);
//            report.put("DateOfEvent", new AttributeValue().withN(String.valueOf(epoch)));
//            //Log.i(TAG, "Date of Event: " + report.get("DateOfEvent"));
//
//
//            String[] keyArray=Utility.AttributeHashMapKeyToArray(report);
//            AttributeValue[] attrArray = Utility.AttributeHashMapValuesToArray(report);
//
//            Bundle b = new Bundle();
//            Intent intent = new Intent(this, SubmitReportDetailsActivity.class);
//
//            intent.putExtra("keyArray",keyArray);
//            intent.putExtra("attrArray",attrArray);
//
//            intent.putExtras(b);
//            intent.putExtra("weatherEventBoolsMap", weatherEventsBoolsMap);
//            startActivity(intent);
//        }
//        else
//            Toast.makeText(this,"Please enter all required fields",Toast.LENGTH_LONG);
    }


    @Override
    public void onProcessComplete(String county, String lat, String lng) {
        Log.i(TAG, "county: onProcessComplete():  " + county +" lat: "+ lat + " lng: " + lng);
        //Todo: Fix county error that occurs on real android phone
        //report.put("County", new AttributeValue().withS(s));

        //if(s != "countyError") {
//            if (validateInputs()) {
                // HashMap<String, AttributeValue> report = new HashMap();

        //datePicker = (DatePicker) findViewById(R.id.submit_report_date_picker);

        report.put("Latitude", new AttributeValue().withN(lat));
        report.put("Longitude", new AttributeValue().withN(lng));
        report.put("County", new AttributeValue().withS(county));

        report.put("DateOfEvent", new AttributeValue().withN(String.valueOf(getDateFromDatePicker(datePicker))));
        Log.d(TAG, "DATEOFEVENT: " + String.valueOf(getDateFromDatePicker(datePicker)));
//            if(!Street.getText().toString().equals(""))
//                report.put("Street", new AttributeValue().withS(Street.getText().toString()));
//            if(!Zip.getText().toString().equals(""))
//                report.put("ZipCode", new AttributeValue().withS(Zip.getText().toString()));
//            if(!State.getText().toString().equals(""))
//                report.put("State", new AttributeValue().withS(State.getText().toString()));
//            if(!Town.getText().toString().equals(""))
//                report.put("Town", new AttributeValue().withS(Town.getText().toString()));
//
//
//            double latitude = MapUtility.getLatLongFromAddress(
//                    this,
//                    Street.getText().toString()
//                    + " " + Town.getText().toString()
//                    +" " + State.getText().toString()
//                    + " " + Zip.getText().toString())[0];
//
//            double longitude = MapUtility.getLatLongFromAddress(
//                    this,
//                    Street.getText().toString()
//                    + " " + Town.getText().toString()
//                    +" " + State.getText().toString()
//                    + " " + Zip.getText().toString())[1];
//
//
//            String[] county = MapUtility.getCountyFromLatLong(this, getResources());
//           // Log.i(TAG, "county: " + count)
//
//
//            report.put("Latitude", new AttributeValue().withN(String.valueOf(latitude)));
//            report.put("Longitude", new AttributeValue().withN(String.valueOf(longitude)));
//            report.put("County", new AttributeValue().withS(county));
//
//            Log.i(TAG, "lat/long: " + latitude + ", " + longitude + "  county: " + county);


        //Enter user details retreived from cognito
        report.put("Username", new AttributeValue().withS(UserInformationModel.getInstance().getUsername()));
        report.put("FirstName", new AttributeValue().withS(UserInformationModel.getFirstName()));
        report.put("LastName", new AttributeValue().withS(UserInformationModel.getLastName()));

        report.put("NumberOfImages", new AttributeValue().withN(String.valueOf(UserInformationModel.getNumberOfImages())));
        report.put("NumberOfVideos", new AttributeValue().withN(String.valueOf(UserInformationModel.getNumberOfVideos())));

        report.put("UpVote", new AttributeValue().withN(String.valueOf(0)));
        report.put("NetVote", new AttributeValue().withN(String.valueOf(0)));
        report.put("DownVote", new AttributeValue().withN(String.valueOf(0)));

        if (!UserInformationModel.getInstance().getAffiliation().equals("")) {
            report.put("Affilliation", new AttributeValue().withS(UserInformationModel.getInstance().getAffiliation()));
        } else
            report.put("Affilliation", new AttributeValue().withS("General"));

        if (!UserInformationModel.getInstance().getSpotterID().equals(""))
            report.put("SpotterID", new AttributeValue().withS(UserInformationModel.getInstance().getSpotterID()));
        else
            report.put("SpotterID", new AttributeValue().withS("|"));

        if (UserInformationModel.getInstance().getCallsign() != null && !UserInformationModel.getInstance().getCallsign().equals(""))
            report.put("CallSign", new AttributeValue().withS(UserInformationModel.getInstance().getCallsign()));
        else
            report.put("CallSign", new AttributeValue().withS("|"));

//                Log.i(TAG, "Callsign::::: " + UserInformationModel.getInstance().getCallsign()
//                        + " SpotterID:::: " + UserInformationModel.getInstance().getSpotterID()
//                        + " Affiliation::::  " + UserInformationModel.getInstance().getAffiliation());

        ///reportToSubmit.setDateSubmittedString(Utility.epochToDateTimeString(reportToSubmit.getDateSubmittedEpoch() /1000));
        /////////////////////////////////////////////////////////////////////////////
        ////              VALIDATE LOCATION WHEN SUBMITTING REPORTs             /////
        /////////////////////////////////////////////////////////////////////////////
        //        Here we check to make sure that enough info is provided for the location
        //            1. Street, City, State != null | = ""
        //                A. Run the address through the geocoder and and show user what will be submitted
        //                   i. User can select one of the addresses or reenter address
        //            2. Lat/Long are filled out (only numbers)
        //                A. (same as 1.A)

        // DateOfEvent Stuff
        long epoch = getDateFromDatePicker(datePicker);
        report.put("DateOfEvent", new AttributeValue().withN(String.valueOf(epoch/1000)));
//                Log.i(TAG, "Date of Event:: " + report.get("DateOfEvent"));

        String[] keyArray = Utility.AttributeHashMapKeyToArray(report);
        AttributeValue[] attrArray = Utility.AttributeHashMapValuesToArray(report);

        Bundle b = new Bundle();
        Intent intent = new Intent(this, SubmitReportDetailsActivity.class);

        intent.putExtra("keyArray", keyArray);
        intent.putExtra("attrArray", attrArray);

        intent.putExtras(b);
        intent.putExtra("weatherEventBoolsMap", weatherEventsBoolsMap);
        startActivity(intent);
//            } else
//                Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_LONG);
//        }
//        else{
//            Toast.makeText(this, "Please enter all fields",Toast.LENGTH_SHORT);
//        }
    }

    @Override
    public void onProcessError(String error) {
        //Indicates an error occured when retreiving lat/long and county from entered address
        Log.e(TAG, "***Error occurred retreiving county, lat and long for entered address***");
    }

    @Override
    public void onProcessComplete(ArrayList<String> items) {

    }


    static class StateHolder {
            TextView street;
            TextView town;
            TextView zip;
            TextView state;
            TextView longitude;
            TextView lattitude;
            TextView dateOfEvent;
            DatePicker dateP;
            HashMap<String, Boolean> WeatherEventsBoolsMap;
        }
}

class GetReportCountyTask extends AsyncTask<Void, Void, String> {
   private final String TAG = "getCounty";
    // public static String getCountyFromAddress(Context mContext, Resources res, String address, JsonCallback jsonCB) {
    Context mContext;
    Resources res;
    StringCallback callback;
    String state;
    private String zip;
    private String town;
    private String street;
    private String county;

    @Override
    protected String doInBackground(Void... params) {
        Log.i(TAG, "onPostExecute()");
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String urlRequest = "https://maps.googleapis.com/maps/api/geocode/json?address=" + street.trim()+",+"+town.trim()+",+"+state.trim()+"&key=" + res.getString(R.string.google_geocoder_api_key);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.i(TAG, "getCountyFromLatLong() OnResponse: " + response);
                        try {
                            //Get County based on address
                            JSONObject reportGeoCodeDetails = new JSONObject(response);
                            JSONArray resultsArray = reportGeoCodeDetails.getJSONArray("results");
                            JSONObject array2 = resultsArray.getJSONObject(0);
                            JSONArray addressComponentsArray = array2.getJSONArray("address_components");
                            JSONObject obj = (JSONObject) addressComponentsArray.get(3);
                            String countyString = obj.getString("long_name");
                           // Log.d(TAG , "county: " + countyString);
                            county = countyString;

                            //Get Lat/Long
                            JSONObject obj2 = array2.getJSONObject("geometry");
                            JSONObject locationObj = obj2.getJSONObject("location");
                            String lat = locationObj.getString("lat");
                            String lng = locationObj.getString("lng");
                            Log.d(TAG, "lat: "+lat +" long: " + lng);


                            callback.onProcessComplete(countyString , lat, lng);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onProcessError("countyError");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "getCountyFromLatLong() : OnErrorResponse: " + error);
            }
        });
        queue.add(stringRequest);
        return county;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i(TAG, "onPostExecute() county: "  +getCounty() );
       // callback.onProcessComplete("");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }



    //Setters and Getters

    public String getCounty(){
        return county;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Resources getRes() {
        return res;
    }

    public void setRes(Resources res) {
        this.res = res;
    }

    public void setCallback(StringCallback callback) {
        this.callback = callback;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}


