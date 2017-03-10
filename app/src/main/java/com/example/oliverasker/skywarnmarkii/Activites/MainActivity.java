package com.example.oliverasker.skywarnmarkii.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.oliverasker.skywarnmarkii.Managers.AmazonClientManager;
import com.example.oliverasker.skywarnmarkii.R;

//import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

/*
    Connects to WeatherSpotterDB
    1. Primary key is made up of partition key & (optional) sort key
        A. Simple Primary keys only have partition keys
        B. With Simple primary keys data can only be found through partition key
        C. Writing to table: DDB sends value of partition key to external hash, which determines
             the partition
    2.  Composite Primary Key has partition key and sort key
        A. DDB creates hashkey with partition key just like
            for simple primary keys
        B. BUT it stores all items with the same partition key
            value physically close together ordered by sort key
        primary key: partition key | partition & sort
        partition key: used to create hashkey
        http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.Partitions.html
        Table: WeatherSpotterDB
        Primary Partition Key:  Location
        Primary Sort Key:       Date

    THINGS TO REMEMBER
        Class           ->      layout_resource.xml
    1.  QueryLauncherActivity   ->      weather_list_view
    2.

    Fragment        ->      layout_resource.xml
    1. CoastalFloodingSubmittedReportFragment   ->  fragment_coastal_flooding_submit
    2. RainFloodSubmitReportFragment    ->  rain_flood_submit_fragment
    3. WinterSubmitReportFragment   ->  fragment_winter_submit
 */

public class MainActivity extends AppCompatActivity {
    public static AmazonClientManager clientManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup client manager
        clientManager = new AmazonClientManager(this);

          Intent intent = new Intent(this, LoginActivity.class);
        //Intent intent = new Intent(this, TabbedUserHomeActivity.class);
       // Intent intent = new Intent(this, QueryLauncherActivity.class);
       // Intent intent =new Intent(this, LaunchCameraActivity.class);
        //Intent intent = new Intent(this,SubmitReportActivity.class);
        startActivity(intent);
    }
}
