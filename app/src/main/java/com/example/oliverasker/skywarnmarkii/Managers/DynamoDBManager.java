package com.example.oliverasker.skywarnmarkii.Managers;

import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.example.oliverasker.skywarnmarkii.Activites.MainActivity;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




/**
 * Created by student on 10/12/16.
 */

public class DynamoDBManager {
    private static final String TAG = "DynamoDBManager";

    static AmazonDynamoDBClient dynamoDB = MainActivity.clientManager.ddb();
    static DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
    static String tableName = "SkywarnWSDB_rev4";
   // static String tableName = "Test_Table";

    //For Queries/Scans
    static ArrayList<SkywarnWSDBMapper> resultList = new ArrayList<SkywarnWSDBMapper>();

    //For new Report Submissions
    static SkywarnWSDBMapper reportToSubmit;


    public static ArrayList<SkywarnWSDBMapper> getTableRow() {
        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb(); //Use MainActivity client manager
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        DynamoDBScanExpression scanExpr = new DynamoDBScanExpression();
            try {
                PaginatedScanList<SkywarnWSDBMapper> result = mapper.scan(SkywarnWSDBMapper.class, scanExpr);
               resultList = new ArrayList<SkywarnWSDBMapper>();
                for (SkywarnWSDBMapper db : result) {
                    resultList.add(db);
                }
                return resultList;
            } catch (AmazonServiceException ex) {
                MainActivity.clientManager.wipeCredentialsonAuthError(ex);
            }
    return resultList;
    }



    public static void insertRecord(SkywarnWSDBMapper m) {
        reportToSubmit = m;
        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb(); //Use MainActivity client manager
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        try {
            mapper.save(reportToSubmit);
        } catch (AmazonServiceException ex) {
            MainActivity.clientManager.wipeCredentialsonAuthError(ex);
            Log.d(TAG, "CREDENTIALS WIPED: " + ex);
        }
    }

    public static ArrayList<SkywarnWSDBMapper> scanDB(HashMap<String, String>... scanFilterVals){
        resultList.clear();
        //AmazonDynamoDBClient dynamoDB = MainActivity.clientManager.ddb(); //Use MainActivity client manager

        ScanRequest scanRequest;

        //  Holds all conditions for scan filter
        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        HashMap<String,String> scanFilterAttr = scanFilterVals[0];
        //  Set Parameters for scan filter
        Condition condition = new Condition();

        //Iterate through results
        Iterator it = scanFilterAttr.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            condition.withComparisonOperator(ComparisonOperator.EQ.toString());
            condition.withAttributeValueList(new AttributeValue().withS(pair.getValue().toString()));
            //  WeatherEvent is clearly attribute name in dynamodb, and condition
            scanFilter.put(pair.getKey().toString(), condition);
        }

        SkywarnWSDBMapper skyMapper = new SkywarnWSDBMapper();
        scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);

       // SkywarnWSDBMapper test = new SkywarnWSDBMapper();
        //resultList.add(test);
       // resultList.add(skyMapper);

        Log.d(TAG, "garbage: "+  scanResult.getItems().get(0).get("CurrentTemperature").getN());

        for(int i =0; i < scanResult.getCount(); i++) {
            SkywarnWSDBMapper test = new SkywarnWSDBMapper();
            HashMap<String, AttributeValue> item = (HashMap<String, AttributeValue>) scanResult.getItems().get(i);

            if (item.get("Street") != null)
                test.setStreet(item.get("Street").getS());
            if (item.get("State") != null)
                test.setEventState(item.get("State").getS());
            if (item.get("Longitude") != null)
                test.setLongitude(Long.parseLong(item.get("Longitude").getNS().toString()));
            if (item.get("Latitude") != null)
                test.setLatitude(Long.parseLong(item.get("Latitude").getS().toString()));


            if (item.get("DateSubmittedEpoch") != null){
                test.setDateSubmittedEpoch((Long.parseLong(item.get("DateSubmittedEpoch").getN())));
                Log.d(TAG, " GETDATSUBMITTEDEPOCH: " + (Long.parseLong(item.get("DateSubmittedEpoch").getN().toString())));
                //test.setDateSubmittedEpoch(Long.parseLong(item.get("DateSubmittedEpoch").getN()));

            }
            if (item.get("DateSubmittedString") != null){
                test.setDateSubmittedString((item.get("DateSubmittedString").getS().toString()));
            }

            if(item.get("DateOfEvent")!= null) {
                Log.d(TAG,"setDateOfEvent: " + item.get("DateOfEvent").getS().toString());
                test.setDateOfEvent(Long.parseLong(String.valueOf(item.get("DateOfEvent").getN())));
            }
            if(item.get("CurrentTemperature")!=null){
               // test.setCurrentTemperature(Float.parseFloat(item.get("CurrentTemperature").getN().toString()));
            }

            if(item.get("ZipCode")!=null)
                test.setZipCode(item.get("ZipCode").getS());

            if(item.get("City")!=null)
                test.setEventCity(item.get("City").getS());

            if(item.get("Comments")!=null)
                test.setComments(item.get("Comments").getS());

            resultList.add(test);

            if(skyMapper.getWeatherEvent().contains("Winter")){
            }

            if(skyMapper.getWeatherEvent() == "Rain/Flood"){
            }

            if(skyMapper.getWeatherEvent() == "Coastal Flooding"){
            }

            if(skyMapper.getWeatherEvent() == "Severe"){
            }
        }
        return resultList;
    }




    public static ArrayList<SkywarnWSDBMapper> query(){
        resultList.clear();
        AmazonDynamoDBClient dynamoDB = MainActivity.clientManager.ddb(); //Use MainActivity client manager



        //Build Query
        /*
        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.BETWEEN.toString())
                .withAttributeValueList(new AttributeValue().withS(startDate),
                        new AttributeValue().withS(endDate));

        Condition rangeKeyCondition = new Condition()
              .withAttributeValueList(new AttributeValue().withS("CT"));

        DynamoDBQueryExpression<SkywarnWSDBMapper> queryExpression = new DynamoDBQueryExpression<SkywarnWSDBMapper>()
            .withHashKeyValues()
                .withRangeKeyCondition("State")

        */


        SkywarnWSDBMapper recordToFind = new SkywarnWSDBMapper();
//        recordToFind.setDateSubmittedEpoch(23213123);
//        recordToFind.setDateSubmittedString("02/12/2017");
        String queryString = "0";

        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.GT)
                .withAttributeValueList(new AttributeValue().withN(String.valueOf(0)));

        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(recordToFind)
               // .withRangeKeyCondition("WeatherEvent", rangeKeyCondition)
                .withRangeKeyCondition("DateSubmittedEpoch", rangeKeyCondition);

        List<SkywarnWSDBMapper> result = mapper.query(SkywarnWSDBMapper.class , queryExpression);

        Iterator<SkywarnWSDBMapper> resultIterator = result.iterator();
        Log.d(TAG,"Size of query result: " + result.size());
        Log.d(TAG, "result: " + result.get(0).getWeatherEvent());
      //  while(resultIterator.hasNext())
        for(int i =0; i < result.size();i++) {
            resultList.add(result.get(i));
            //Log.d(TAG, "result: " + result.get(0).getWeatherEvent());
               // Log.d(TAG, "resultList: " + resultList.get(i).getWeatherEvent().toString() + "username: " + resultList.get(i).getUsername().toString());
            }
        return resultList;
    }

}
