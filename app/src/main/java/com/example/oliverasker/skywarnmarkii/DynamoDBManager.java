package com.example.oliverasker.skywarnmarkii;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;


import com.amazonaws.AmazonServiceException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by student on 10/12/16.
 */

public class DynamoDBManager {
    private static final String TAG = "DynamoDBManager";
    static ArrayList<SkywarnWSDBMapper> resultList = new ArrayList<SkywarnWSDBMapper>();
    static SkywarnWSDBMapper reportToSubmit;
    static String tableName = "SkywarnWSDB_rev2";

    public static ArrayList<SkywarnWSDBMapper> getTableRow() {
        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb(); //Use MainActivity client manager
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        DynamoDBScanExpression scanExpr = new DynamoDBScanExpression();

            try {
                PaginatedScanList<SkywarnWSDBMapper> result = mapper.scan(SkywarnWSDBMapper.class, scanExpr);
                ArrayList<SkywarnWSDBMapper>resultList = new ArrayList<SkywarnWSDBMapper>();
                for (SkywarnWSDBMapper db : result) {
                    resultList.add(db);
                    //System.out.println(db.getReporter());
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
            System.out.println("CREDENTIALSWIPED");
        }
    }


    public static ArrayList<SkywarnWSDBMapper> scanDB(HashMap<String, String>... scanFilterVals){
        AmazonDynamoDBClient dynamoDB = MainActivity.clientManager.ddb(); //Use MainActivity client manager
        String keyword = scanFilterVals[0].get("ThunderSnow");
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
            System.out.println("Pairs: " + pair.getKey().toString());
            condition.withComparisonOperator(ComparisonOperator.EQ.toString());
            condition.withAttributeValueList(new AttributeValue().withS(pair.getValue().toString()));

            //  WeatherEvent is clearly attribute name in dynamodb, and condition
            scanFilter.put(pair.getKey().toString(), condition);
        }
        scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);
        System.out.println("Size of scan result: " + scanResult.getCount());

        for(int i =0; i < scanResult.getCount(); i++){
            HashMap<String, AttributeValue> item = (HashMap<String,AttributeValue>) scanResult.getItems().get(i);
            SkywarnWSDBMapper skyMapper = new SkywarnWSDBMapper();
            System.out.println("ScanResult: " + item.get("UsernameEpoch") + " WeatherEventType: " +item.get("WeatherEvent"));

            //Check for Weather Specific Event to determine which values to add to
            //  1. Check if val is string/number
            //  2. Check if vall is default val
            //      if default val don't add to list
            //  3. Add attribute to class object
            //  4. Add class object to master list

            skyMapper.setEventCity(item.get("UsernameEpoch").toString());
            //skyMapper.setStreet(item.get("Street").toString());
            System.out.println(skyMapper.getEventCity());
            resultList.add(skyMapper);
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

    private boolean isDefaultValue(String o){
        if(o == "|" | o== "9999" | o=="false" | o=="")
            return false;
        else
            return true;
    }
}
