package com.example.oliverasker.skywarnmarkii;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.s3.AmazonS3Client;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.amazonaws.regions.Regions.US_EAST_1;

/**
 * Created by student on 10/27/16.
 */

public class Utility {
    private static String delimiter = "_";
    private static final String TAG = "Utility: ";


    public static TransferUtility sTransferUtility;
    public static AmazonS3Client s3Client;
    public static CognitoCachingCredentialsProvider credPro;


    public static TransferUtility getTransferUtility(Context context) {
        if (sTransferUtility == null) {
            sTransferUtility = new TransferUtility(getS3Client(context.getApplicationContext()),
                    context.getApplicationContext());
        }
        return sTransferUtility;
    }

    public static AmazonS3Client getS3Client(Context context) {
        if (s3Client == null) {
            s3Client = new AmazonS3Client(getCredProvider(context.getApplicationContext()));
            s3Client = new AmazonS3Client(new BasicAWSCredentials(Constants.MY_ACCESS_KEY_ID, Constants.MY_SECRET_KEY));
        }

        return s3Client;
    }

    public static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (credPro == null) {
            CognitoCachingCredentialsProvider credPro = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    "us-east-1:75c2afbc-dfba-4e27-bdc0-0d7e65027111",       //user_ID
                    US_EAST_1                                               // Region
            );
        }
        return credPro;
    }

    public static void fillMap(Map<String, Object> map, TransferObserver observer, boolean isChecked) {
        int progress = (int) ((double) observer.getBytesTransferred() * 100 / observer.getBytesTotal());

        map.put("id", observer.getId());
        map.put("checked", isChecked);
        map.put("fileName", observer.getAbsoluteFilePath());
        map.put("progree", progress);
        map.put("bytes", getBytesString(observer.getBytesTransferred()) + " /" + getBytesString(observer.getBytesTotal()));
        map.put("state", observer.getState());
        map.put("percentage", progress + "%");
    }

    public static String getBytesString(long bytes) {
        String[] quantifiers = new String[]{
                "KB", "MB", "GB", "TB"
        };
        double speedNum = bytes;
        for (int i = 0; ; i++) {
            if (i >= quantifiers.length) {
                return "";
            }
            speedNum /= 1024;

            if (speedNum < 512) {
                return String.format("%.2f", speedNum) + " " + quantifiers[i];
            }
        }
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //System.out.println("AttrMap contains: "+pair.getKey() + " = " + pair.getValue());
            Log.d(TAG, "AttrMap contains: " + pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public static String convertUsername(String string) {
        if (string != null)
            return string.split(delimiter)[0];
        else
            return string;
    }

    public static String epochToDateTimeString(double epoch) {

        Calendar calendar = Calendar.getInstance();
        long d = (long) epoch;
        calendar.setTimeInMillis(d * 1000);
        // calendar.setTimeInMillis((long) 1491252072 *1000);
        //calendar.setTimeInMillis((long) 1491252072*1000);
        //Log.d(TAG, "Day Of Month " + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

        String AMPMString = "";
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        //int year2 = calendar.YEAR;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int AMPM = calendar.get(Calendar.AM_PM);

        if (AMPM == 0) ;
        AMPMString = "AM";

        if (AMPM == 1) ;
        AMPMString = "PM";

        StringBuilder s = new StringBuilder(month + "/" + day + "/" + year + " at: " + hour + ":" + minutes + " " + AMPMString);
//        Log.d(TAG, "Year 1: " + year + " Year2: ");
        Log.d(TAG, " epochToDateTimeString():    Input: " + epoch + " Result: " + s);
        return s.toString();
    }

    public static String[] AttributeHashMapKeyToArray(HashMap<String, AttributeValue> attrMap) {
        //Accepts hashmap  and returns an array holding arrays of the keys and attributes
        String[] keyArray = new String[attrMap.size()];
        int counter = 0;
        Set<Map.Entry<String, AttributeValue>> entrySet = attrMap.entrySet();
        for (Map.Entry entry : entrySet) {
//            Log.d(TAG, "looping counter: " + counter + " total Size: " + entrySet.size());
//            Log.d(TAG, "key: " + entry.getKey() + " value: " + entry.getValue().toString());

            keyArray[counter] = entry.getKey().toString();
            if (counter < entrySet.size())
                counter++;
        }
        for(int i=0; i < keyArray.length;i++)
            Log.d(TAG, keyArray[i]);
        return keyArray;
    }

    public static AttributeValue[] AttributeHashMapValuesToArray(HashMap<String, AttributeValue> attrMap) {
        //Accepts hashmap  and returns an array holding attributeValues of the values
        AttributeValue[] attrArray = new AttributeValue[attrMap.size()];

        int counter = 0;
        Set<Map.Entry<String, AttributeValue>> entrySet = attrMap.entrySet();
        for (Map.Entry entry : entrySet) {
            attrArray[counter] = (AttributeValue) entry.getValue();
            Log.d(TAG, entry.getValue().toString());

            if (counter < entrySet.size())
                counter++;
        }

        return attrArray;
    }

    public static String parseDynamoDBResultValuesToString(String dynamoDBValue){
        dynamoDBValue = dynamoDBValue.substring(dynamoDBValue.indexOf(" ")+1);
        dynamoDBValue = dynamoDBValue.substring(0, dynamoDBValue.lastIndexOf(",")).trim();
        Log.i(TAG, "Value after parseToString: " + dynamoDBValue);
        return dynamoDBValue;
    }

    public static double parseDynamoDBResultValuesToLong(String dynamoDBValue){
        String s = dynamoDBValue;
        dynamoDBValue = dynamoDBValue.substring(dynamoDBValue.indexOf(" ")+1);
        dynamoDBValue = dynamoDBValue.substring(0, dynamoDBValue.lastIndexOf(",")).trim();
//        if(dynamoDBValue.length() > 10) {
//         //   dynamoDBValue = dynamoDBValue.substring(0, dynamoDBValue.length() - (dynamoDBValue.length() - 10));



/*            dynamoDBValue = dynamoDBValue.substring(0, 10);
//           if(dynamoDBValue.indexOf('.') >= 0) {
/               dynamoDBValue = dynamoDBValue.substring(0, dynamoDBValue.indexOf('.'));
 */
        Log.d(TAG,"dynamodbValue after parsing out of json: " +dynamoDBValue);
        Log.d(TAG, String.valueOf(Double.parseDouble(dynamoDBValue)));

        //}
        Log.i(TAG, "Before Parsing: " + s+ " Value after parseToLong: " + dynamoDBValue);
        double longVal = Double.parseDouble(dynamoDBValue.trim());
        return longVal;
    }

}
