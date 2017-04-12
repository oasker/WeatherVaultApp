package com.example.oliverasker.skywarnmarkii.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.oliverasker.skywarnmarkii.R;

import java.util.HashMap;

public class SubmitMultipleReportsInfoActivity extends AppCompatActivity {

    private Button incrementNumReportsButton;
    private Button decrementNumReportsButton;
    private EditText numberReportsInput;
    private Button continueButton;
    private HashMap<String, Boolean> weatherEventTypeMap;
    private int numberOfReports;

    private CheckBox severeWeatherCB, winterWeatherCB, rainFloodCB, coastalFloodCB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_multiple_reports_info);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherEventTypeMap = new HashMap<>();


        numberReportsInput = (EditText)findViewById(R.id.number_of_reports_input);
        numberReportsInput.setText("1");

        incrementNumReportsButton = (Button)findViewById(R.id.increment_number_of_reports);
        incrementNumReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numRep = Integer.parseInt(numberReportsInput.getText().toString());
                numRep++;
                numberReportsInput.setText(String.valueOf(numRep));
            }
        });

        decrementNumReportsButton = (Button)findViewById(R.id.decrement_number_of_reports);
        decrementNumReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numRep = Integer.parseInt(numberReportsInput.getText().toString());
                if(numRep>0)
                    numRep--;
                numberReportsInput.setText(String.valueOf(numRep));
            }
        });


        /////////////////   Weather Event Type Checkboxes   ////////////////////
        severeWeatherCB = (CheckBox)findViewById(R.id.severe_event_CB);
        winterWeatherCB= (CheckBox)findViewById(R.id.winter_event_CB);
        coastalFloodCB = (CheckBox)findViewById(R.id.coastal_event_CB);
        rainFloodCB= (CheckBox)findViewById(R.id.rain_event_CB);



        // continue to the next page to start submitting reports
        continueButton = (Button)findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            // Get weather types for events
            weatherEventTypeMap.put("severeBool", severeWeatherCB.isChecked());
            weatherEventTypeMap.put("winterBool", winterWeatherCB.isChecked());
            weatherEventTypeMap.put("rainFloodBool", rainFloodCB.isChecked());
            weatherEventTypeMap.put("coastalFloodBool", coastalFloodCB.isChecked());


            Intent i = new Intent(getApplicationContext(), SubmitMultipleReportsActivity.class);
            i.putExtra("weatherEventBoolsMap", weatherEventTypeMap);
            i.putExtra("numberOfReports", Integer.parseInt(numberReportsInput.getText().toString()));
            startActivity(i);
            }
        });
    }
}
