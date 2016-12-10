package com.example.oliverasker.skywarnmarkii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;

import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import static com.amazonaws.regions.Regions.US_EAST_1;

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
    1.  queryLauncher   ->      weather_list_view
    2.

    Fragment        ->      layout_resource.xml
    1. CoastalFloodingSubmittedReportFragment   ->  coastal_flooding_submit_fragment
    2. RainFloodSubmitReportFragment    ->  rain_flood_submit_fragment
    3. WinterSubmitReportFragment   ->  winter_submit_fragment
 */

public class MainActivity extends AppCompatActivity {
    public static AmazonClientManager clientManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup client manager
        clientManager = new AmazonClientManager(this);
        CognitoCachingCredentialsProvider credPro = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:75c2afbc-dfba-4e27-bdc0-0d7e65027111",       //user_ID
                US_EAST_1                                               // Region
        );

        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
    }
}
