package com.example.oliverasker.skywarnmarkii.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oliverasker on 2/17/17.
 */

public class GetAllRecordsForDayTask extends AsyncTask<Void,Void,Void> {
    private String[] reportAttributes;
    private static final String TAG = "GetAllRecordsForDayTask";
    ArrayList<SkywarnWSDBMapper> reportList = new ArrayList<>();
    SkywarnWSDBMapper testReport;
    AmazonDynamoDBClient ddb;
    Context mContext;
    private String date;

    public ICallback delegate = null;
    ArrayList<SkywarnWSDBMapper> weatherList = null;

    public void setDelegate(ICallback delegate){
        this.delegate=delegate;
    }

    public void setReportAttributesArray(String[] reportAttr){
        reportAttributes=reportAttr;
        for(String s: reportAttributes)
            Log.d(TAG, "setReportAttributesArray: " +s);
    }

    public void setDate(String Date){
        date = Date;
    }
    public void setContext(Context c){
        mContext=c;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //AmazonDynamoDBClient dynamoDB = MainActivity.clientManager.ddb(); //Use MainActivity client manager
       // DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                mContext,
                Constants.IDENTITY_POOL_ID,
                Regions.US_EAST_1);

        ddb= new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);

       // DynamoDBManager.query();


        //FINALLY WORKS
        //https://aws.amazon.com/blogs/mobile/amazon-dynamodb-on-mobile-part-4-local-secondary-indexes/
        Map keyCondition = new HashMap();

        //Create condition for hashkey
        Condition hashKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
