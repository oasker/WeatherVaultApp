package com.example.oliverasker.skywarnmarkii.Activites;

import android.content.Intent;
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
import android.widget.Toast;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
public class SubmitReportActivity extends AppCompatActivity {
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
    private EditText Longitude;
    private EditText Latitude;

    private TimePicker timePicker;
    private DatePicker datePicker;
    private SkywarnWSDBMapper reportToSubmit;

    private Map<String, AttributeValue> report;
    HashMap<String, Boolean> weatherEventsBoolsMap = new HashMap<String, Boolean>();

    private String dateSubmittedString;
    private String dateSubmittedEpoch;

    StateHolder sH;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        Intent i = getIntent();
        setContentView(R.layout.submit_report_activity);
        reportToSubmit = new SkywarnWSDBMapper();
        //Setup toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //SubmitReportSingleton.getInstance().setMapHolder(new HashMap<String,AttributeValue>());

        if (sH == null) {
            //Add Bools to hashmap so we know what fragments to place on next screen
            weatherEventsBoolsMap.put("severeBool", false);
            weatherEventsBoolsMap.put("winterBool", false);
            weatherEventsBoolsMap.put("coastalFloodBool", false);
            weatherEventsBoolsMap.put("rainFloodBool", false);

            proceedButton = (Button) findViewById(R.id.submit_activity_button_to_next_screen);
            proceedButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
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


            Town = (EditText) findViewById(R.id.submit_activity_town_text_field);
            State = (EditText) findViewById(R.id.submit_activity_zip_text_field);
            Zip = (EditText) findViewById(R.id.submit_activity_zip_text_field);
            Street = (EditText) findViewById(R.id.submit_activity_street_text_field);
            Latitude = (EditText) findViewById(R.id.submit_activity_lattitude);
            Longitude = (EditText) findViewById(R.id.submit_activity_longitude);
            datePicker = (DatePicker) findViewById(R.id.submit_report_date_picker);
            timePicker = (TimePicker) findViewById(R.id.timePicker);
        } else {
            Log.d(TAG, "StateHolder != null");
        }
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

            // Set Hashkey and RangeKey on report to submit
            //reportToSubmit.setUsername(UserInformationModel.getInstance().getUsername());
            //reportToSubmit.setDateSubmittedEpoch(System.currentTimeMillis());
            //reportToSubmit.setDateSubmittedString(Utility.convertDate(reportToSubmit.getDateSubmittedEpoch()));
            if (sH == null) {
                //Add values to stateholder
                sH = new StateHolder();
                sH.dateP = datePicker;
                sH.lattitude = Latitude;
                sH.longitude = Longitude;
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
                | Town.getText().toString() == "")

                | Latitude.getText().toString() == ""
                | Longitude.getText().toString() == ""){
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
        HashMap<String, AttributeValue> report = new HashMap();
        if(validateInputs()) {
           // HashMap<String, AttributeValue> report = new HashMap();

            datePicker = (DatePicker) findViewById(R.id.submit_report_date_picker);
            report.put("DateOfEvent", new AttributeValue().withN(String.valueOf(getDateFromDatePicker(datePicker))));

            if(!Street.getText().toString().equals(""))
                report.put("Street", new AttributeValue().withS(Street.getText().toString()));
            if(!Zip.getText().toString().equals(""))
                report.put("ZipCode", new AttributeValue().withS(Zip.getText().toString()));
            if(!State.getText().toString().equals(""))
                report.put("State", new AttributeValue().withS(State.getText().toString()));
            if(!Town.getText().toString().equals(""))
                report.put("Town", new AttributeValue().withS(Town.getText().toString()));

            //Enter user details retreived from cognito
            report.put("Username", new AttributeValue().withS(UserInformationModel.getInstance().getUsername()));
            report.put("FirstName", new AttributeValue().withS(UserInformationModel.getFirstName()));
            report.put("LastName", new AttributeValue().withS(UserInformationModel.getLastName()));


        report.put("NumberOfImages", new AttributeValue().withN(String.valueOf(UserInformationModel.getNumberOfImages())));
        report.put("NumberOfVideos", new AttributeValue().withN(String.valueOf(UserInformationModel.getNumberOfVideos())));

        if(!UserInformationModel.getInstance().getAffiliation().equals(""))
            report.put("Affiliation", new AttributeValue().withS(UserInformationModel.getInstance().getAffiliation()));

        if(!UserInformationModel.getInstance().getSpotterID().equals(""))
            report.put("SpotterID", new AttributeValue().withS(UserInformationModel.getInstance().getSpotterID()));

        if(!UserInformationModel.getInstance().getCallsign().equals(""))
            report.put("CallSign", new AttributeValue().withS(UserInformationModel.getInstance().getCallsign()));

            Log.i(TAG, "Callsign: " + UserInformationModel.getInstance().getCallsign()
                    +" SpotterID: " +  UserInformationModel.getInstance().getSpotterID()
                    +" Affiliation:  " +  UserInformationModel.getInstance().getAffiliation());

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
    //        reportToSubmit.setStreet(Street.getText().toString());
    //        reportToSubmit.setZipCode(Zip.getText().toString());
    //        reportToSubmit.setEventCity(Town.getText().toString());

        //Get Lat/Long from user input
       // if (Longitude.getText() != null & Longitude.getText().toString() != "" & Latitude.getText() != null & Latitude.getText().toString() != "") {
            try {
                if(!Longitude.getText().toString().equals("") & !Latitude.getText().toString().equals("")) {
                    report.put("Longitude", new AttributeValue().withN(Longitude.getText().toString()));
                    report.put("Latitude", new AttributeValue().withN(Latitude.getText().toString()));
                }
    //                reportToSubmit.setLongitude(Long.parseLong(Longitude.getText().toString()));
    //                reportToSubmit.setLatitude(Long.parseLong(Latitude.getText().toString()));
            } catch (NumberFormatException e) {
                Log.e(TAG, "NumberFormatException", e);
            }

            // DateOfEvent Stuff
            long epoch = getDateFromDatePicker(datePicker);
            //Log.d(TAG, "LaunchSubmitReportDetails():::: DATE: " + epoch);
            report.put("DateOfEvent", new AttributeValue().withN(String.valueOf(epoch)));
            //Log.i(TAG, "Date of Event: " + report.get("DateOfEvent"));


            String[] keyArray=Utility.AttributeHashMapKeyToArray(report);
            AttributeValue[] attrArray = Utility.AttributeHashMapValuesToArray(report);

            //Log.d(TAG, "LaunchSubmitReport:");
           // Utility.printMap(report);

            for(int i =0; i < attrArray.length; i++){
               // Log.d(TAG, "key: " + keyArray[i] + " val: " + attrArray[i].toString());
            }


            Bundle b = new Bundle();
            Intent intent = new Intent(this, SubmitReportDetailsActivity.class);

            intent.putExtra("keyArray",keyArray);
            intent.putExtra("attrArray",attrArray);

            intent.putExtras(b);
            intent.putExtra("weatherEventBoolsMap", weatherEventsBoolsMap);
            startActivity(intent);
        }
        else
            Toast.makeText(this,"Please enter all required fields",Toast.LENGTH_LONG);
    }
        // This class simply holds the widget instances for each table item
        // so it is not lost when switching between activities.
        // (i.e. Back Button, rotate sreen eventually)

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

