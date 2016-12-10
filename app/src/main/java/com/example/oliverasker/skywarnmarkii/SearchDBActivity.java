package com.example.oliverasker.skywarnmarkii;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.oliverasker.skywarnmarkii.Utility.getTextFields;

/**
 *  Created by student on 10/27/16.
 *  This activity shows view to enter query features and then on submit swaps out seach fragment to
 *  show result fragment
 */

public class SearchDBActivity extends FragmentActivity implements ICallback {
    private ListView listView;
    HashMap<EditText, String>  generalInfoTextEditFields;
    ArrayList<EditText> allEditTexts;

    //Fragment layout objects are used in method that auto collects editTextFields
    //private LinearLayout generalInfoLayout = (LinearLayout)findViewById(R.id.search_db_linear_layout);
    // private LinearLayout severeWeatherLayout
    // private LinearLayout winterWeatherLayout
    // private LinearLayout  rainWeatherLayout

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
        getFragmentManager().beginTransaction()
                .add(R.id.first_frame_view,searchFrag)
                .commit();

        //Setup Toolbar
        /*
        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        */

        //To test scan function
        HashMap<String,String> testAttr = new HashMap<String,String>();
        testAttr.put("WeatherEvent","Winter");
        performScan(testAttr);

    }

    @Override
    public void processFinish(ArrayList<SkywarnWSDBMapper> result) {
        listView = (ListView)findViewById(R.id.weather_list_view);
        SkywarnWSDBMapper[] data = result.toArray(new SkywarnWSDBMapper[result.size()]);
        System.out.println("processFinishedResult City: "+result.get(0).getEventCity());
        SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(this, data);
        listView.setAdapter(skywarnAdapter);
        for(int i =0; i < data.length;i++){
            System.out.println("searchdBReceivedVals: " + data[i]);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPos = position;
                SkywarnWSDBMapper itemValue = (SkywarnWSDBMapper) listView.getItemAtPosition(position);
                System.out.println(itemValue.getWeatherEvent());
                swapFragmentViews();
            }
        });
    }
    private void swapFragmentViews(){

    }

    private void nextButtonPressed(){
       // ArrayList<EditText> allEditTexts = getTextFields(generalInfoLayout);

        for(int i =0; i <allEditTexts.size(); i++) {
           /// generalInfoTextEditFields.put(((allEditTexts.), ));
          //  getTextFields(generalInfoLayout);
        }
        //Pass in hashmap with attributes
       /// performScan(genAttrVals);
    }

    //On Submit Query Button Press
    public void performScan(HashMap<String,String> attrVals){
        MyScanAyncTask scanTask = new MyScanAyncTask();
        scanTask.delegate = this;
        scanTask.execute(attrVals);
    }
}
//Icons provided by: https://icons8.com/web-app/for/android/next