//                .withAttributeValueList(new AttributeValue().withS("02/12/2017"));
                .withAttributeValueList(new AttributeValue().withS(date));

        keyCondition.put("DateSubmittedString", hashKeyCondition);

        // Create Rangekeycondition
        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.GT.toString())
                .withAttributeValueList(new AttributeValue().withN("0"));
        keyCondition.put("DateSubmittedEpoch", rangeKeyCondition);

        QueryRequest queryRequest = new QueryRequest()
                .withTableName("SkywarnWSDB_rev4")
                .withKeyConditions(keyCondition);

        QueryResult queryResult = ddb.query(queryRequest);
        List<Map<String, AttributeValue>> valMap = queryResult.getItems();

        for(Map item : queryResult.getItems()) {
            SkywarnWSDBMapper reportEntry = new SkywarnWSDBMapper();

            reportEntry.setDateSubmittedEpoch(Utility.parseDynamoDBResultValuesToLong(item.get("DateSubmittedEpoch").toString()));
            Log.d(TAG, "dateSubmittedEpoch(): " + Utility.parseDynamoDBResultValuesToLong(item.get("DateSubmittedEpoch").toString()));

            reportEntry.setDateSubmittedString(Utility.parseDynamoDBResultValuesToString(item.get("DateSubmittedString").toString()));
            if(item.containsKey("DateOfEvent"))
                reportEntry.setDateOfEvent(Utility.parseDynamoDBResultValuesToLong(item.get("DateOfEvent").toString()));
            else
                reportEntry.setDateOfEvent(9999);

            /// Location Attributes
            if(reportEntry.getState() != null &&  !reportEntry.getState().equals(""))
                reportEntry.setEventState(Utility.parseDynamoDBResultValuesToString(item.get("State").toString()));

            if(reportEntry.getCity() != null &&  !reportEntry.getCity().equals(""))
                reportEntry.setEventCity(Utility.parseDynamoDBResultValuesToString(item.get("City").toString()));
            Log.i(TAG, "City: " + reportEntry.getCity());

            //if(reportEntry.getStreet() != null &&  !reportEntry.getStreet().equals(""))
            reportEntry.setStreet(Utility.parseDynamoDBResultValuesToString(item.get("Street").toString()));
                Log.i(TAG, "Streettt: " + item.get("Street").toString());

            if(reportEntry.getFirstName() != null && !reportEntry.getFirstName().equals(""))
                reportEntry.setFirstName(Utility.parseDynamoDBResultValuesToString(item.get("FirstName").toString()));

            if(reportEntry.getLastName() != null && !reportEntry.getLastName().equals(""))
                reportEntry.setLastName(Utility.parseDynamoDBResultValuesToString(item.get("LastName").toString()));

            if(reportEntry.getZipCode() != null && !reportEntry.getZipCode().equals(""))
                reportEntry.setZipCode(Utility.parseDynamoDBResultValuesToString(item.get("ZipCode").toString()));

            //if(reportEntry.getUsername() != null &&  !reportEntry.getUsername().equals(""))
                reportEntry.setUsername(Utility.parseDynamoDBResultValuesToString(item.get("Username").toString()));
//            if(!item.get("Latitude").toString().equals(""))
//                reportEntry.setLatitude(Utility.parseDynamoDBResultValuesToLong((item.get("Latitude").toString())));
//            if(!item.get("Longitude").toString().equals(""))
//                reportEntry.setLongitude(Utility.parseDynamoDBResultValuesToLong(item.get("Longitdue").toString()));


            ////////// WeatherSpotter Attributes //////////

            if(item.containsKey("CallSign"))
                reportEntry.setCallSign(Utility.parseDynamoDBResultValuesToString(item.get("CallSign").toString()));
            if(item.containsKey("Affilliation"))
                reportEntry.setAffiliation(Utility.parseDynamoDBResultValuesToString(item.get("Affilliation").toString()));
            if(item.containsKey("SpotterID"))
                reportEntry.setSpotterID(Utility.parseDynamoDBResultValuesToString(item.get("SpotterID").toString()));


            //////////  Number photos/videos for s3 //////////
            if(item.containsKey("NumberOfImages"))
                reportEntry.setNumberOfImages(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("NumberOfImages").toString())));
            if(item.containsKey("NumberOfVideos"))
                reportEntry.setNumberOfVideos(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("NumberOfVideos").toString())));

            if(item.containsKey("Comments"))
                reportEntry.setComments(Utility.parseDynamoDBResultValuesToString(item.get("Comments").toString()));
            if(item.containsKey("WeatherEvent"))
                reportEntry.setWeatherEvent(Utility.parseDynamoDBResultValuesToString(item.get("WeatherEvent").toString()));

            if(item.containsKey("CurrentTemperature"))
                reportEntry.setCurrentTemperature(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("CurrentTemperature").toString())));
            if(item.containsKey("Barometer"))
                reportEntry.setBarometer(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("Barometer").toString())));

                ////////// Winter Attributes //////////
            if(item.containsKey("BlowDrift"))
                reportEntry.setBlowDrift(Utility.parseDynamoDBResultValuesToString(item.get("BlowDrift").toString()));
            if(item.containsKey("FreezingRain"))
                reportEntry.setFreezingRain(Utility.parseDynamoDBResultValuesToString(item.get("FreezingRain").toString()));
            if(item.containsKey("FreezingRainAccum"))
                reportEntry.setFreezingRainAccum(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("FreezingRainAccum").toString())));
            if(item.containsKey("SnowfallDepth"))
                reportEntry.setSnowfall(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("SnowfallDepth").toString())));
            if(item.containsKey("SnowfallRate"))
                reportEntry.setSnowfallRate(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("SnowfallRate").toString())));
            if(item.containsKey("SnowfallSleet"))
                reportEntry.setSnowFallSleet(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("SnowfallSleet").toString())));

            if(item.containsKey("ThunderSnow"))
                reportEntry.setThundersnow(Utility.parseDynamoDBResultValuesToString(item.get("ThunderSnow").toString()));

            if(item.containsKey("WaterEquivalent"))
                reportEntry.setWaterEquivalent(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("WaterEquivalent").toString())));

            if(item.containsKey("WhiteOut"))
                reportEntry.setWhiteout(Utility.parseDynamoDBResultValuesToString(item.get("WhiteOut").toString()));

            if(item.containsKey("WinterWeatherComments"))
                reportEntry.setWinterWeatherComments(Utility.parseDynamoDBResultValuesToString(item.get("WinterWeatherComments").toString()));

            ////////// Rain/Flood Attributes //////////
            if(item.containsKey("FloodComments"))
                reportEntry.setFloodComments(Utility.parseDynamoDBResultValuesToString(item.get("FloodComments").toString()));
            if(item.containsKey("PrecipRate"))
                reportEntry.setPrecipRate(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("PrecipRate").toString())));

            if(item.containsKey("Rain"))
                reportEntry.setRain(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("Rain").toString())));

            if(item.containsKey("RainEventComments"))
                reportEntry.setRainEventComments(Utility.parseDynamoDBResultValuesToString(item.get("RainEventComments").toString()));

            if(item.containsKey("StormSurge"))
                reportEntry.setStormSurge(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("StormSurge").toString())));


            ////////// Severe Attributes //////////
            if(item.containsKey("HailSize"))
                reportEntry.setHailSize(Utility.parseDynamoDBResultValuesToString(item.get("HailSize").toString()));

            if(item.containsKey("Tornado"))
                reportEntry.setTornado(Utility.parseDynamoDBResultValuesToString(item.get("Tornado").toString()));

            if(item.containsKey("WindDirection"))
                reportEntry.setWindDirection(Utility.parseDynamoDBResultValuesToString(item.get("WindDirection").toString()));
            if(item.containsKey("WindGust"))
                reportEntry.setWindGust(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("WindGust").toString())));
            if(item.containsKey("WindSpeed"))
                reportEntry.setWindSpeed(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("WindSpeed").toString())));


            //////////  Report Rating Fiels //////////
            if(item.containsKey("NetVote"))
                reportEntry.setNetVote(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("NetVote").toString())));
            if(item.containsKey("UpVote"))
                reportEntry.setUpVote(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("UpVote").toString())));
            if(item.containsKey("DownVote"))
                reportEntry.setDownVote(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("DownVote").toString())));



            ////////// Severe Attributes //////////
            if(item.containsKey("NumberOfInjuries"))
                reportEntry.setInjuries(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("NumberOfInjuries").toString())));

            if(item.containsKey("NumberOfFatalities"))
                reportEntry.setFatalities(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("NumberOfFatalities").toString())));
            if(item.containsKey("NumberOfInjuries"))
                reportEntry.setInjuryComments(Utility.parseDynamoDBResultValuesToString(item.get("NumberOfInjuries").toString()));

            reportList.add(reportEntry);
            reportEntry = null;
            //Log.d(TAG, "report: " + item.values());
            for(Object items: item.values())
                Log.d(TAG, items.toString());
        }


        int counter = 0;
        Log.d(TAG, "QueryResultSize: "+ queryResult.getCount());
//        for(Map item : queryResult.getItems()) {
//            SkywarnWSDBMapper report = new SkywarnWSDBMapper();
//            //Prints all report attributes
//            //Log.d(TAG, "All report Attributes: " + item.toString());
//            //Gets attribute values
//            //AttributeValue val = (AttributeValue)  item.get("DateSubmittedEpoch");
//            //AttributeValue val  = (AttributeValue) item.get(reportAttributes[counter]);
//            //if(((AttributeValue) item.get(reportAttributes[counter])).getS() == null) {
//          //for(int i =0; i < reportAttributes.length-1; i++){
//              //Log.i(TAG, "attribute: " + reportAttributes[i] + " report: " + item.get("DateSubmittedEpoch")+ " has: " +  item.get(reportAttributes[i]).toString());
//             // if(item.get("WaterEquivalent")!= null)
//              //Log.d(TAG)
//
//         // }
//        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        SkywarnWSDBMapper test = new SkywarnWSDBMapper();
//                test.setDateSubmittedEpoch((long)12312442);
//                test.setDateSubmittedString("2/2/2");
        delegate.processFinish(reportList);
        delegate = null;
    }


}
