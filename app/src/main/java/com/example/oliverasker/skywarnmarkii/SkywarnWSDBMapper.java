package com.example.oliverasker.skywarnmarkii;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by student on 10/16/16.
 */
@DynamoDBTable(tableName ="SkywarnWSDB_rev2")
public class SkywarnWSDBMapper implements Serializable {
    //Map these fields to database attributes
    private String UsernameEpoch;
    private String WeatherEvent="|";
    private String EventDate="10/29/2016";        //http://www.epochconverter.com/
    private String Street="";
    private String City="Wareham";
    private String State="MA";
    private String ZipCode="|";
    private String Longitude="9999";
    private String Lattitude="9999";
    private String CurrentTemperature;
    private String Comments="|";
    private String Username;
    private String rating="|";
    private String time = "10:43";

    // Severe Weather
    private String SevereType="|";
    private String WindSpeed="10 mph";
    private String WindGust="9999";
    private String WindDirection="|";
    private String HailSize="1.2cm";
    private String Tornado="|";
    private String Barometer="9999";
    private String DownedTrees="9999";
    private String DownedLimbs="9999";
    private String DiameterLimbs="9999";
    private String DownedPoles="9999";
    private String DownedWires="9999";
    private String WindDamage="|";
    private String LightningDamage="|";
    private String DamageComments="|";

    //  Winter Weather
    private  String Snowfall = "9999";
    private String SnowfallRate ="9999";
    private String SnowDepth = "9999";
    private String WaterEquivalent  = "9999";
    private String FreezingRain= "false";
    private String Sleet = "false";
    private String BlowDrift = "false";
    private String Whiteout = "false";
    private String Thundersnow = "false";


    //  Rain Weather
    private String Rain = "9999";
    private String PrecipRate="9999";
    private String RiverFlood="|";
    private String Creek_StreamFlood="|";
    private String StreetFlood="false";
    private String LargeRiverFlood ="false";
    private String IceJamFlood ="false";


    //Coastal Flooding Specific Attributes
    private String CoastalArea="9999";
    private String FirstFloorFlood = "false";
    private String StormSurge = "9999";

    //Cross Event Attributes
    private String FloodDepth;
    private String RoadWashout;
    private String FloodBasement;


    private String delimiter = "_"; //Delimiter seperate epoch and username


    ///////////////////////////////////////
    //  Constructors
    //  All Fields
    public SkywarnWSDBMapper(String usernameEpoch,
                             String longitude,
                             String lattitude,
                             String currentTemp,
                             String comments,
                             String street,
                             String state,
                             String City,
                             String zip,
                             String date,
                             String weatherEvent) {
        State = state;
        Street = street;
        ZipCode = zip;

        Lattitude = lattitude;
        Longitude = longitude;

        Comments = comments;
        CurrentTemperature = currentTemp;
        EventDate = date;
        UsernameEpoch = usernameEpoch;
        WeatherEvent = weatherEvent;
        Username = convertUsername(UsernameEpoch);
    }

    // All fields minus long/lat
    public SkywarnWSDBMapper(String usernameEpoch,
                             String currentTemp,
                             String comments,
                             String street,
                             String state,
                             String City,
                             String zip,
                             String date,
                             String weatherEvent) {
        State = state;
        Street = street;
        ZipCode = zip;
        Comments = comments;
        CurrentTemperature = currentTemp;
        EventDate = date;
        UsernameEpoch = usernameEpoch;
        WeatherEvent = weatherEvent;
        Username = convertUsername(UsernameEpoch);
    }

    public SkywarnWSDBMapper(String usernameEpoch, String state){
        UsernameEpoch=usernameEpoch;
        State = state;
    }

    //All fields without state, street, City, zip
    public SkywarnWSDBMapper(String usernameEpoch,
                             String longitude,
                             String lattitude,
                             String currentTemp,
                             String comments,
                             String date,
                             String weatherEvent) {

        Lattitude = lattitude;
        Longitude = longitude;

        Comments = comments;
        CurrentTemperature = currentTemp;
        EventDate = date;
        UsernameEpoch = usernameEpoch;
        WeatherEvent = weatherEvent;
        Username = convertUsername(UsernameEpoch);
    }


