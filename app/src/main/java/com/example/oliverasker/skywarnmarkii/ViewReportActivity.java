package com.example.oliverasker.skywarnmarkii;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by student on 10/19/16.
 */

public class ViewReportActivity extends Activity {

    ISetTextInFragment setFragText1;

    ListView reportDataList=null;
    private String UsernameEpoch;
    private String WeatherEvent;
    private Double Longitude;
    private Double Latitude;
    private String Street;
    private String City;
    private String State;
    private String ZipCode;
    private int Date;
    private String Comments;
    private float CurrentTemperature= 9999;

    //Winter Weather Specfic Attributes
    /*
    private float Snowfall = 9999;
    private float SnowfallRate =9999;
    private float SnowDepth = 9999;
    private float WaterEquivalent  = 9999;
    private boolean FreezingRain= false;
    private boolean Sleet = false;
    private boolean BlowDrift = false;
    private boolean Whiteout = false;
    private boolean Thundersnow = false;

    //Severe Weather Specific Attributes
    private String SevereType = "|";
    private float WindSpeed  = 9999;
    private float WindGust  = 9999;
    private String WindDirection  ="|";
    private float HailSize  = 9999;
    private String Tornado = "|";
    private float Barometer  = 9999;
    private float DownedTrees = 9999;
    private float DownedLimbs  = 9999;
    private float DiameterLimbs = 9999;
    private float DownedPoles = 9999;
    private boolean DownedWires = false;
    private String WindDamage  = "|";
    private String LightningDamage  = "|";
    private String DamageComments = "|";
    */

    //Rainfall/Flooding Specific Attributes
    private float Rain = 9999;
    private float PrecipRate = 9999;
    private boolean SmallBodyFlood = false;
    private boolean LargeBodyFlood = false;
    private boolean IceJamFlood = false;

    //Cross Event Attributes
    private float FloodDepth  = 9999;			//Use in coastal and rain
    private boolean RoadWashout = false; 	//Use in coastal and rain
    private boolean FloodBasement = false; 	//Use in coastal and rain

    //Coastal Flooding Specific Attributes
    private String CoastalArea = "|";
    private boolean FirstFloorFlood = false;
    private float StormSurge = 9999;

    private TableLayout tl =null;
    private SkywarnWSDBMapper subject;
    private HashMap<String, Boolean> hashMap;

  //  private WinterSubmitReportFragment winterFrag;
    private WinterViewReportFragment winterFrag;
    private CoastalFloodingViewReportFragment coastalFloodingFrag;
    private RainFloodViewReportFragment rainFrag;
    private SevereViewReportFragment severeFrag;
    private GeneralSubmitReportFragment generalInfoFrag;

    SkywarnWSDBMapper map;

    TextView username;
    TextView date;
    TextView eventType;
    TextView location;
    TextView longitude;
    TextView lattitude;
    TextView comments;
    TextView reportRating;

    //Winter
    TextView Snowfall;
    TextView SnowfallRate;
    TextView SnowDepth;
    TextView WaterEquivalent;
    TextView FreezingRain;

    //Severe
    TextView SevereType;
    TextView WindSpeed;
    TextView WindGust;
    TextView WindDirection;
    TextView HailSize;
    TextView Tornado;
    TextView Barometer;
    TextView DownedTrees;
    TextView DownedLimbs;
    TextView DiameterLimbs;
    TextView DownedPoles;
    TextView DownedWires;
    TextView WindDamage;
    TextView LightningDamage;
    TextView DamageComments;

