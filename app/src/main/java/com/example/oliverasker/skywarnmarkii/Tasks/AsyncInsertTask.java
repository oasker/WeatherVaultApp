package com.example.oliverasker.skywarnmarkii.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.oliverasker.skywarnmarkii.Activites.MainActivity;
import com.example.oliverasker.skywarnmarkii.ICallback;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by student on 10/27/16.
 * Async class to insert records into database
 */

//public class AsyncInsertTask extends AsyncTask<Map<String,AttributeValue>, Void, Void> implements ICallback {
public class AsyncInsertTask extends AsyncTask<String[], Void, Void> implements ICallback {

    private static final String TAG = "AsyncInsertTask";
    private Map<String, AttributeValue> report;
    //SkywarnWSDBMapper report;
    AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
    AttributeValue[] attributeValues=null;

   // public AsyncInsertTask(Map<String, AttributeValue> Report){
    public  AsyncInsertTask(AttributeValue[] attributeVals){
        attributeVals = new AttributeValue[attributeVals.length];
        attributeValues = attributeVals;
        //report = Report;
        //Log.d(TAG, "Date submitted: " +.get("DateSubmittedString")  + " Or " + report.get("DateSubmittedEpoch"));
        for(int i =0; i < attributeValues.length-1;i++){
            Log.d(TAG, attributeValues[i].toString());
        }
    }

    @Override
    protected Void doInBackground(String[]... params) {
            try {

                 //Each attribute will be an AttributeValue
//                report.put("DateSubmittedEpoch",
//                        new AttributeValue().withN("11111111"));
//                report.put("DateSubmittedString",
//                        new AttributeValue().withS("4/999/2017"));
                //Log.d(TAG, "_____ map received in doInBackground()__params[0]");


                String[] keyArray = params[0];
                //String[] valArray =params[1];
                //String[] numberOrStringArray = params[2];

                for(int i =0; i < keyArray.length-1;i++) {
                    Log.d(TAG, "Key: " + keyArray[i] + " Value: " + attributeValues[i].getS().toString() + " N/S: " );// numberOrStringArray[i]);
                }



                Log.d(TAG, "_____________________________________________");

               // PutItemRequest putItemRequest = new PutItemRequest("SkywarnWSDB_rev4", report);


//                PutItemRequest putItemRequest = new PutItemRequest("SkywarnWSDB_rev4", params[0]);
//                PutItemResult putItemResult = ddb.putItem(putItemRequest);

                //DynamoDBManager.insertRecord(report);
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