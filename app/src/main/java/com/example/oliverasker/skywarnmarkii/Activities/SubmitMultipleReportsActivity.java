package com.example.oliverasker.skywarnmarkii.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;

public class SubmitMultipleReportsActivity extends ActionBarActivity {

    private static final String TAG = "SbmtMultReportActivity";
    public HashMap<String, AttributeValue> attributeMap;
    private SubmitMultipleReportsLocation locationFragment;
    private SubmitMultipleReportsDate dateFragment;

    //    private boolean isWinterEvent;
//    private boolean isSevereEvent;
//    private boolean isCoastalEvent;
//    private boolean isRainEvent;
    private SubmitMultipleReportsAttributes attributesFragment;
    private Button writeNextReportButton;
    private Button viewPreviousReportButton;
    private WinterSubmitReportFragment winterFrag;
    private CoastalFloodingSubmitReportFragment coastalFloodingFrag;
    private RainFloodSubmitReportFragment rainFrag;
    private SevereWeatherSubmitReportFragment severeFrag;
    private GeneralSubmitReportFragment generalInfoFrag;

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
        final HashMap<String, Boolean> eventBools = (HashMap<String, Boolean>) bundle.get("weatherEventBoolsMap");

        //  Button and listeners
        writeNextReportButton = (Button) findViewById(R.id.create_new_report_button);
        writeNextReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "writeNextReportButton: onClick()");
                attributeMap = new HashMap<String, AttributeValue>();

                attributeMap = setUserAttributes(attributeMap);
//              Get Date and location
                attributeMap = dateFragment.getValues(attributeMap);
                attributeMap = locationFragment.getValues(attributeMap);


