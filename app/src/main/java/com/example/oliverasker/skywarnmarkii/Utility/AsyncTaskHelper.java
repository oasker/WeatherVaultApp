package com.example.oliverasker.skywarnmarkii.Utility;

import android.util.Log;

import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oliverasker on 4/30/17.
 * This class holds methods async will be passed to asynctasks
 */

public class AsyncTaskHelper {
    private static final String TAG = "AsyncTaskHelper";
    private ArrayList<SkywarnWSDBMapper> reportList;

    public ArrayList<SkywarnWSDBMapper> addReportsToMap(QueryResult queryResult) {
        reportList = new ArrayList();
        Log.i(TAG, "NUMBER RETURNED REPORTS: " + queryResult.getCount().toString());
        for (Map item : queryResult.getItems()) {
            SkywarnWSDBMapper reportEntry = new SkywarnWSDBMapper();
            if (item.containsKey("DateSubmittedEpoch"))
                reportEntry.setDateSubmittedEpoch(Utility.parseDynamoDBResultValuesToLong(item.get("DateSubmittedEpoch").toString()));
            if (item.containsKey("DateSubmittedString"))
                reportEntry.setDateSubmittedString(Utility.parseDynamoDBResultValuesToString(item.get("DateSubmittedString").toString()));
            if (item.containsKey("DateOfEvent"))
                reportEntry.setDateOfEvent(Utility.parseDynamoDBResultValuesToLong(item.get("DateOfEvent").toString()));

            /// Location Attributes
            if (item.containsKey("State"))
                reportEntry.setEventState(Utility.parseDynamoDBResultValuesToString(item.get("State").toString()));
            if (item.containsKey("City"))
                reportEntry.setEventCity(Utility.parseDynamoDBResultValuesToString(item.get("City").toString()));
            if (item.containsKey("Street"))
                reportEntry.setStreet(Utility.parseDynamoDBResultValuesToString(item.get("Street").toString()));
            if (item.containsKey("FirstName"))
                reportEntry.setFirstName(Utility.parseDynamoDBResultValuesToString(item.get("FirstName").toString()));
            if (item.containsKey("LastName"))
                reportEntry.setLastName(Utility.parseDynamoDBResultValuesToString(item.get("LastName").toString()));
            if (item.containsKey("ZipCode"))
                reportEntry.setZipCode(Utility.parseDynamoDBResultValuesToString(item.get("ZipCode").toString()));

            if (item.containsKey("Username"))
                reportEntry.setUsername(Utility.parseDynamoDBResultValuesToString(item.get("Username").toString()));
            ////////// WeatherSpotter Attributes //////////

            if (item.containsKey("CallSign"))
                reportEntry.setCallSign(Utility.parseDynamoDBResultValuesToString(item.get("CallSign").toString()));
            if (item.containsKey("Affilliation"))
                reportEntry.setAffiliation(Utility.parseDynamoDBResultValuesToString(item.get("Affilliation").toString()));
            if (item.containsKey("SpotterID"))
                reportEntry.setSpotterID(Utility.parseDynamoDBResultValuesToString(item.get("SpotterID").toString()));

            //////////  Report Rating Fiels //////////
            if (item.containsKey("NetVote")) {
                reportEntry.setNetVote(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("NetVote").toString())));
                Log.d(TAG, "NetVOTE: " + item.get("NetVote").toString());
            }
            if (item.containsKey("UpVote"))
                reportEntry.setUpVote(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("UpVote").toString())));
            if (item.containsKey("DownVote"))
                reportEntry.setDownVote(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("DownVote").toString())));


