package com.example.oliverasker.skywarnmarkii;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Created by student on 10/22/16.
 *  Dynamically set layout using information entered by user on previous screen.
 *  Retreive 'extras'passed within Intent as Boolean Hashmap.
 */

public class SubmitReportDetails extends AppCompatActivity{
    WinterSubmitReportFragment winterFrag;
    CoastalFloodingSubmitReportFragment coastalFloodingFrag;
    RainFloodSubmitReportFragment rainFrag;
    SevereWeatherSubmitReportFragment severeFrag;
    GeneralSubmitReportFragment generalInfoFrag;

    //Rain
    ArrayList<EditText> rainList;
    EditText Rain;
    EditText PrecipRate;
    EditText RiverFlood;
    EditText Creek_StreamFlood;
    EditText StreetFlood;
    EditText LargeRiverFlood;
    EditText IceJamFlood;

    //Severe
    ArrayList<EditText> severeList;
    EditText SevereType;
    EditText WindSpeed;
    EditText WindGust;
    EditText WindDirection;
    EditText HailSize;
    EditText Tornado;
    EditText Barometer;
    EditText DownedTrees;
    EditText DownedLimbs;
    EditText DiameterLimbs;
    EditText DownedPoles;
    EditText DownedWires;
    EditText WindDamage;
    EditText LightningDamage;
    EditText DamageComments;

    //Winter
    ArrayList<EditText> winterList;
    EditText Snowfall;
    EditText SnowfallRate;
    EditText SnowDepth;
    EditText WaterEquivalent;
    EditText FreezingRain;
    EditText Sleet;
    EditText BlowDrift;
    EditText Whiteout;
    EditText Thundersnow;

    //Coastal
    ArrayList<EditText> coastalList;
    EditText CoastalArea;
    EditText FirstFloorFlood;
    EditText StormSurge;

    //Cross Event Attributes
    EditText FloodDepth;
    EditText RoadWashout;
    EditText FloodBasement;

    //To determine which report values to scan
    boolean isSevereEvent;
    boolean isWinterEvent;
    boolean isCoastalEvent;
    boolean isRainEvent;

    SkywarnWSDBMapper reportToSubmit = new SkywarnWSDBMapper();

    @Override
   public void onCreate(Bundle savedInstance) {
       super.onCreate(savedInstance);
        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        Bundle weatherEventBoolsArray = i.getExtras();
        HashMap<String, Boolean> eventBools = (HashMap<String, Boolean>)weatherEventBoolsArray.get("weatherEventBoolsMap");

        //Get report from previous activity
        reportToSubmit = (SkywarnWSDBMapper) bundle.getSerializable("reportToSubmit");
        System.out.println("ID: "+reportToSubmit.getReportID());
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
           /*
            Snowfall = (EditText)findViewById(R.id.snowfall_field_tv);
            SnowfallRate = (EditText)findViewById(R.id.snowfall_rate_field_tv);
            SnowDepth = (EditText)findViewById(R.id.snow_depth_field_tv);
            WaterEquivalent = (EditText)findViewById(R.id.water_equiv_field_tv);
            FreezingRain = (EditText)findViewById(R.id.freezing_rain_field_tv);
            Sleet = (EditText)findViewById(R.id.sleet_field_tv);
            BlowDrift= (EditText)findViewById(R.id.blowdrift_tv);
            Whiteout= (EditText)findViewById(R.id.whiteout_field_tv);
            Thundersnow= (EditText)findViewById(R.id.thundersnow_field_tv);

            winterList.add(Snowfall);
            winterList.add(SnowfallRate);
            winterList.add(SnowDepth);
            winterList.add(WaterEquivalent);
            winterList.add(FreezingRain);
            winterList.add(Sleet);
            winterList.add(BlowDrift);
            winterList.add(Whiteout);
            winterList.add(Thundersnow);
            */
        }

        if(isCoastalEvent) {
            fragTransaction.add(R.id.second_container, coastalFloodingFrag, "coastalFloodFrag");
            /*
            CoastalArea = (EditText)findViewById(R.id.coastal_area_field_tv);
            FirstFloorFlood= (EditText)findViewById(R.id.first_floor_flood_field_tv);
            StormSurge= (EditText)findViewById(R.id.storm_surge_field_tv);

            coastalList.add(CoastalArea);
            coastalList.add(FirstFloorFlood);
            coastalList.add(StormSurge);
            */
        }

