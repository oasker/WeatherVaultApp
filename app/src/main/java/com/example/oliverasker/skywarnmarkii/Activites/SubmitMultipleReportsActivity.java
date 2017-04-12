package com.example.oliverasker.skywarnmarkii.Activites;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Fragments.GeneralSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.SubmitMultipleReportsActivityFragments.SubmitMultipleReportsAttributes;
import com.example.oliverasker.skywarnmarkii.Fragments.SubmitMultipleReportsActivityFragments.SubmitMultipleReportsDate;
import com.example.oliverasker.skywarnmarkii.Fragments.SubmitMultipleReportsActivityFragments.SubmitMultipleReportsLocation;
import com.example.oliverasker.skywarnmarkii.Fragments.WeatherEventSubmitFragments.CoastalFloodingSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.WeatherEventSubmitFragments.RainFloodSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.WeatherEventSubmitFragments.SevereWeatherSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.WeatherEventSubmitFragments.WinterSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;

public class SubmitMultipleReportsActivity extends ActionBarActivity {

    private static final String TAG="SbmtMultReportActivity";

    private SubmitMultipleReportsLocation locationFragment;
    private SubmitMultipleReportsDate dateFragment;
    private SubmitMultipleReportsAttributes attributesFragment;

    private boolean isWinterEvent;
    private boolean isSevereEvent;
    private boolean isCoastalEvent;
    private boolean isRainEvent;

    private int numberOfReports;

    private Button writeNextReportButton;
    private Button viewPreviousReportButton;

    private WinterSubmitReportFragment winterFrag;
    private CoastalFloodingSubmitReportFragment coastalFloodingFrag;
    private RainFloodSubmitReportFragment rainFrag;
    private SevereWeatherSubmitReportFragment severeFrag;
    private GeneralSubmitReportFragment generalInfoFrag;


    private HashMap<String,AttributeValue> attributeMap;
    private String[] keyArray;
    private AttributeValue[] attributeValArray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_multiple_reports_layout);


//        Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Get number of reports to submit and the type of reports
        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        final HashMap<String, Boolean> eventBools = (HashMap<String, Boolean>)bundle.get("weatherEventBoolsMap");
        numberOfReports= i.getIntExtra("numberOfReports", 0);

        //  Button and listeners
        writeNextReportButton =(Button)findViewById(R.id.create_new_report_button);
        writeNextReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "writeNextReportButton: onClick()");
                attributeMap = new HashMap<String, AttributeValue>();

//              Get Date and location
                attributeMap=dateFragment.getValues(attributeMap);
//                attributeMap=locationFragment.getValues(attributeMap);

                attributeMap = setUserAttributes(attributeMap);
