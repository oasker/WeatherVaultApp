package com.example.oliverasker.skywarnmarkii.Mappers;



import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;

import java.io.Serializable;

//import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by student on 10/16/16.
 */
 //@DynamoDBTable(tableName = "Test_Table")
//@DynamoDBTable(tableName = Constants.REPORTS_TABLE_NAME)

@com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable(tableName = "SkywarnWSDB_rev4")
public class SkywarnWSDBMapper implements Serializable {
    private static final String TAG = "SkywarnWSDBMapper";
    int NetVote = 0;
//    private long DateSubmittedEpoch =758698787;        //Sort Key
//   // private String DateSubmittedEpoch ="34343434";        //Sort Key
//    private String DateSubmittedString = "2/15/2017";     // Primary Key
//Map these fields to database attributes
//http://www.epochconverter.com/
private double DateOfEvent;
    private String Affiliation;
    private String CallSign;
    private String SpotterID;
    private String FirstName;
    private String LastName;
    //Number of photos/videos associated with S3
    private int NumberOfImages=0;
    private int NumberOfVideos=0;
    private String Comments="|";
    private String Username;
    private String rating="|";
    private String time = "|";
    private String WeatherEvent="|";
    // Location Details
    private String Street="|";
    private String City="|";
    private String State="|";
    private String ZipCode="|";
    private double Longitude=9999;


    ///////////////////////////////////////////////////////////////
    //                  Severe Weather                          //
    //////////////////////////////////////////////////////////////
    private double Latitude = 9999;
    //Drop down with following options
        //  Non- Tstorm
        //  Tstorm
        //  Tropical Storm
        //  Hurricane
    private String SevereType="|";
    private float WindSpeed= 9999;
    private float WindGust=9999;
    private String WindDirection="|";
    // Only appears when Tstorm, Tropical Storm or Hurricane
    private String HailSize= "9999";
    private String Tornado="|";
    private float Barometer = 9999;
    private String WindDamage="|";
    private String LightningDamage="|";
    private String DamageComments="|";
    private String SevereInjuries = "|";
    private String SevereFatalities="|";
    private String LightningDamageComments="|";
    private String WindDamageComments="|";
    private String SevereWeatherComments = "|";
    ///////////////////////////////////////////////////////////////
    //                  Winter Weather                          //
    //////////////////////////////////////////////////////////////
    private float Snowfall = 9999;
    private float SnowfallRate = 9999;
    private float SnowDepth = 9999;
    private float WaterEquivalent = 9999;
    private String FreezingRain= "false";
    private float FreezingRainAccum = 9999;
    private float SnowFallSleet = 9999;
    private String BlowDrift = "false";
    private String Whiteout = "false";
    private String Thundersnow = "false";
    private String WinterWeatherComments="|";
    private int WinterWeatherInjuries;
    private int WinterWeatherFatalities;
    ///////////////////////////////////////////////////////////////
    //                  Rain Weather                          //
    //////////////////////////////////////////////////////////////
    private float Rain = 9999;
    private float PrecipRate= 9999;
    private String FloodComments="|";
    private String RainEventComments="|";
    private int RainEventFatalities;
    private int RainEventInjuries;
    ///////////////////////////////////////////////////////////////
    //                      Costal Weather                       //
    //////////////////////////////////////////////////////////////
    //Coastal Flooding Specific Attributes
    private float StormSurge = 9999;
    private String CoastalEventComments="|";
    private int CoastalEventFatalities;