        if(isSevereEvent) {
            fragTransaction.add(R.id.third_container, severeFrag, "severeFrag");

            /*
            SevereType= (EditText)findViewById(R.id.severe_type_field_tv);
            WindSpeed= (EditText)findViewById(R.id.wind_speed_field_tv);
            WindGust= (EditText)findViewById(R.id.wind_gust_field_tv);
            WindDirection= (EditText)findViewById(R.id.wind_direction_field_tv);
            HailSize= (EditText)findViewById(R.id.hail_size_field_tv);
            Tornado = (EditText)findViewById(R.id.tornado_field_tv);
            Barometer= (EditText)findViewById(R.id.barometer_field_tv);
            DownedLimbs= (EditText)findViewById(R.id.downed_limbs_field_tv);
            DownedPoles= (EditText)findViewById(R.id.downed_poles_field_tv);
            DownedTrees= (EditText)findViewById(R.id.downed_trees_field_tv);
            DownedWires= (EditText)findViewById(R.id.downed_wires_field_tv);
            DiameterLimbs= (EditText)findViewById(R.id.diameter_limbs_field_tv);
            LightningDamage= (EditText)findViewById(R.id.lightning_damage_field_tv);
            DamageComments= (EditText)findViewById(R.id.damage_comments_field_tv);

            severeList.add(SevereType);
            severeList.add(WindSpeed);
            severeList.add(WindGust);
            severeList.add(WindDirection);
            severeList.add(HailSize);
            severeList.add(Tornado);
            severeList.add(Barometer);
            severeList.add(DownedLimbs);
            severeList.add(DownedPoles);
            severeList.add(DownedTrees);
            severeList.add(DownedWires);
            severeList.add(DiameterLimbs);
            severeList.add(LightningDamage);
            severeList.add(DamageComments);
            */
        }

        if(isRainEvent) {
            fragTransaction.add(R.id.fourth_container, rainFrag, "rainFrag");

            /*
            Rain = (EditText)findViewById(R.id.rain_field_tv);
            PrecipRate= (EditText)findViewById(R.id.precip_rate_field_tv);
            RiverFlood= (EditText)findViewById(R.id.river_flood_field_tv);
            Creek_StreamFlood= (EditText)findViewById(R.id.creek_stream_flood_field_tv);
            StreetFlood= (EditText)findViewById(R.id.street_flood_field_tv);
            LargeRiverFlood= (EditText)findViewById(R.id.large_river_flood_field_tv);
            IceJamFlood= (EditText)findViewById(R.id.ice_jam_field_tv);

            rainList.add(Rain);
            rainList.add(PrecipRate);
            rainList.add(RiverFlood);
            rainList.add(Creek_StreamFlood);
            rainList.add(StreetFlood);
            rainList.add(LargeRiverFlood);
            rainList.add(IceJamFlood);
            */
        }

