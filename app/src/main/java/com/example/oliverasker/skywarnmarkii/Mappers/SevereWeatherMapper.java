package com.example.oliverasker.skywarnmarkii.Mappers;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;

import java.util.HashMap;

/**
 * Created by Oliver Asker on 10/25/16.
 */

//@DynamoDBTable(tableName =Constants.REPORTS_TABLE_NAME)
//@DynamoDBTable(tableName ="Test_Table")

public class SevereWeatherMapper {
    String SevereType;
    String WindSpeed;
    String WindGust;
    String WindDirection;
    String HailSize;
    String Tornado;
    String Barometer;
    String DownedTrees;
    String DownedLimbs;
    String DiameterLimbs;
    String DownedPoles;
    String DownedWires;
    String WindDamage;
    String LightningDamage;
    String DamageComments;
    String rating;
    ///////////////////////////////////////
    /////// Constructors
    //////////////////////////////////////
    public SevereWeatherMapper(HashMap<String, String> m){
        if (m.containsKey("SevereType"))
            WindGust = m.get("SevereType");

        if (m.containsKey("WindGust"))
            WindGust = m.get("WindGust");

        if (m.containsKey("WindDirection"))
            WindGust = m.get("WindDirection");

        if (m.containsKey("Tornado"))
             Tornado= m.get("Tornado");

        if (m.containsKey("DownedTrees"))
             DownedTrees= m.get("DownedTrees");

        if (m.containsKey("DownedLimbs"))
             DownedLimbs= m.get("DownedLimbs");

        if (m.containsKey("DiameterLimbs"))
             DiameterLimbs= m.get("DiameterLimbs");

        if (m.containsKey("DownedPoles"))
             DownedPoles= m.get("DownedPols");

        if (m.containsKey("DownedWires"))
            DownedWires = m.get("DownedWires");

        if (m.containsKey("WindDamage"))
            WindDamage= m.get("WindDamage");

        if (m.containsKey("LightningDamage"))
            LightningDamage = m.get("LighntingDamage");

        if (m.containsKey("DamageComments"))
            DamageComments= m.get("DamageComments");

    }
    //////////////////////////////////////
    //////////////////////////////////////

    public String getSevereType() {
        return SevereType;
    }

    @DynamoDBAttribute(attributeName  ="HailSize")
    public void setSevereType(String severeType) {
        SevereType = severeType;
    }

    @DynamoDBAttribute(attributeName  = "WindSpeed")
    public String getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        WindSpeed = windSpeed;
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



    @DynamoDBAttribute(attributeName  ="LighningDamage")
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

    @DynamoDBAttribute(attributeName = "Rating")
    public String getRating(){
        return rating;
    }
    public void setRating(String rate){
        rating = rate;
    }
}



