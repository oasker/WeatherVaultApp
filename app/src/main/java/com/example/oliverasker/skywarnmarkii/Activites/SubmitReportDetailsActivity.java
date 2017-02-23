package com.example.oliverasker.skywarnmarkii.Activites;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.example.oliverasker.skywarnmarkii.Fragments.CoastalFloodingSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.GeneralSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.RainFloodSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.SevereWeatherSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.WinterSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.ICallback;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *  Created by student on 10/22/16.
 *  Dynamically set layout using information entered by user on previous screen.
 *  Retreive 'extras'passed within Intent as Boolean Hashmap.
 */

public class SubmitReportDetailsActivity extends AppCompatActivity{
    private final String TAG = "SubmtRprtDetailsActvty";

    public AttributeValue[] attributeValArray;
    public String[] keyArray;

    WinterSubmitReportFragment winterFrag;
    CoastalFloodingSubmitReportFragment coastalFloodingFrag;
    RainFloodSubmitReportFragment rainFrag;
    SevereWeatherSubmitReportFragment severeFrag;
    GeneralSubmitReportFragment generalInfoFrag;


    //Rain
    private EditText Rain;
    private EditText PrecipRate;

    //Severe
    private EditText SevereType;
    private EditText WindSpeed;
    private EditText WindGust;
    private EditText WindDirection;
    private EditText HailSize;
    private EditText Tornado;
    private EditText Barometer;

    private EditText WindDamage;
    private EditText LightningDamage;
    private EditText DamageComments;

    //Winter
    private EditText Snowfall;
    private EditText SnowfallRate;
    private EditText SnowDepth;
    private EditText WaterEquivalent;
    private EditText FreezingRain;
    private EditText SnowfallSleet;
    private EditText BlowDrift;
    private EditText Whiteout;
    private EditText Thundersnow;

    //Coastal
    private EditText StormSurge;

    //To determine which report values to scan
    private boolean isSevereEvent;
    private boolean isWinterEvent;
    private boolean isCoastalEvent;
    private boolean isRainEvent;

    SkywarnWSDBMapper reportToSubmit = new SkywarnWSDBMapper();
    public HashMap<String, AttributeValue> report = new HashMap<>();

    @Override
   public void onCreate(Bundle savedInstance) {
       super.onCreate(savedInstance);
        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        Bundle weatherEventBoolsArray = i.getExtras();
        HashMap<String, Boolean> eventBools = (HashMap<String, Boolean>)weatherEventBoolsArray.get("weatherEventBoolsMap");


        Log.d(TAG, "AAAAAAHHHHHHHH");
        String[] keyArrayTest = i.getStringArrayExtra("keyArray");
        AttributeValue[] attrArrayTest = (AttributeValue[])i.getSerializableExtra("attrArray");

        for(int j =0; j <keyArrayTest.length;j++ ){
            Log.d(TAG,"KEY: " + keyArrayTest[j] + " VALUE: " + attrArrayTest[j].toString());
           if(attrArrayTest[j].getS()==""){
               Log.d(TAG,"onCReate() attrVal is empty: key"+keyArrayTest[j] + " val: " +attrArrayTest[j].toString());
           }
            report.put(keyArrayTest[j],attrArrayTest[j]);
        }
        //Get report from previous activity
        reportToSubmit = (SkywarnWSDBMapper) bundle.getSerializable("reportToSubmit");


        //Setup Toolbar
        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();

        winterFrag = new WinterSubmitReportFragment();
        rainFrag = new RainFloodSubmitReportFragment();
        coastalFloodingFrag = new CoastalFloodingSubmitReportFragment();
        severeFrag = new SevereWeatherSubmitReportFragment();
        generalInfoFrag= new GeneralSubmitReportFragment();

        isWinterEvent = (eventBools.containsKey("winterBool") && eventBools.get("winterBool"));
        isSevereEvent = (eventBools.get("severeBool")  && eventBools.get("severeBool"));
        isCoastalEvent = (eventBools.containsKey("coastalFloodBool") && eventBools.get("coastalFloodBool"));
        isRainEvent = (eventBools.get("rainFloodBool") && eventBools.get("rainFloodBool"));

        if(isWinterEvent | isSevereEvent | isCoastalEvent | isRainEvent){
        if(isWinterEvent) {
            fragTransaction.add(R.id.first_container, winterFrag, "winterFrag");
        }

        if(isCoastalEvent) {
            fragTransaction.add(R.id.second_container, coastalFloodingFrag, "coastalFloodFrag");
        }

        if(isSevereEvent) {
            fragTransaction.add(R.id.third_container, severeFrag, "severeFrag");
        }

        if(isRainEvent) {
            fragTransaction.add(R.id.fourth_container, rainFrag, "rainFrag");
        }
        fragTransaction.commit();
        setContentView(R.layout.activity_submit_report_details);
        }
        else {
            launchConfirmSubmitReportActivity(findViewById(R.id.submit_report));
        }
   }