    HashMap<String,String> valueMap;
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.view_report_activity_layout);

        Intent intent = getIntent();
        Bundle weatherReport = intent.getExtras();

        valueMap = new HashMap<String, String>();

        map = (SkywarnWSDBMapper) weatherReport.get("selectedReport");

        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();

        //Send data to fragments
        //Stupid way to do this i think, but need to brute force it
        valueMap.put("Snowfall", map.getSnowfall());
        valueMap.put("SnowfallRate", map.getSnowfallRate());
        valueMap.put("SnowDepth", map.getSnowDepth());
        valueMap.put("WaterEquivalent", map.getWaterEquivalent());
        valueMap.put("FreezingRain", map.getFreezingRain());
        valueMap.put("Sleet", map.getSleet());
        valueMap.put("BlowDrift", map.getBlowDrift());
        valueMap.put("Whiteout", map.getWhiteout());
        valueMap.put("ThunderSnow", map.getThundersnow());

        //Severe Weather
        valueMap.put("SevereType", map.getSevereType());
        valueMap.put("WindSpeed", map.getWindSpeed());
        valueMap.put("WindGust", map.getWindGust());
        valueMap.put("WindDirection", map.getWindDirection());
        valueMap.put("HailSize", map.getHailSize());
        valueMap.put("Tornado", map.getTornado());
        valueMap.put("Barometer", map.getBarometer());
        valueMap.put("DownedTrees", map.getDownedTrees());
        valueMap.put("DownedPoles", map.getDownedPoles());
        valueMap.put("DownedWires", map.getDownedWires());
        valueMap.put("WindDamage", map.getWindDamage());
        valueMap.put("LightningDamage", map.getLightningDamage());
        valueMap.put("DamageComments", map.getDamageComments());

        //Rainfall/flooding
        valueMap.put("Rain", map.getRain());
        valueMap.put("FreezingRain", map.getFreezingRain());
        valueMap.put("PrecipRate", map.getPrecipRate());
        valueMap.put("RiverFlood", map.getRiverFlood());
        valueMap.put("Creek_StreamFlood", map.getCreek_StreamFlood());
        valueMap.put("StreetFlood", map.getStreetFlood());
        valueMap.put("LargeRiverFlood", map.getLargeRiverFlood());
        valueMap.put("IceJamFlood", map.getIceJamFlood());

        //Coastal
        valueMap.put("CoastalArea", map.getCoastalArea());
        valueMap.put("FirstFloorFlood", map.getFirstFloorFlood());
        valueMap.put("StormSurge", map.getStormSurge());

        //Common attributes
        valueMap.put("FloodDepth", map.getFloodDepth());
        valueMap.put("RoadWashout", map.getRoadWashout());
        valueMap.put("FloodBasement", map.getFloodBasement());

        //winterFrag = new WinterViewReportFragment().newInstance(valueMap);

        imageView = (ImageView) findViewById(R.id.view_report_image_view);
        if (map.getWeatherEvent().toUpperCase().contains("SEVERE"))
            imageView.setImageResource(R.drawable.severe);
        if (map.getWeatherEvent().toUpperCase().contains("RAIN"))
            imageView.setImageResource(R.drawable.rain);
        if (map.getWeatherEvent().toUpperCase().contains("WINTER"))
            imageView.setImageResource(R.drawable.snow_icon);
        if (map.getWeatherEvent().toUpperCase().contains("COASTAL"))
            imageView.setImageResource(R.drawable.coastal);


        rainFrag = new RainFloodViewReportFragment();
        coastalFloodingFrag = new CoastalFloodingViewReportFragment();
        severeFrag = new SevereViewReportFragment();
        generalInfoFrag= new GeneralSubmitReportFragment();


        //General Report Details
        username = (TextView)findViewById(R.id.view_report_activity_reporter);
        username.setText("Subitted by  " + map.getUsername());

        date = (TextView)findViewById(R.id.view_report_activity_date);
        date.setText(map.getDate());

        comments = (TextView)findViewById(R.id.view_report_activity_comments);
        comments.setText(map.getComments());

        eventType = (TextView)findViewById(R.id.view_report_activity_weather_event);
        eventType.setText(map.getWeatherEvent() );

        location = (TextView)findViewById(R.id.view_report_activity_location);
        location.setText(map.getEventCity() + ", " + map.getEventState());

        reportRating = (TextView)findViewById(R.id.view_report_activity_report_rating);
        reportRating.setText("Rating: " + map.getRating());

        longitude = (TextView)findViewById(R.id.view_report_activity_long_latt);
        longitude.setText("Lat/Long: " + map.getLattitude()+ (char)0x00B0 + " " + map.getLongitude()+(char)0x00B0 );




        if(map.getWeatherEvent().toUpperCase().contains("SEVERE")) {
            severeFrag = new SevereViewReportFragment().newInstance(valueMap);
            fragTransaction.add(R.id.frag1_container, severeFrag, "severeFrag");
        }

        if(map.getWeatherEvent().toUpperCase().contains("WINTER")) {
            winterFrag = new WinterViewReportFragment().newInstance(valueMap);
            fragTransaction.add(R.id.frag2_container, winterFrag, "winterFrag");
        }

        if(map.getWeatherEvent().toUpperCase().contains("COASTAL")) {
            coastalFloodingFrag = new CoastalFloodingViewReportFragment().newInstance(valueMap);
            fragTransaction.add(R.id.frag3_container, coastalFloodingFrag, "coastalFrag");
        }

        if(map.getWeatherEvent().toUpperCase().contains("RAIN")) {
            rainFrag = new RainFloodViewReportFragment().newInstance(valueMap);
            fragTransaction.add(R.id.frag4_container, rainFrag, "rainFrag");
        }
        fragTransaction.commit();
    }
}