    public SkywarnWSDBMapper(String event) {
        WeatherEvent = event;
    }

    public SkywarnWSDBMapper() {

    }

    public String convertUsername(String string) {
        if(string != null)
            return string.split(delimiter)[0];
        else
            return string;
    }

    public String convertDate(String s) throws NumberFormatException{
        String date="ExampleDate";
        try {
            long epoch = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse("01/01/1970 01:00:00").getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long epochVal = 0;
        if(s != null) {
            if (s.contains("_") && s.split(delimiter).length == 2) {
                String newDate = s.split(delimiter)[1];   //Separate username and timestamp
                Integer i = Math.round(Float.parseFloat(newDate));
                epochVal = i * 1000;
                date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm").format(new java.util.Date(epochVal * 1000));
                System.out.println("Date: " + date + "    epochVal: " + epochVal);
            } else
                date = "Icorrect Input Format";
        }

        return date;
    }

    //////////////////////////////////////////////
    //  General Attributes (Common to all reports)
    //////////////////////////////////////////////

    // HASH KEY
    @DynamoDBHashKey(attributeName = "UsernameEpoch")
    public String getReportID() {
        return UsernameEpoch;
    }

    public void setReportID(String id) {
        UsernameEpoch = id;
    }


    //PRIMARY SORT KEY
    @DynamoDBRangeKey(attributeName = "WeatherEvent")
    public String getWeatherEvent() {
        return WeatherEvent;
    }

    public void setWeatherEvent(String event) {
        WeatherEvent = event;
    }


    @DynamoDBAttribute(attributeName = "City")
    public String getEventCity() {
        return City;
    }

    public void setEventCity(String t) {
        City = t;
    }

    @DynamoDBAttribute(attributeName = "Street")
    public String getStreet() {
        return Street;
    }

    public void setStreet(String s) {
        Street = s;
    }

    @DynamoDBAttribute(attributeName = "State")
    public String getEventState() {
        return State;
    }

    public void setEventState(String s) {
        State = s;
    }

    @DynamoDBAttribute(attributeName = "ZipCode")
    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String d) {
        ZipCode = d;
    }

    @DynamoDBAttribute(attributeName = "Date")
    public String getDate() {
        //return EventDate;
        return convertDate(UsernameEpoch);
    }

    public void setDate(String d) {
        EventDate = d;
    }