    ///////////////////////////////////////////////////////////////
    //                      Report Rating                       //
    //////////////////////////////////////////////////////////////
    private int CoastalEventInjuries;
    private int UpVote =0 ;
    private int DownVote = 0;
    ////////////////////////////////////////////////////////////////////////////////
     ///                             Constructors                                ///
    //////////////////////////////////////////////////////////////////////////////
    private double DateSubmittedEpoch;        //Sort Key
    //private String DateSubmittedEpoch ="34343434";        //Sort Key
    private String DateSubmittedString;     // Primary Key
    private float CurrentTemperature;
    //  All Fields
    public SkywarnWSDBMapper(String username,
                             long longitude,
                             long lattitude,
                             float currentTemp,
                             String comments,
                             String street,
                             String state,
                             String city,
                             String zip,
                             long date,
                             String weatherEvent) {
        State = state;
        Street = street;
        ZipCode = zip;
        City = city;

        Latitude = lattitude;
        Longitude = longitude;

        Comments = comments;
        //CurrentTemperature = currentTemp;
        DateOfEvent = date;
        Username = username;
        WeatherEvent = weatherEvent;
        Username = Utility.convertUsername(Username);
    }
    // All fields minus long/lat
    public SkywarnWSDBMapper(String Username,
                             float currentTemp,
                             String comments,
                             String street,
                             String state,
                             String City,
                             String zip,
                             long date,
                             String weatherEvent) {
        State = state;
        Street = street;
        ZipCode = zip;
        Comments = comments;
       // CurrentTemperature = currentTemp;
        DateOfEvent = date;
        WeatherEvent = weatherEvent;
        Username = UserInformationModel.getInstance().getUsername();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    /////                     General Attributes (Common to all reports)                    ////
    ////////////////////////////////////////////////////////////////////////////////////////////


    //All fields without state, street, City, zip
    public SkywarnWSDBMapper(
                             long longitude,
                             long lattitude,
                             float currentTemp,
                             String comments,
                             long date,
                             String weatherEvent) {

        Latitude = lattitude;
        Longitude = longitude;
        Comments = comments;
       // CurrentTemperature = currentTemp;
        DateOfEvent = date;
        WeatherEvent = weatherEvent;
        Username = UserInformationModel.getInstance().getUsername();

    }
    public SkywarnWSDBMapper(String event) {
        WeatherEvent = event;
    }

    public SkywarnWSDBMapper() {

    }

    //PRIMARY PARTITION KEY
//    @DynamoDBHashKey(attributeName = "DateSubmittedString")
    //@DynamoDBHashKe
    // com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
    @com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey(attributeName = "DateSubmittedString")
    public String getDateSubmittedString() {
        return DateSubmittedString;
    }

    public void setDateSubmittedString(String dateSubmittedString) {
        DateSubmittedString = dateSubmittedString;
    }

    //PRIMARY SORT KEY
    @com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey(attributeName = "DateSubmittedEpoch")
    public double getDateSubmittedEpoch() {
        return DateSubmittedEpoch;
    }


    ////////////////////////////////////////////////////
    //// ******Secondary Indexes go here **********////
    ///////////////////////////////////////////////////

    public void setDateSubmittedEpoch(double dateSubmittedEpoch) {
        DateSubmittedEpoch = dateSubmittedEpoch;
    }

    @DynamoDBAttribute(attributeName = "DateOfEvent")
    public double getDateOfEvent() {
        return DateOfEvent;
    }

    public void setDateOfEvent(double s){
        DateOfEvent = s;
    }

    @DynamoDBIndexHashKey(attributeName = "Username")
    public String getUsername(){return Username;}

    public void setUsername(String un){Username = un;}

    @DynamoDBAttribute(attributeName = "CurrentTemperature")
    public float getCurrentTemperature() {
        return CurrentTemperature;
    }
    public void setCurrentTemperature(float temp) {
        CurrentTemperature = temp;
    }


    @DynamoDBAttribute(attributeName = "WeatherEvent")
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

    @DynamoDBAttribute(attributeName = "Longitude")
    public double getLongitude() {
        return Longitude;
    }
    public void setLongitude(double rep) {
        Longitude = rep;
    }

    @DynamoDBAttribute(attributeName = "Latitude")
    public double getLatitude() {
        return Latitude;
    }
    public void setLatitude(double rep) {
        Latitude = rep;

    }

    @DynamoDBAttribute(attributeName = "Comments")
    public String getComments() {
        return Comments;
    }
    public void setComments(String eDescr) {
        Comments = eDescr;
    }



    ////////////////////////////////////////////////////////////
    //////      Severe Weather Attributes               ///////
    //////////////////////////////////////////////////////////

     @DynamoDBAttribute(attributeName  ="SevereType")
    public void setHailSize(float severeType) {
        severeType = severeType;
    }
    public String getSevereType() {
        return SevereType;
    }

    public void setSevereType(String severeType) {
        SevereType = severeType;
    }

    @DynamoDBAttribute(attributeName  = "WindSpeed")
    public float getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        WindSpeed = windSpeed;
    }

