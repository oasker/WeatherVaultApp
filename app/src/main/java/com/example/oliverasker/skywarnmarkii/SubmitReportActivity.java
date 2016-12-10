package com.example.oliverasker.skywarnmarkii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.HashMap;
import java.util.Random;

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
    //  Used for determining which fragments are shown in next activity (instance bools initialized to 0 fyi)
    private Boolean severeWeatherBool = false;
    private Boolean winterWeatherBool = false;
    private Boolean rainAndFloodBool = false;
    private Boolean coastalFloodingBool = false;
    private Button proceedButton;
    private CheckBox severeWeatherCB, winterWeatherCB, rainFloodCB, coastalFloodCB;

    EditText State;
    EditText Street;
    EditText Zip;
    EditText Town;
    EditText Longitude;
    EditText Lattitude;

    DatePicker datePicker;
    TimePicker timePicker;
    SkywarnWSDBMapper reportToSubmit;

    HashMap<String, Boolean> weatherEventsBoolsMap = new HashMap<String, Boolean>();

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        Intent i = getIntent();
        setContentView(R.layout.submit_report_activity);

        reportToSubmit = new SkywarnWSDBMapper();
        //Setup toolbar
        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Add Bools to hashmap so we know what fragments to place on next screen
        weatherEventsBoolsMap.put("severeBool", false);
        weatherEventsBoolsMap.put("winterBool", false);
        weatherEventsBoolsMap.put("coastalFloodBool", false);
        weatherEventsBoolsMap.put("rainFloodBool", false);

        proceedButton = (Button)findViewById(R.id.submit_activity_button_to_next_screen);

        severeWeatherCB = (CheckBox)findViewById(R.id.submit_activity_severe_weather_checkbox);
        severeWeatherCB.setOnClickListener(cbListener);

        winterWeatherCB = (CheckBox)findViewById(R.id.submit_activity_winter_checkbox);
        winterWeatherCB.setOnClickListener(cbListener);

        rainFloodCB=(CheckBox)findViewById(R.id.submit_activity_rainfall_flooding_checkbox);
        rainFloodCB.setOnClickListener(cbListener);

        coastalFloodCB=(CheckBox)findViewById(R.id.submit_activity_coastal_flooding_checkbox);
        coastalFloodCB.setOnClickListener(cbListener);

        Town = (EditText)findViewById(R.id.submit_activity_town_text_field);
        State = (EditText)findViewById(R.id.submit_activity_zip_text_field);
        Zip  = (EditText)findViewById(R.id.submit_activity_zip_text_field);
        Street = (EditText)findViewById(R.id.submit_activity_street_text_field);
        Lattitude = (EditText)findViewById(R.id.submit_activity_lattitude);
        Longitude = (EditText)findViewById(R.id.submit_activity_longitude);
        datePicker= (DatePicker)findViewById(R.id.submit_report_date_picker);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
    }

    private OnClickListener cbListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            severeWeatherBool = severeWeatherCB.isChecked();
            coastalFloodingBool = coastalFloodCB.isChecked();
            rainAndFloodBool = rainFloodCB.isChecked();
            winterWeatherBool = winterWeatherCB.isChecked();

            weatherEventsBoolsMap.put("severeBool", severeWeatherBool);
            weatherEventsBoolsMap.put("winterBool", winterWeatherBool);
            weatherEventsBoolsMap.put("rainFloodBool", rainAndFloodBool);
            weatherEventsBoolsMap.put("coastalFloodBool", coastalFloodingBool);

            //Add elements to row object and pass to next activity

            datePicker= (DatePicker)findViewById(R.id.submit_report_date_picker);

            String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
            String unEpoch = "Frank" + "_" + date;
            reportToSubmit.setReportID(unEpoch);

        }
    };

    public void addListenerOnButton(){
        severeWeatherCB = (CheckBox)findViewById(R.id.submit_activity_severe_weather_checkbox);
        winterWeatherCB = (CheckBox)findViewById(R.id.submit_activity_winter_checkbox);
        rainFloodCB=(CheckBox)findViewById(R.id.submit_activity_rainfall_flooding_checkbox);
        coastalFloodCB=(CheckBox)findViewById(R.id.submit_activity_coastal_flooding_checkbox);

        proceedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherEventsBoolsMap.put("severeBool", severeWeatherBool);
                weatherEventsBoolsMap.put("winterBool", winterWeatherBool);
                weatherEventsBoolsMap.put("rainFloodBool", rainAndFloodBool);
                weatherEventsBoolsMap.put("coastalFloodBool", coastalFloodingBool);
            }
        });
    }

    //Launches activity where reporter enters specific weather info
    public void LaunchSubmitReportDetails(View v){
        Bundle b = new Bundle();
       // gatherData();
        Intent intent = new Intent(this, SubmitReportDetails.class);

        datePicker= (DatePicker)findViewById(R.id.submit_report_date_picker);
        ///     Need to implement converter for unix timestamp
        //Date d = getDateFromDatePicker(datePicker);
        //String date = d.toString();
       // String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
       // System.out.println("DATETETEE: "+ date);
        String random = getRandomNumber();
        String date = "147700" + random+"."+getRandomNumber();
        String unEpoch = "Frank" + "_" + date;

        //String hour = Integer.toString(timePicker.getHour());
        //String minute = Integer.toString(timePicker.getMinute());
        reportToSubmit.setReportID(unEpoch);
        reportToSubmit.setStreet(Street.getText().toString());
        reportToSubmit.setZipCode(Zip.getText().toString());
        reportToSubmit.setLongitude(Longitude.getText().toString());
        reportToSubmit.setLattitude(Lattitude.getText().toString());
        reportToSubmit.setEventCity(Town.getText().toString());
        //reportToSubmit.setTime(hour+":"+minute);
        reportToSubmit.setDate(date);

        b.putSerializable("reportToSubmit", reportToSubmit);
        intent.putExtras(b);
        intent.putExtra("weatherEventBoolsMap", weatherEventsBoolsMap);
        startActivity(intent);
    }

    // This method gathers all entered info and puts it in a static class holder
    // so the data is not lost when switching between activities.
    public void gatherData(){
        StateHolder holder = new StateHolder();
        //Location Data
        TextView streetTV = (TextView)findViewById(R.id.submit_activity_street_text_field);
        TextView townTV = (TextView)findViewById(R.id.submit_activity_town_text_field);
        TextView zipTV = (TextView)findViewById(R.id.submit_activity_zip_text_field);
        TextView longitudeTV = (TextView)findViewById(R.id.submit_activity_longitude);
        TextView lattitudeTV = (TextView)findViewById(R.id.submit_activity_lattitude);
        //Date Data
        DatePicker datePicker = (DatePicker)findViewById(R.id.submit_report_date_picker);
        //Weather Event Type Data
        CheckBox severeWeatherCB = (CheckBox)findViewById(R.id.submit_activity_severe_weather_checkbox);
        CheckBox winterWeatherCB = (CheckBox)findViewById(R.id.submit_activity_winter_checkbox);
        CheckBox coastalFloodingWeatherCB = (CheckBox)findViewById(R.id.submit_activity_coastal_flooding_checkbox);
        CheckBox rainFloodingWeatherCB = (CheckBox)findViewById(R.id.submit_activity_rainfall_flooding_checkbox);

        /*
        holder.street=streetTV;
        holder.dateP=datePicker;
        holder.lattitude=lattitudeTV;
        holder.longitude=longitudeTV;
        holder.town=townTV;
        holder.zip=zipTV;
        */
       // severeWeatherBool = severeWeatherCB.

    }
    static class StateHolder {
        TextView street;
        TextView town;
        TextView zip;
        TextView longitude;
        TextView lattitude;
        DatePicker dateP;
    }

    public String getRandomNumber() {
        int min =0;
        int max =2200;
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return Integer.toString(randomNum);
    }
    /*
    public static Date getDateFromDatePicker(DatePicker dateP){
        int day = dateP.getDayOfMonth();
        int month = dateP.getMonth();
        int year = dateP.getYear();


        Calendar calendar = Calendar()
        calendar.set(year,month,day);
        return calendar.getTime();

    }
    */
}
