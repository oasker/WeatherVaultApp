package com.example.oliverasker.skywarnmarkii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;

import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import static com.amazonaws.regions.Regions.US_EAST_1;

/*  Resources:
 *  1. Amazingly helpful for AsyncTasks
 *     http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
 */

public class queryLauncher extends AppCompatActivity implements ICallback {

    ListView listView;
    AmazonDynamoDBClient ddbClient=null;
    DynamoDBMapper mapper=null;
    ArrayList<SkywarnWSDBMapper> weatherList=null;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        //weather_list_view MUST CONTAIN ListView Widget in resource file!!
        setContentView(R.layout.weather_list_view);

        //Setup toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbars);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //Loads in all Reports
        MyAsyncTask myAsync = new MyAsyncTask(this);
        myAsync.delegate = this;
        myAsync.execute();
    }


    public void setSkywarnMapperValueList(ArrayList<SkywarnWSDBMapper> tempWeatherList){
        weatherList=tempWeatherList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_query, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit_report:
               launchSubmitReportActivity();
                return true;
            case (R.id.search_reports):
                launchScanReportActivity();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void launchSubmitReportActivity(){
        Intent intent = new Intent(this, SubmitReportActivity.class);
        startActivity(intent);
    }
    public void launchScanReportActivity(){
        Intent intent = new Intent(this, SearchDBActivity.class);
        startActivity(intent);
    }


    public void launchViewReportActivity(SkywarnWSDBMapper clickedReport){
        Intent intent = new Intent(this, ViewReportActivity.class);
        intent.putExtra("selectedReport", (Serializable) clickedReport);
        startActivity(intent);
    }

    //Not used but has good references

    public void call() {
        CognitoCachingCredentialsProvider credPro = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:75c2afbc-dfba-4e27-bdc0-0d7e65027111",       //user_ID
                US_EAST_1                                               // Region
        );

        CognitoSyncManager syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_EAST_1,
                credPro
        );

        ddbClient = new AmazonDynamoDBClient(credPro);
        mapper = new DynamoDBMapper(ddbClient);

        //////////Read in by ID/////////////////////
        //selectedRow = (WeatherDBRowMap)mapper.load(WeatherDBRowMap.class,"Marion");
        //System.out.println(selectedRow.getReporter());
        ///////////////////////////////////////////

        //////////Scan DB/////////////////////////////
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        PaginatedScanList<SkywarnWSDBMapper> scanResult = mapper.scan(SkywarnWSDBMapper.class, scanExp);
        scanResult.loadAllResults();

        //Read scan results to list
        weatherList = new ArrayList<SkywarnWSDBMapper>(scanResult.size());
        Iterator<SkywarnWSDBMapper> iterator = scanResult.iterator();

        while (iterator.hasNext()) {
            SkywarnWSDBMapper dbElement = iterator.next();
            weatherList.add(dbElement);     //Add data to list
        }

        //Display results -WORKS
        // for(WeatherDBRowMap db: list)
        //    System.out.println(db.getReporter());

        //System.out.println(scanResult.toArray()[0]);
        ///////////////////////////////////////////////////


                 /*
                //////////DB Entry///////////////////////////////
                dbmap = new WeatherDBRowMap();
                dbmap.setLocation("Franklin");
                dbmap.setDate("10/01/2017");
                dbmap.setTemp(200);
                dbmap.setReporter("Freddy Mercury");
                mapper.save(dbmap);
                /////////////////////////////////////////////////
                */

        //////////Query//////////////////////////////////

       // WeatherDBRowMap rowToFind = new WeatherDBRowMap();
       // rowToFind.setLocation("Marion");
        String queryString = "Charlie";

        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
                .withAttributeValueList(new AttributeValue().withS(queryString.toString()));
    }

    //Filtered Scan -loads reports that match criteria
    public void performFilteredScan(){
        /*
        MyScanAyncTask myAsync = new MyScanAyncTask(this);
        myAsync.delegate = this;
        myAsync.execute();
        */
    }

    //Full Scan -loads all reports
    public void performFullScan(){
        MyAsyncTask myAsync = new MyAsyncTask(this);
        myAsync.delegate = this;
        myAsync.execute();
    }

    @Override
    public void processFinish(ArrayList<SkywarnWSDBMapper> result) {
        /*
         *  Once database row is received this interface method runs.
         *  We need to use the data immediately, so send data to
         */

        listView = (ListView)findViewById(R.id.weather_list_view);

        SkywarnWSDBMapper[] data = result.toArray(new SkywarnWSDBMapper[result.size()]);
        SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(this, data);
        listView.setAdapter(skywarnAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id ) {
                int itemPos = position;
                SkywarnWSDBMapper itemValue = (SkywarnWSDBMapper) listView.getItemAtPosition(position);
                launchViewReportActivity(itemValue);
            }
        });
    }
}