//            Todo: make sure all fields are entered and correct
//            LightningDamace, comments etc..


            //////////  Number photos/videos for s3 //////////
            if (item.containsKey("NumberOfImages"))
                reportEntry.setNumberOfImages(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("NumberOfImages").toString())));
            if (item.containsKey("NumberOfVideos"))
                reportEntry.setNumberOfVideos(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("NumberOfVideos").toString())));

            if (item.containsKey("Comments"))
                reportEntry.setComments(Utility.parseDynamoDBResultValuesToString(item.get("Comments").toString()));
            if (item.containsKey("WeatherEvent"))
                reportEntry.setWeatherEvent(Utility.parseDynamoDBResultValuesToString(item.get("WeatherEvent").toString()));

            if (item.containsKey("CurrentTemperature"))
                reportEntry.setCurrentTemperature(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("CurrentTemperature").toString())));
            if (item.containsKey("Barometer"))
                reportEntry.setBarometer(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("Barometer").toString())));

            ////////// Winter Attributes //////////
            if (item.containsKey("BlowDrift"))
                reportEntry.setBlowDrift(Utility.parseDynamoDBResultValuesToString(item.get("BlowDrift").toString()));
            if (item.containsKey("FreezingRain"))
                reportEntry.setFreezingRain(Utility.parseDynamoDBResultValuesToString(item.get("FreezingRain").toString()));
            if (item.containsKey("FreezingRainAccum"))
                reportEntry.setFreezingRainAccum(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("FreezingRainAccum").toString())));
            if (item.containsKey("SnowfallDepth"))
                reportEntry.setSnowfall(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("SnowfallDepth").toString())));
            if (item.containsKey("SnowfallRate"))
                reportEntry.setSnowfallRate(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("SnowfallRate").toString())));
            if (item.containsKey("SnowfallSleet"))
                reportEntry.setSnowFallSleet(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("SnowfallSleet").toString())));

            if (item.containsKey("ThunderSnow"))
                reportEntry.setThundersnow(Utility.parseDynamoDBResultValuesToString(item.get("ThunderSnow").toString()));

            if (item.containsKey("WaterEquivalent"))
                reportEntry.setWaterEquivalent(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("WaterEquivalent").toString())));

            if (item.containsKey("Whiteout"))
                reportEntry.setWhiteout(Utility.parseDynamoDBResultValuesToString(item.get("Whiteout").toString()));

            if (item.containsKey("WinterWeatherComments"))
                reportEntry.setWinterWeatherComments(Utility.parseDynamoDBResultValuesToString(item.get("WinterWeatherComments").toString()));

            ////////// Rain/Flood Attributes //////////
            if (item.containsKey("FloodComments"))
                reportEntry.setFloodComments(Utility.parseDynamoDBResultValuesToString(item.get("FloodComments").toString()));
            if (item.containsKey("PrecipRate"))
                reportEntry.setPrecipRate(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("PrecipRate").toString())));

            if (item.containsKey("Rain"))
                reportEntry.setRain(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("Rain").toString())));

            if (item.containsKey("RainEventComments"))
                reportEntry.setRainEventComments(Utility.parseDynamoDBResultValuesToString(item.get("RainEventComments").toString()));

            if (item.containsKey("StormSurge"))
                reportEntry.setStormSurge(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("StormSurge").toString())));


            ////////// Severe Attributes //////////
            if (item.containsKey("HailSize"))
                reportEntry.setHailSize(Utility.parseDynamoDBResultValuesToString(item.get("HailSize").toString()));

            if (item.containsKey("Tornado"))
                reportEntry.setTornado(Utility.parseDynamoDBResultValuesToString(item.get("Tornado").toString()));

            if (item.containsKey("WindDirection"))
                reportEntry.setWindDirection(Utility.parseDynamoDBResultValuesToString(item.get("WindDirection").toString()));
            if (item.containsKey("WindGust"))
                reportEntry.setWindGust(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("WindGust").toString())));
            if (item.containsKey("WindSpeed"))
                reportEntry.setWindSpeed(Float.parseFloat(Utility.parseDynamoDBResultValuesToString(item.get("WindSpeed").toString())));

            if (item.containsKey("WindDamageComments")) {
                reportEntry.setWindDamageComments((Utility.parseDynamoDBResultValuesToString(item.get("WindDamageComments").toString())));
                Log.d(TAG, "WindDamageComments: " + item.get("WindDamageComments"));
            }
            ////////// Severe Attributes //////////
            if (item.containsKey("NumberOfInjuries"))
                reportEntry.setCoastalEventInjuries(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("NumberOfInjuries").toString())));

            if (item.containsKey("NumberOfFatalities"))
                reportEntry.setCoastalEventFatalities(Integer.parseInt(Utility.parseDynamoDBResultValuesToString(item.get("NumberOfFatalities").toString())));
            if (item.containsKey("CoastalEventComments"))
                reportEntry.setCoastalEventComments(Utility.parseDynamoDBResultValuesToString(item.get("CoastalEventComments").toString()));

//            Position
            if (item.containsKey("Longitude"))
                reportEntry.setLongitude(Utility.parseDynamoDBResultValuesToLong(item.get("Longitude").toString()));
            if (item.containsKey("Latitude"))
                reportEntry.setLatitude(Utility.parseDynamoDBResultValuesToLong(item.get("Latitude").toString()));


            Log.d(TAG, "adding report with epoch to list: " + String.valueOf(reportEntry.getDateSubmittedEpoch()));
            reportList.add(reportEntry);
//            reportEntry =null;
//            for(Object items: item.keySet())
//                Log.d(TAG, items.toString());
        }
        return reportList;
    }
}