    @DynamoDBAttribute(attributeName = "Longitude")
    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String rep) {
        Longitude = rep;
    }

    @DynamoDBAttribute(attributeName = "Lattitude")
    public String getLattitude() {
        return Lattitude;
    }

    public void setLattitude(String rep) {
        Lattitude = rep;
    }


    @DynamoDBAttribute(attributeName = "Comments")
    public String getComments() {
        return Comments;
    }

    public void setComments(String eDescr) {
        Comments = eDescr;
    }

    @DynamoDBAttribute(attributeName = "CurrentTemperature")
    public String getCurrentTemperature() {
        return CurrentTemperature;
    }

    public void setCurrentTemperature(String temp) {
        CurrentTemperature = temp;
    }

    @DynamoDBAttribute(attributeName = "Username")
    public String getUsername() {
       System.out.println( convertUsername(getReportID()));
        return convertUsername(getReportID());
    }

    public void setUsername(String temp) {
        Username = temp;
    }

    /*
    public String getRating() {
        int min =0;
        int max =100;
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return Integer.toString(randomNum);
    }
*/



    ////////////////////////////////////////////////////
    //////      Severe Weather Attributes
    ////////////////////////////////////////////////////


     @DynamoDBAttribute(attributeName  ="HailSize")
    public void setSevereType(String severeType) {
        SevereType = severeType;
    }

    public String getSevereType() {
        return SevereType;
    }

    @DynamoDBAttribute(attributeName  = "WindSpeed")
    public String getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        WindSpeed = windSpeed;
    }

    @DynamoDBAttribute(attributeName  ="WindGust")
    public String getWindGust() {
        return WindGust;
    }

    public void setWindGust(String windGust) {
        WindGust = windGust;
    }

    @DynamoDBAttribute(attributeName  ="WindDirection")
    public String getWindDirection() {
        return WindDirection;
    }

    public void setWindDirection(String windDirection) {
        WindDirection = windDirection;
    }

    @DynamoDBAttribute(attributeName  ="HailSize")
    public String getHailSize() {
        return HailSize;
    }

    public void setHailSize(String hailSize) {
        HailSize = hailSize;
    }

    @DynamoDBAttribute(attributeName  ="Tornado")
    public String getTornado() {
        return Tornado;
    }

    public void setTornado(String tornado) {
        Tornado = tornado;
    }


    @DynamoDBAttribute(attributeName  ="Barometer")
    public String getBarometer() {
        return Barometer;
    }

    public void setBarometer(String barometer) {Barometer = barometer;}


    @DynamoDBAttribute(attributeName  ="DownedTrees")
    public String getDownedTrees() {
        return DownedTrees;
    }

    public void setDownedTrees(String downedTrees) {
        DownedTrees = downedTrees;
    }


    @DynamoDBAttribute(attributeName  ="DownedLimbs")
    public String getDownedLimbs() {
        return DownedLimbs;
    }

    public void setDownedLimbs(String downedLimbs) {
        DownedLimbs = downedLimbs;
    }



    @DynamoDBAttribute(attributeName  ="DiameterLimbs")
    public String getDiameterLimbs() {
        return DiameterLimbs;
    }

    public void setDiameterLimbs(String diameterLimbs) {
        DiameterLimbs = diameterLimbs;
    }



    @DynamoDBAttribute(attributeName  ="DownedPoles")
    public String getDownedPoles() {
        return DownedPoles;
    }

    public void setDownedPoles(String downedPoles) {
        DownedPoles = downedPoles;
    }


    @DynamoDBAttribute(attributeName  ="DownedWires")
    public String getDownedWires() {
        return DownedWires;
    }

    public void setDownedWires(String downedWires) {
        DownedWires = downedWires;
    }


    @DynamoDBAttribute(attributeName  ="WindDamage")
    public String getWindDamage() {
        return WindDamage;
    }

    public void setWindDamage(String windDamage) {
        WindDamage = windDamage;
    }


    @DynamoDBAttribute(attributeName  ="LightningDamage")
    public String getLightningDamage() {
        return LightningDamage;
    }

    public void setLightningDamage(String lightningDamage) {
        LightningDamage = lightningDamage;
    }


    @DynamoDBAttribute(attributeName  ="DamageComments")
    public String getDamageComments() {
        return DamageComments;
    }

    public void setDamageComments(String damageComments) {
        DamageComments = damageComments;
    }

    ////////////////////////////////////////////////////
    //////      Snow Weather Attributes
    ////////////////////////////////////////////////////
    @DynamoDBAttribute(attributeName = "Snowfall" )
    public String getSnowfall() {
        return Snowfall;
    }
    public void setSnowfall(String snowfall) {
        Snowfall = snowfall;
    }

    @DynamoDBAttribute(attributeName = "SnowfallRate" )
    public String getSnowfallRate() {
        return SnowfallRate;
    }

    public void setSnowfallRate(String snowfallRate) {
        SnowfallRate = snowfallRate;
    }

    @DynamoDBAttribute(attributeName = "SnowDepth" )
    public String getSnowDepth() {
        return SnowDepth;
    }

    public void setSnowDepth(String snowDepth) {
        SnowDepth = snowDepth;
    }

    @DynamoDBAttribute(attributeName = "WaterEquivalent" )
    public String getWaterEquivalent() {
        return WaterEquivalent;
    }

    public void setWaterEquivalent(String waterEquivalent) {
        WaterEquivalent = waterEquivalent;
    }

    @DynamoDBAttribute(attributeName = "FreezingRain" )
    public String getFreezingRain() {
        return FreezingRain;
    }

    public void setFreezingRain(String freezingRain) {
        FreezingRain = freezingRain;
    }

    @DynamoDBAttribute(attributeName = "Sleet" )
    public String getSleet() {
        return Sleet;
    }

    public void setSleet(String sleet) {
        Sleet = sleet;
    }


    @DynamoDBAttribute(attributeName = "BlowDrift" )
    public String getBlowDrift() {
        return BlowDrift;
    }

    public void setBlowDrift(String blowDrift) {
        BlowDrift = blowDrift;
    }

    @DynamoDBAttribute(attributeName = "Whiteout" )
    public String getWhiteout() {
        return Whiteout;
    }

    public void setWhiteout(String whiteout) {
        Whiteout = whiteout;
    }

    @DynamoDBAttribute(attributeName = "Thundersnow" )
    public String getThundersnow() {
        return Thundersnow;
    }

    public void setThundersnow(String thundersnow) {
        Thundersnow = thundersnow;
    }


    ////////////////////////////////////////////////////
    //////      Rain/Flood Weather Attributes
    ////////////////////////////////////////////////////

    @DynamoDBAttribute(attributeName = "Rain" )
    public String getRain() {
        return Rain;
    }

    public void setRain(String rain) {
        Rain = rain;
    }

    @DynamoDBAttribute(attributeName = "PrecipRate" )
    public String getPrecipRate() {
        return PrecipRate;
    }

    public void setPrecipRate(String precipRate) {
        PrecipRate = precipRate;
    }

    @DynamoDBAttribute(attributeName = "RiverFlood" )
    public String getRiverFlood() {
        return RiverFlood;
    }

    public void setRiverFlood(String riverFlood) {
        RiverFlood = riverFlood;
    }

    @DynamoDBAttribute(attributeName = "Creek_StreamFlood" )
    public String getCreek_StreamFlood() {
        return Creek_StreamFlood;
    }

    public void setCreek_StreamFlood(String creek_StreamFlood) {
        Creek_StreamFlood = creek_StreamFlood;
    }

    @DynamoDBAttribute(attributeName = "StreetFlood" )
    public String getStreetFlood() {
        return StreetFlood;
    }

    public void setStreetFlood(String streetFlood) {
        StreetFlood = streetFlood;
    }

    @DynamoDBAttribute(attributeName = "LargeRiverFlood" )
    public String getLargeRiverFlood() {
        return LargeRiverFlood;
    }

    public void setLargeRiverFlood(String largeRiverFlood) {
        LargeRiverFlood = largeRiverFlood;
    }

    @DynamoDBAttribute(attributeName = "IceJamFlood" )
    public String getIceJamFlood() {
        return IceJamFlood;
    }

    public void setIceJamFlood(String iceJamFlood) {
        IceJamFlood = iceJamFlood;
    }
    @DynamoDBAttribute(attributeName = "CoastalArea" )
    public String getCoastalArea() {
        return CoastalArea;
    }

    public void setCoastalArea(String coastalArea) {
        CoastalArea = coastalArea;
    }

    @DynamoDBAttribute(attributeName = "FirstFloorFlood" )
    public String getFirstFloorFlood() {
        return FirstFloorFlood;
    }

    public void setFirstFloorFlood(String firstFloorFlood) {
        FirstFloorFlood = firstFloorFlood;
    }

    @DynamoDBAttribute(attributeName = "StormSurge" )
    public String getStormSurge() {
        return StormSurge;
    }

    public void setStormSurge(String stormSurge) {
        StormSurge = stormSurge;
    }

    @DynamoDBAttribute(attributeName = "FloodDepth" )
    public String getFloodDepth() {
        return FloodDepth;
    }

    public void setFloodDepth(String floodDepth) {
        FloodDepth = floodDepth;
    }

    @DynamoDBAttribute(attributeName = "RoadWashout" )
    public String getRoadWashout() {
        return RoadWashout;
    }

    public void setRoadWashout(String roadWashout) {
        RoadWashout = roadWashout;
    }
    @DynamoDBAttribute(attributeName = "FloodBasement" )
    public String getFloodBasement() {
        return FloodBasement;
    }

    public void setFloodBasement(String floodBasement) {
        FloodBasement = floodBasement;
    }

    @DynamoDBAttribute(attributeName = "Rating")
    public String getRating(){
        return rating;
    }
    public void setRating(String rate){
        rating = rate;
    }

    @DynamoDBAttribute(attributeName ="Time")
    public String getTime(){return time;}

    public void setTime(String temp) {
        time = temp;
    }
}

