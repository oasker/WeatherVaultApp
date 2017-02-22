package com.example.oliverasker.skywarnmarkii.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import com.example.oliverasker.skywarnmarkii.Fragments.SearchDBFragment;
import com.example.oliverasker.skywarnmarkii.R;

import java.util.HashMap;

/**
 *  Created by student on 10/27/16.
 *  This activity shows view to enter query features and then on submit swaps out seach fragment to
 *  show result fragment
 */

public class SearchDBActivity extends AppCompatActivity implements  SearchDBFragment.OnItemSelectedListener{
    private ListView listView;
    HashMap<String,String> testAttr;

    private static final String TAG = "SearchDBActivity";

    EditText cityTF;
    EditText stateTF;
    EditText zipTF;
    EditText streetTF;
    Switch singleOrRangedDate;
    CheckBox useCurrentLocation;
    
    //DateRange Stuff
    DatePicker startDP;
    DatePicker endDP;

    private int startMonth;
    private int endMonth;
    private int startDay;
    private int endDay;
    private int startYear;
    private int endYear;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.search_db_activity_layout);

        if(findViewById(R.id.frag1_container)!=null) {
            if (savedInstance != null) {
                return;
            }
        }


        //General Query info shown first
        SearchDBFragment searchFrag = new SearchDBFragment();
        // In cas the intent holds instructons pass Intents extras to fragments as arguments
        searchFrag.setArguments(getIntent().getExtras());
        //Add Fragment to FrameLayout placeholders on search_db_activity_layout

       android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchDBFragment searchDBFragment = new SearchDBFragment();
        fragmentTransaction.add(R.id.first_frame_view, searchFrag,"Yo");
        fragmentTransaction.commit();

        ///////////////////////////////////////////////////
        ////////  TEST SCAN
        ///////////////////////////////////////////////////
        /*
        testAttr = new HashMap<String,String>();
        //testAttr.put("City", "Egypt");
        testAttr.put("State","CT");

        Bundle bundle = new Bundle();
        bundle.putSerializable("HashMap",testAttr );
        Intent intent = new Intent(SearchDBActivity.this, QueryLauncherActivity.class);
        intent.putExtras(bundle);
        launchQueryScan(intent);
        */

        ///////////////////////////////////////////////////
        ////////  Add Listeners
        ///////////////////////////////////////////////////
    }

    public void launchQueryScan(HashMap<String,String> testAttr){
        Intent intent = new Intent(this, QueryLauncherActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("HashMap", testAttr);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onSubmitScan(){

        testAttr = new HashMap<String,String>();
        //Get Values user entered values from fragment
        cityTF = (EditText)findViewById(R.id.city_input_text);
        stateTF  = (EditText)findViewById(R.id.state_input_text);
        streetTF = (EditText)findViewById(R.id.street_input_text);
        zipTF  = (EditText)findViewById(R.id.zip_input_field);
        singleOrRangedDate = (Switch)findViewById(R.id.single_or_ranged_date);
        useCurrentLocation = (CheckBox)findViewById(R.id.use_current_location_cb);

        // Add values to hashMap
        Log.d(TAG, cityTF.getText().toString() );
        //if(cityTF.getText().toString() != null)
        //testAttr.put("City", cityTF.getText().toString());
        //testAttr.put("State", stateTF.getText().toString());
        //testAttr.put("Zip", zipTF.getText().toString());
        //testAttr.put("Street", streetTF.getText().toString());
        testAttr.put("State", "CT");


        ///////////////////////////////////////////////
        ////////    Date Range Stuff        ///////////
        ///////////////////////////////////////////////
        startDP = (DatePicker) findViewById(R.id.start_datepicker);
        endDP = (DatePicker) findViewById(R.id.end_datepicker);

        //Get start date range from StartDP
        startYear = startDP.getYear();
        startMonth = startDP.getMonth()+1;
        startDay = startDP.getDayOfMonth();

        //Get end date range from EndDP
        endYear = endDP.getYear();
        endMonth = endDP.getMonth() +1;
        endDay = endDP.getDayOfMonth();



        //Log for debug
        //Log.d(TAG, startMonth + "/" +startDay+ "/" + startYear);


        //Utility.printMap(testAttr);
        //testAttr = Utility.checkForDefaultValue(testAttr);
        //Log.d(TAG, "After  removing default values size is " + testAttr.size());
        //Utility.printMap(testAttr);
        launchQueryScan(testAttr);
    }
    @Override
    public void onSubmitScan(HashMap<String, String> tempMap) {

    }
}

//Icons provided by: https://icons8.com/web-app/for/android/next