//
////              Get Attribtues for weather events from frags
//                if(eventBools.containsKey("winterBool") && eventBools.get("winterBool"))
//                    attributeMap = winterFrag.getValues(attributeMap);
//                if(eventBools.containsKey("severeBool") && eventBools.get("severeBool"))
//                    attributeMap=severeFrag.getValues(attributeMap);
//                if(eventBools.containsKey("coastalFloodBool") && eventBools.get("coastalFloodBool"))
//                    attributeMap=coastalFloodingFrag.getValues(attributeMap);
//                if(eventBools.containsKey("rainFloodBool") && eventBools.get("rainFloodBool"))
//                  attributeMap=rainFrag.getValues(attributeMap);

                Utility.printMap(attributeMap);

                keyArray = new String[attributeMap.size()];
                attributeValArray = new AttributeValue[attributeMap.size()];

                int counter = 0;
                Set<Map.Entry<String, AttributeValue>> entrySet = attributeMap.entrySet();
                for (Map.Entry entry : entrySet) {
                    Log.d(TAG, "looping counter: " + counter + " total Size: " + entrySet.size());
                    Log.d(TAG, "key: " + entry.getKey() + " value: " + entry.getValue().toString());

                    keyArray[counter] = entry.getKey().toString();
                    attributeValArray[counter] = (AttributeValue) entry.getValue();
                    if (counter < entrySet.size()+1)
                        counter++;
                }
                MultipleReportInsertTask multipleInsertTask= new MultipleReportInsertTask(attributeValArray);
                multipleInsertTask.execute(keyArray);
            }
        });

        viewPreviousReportButton =(Button)findViewById(R.id.view_previous_report_button);
        viewPreviousReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        winterFrag=new WinterSubmitReportFragment();
        severeFrag=new SevereWeatherSubmitReportFragment();
        coastalFloodingFrag = new CoastalFloodingSubmitReportFragment();
        rainFrag = new RainFloodSubmitReportFragment();
        //generalInfoFrag = new GeneralSubmitReportFragment();


        // Create a new Fragment to be placed in the activity layout
        dateFragment = new SubmitMultipleReportsDate();
        locationFragment = new SubmitMultipleReportsLocation();


        //  Set Weather bools
        if(eventBools.containsKey("winterBool") && eventBools.get("winterBool"))
            isWinterEvent=true;
        if(eventBools.containsKey("severeBool") && eventBools.get("severeBool"))
            isSevereEvent=true;
        if(eventBools.containsKey("coastalFloodBool") && eventBools.get("coastalFloodBool"))
            isCoastalEvent=true;
        if(eventBools.containsKey("rainFloodBool") && eventBools.get("rainFloodBool"))
            isRainEvent=true;

        //  Add fragments to containers
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.date_container, dateFragment);
        transaction.replace(R.id.location_container, locationFragment);


        if(isWinterEvent | isSevereEvent | isCoastalEvent | isRainEvent){
            if(isWinterEvent) {
                transaction.add(R.id.winter_container, winterFrag, "winterFrag");
            }
            if(isCoastalEvent) {
                transaction.add(R.id.coastal_container, coastalFloodingFrag, "coastalFloodFrag");
            }
            if(isSevereEvent) {
                transaction.add(R.id.severe_container, severeFrag, "severeFrag");
            }
            if(isRainEvent) {
                transaction.add(R.id.rain_container, rainFrag, "rainFrag");
            }
            // transaction.add(R.id.general_info_container, generalInfoFrag," generalInfoFrag");
        }
        transaction.commit();
    }

    private HashMap<String, AttributeValue> setUserAttributes(HashMap<String,AttributeValue> vals){
        vals.put("FirstName", new AttributeValue().withS(UserInformationModel.getFirstName()));
        vals.put("LastName", new AttributeValue().withS(UserInformationModel.getLastName()));

        vals.put("Username", new AttributeValue().withS(UserInformationModel.getInstance().getUsername()));
        vals.put("CallSign", new AttributeValue().withS(UserInformationModel.getInstance().getCallsign()));
        vals.put("Affiliation", new AttributeValue().withS(UserInformationModel.getInstance().getAffiliation()));
        vals.put("SpotterID", new AttributeValue().withS(UserInformationModel.getInstance().getSpotterID()));

        return  vals;
    }

    private void refreshActivity(){
        finish();
        startActivity(getIntent());
    }
    class MultipleReportInsertTask extends AsyncTask<String[], Void, Void> implements ICallback {

        private static final String TAG = "AsyncInsertTask2";
        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
        AttributeValue[] attributeValues=null;

        public  MultipleReportInsertTask(AttributeValue[] attributeVals){
            attributeVals = new AttributeValue[attributeVals.length];
        }

        @Override
        protected Void doInBackground(String[]... params) {

            Log.i(TAG, "In AsyncTask2");
            Map<String, AttributeValue> attributeValueMap= new HashMap<>();

            for(int i=0; i < keyArray.length; i++){
                Log.d(TAG, "key: " + keyArray[i] + " val: "+ attributeValArray[i]);
                attributeValueMap.put(keyArray[i], attributeValArray[i]);
            }
            try {
                PutItemRequest putItemRequest = new PutItemRequest("SkywarnWSDB_rev4", attributeValueMap);
                PutItemResult putItemResult = ddb.putItem(putItemRequest);
            }catch (DynamoDBMappingException dynamoDBMappingException){
                Log.e(TAG, dynamoDBMappingException.toString());
            }catch (com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException dynamoDBMappingException){
                Log.e(TAG, dynamoDBMappingException.toString());
            }
            catch (AmazonServiceException ex) {
                MainActivity.clientManager.wipeCredentialsonAuthError(ex);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void v){
            //delegate.processFinish();
        }
        @Override
        public void processFinish(ArrayList<SkywarnWSDBMapper> result) {

        }
    }
}