        fragTransaction.commit();
        setContentView(R.layout.activity_submit_report_details);
        }
        else
            launchConfirmSubmitReportActivity(findViewById(R.id.submit_report));
   }



    public void launchConfirmSubmitReportActivity(View v){
        //Insert into DynamoDB when ready
        reportToSubmit.setReportID("TestUser_");
        //reportToSubmit.setWeatherEvent("Rain");

        if(isRainEvent){

            Rain = (EditText)findViewById(R.id.rain_field_tv);
            PrecipRate= (EditText)findViewById(R.id.precip_rate_field_tv);
            RiverFlood= (EditText)findViewById(R.id.river_flood_field_tv);
            Creek_StreamFlood= (EditText)findViewById(R.id.creek_stream_flood_field_tv);
            StreetFlood= (EditText)findViewById(R.id.street_flood_field_tv);
            LargeRiverFlood= (EditText)findViewById(R.id.large_river_flood_field_tv);
            IceJamFlood= (EditText)findViewById(R.id.ice_jam_field_tv);

            reportToSubmit.setWeatherEvent("Rain");
            reportToSubmit.setRain(Rain.getText().toString());
            reportToSubmit.setPrecipRate(PrecipRate.getText().toString());
            reportToSubmit.setRiverFlood(RiverFlood.getText().toString());
            reportToSubmit.setCreek_StreamFlood(Creek_StreamFlood.getText().toString());
            reportToSubmit.setStreetFlood(StreetFlood.getText().toString());
            reportToSubmit.setLargeRiverFlood(LargeRiverFlood.getText().toString());
            reportToSubmit.setIceJamFlood(IceJamFlood.getText().toString());

        }
        if(isCoastalEvent){
            CoastalArea = (EditText)findViewById(R.id.coastal_area_field_tv);
            FirstFloorFlood= (EditText)findViewById(R.id.first_floor_flood_field_tv);
            StormSurge= (EditText)findViewById(R.id.storm_surge_field_tv);

            reportToSubmit.setWeatherEvent("Coastal Flooding");
            reportToSubmit.setCoastalArea(CoastalArea.getText().toString());
            reportToSubmit.setFirstFloorFlood(FirstFloorFlood.getText().toString());
            reportToSubmit.setStormSurge(StormSurge.getText().toString());
        }

        if(isSevereEvent){
            SevereType= (EditText)findViewById(R.id.severe_type_field_tv);
            WindSpeed= (EditText)findViewById(R.id.wind_speed_field_tv);
            WindGust= (EditText)findViewById(R.id.wind_gust_field_tv);
            WindDirection= (EditText)findViewById(R.id.wind_direction_field_tv);
            HailSize= (EditText)findViewById(R.id.hail_size_field_tv);
            Tornado = (EditText)findViewById(R.id.tornado_field_tv);
            Barometer= (EditText)findViewById(R.id.barometer_field_tv);
            DownedLimbs= (EditText)findViewById(R.id.downed_limbs_field_tv);
            DownedPoles= (EditText)findViewById(R.id.downed_poles_field_tv);
            DownedTrees= (EditText)findViewById(R.id.downed_trees_field_tv);
            DownedWires= (EditText)findViewById(R.id.downed_wires_field_tv);
            DiameterLimbs= (EditText)findViewById(R.id.diameter_limbs_field_tv);
            LightningDamage= (EditText)findViewById(R.id.lightning_damage_field_tv);
            DamageComments= (EditText)findViewById(R.id.damage_comments_field_tv);
            WindDamage = (EditText)findViewById(R.id.wind_damage_field_tv);

            reportToSubmit.setWeatherEvent("Severe");
            reportToSubmit.setSevereType(SevereType.getText().toString());
            reportToSubmit.setWindSpeed(WindSpeed.getText().toString());
            reportToSubmit.setWindGust(WindGust.getText().toString());
            reportToSubmit.setWindDirection(WindDirection.getText().toString());
            reportToSubmit.setHailSize(HailSize.getText().toString());
            reportToSubmit.setTornado(Tornado.getText().toString());
            reportToSubmit.setBarometer(Barometer.getText().toString());
            reportToSubmit.setDownedTrees(DownedTrees.getText().toString());
            reportToSubmit.setDownedLimbs(DownedLimbs.getText().toString());
            reportToSubmit.setDiameterLimbs(DiameterLimbs.getText().toString());
            reportToSubmit.setDownedPoles(DownedPoles.getText().toString());
            reportToSubmit.setDownedWires(DownedWires.getText().toString());
            reportToSubmit.setWindDamage(WindDamage.getText().toString());
            reportToSubmit.setDamageComments(DamageComments.getText().toString());
            reportToSubmit.setLightningDamage(LightningDamage.getText().toString());
        }
        if(isWinterEvent){
            Snowfall = (EditText)findViewById(R.id.snowfall_field_tv);
            SnowfallRate = (EditText)findViewById(R.id.snowfall_rate_field_tv);
            SnowDepth = (EditText)findViewById(R.id.snow_depth_field_tv);
            WaterEquivalent = (EditText)findViewById(R.id.water_equiv_field_tv);
            FreezingRain = (EditText)findViewById(R.id.freezing_rain_field_tv);
            Sleet = (EditText)findViewById(R.id.sleet_field_tv);
            BlowDrift= (EditText)findViewById(R.id.blowdrift_tv);
            Whiteout= (EditText)findViewById(R.id.whiteout_field_tv);
            Thundersnow= (EditText)findViewById(R.id.thundersnow_field_tv);


            reportToSubmit.setWeatherEvent("Winter");
            reportToSubmit.setSnowfall(Snowfall.getText().toString());
            reportToSubmit.setSnowDepth(SnowDepth.getText().toString());
            reportToSubmit.setSnowfallRate(SnowfallRate.getText().toString());
            reportToSubmit.setWaterEquivalent(WaterEquivalent.getText().toString());
            reportToSubmit.setFreezingRain(FreezingRain.getText().toString());
            reportToSubmit.setSleet(Sleet.getText().toString());
            reportToSubmit.setBlowDrift(BlowDrift.getText().toString());
            reportToSubmit.setWhiteout(Whiteout.getText().toString());
            reportToSubmit.setThundersnow(Thundersnow.getText().toString());
        }

        AsyncInsertTask insertTask = new AsyncInsertTask(reportToSubmit);
        insertTask.execute();
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, ConfirmSubmitReportActivity.class);
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
}
//Icons provided by: https://icons8.com/web-app/for/android/next