    public boolean checkIfEditTextFieldEmpty(ArrayList<EditText> editTextList){
        for(EditText eT : editTextList){
            if(eT.getText().equals("")) {
                return false;
            }
        }
            return true;
    }

    public void launchConfirmSubmitReportActivity(View v) {
        if (isRainEvent) {
            Rain = (EditText) findViewById(R.id.rain_field_tv);
            PrecipRate = (EditText) findViewById(R.id.precip_rate_field_tv);

            if(!Rain.getText().toString().equals(""))
                report.put("Rain", new AttributeValue().withN(Rain.getText().toString()));
            if(!PrecipRate.getText().toString().equals(""))
                report.put("PrecipRate", new AttributeValue().withN(PrecipRate.getText().toString()));
            report.put("WeatherEvent", new AttributeValue().withS("Rain"));
            //report.put("RainEventComments", new AttributeValue().withS(RainEventComments.getText().toString()));
            //report.put("FloodComments", new AttributeValue().withS(FLoodComments.getText().toString()));

        }
        if (isCoastalEvent) {
            StormSurge = (EditText) findViewById(R.id.storm_surge_field_tv);
            //CoastalEventComments = (EditText)findViewById(R.id.coastal_event_comments_tv);

            report.put("WeatherEvent", new AttributeValue().withS("Coastal Event"));
            if(!StormSurge.getText().toString().equals(""))
                report.put("StormSurge", new AttributeValue().withS(StormSurge.getText().toString()));
        }

        if (isSevereEvent) {
            SevereType = (EditText) findViewById(R.id.severe_type_field_tv);
            WindSpeed = (EditText) findViewById(R.id.wind_speed_field_tv);
            WindGust = (EditText) findViewById(R.id.wind_gust_field_tv);
            WindDirection = (EditText) findViewById(R.id.wind_direction_field_tv);
            HailSize = (EditText) findViewById(R.id.hail_size_field_tv);
            Tornado = (EditText) findViewById(R.id.tornado_field_tv);
            Barometer = (EditText) findViewById(R.id.barometer_field_tv);
            LightningDamage = (EditText) findViewById(R.id.lightning_damage_field_tv);
            DamageComments = (EditText) findViewById(R.id.damage_comments_field_tv);
            WindDamage = (EditText) findViewById(R.id.wind_damage_field_tv);
//
            //if (checkIfEditTextFieldEmpty(list)) {
                report.put("WeatherEvent", new AttributeValue().withS("Severe"));
                if(!WindSpeed.getText().toString().equals(""))
                    report.put("WindSpeed", new AttributeValue().withN(WindSpeed.getText().toString()));

                if(!WindGust.getText().toString().equals(""))
                    report.put("WindGust", new AttributeValue().withS(WindGust.getText().toString()));

                if(!WindDirection.getText().toString().equals(""))
                    report.put("WindDirection", new AttributeValue().withS(WindDirection.getText().toString()));

                if(!HailSize.getText().toString().equals(""))
                    report.put("HailSize", new AttributeValue().withN(HailSize.getText().toString()));

                if(!Tornado.getText().toString().equals(""))
                    report.put("Tornado", new AttributeValue().withS(Tornado.getText().toString()));

                if(!Barometer.getText().toString().equals(""))
                    report.put("Barometer", new AttributeValue().withS(Barometer.getText().toString()));

                if(!LightningDamage.getText().toString().equals(""))
                    report.put("LightningDamage", new AttributeValue().withS(LightningDamage.getText().toString()));

                if(!DamageComments.getText().toString().equals(""))
                    report.put("DamageComments", new AttributeValue().withS(DamageComments.getText().toString()));

                if(!WindDamage.getText().toString().equals(""))
                    report.put("WindDamage", new AttributeValue().withS(WindDamage.getText().toString()));
            //}
            else
             Log.d(TAG, "SEVERE FIELDS NOT FILLED IN");

        }
        if (isWinterEvent) {
            Snowfall = (EditText) findViewById(R.id.snowfall_field_tv);
            SnowfallRate = (EditText) findViewById(R.id.snowfall_rate_field_tv);
            SnowDepth = (EditText) findViewById(R.id.snow_depth_field_tv);
            WaterEquivalent = (EditText) findViewById(R.id.water_equiv_field_tv);
            FreezingRain = (EditText) findViewById(R.id.freezing_rain_field_tv);
            SnowfallSleet = (EditText) findViewById(R.id.sleet_field_tv);
            BlowDrift = (EditText) findViewById(R.id.blowdrift_tv);
            Whiteout = (EditText) findViewById(R.id.whiteout_field_tv);
            Thundersnow = (EditText) findViewById(R.id.thundersnow_field_tv);

            if (!Snowfall.getText().toString().equals(""))
                report.put("Snowfall", new AttributeValue().withN(Snowfall.getText().toString()));
            if (!SnowfallRate.getText().toString().equals(""))
                report.put("SnowfallRate", new AttributeValue().withN(SnowfallRate.getText().toString()));
            if (!SnowDepth.getText().toString().equals(""))
                report.put("SnowDepth", new AttributeValue().withN(SnowDepth.getText().toString()));
            if (!WaterEquivalent.getText().toString().equals(""))
                report.put("WaterEquivalent", new AttributeValue().withN(WaterEquivalent.getText().toString()));
            if (!FreezingRain.getText().toString().equals(""))
                report.put("FreezingRain", new AttributeValue().withS(FreezingRain.getText().toString()));
            if (!SnowfallSleet.getText().toString().equals(""))
                report.put("SnowfallSleet", new AttributeValue().withN(SnowfallSleet.getText().toString()));
            if (!BlowDrift.getText().toString().equals(""))
                report.put("BlowDrift", new AttributeValue().withS(BlowDrift.getText().toString()));
            if (!Whiteout.getText().toString().equals(""))
                report.put("Whiteout", new AttributeValue().withS(Whiteout.getText().toString()));
            if (!Thundersnow.getText().toString().equals(""))
                report.put("Thundersnow", new AttributeValue().withS(Thundersnow.getText().toString()));
////            reportToSubmit.setWeatherEvent("Winter");
////            reportToSubmit.setSnowfall(Float.parseFloat(Snowfall.getText().toString()));
////            reportToSubmit.setSnowDepth(Float.parseFloat(SnowDepth.getText().toString()));
////            reportToSubmit.setSnowfallRate(Float.parseFloat(SnowfallRate.getText().toString()));
////            reportToSubmit.setWaterEquivalent(Float.parseFloat(WaterEquivalent.getText().toString()));
////            reportToSubmit.setFreezingRain(FreezingRain.getText().toString());
////            reportToSubmit.setSnowFallSleet(Float.parseFloat((Sleet.getText().toString())));
////            reportToSubmit.setBlowDrift(BlowDrift.getText().toString());
////            reportToSubmit.setWhiteout(Whiteout.getText().toString());
////            reportToSubmit.setThundersnow(Thundersnow.getText().toString())
//        }
        }

            Calendar cal = Calendar.getInstance();
            ///cal.add(Calendar.DATE, 1);
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
//        Log.d(TAG, "FORMATTED DATE: " + String.valueOf(cal.getTime()));
            Log.d(TAG, "FORMATTED DATE: " + String.valueOf(format1.format(cal.getTime())));
            report.put("DateSubmittedString", new AttributeValue().withS(format1.format(cal.getTime())));
            report.put("DateSubmittedEpoch", new AttributeValue().withN(String.valueOf(System.currentTimeMillis())));


        keyArray = new String[report.size()];
            attributeValArray = new AttributeValue[report.size()];

           // Log.d(TAG, "Iterating through reports HMap:---END ");
            int counter = 0;
            Set<Map.Entry<String, AttributeValue>> entrySet = report.entrySet();
            for (Map.Entry entry : entrySet) {
                Log.d(TAG, "looping counter: " + counter + " total Size: " + entrySet.size());
                Log.d(TAG, "key: " + entry.getKey() + " value: " + entry.getValue().toString());

                keyArray[counter] = entry.getKey().toString();
                attributeValArray[counter] = (AttributeValue) entry.getValue();
                if (counter < entrySet.size()+1)
                    counter++;
            }
       // Log.i(TAG, "Before submitting to async"+report.get("DateOfEvent").getN().toString());

            AsyncInsertTask2 insertTask2 = new AsyncInsertTask2(attributeValArray);
            insertTask2.execute(keyArray);

            Intent intent = new Intent(this, LaunchCameraActivity.class);
            startActivity(intent);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }
    //Evenetually holds camera stuff
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
               // Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                //        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
                return true;
            case R.id.submit_report:

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }

    }


/**
 * Created by student on 10/27/16.
 * Async class to insert records into database
 */

//public class AsyncInsertTask extends AsyncTask<Map<String,AttributeValue>, Void, Void> implements ICallback {
class AsyncInsertTask2 extends AsyncTask<String[], Void, Void> implements ICallback {

    private static final String TAG = "AsyncInsertTask2";
    AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
    AttributeValue[] attributeValues=null;

    public  AsyncInsertTask2(AttributeValue[] attributeVals){
        attributeVals = new AttributeValue[attributeVals.length];
    }

    @Override
    protected Void doInBackground(String[]... params) {

        Log.i(TAG, "In AsyncTask2");
        Utility.printMap(report);
        Map<String, AttributeValue> attributeValueMap= new HashMap<>();

        for(int i=0; i < keyArray.length; i++){
            Log.d(TAG, "key: " + keyArray[i] + " val: "+ attributeValArray[i]);
            attributeValueMap.put(keyArray[i], attributeValArray[i]);
        }
        try {
             PutItemRequest putItemRequest = new PutItemRequest("SkywarnWSDB_rev4", attributeValueMap);
            //PutItemRequest putItemRequest = new PutItemRequest("SkywarnWSDB_rev4", report);
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