////              Get Attribtues for weather events from frags
                if (eventBools.containsKey("winterBool") && eventBools.get("winterBool"))
                    attributeMap = winterFrag.getValues(attributeMap);
                if (eventBools.containsKey("severeBool") && eventBools.get("severeBool")) {
                    attributeMap = severeFrag.getValues(attributeMap);
                    severeFrag.setmContext(getApplicationContext());
                }
                if (eventBools.containsKey("coastalFloodBool") && eventBools.get("coastalFloodBool"))
                    attributeMap = coastalFloodingFrag.getValues(attributeMap);
                if (eventBools.containsKey("rainFloodBool") && eventBools.get("rainFloodBool"))
                    attributeMap = rainFrag.getValues(attributeMap);

                EditText f = (EditText) locationFragment.getView().findViewById(R.id.street_input_field_multiple_reports);
                Log.d(TAG, "multipleReports locFrag Street:: " + f.getText());
                if (allRequiredFieldsEntered()) {
//                Log.d(TAG, "********** attributeMap size before: " + attributeMap.size());
                    Utility.printMap(attributeMap);
//                Log.d(TAG, "********** attributeMap size after: " + attributeMap.size());
                    MultipleReportInsertTask multipleInsertTask = new MultipleReportInsertTask();
                    multipleInsertTask.setAttributeMap(attributeMap);
                    multipleInsertTask.execute();
                    swapOutFrags();
                } else
                    Toast.makeText(getApplicationContext(), "Please enter a city, state, and zip code", Toast.LENGTH_SHORT).show();
            }
        });


        winterFrag = new WinterSubmitReportFragment();
        winterFrag.setmContext(this);

        severeFrag = new SevereWeatherSubmitReportFragment();
        severeFrag.setmContext(getApplicationContext());

        coastalFloodingFrag = new CoastalFloodingSubmitReportFragment();
        coastalFloodingFrag.setmContext(this);

        rainFrag = new RainFloodSubmitReportFragment();
        rainFrag.setmContext(this);

        //generalInfoFrag = new GeneralSubmitReportFragment();

        // Create a new Fragment to be placed in the activity layout
        dateFragment = new SubmitMultipleReportsDate();
        dateFragment.setmContext(this);

        locationFragment = new SubmitMultipleReportsLocation();
        locationFragment.setmContext(this);

        //  Add fragments to containers
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.date_container, dateFragment);
        transaction.replace(R.id.location_container, locationFragment);

        //  Set Weather bools
        if (eventBools.containsKey("winterBool") && eventBools.get("winterBool")) {
            transaction.add(R.id.winter_container, winterFrag, "winterFrag");
        }
        if (eventBools.containsKey("severeBool") && eventBools.get("severeBool")) {
            transaction.add(R.id.severe_container, severeFrag, "severeFrag");
        }
        if (eventBools.containsKey("coastalFloodBool") && eventBools.get("coastalFloodBool")) {
            transaction.add(R.id.coastal_container, coastalFloodingFrag, "coastalFloodFrag");
        }
        if (eventBools.containsKey("rainFloodBool") && eventBools.get("rainFloodBool")) {
            transaction.add(R.id.rain_container, rainFrag, "rainFrag");
        }
        transaction.commit();
    }

    public void swapOutFrags() {
        refreshActivity();
    }

    private boolean allRequiredFieldsEntered() {
        //Only need to check if state, city, and zip are set in location. Date submits todays date by default
        return locationFragment.areAllRequiredFieldsEntered();
    }

    private HashMap<String, AttributeValue> setUserAttributes(HashMap<String, AttributeValue> vals) {
        if (!UserInformationModel.getFirstName().equals(""))
            vals.put("FirstName", new AttributeValue().withS(UserInformationModel.getFirstName()));
        if (!UserInformationModel.getLastName().equals(""))
            vals.put("LastName", new AttributeValue().withS(UserInformationModel.getLastName()));

        if (!UserInformationModel.getInstance().getUsername().equals(""))
            vals.put("Username", new AttributeValue().withS(UserInformationModel.getInstance().getUsername()));

        if (UserInformationModel.getInstance().getCallsign() != null && !UserInformationModel.getInstance().getCallsign().equals(""))
            vals.put("CallSign", new AttributeValue().withS(UserInformationModel.getInstance().getCallsign()));
        if (!UserInformationModel.getInstance().getAffiliation().equals(""))
            vals.put("Affilliation", new AttributeValue().withS(UserInformationModel.getInstance().getAffiliation()));

        if (!UserInformationModel.getInstance().getSpotterID().equals(""))
            vals.put("SpotterID", new AttributeValue().withS(UserInformationModel.getInstance().getSpotterID()));
        return vals;
    }

    private void refreshActivity() {
        Toast.makeText(this, "Report Submitted", Toast.LENGTH_SHORT);
        finish();
        startActivity(getIntent());
    }

    class MultipleReportInsertTask extends AsyncTask<Void, Void, Void> implements ICallback {

        private static final String TAG = "AsyncInsertTask2";
        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
        HashMap<String, AttributeValue> attributeMap;

        public MultipleReportInsertTask() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "MultipleReportInsertTask doInBackground(): ");
            try {
                PutItemRequest putItemRequest = new PutItemRequest("SkywarnWSDB_rev4", attributeMap);
                putItemRequest.setItem(attributeMap);

                PutItemResult putItemResult = ddb.putItem(putItemRequest);
                Log.d(TAG, putItemResult.toString());
            } catch (DynamoDBMappingException dynamoDBMappingException) {
                Log.e(TAG, dynamoDBMappingException.toString());
            } catch (com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException dynamoDBMappingException) {
                Log.e(TAG, dynamoDBMappingException.toString());
            } catch (AmazonServiceException ex) {
                MainActivity.clientManager.wipeCredentialsonAuthError(ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            //delegate.processFinish();
        }

        @Override
        public void processFinish(ArrayList<SkywarnWSDBMapper> result) {

        }

        @Override
        public void allQueriesComplete() {

        }

        public void setAttributeMap(HashMap<String, AttributeValue> attributeMap) {
            this.attributeMap = attributeMap;
        }
    }
}