    @DynamoDBAttribute(attributeName  ="WindGust")
    public float getWindGust() {
        return WindGust;
    }

    public void setWindGust(float windGust) {
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
    public float getBarometer() {
        return Barometer;
    }

    public void setBarometer(float barometer) {Barometer = barometer;}

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

    /////////////////////////////////////////////////////////
    ////////      Snow Weather Attributes           ////////
    /////////////////////////////////////////////////////////
    @DynamoDBAttribute(attributeName = "Snowfall" )
    public float getSnowfall() {
        return Snowfall;
    }

    public void setSnowfall(float snowfall) {
        Snowfall = snowfall;
    }

    @DynamoDBAttribute(attributeName = "SnowfallRate" )
    public float getSnowfallRate() {
        return SnowfallRate;
    }

    public void setSnowfallRate(float snowfallRate) {
        SnowfallRate = snowfallRate;
    }

    @DynamoDBAttribute(attributeName = "SnowDepth" )
    public float getSnowDepth() {
        return SnowDepth;
    }

    public void setSnowDepth(float snowDepth) {
        SnowDepth = snowDepth;
    }

    @DynamoDBAttribute(attributeName = "WaterEquivalent" )
    public float getWaterEquivalent() {
        return WaterEquivalent;
    }

    public void setWaterEquivalent(float waterEquivalent) {
        WaterEquivalent = waterEquivalent;
    }

    @DynamoDBAttribute(attributeName = "FreezingRain" )
    public String getFreezingRain() {
        return FreezingRain;
    }

    public void setFreezingRain(String freezingRain) {
        FreezingRain = freezingRain;
    }

    @DynamoDBAttribute(attributeName = "SnowfallRate")
    public float getSnowFallSleet() {
        return SnowFallSleet;
    }

    public void setSnowFallSleet(float snowFallSleet) {
        SnowFallSleet = snowFallSleet;
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

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getSevereInjuries() {
        return SevereInjuries;
    }

    public void setSevereInjuries(String severeInjuries) {
        SevereInjuries = severeInjuries;
    }

    public String getSevereFatalities() {
        return SevereFatalities;
    }

    public void setSevereFatalities(String severeFatalities) {
        SevereFatalities = severeFatalities;
    }

    public String getFloodComments() {
        return FloodComments;
    }

    public void setFloodComments(String floodComments) {
        FloodComments = floodComments;
    }

    public String getRainEventComments() {
        return RainEventComments;
    }

    public void setRainEventComments(String rainEventComments) {
        RainEventComments = rainEventComments;
    }

    public int getRainEventFatalities() {
        return RainEventFatalities;
    }

    public void setRainEventFatalities(int rainEventFatalities) {
        RainEventFatalities = rainEventFatalities;
    }

    public int getRainEventInjuries() {
        return RainEventInjuries;
    }

    public void setRainEventInjuries(int rainEventInjuries) {
        RainEventInjuries = rainEventInjuries;
    }

    public String getCoastalEventComments() {
        return CoastalEventComments;
    }

    public void setCoastalEventComments(String coastalEventComments) {
        CoastalEventComments = coastalEventComments;
    }

    public int getCoastalEventFatalities() {
        return CoastalEventFatalities;
    }

    public void setCoastalEventFatalities(int coastalEventFatalities) {
        CoastalEventFatalities = coastalEventFatalities;
    }

    public int getCoastalEventInjuries() {
        return CoastalEventInjuries;
    }

    public void setCoastalEventInjuries(int coastalEventInjuries) {
        CoastalEventInjuries = coastalEventInjuries;
    }

    //////////////////////////////////////////////////////////
    //////      Rain/Flood Weather Attributes       /////////
    /////////////////////////////////////////////////////////

    @DynamoDBAttribute(attributeName = "Rain" )
    public float getRain() {
        return Rain;
    }
    public void setRain(float rain) {
        Rain = rain;
    }


    @DynamoDBAttribute(attributeName = "PrecipRate" )
    public float getPrecipRate() {
        return PrecipRate;
    }
    public void setPrecipRate(float precipRate) {
        PrecipRate = precipRate;
    }


    @DynamoDBAttribute(attributeName = "StormSurge" )
    public float getStormSurge() {
        return StormSurge;
    }
    public void setStormSurge(float stormSurge) {
        StormSurge = stormSurge;
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


    // Tells number of photos/videos is linked to report in S3
    @DynamoDBAttribute(attributeName ="NumberOfImages")
    public int getNumberOfImages() {
        return NumberOfImages;
    }
    public void setNumberOfImages(int numberOfImages) {
        NumberOfImages = numberOfImages;
    }

    @DynamoDBAttribute(attributeName ="NumberOfVideos")
    public int getNumberOfVideos() {
        return NumberOfVideos;
    }
    public void setNumberOfVideos(int numberOfVideos) {
        NumberOfVideos = numberOfVideos;
    }

    
    @DynamoDBAttribute(attributeName ="City")
    public String getCity() {
        return City;
    }
    public void setCity(String city) {
        City = city;
    }

    
    @DynamoDBAttribute(attributeName ="State")
    public String getState() {
        return State;
    }
    public void setState(String state) {
        State = state;
    }


    @DynamoDBAttribute(attributeName = "Affiliation")
    public String getAffiliation() {
        return Affiliation;
    }
    public void setAffiliation(String affiliation) {
        Affiliation = affiliation;
    }


    @DynamoDBAttribute(attributeName ="CallSign")
    public String getCallSign() {
        return CallSign;
    }
    public void setCallSign(String callSign) {
        CallSign = callSign;
    }


    @DynamoDBAttribute(attributeName ="SpotterID")
    public String getSpotterID() {
        return SpotterID;
    }
    public void setSpotterID(String spotterID) {
        SpotterID = spotterID;
    }



    @DynamoDBAttribute(attributeName = "FreezingRainAccum")
    public float getFreezingRainAccum() {
        return FreezingRainAccum;
    }
    public void setFreezingRainAccum(float freezingRainAccum) {
        FreezingRainAccum = freezingRainAccum;
    }

    @DynamoDBAttribute(attributeName = "WinterWeatherComments")
    public String getWinterWeatherComments() {
        return WinterWeatherComments;
    }
    public void setWinterWeatherComments(String winterWeatherComments) {
        WinterWeatherComments = winterWeatherComments;
    }


    @DynamoDBAttribute(attributeName = "UpVote")
    public int getUpVote() {
        return UpVote;
    }

    public void setUpVote(int upVote) {
        UpVote = upVote;
    }

    @DynamoDBAttribute(attributeName = "DownVote")
    public int getDownVote() {
        return DownVote;
    }

    public void setDownVote(int downVote) {
        DownVote = downVote;
    }
    @DynamoDBAttribute(attributeName = "NetVote")
    public int getNetVote() {
        return NetVote;
    }

    public void setNetVote(int netVote) {
        NetVote = netVote;
    }

    public String getLightningDamageComments() {
        return LightningDamageComments;
    }

    public void setLightningDamageComments(String lightningDamageComments) {
        LightningDamageComments = lightningDamageComments;
    }

    public String getWindDamageComments() {
        return WindDamageComments;
    }

    public void setWindDamageComments(String windDamageComments) {
        WindDamageComments = windDamageComments;
    }

    public String getSevereWeatherComments() {
        return SevereWeatherComments;
    }

    public void setSevereWeatherComments(String severeWeatherComments) {
        SevereWeatherComments = severeWeatherComments;
    }

    public int getWinterWeatherInjuries() {
        return WinterWeatherInjuries;
    }

    public void setWinterWeatherInjuries(int winterWeatherInjuries) {
        WinterWeatherInjuries = winterWeatherInjuries;
    }

    public int getWinterWeatherFatalities() {
        return WinterWeatherFatalities;
    }

    public void setWinterWeatherFatalities(int winterWeatherFatalities) {
        WinterWeatherFatalities = winterWeatherFatalities;
    }
}

