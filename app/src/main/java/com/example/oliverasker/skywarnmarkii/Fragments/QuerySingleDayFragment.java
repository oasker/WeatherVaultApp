//package com.example.oliverasker.skywarnmarkii.Fragments;
//
//import android.app.Fragment;
//import android.app.ListFragment;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.ToggleButton;
//
//import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
//import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
//import com.amazonaws.services.dynamodbv2.model.Condition;
//import com.example.oliverasker.skywarnmarkii.Activites.MapsActivity;
//import com.example.oliverasker.skywarnmarkii.Activites.SearchDBActivity;
//import com.example.oliverasker.skywarnmarkii.Activites.SubmitReportActivity;
//import com.example.oliverasker.skywarnmarkii.Activites.TabbedUserHomeActivity;
//import com.example.oliverasker.skywarnmarkii.Activites.ViewReportActivity;
//import com.example.oliverasker.skywarnmarkii.Adapters.SkywarnDBAdapter;
//import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
//import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
//import com.example.oliverasker.skywarnmarkii.R;
//import com.example.oliverasker.skywarnmarkii.Tasks.GetAllRecordsForDayTask;
//import com.example.oliverasker.skywarnmarkii.Tasks.MyAsyncTask;
//import com.google.gson.internal.bind.ArrayTypeAdapter;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//
//import static com.amazonaws.regions.Regions.US_EAST_1;
//import static com.google.android.gms.fitness.data.WorkoutExercises.ROW;
//
///**
// * Created by oliverasker on 2/19/17.
// */
//
//public class QuerySingleDayFragment extends Fragment implements AdapterView.OnItemClickListener, ICallback,Parcelable{
//    ListView listView = null;
//    SkywarnWSDBMapper[] data = null;
//
//
//    @Override
//    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        View v = inflater.inflate(android.R.layout.simple_list_item_1, container,false);
//        listView = (ListView)v.findViewById(R.id.weather_list_view);
//
//        return v;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstance){
//        super.onActivityCreated(savedInstance);
//        ArrayList<SkywarnWSDBMapper> test = new ArrayList<>();
//        SkywarnWSDBMapper report = new SkywarnWSDBMapper();
//        report.setDateSubmittedEpoch((long)323523532);
//        report.setDateSubmittedString("02/43/21-0");
//        test.add(report);
//        ArrayAdapter<SkywarnWSDBMapper> adapter = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//
//            data = test.toArray(new SkywarnWSDBMapper[test.size()]);
//            SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(getContext(), data);
//            listView.setAdapter(skywarnAdapter);
//            adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,test);
//
//        }
//        setListAdapter(adapter);
//        getListView().setOnItemClickListener(this);
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,long id){
//        Toast.makeText(getActivity(),"Item " + position,Toast.LENGTH_LONG).show();
//    }
//
//
//    ListView listView;
//    AmazonDynamoDBClient ddbClient = null;
//    DynamoDBMapper mapper = null;
//    ArrayList<SkywarnWSDBMapper> weatherList = null;
//    HashMap<String,String> attrHMap = null;
//    SkywarnWSDBMapper[] data = null;
//    //asyncTask
//    MyAsyncTask myAsync = new MyAsyncTask(this);
//    GetAllRecordsForDayTask getRecordsForDayTask;
//
//    private ToggleButton toggleMapViewButton;
//    ReportListViewFragment reportLV;
//
//    public QueryLauncherActivity(){
//
//    }
//    protected QueryLauncherActivity(Parcel in) {
//    }
//
//    public static final Creator<com.example.oliverasker.skywarnmarkii.Activites.QueryLauncherActivity> CREATOR = new Creator<com.example.oliverasker.skywarnmarkii.Activites.QueryLauncherActivity>() {
//        @Override
//        public com.example.oliverasker.skywarnmarkii.Activites.QueryLauncherActivity createFromParcel(Parcel in) {
//            return new com.example.oliverasker.skywarnmarkii.Activites.QueryLauncherActivity(in);
//        }
//
//        @Override
//        public com.example.oliverasker.skywarnmarkii.Activites.QueryLauncherActivity[] newArray(int size) {
//            return new com.example.oliverasker.skywarnmarkii.Activites.QueryLauncherActivity[size];
//        }
//    };
//
//    @Override
//    public void onCreate(Bundle b) {
//        super.onCreate(b);
//
//        //weather_list_view MUST CONTAIN ListView Widget in resource file!!
//        //ORIGNINAL
//        setContentView(R.layout.weather_list_view);
//
//        //fragment placeholder
//        //setContentView(R.layout.fragment_placeholder_layout);
//
//        //insert fragment
//        //reportLV = new ReportListViewFragment();
//
//
//        //getFragmentManager().beginTransaction().add(R.id.listview_container, reportLV).commit();
//
//        /*
//        toggleMapViewButton = (ToggleButton)findViewById(R.id.toggle_map_list_view);
//        toggleMapViewButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked){
//                    Log.d(TAG, "Toggle Maps View togglebutton enabled");
//                    startMapActivity();
//                }else{
//                    Log.d(TAG, "Toggle Maps View togglebutton disablesd");
//                }
//            }
//        });
//        */
//        Bundle bundle = this.getIntent().getExtras();
//        if(bundle != null) {
//            attrHMap = (HashMap<String, String>) bundle.getSerializable("HashMap");
//            Log.d(TAG,"attrHMap size: " + attrHMap.size());
//        }
//        else
//            Log.d(TAG,"attrHMap2 IS NULL");
//
//        //Setup toolbar
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbars);
//        setSupportActionBar(myToolbar);
//
//
//        //If no attributes do a full scan
//        ///////////////////////////////////////////////////////////////
//        // Loads in all Reports if attribute Map is null
//        ///////////////////////////////////////////////////////////////
//
//        //Pass array with attribute names(from db) to get values from hashmap
//        String[] test = {"Comments", "DateSubmittedEpoch","DateSubmittedString", "Bac"};
//        getRecordsForDayTask = new GetAllRecordsForDayTask();
//        getRecordsForDayTask.setContext(this);
//        // getRecordsForDayTask.setReportAttributesArray(getResources().getStringArray(R.array.reportFields));
//        getRecordsForDayTask.setReportAttributesArray(test);
//        getRecordsForDayTask.delegate = this;
//        getRecordsForDayTask.execute();
//
//
////        myAsync.delegate = null;
////        if(attrHMap==null){
////            myAsync.delegate = this;
////            myAsync.execute();
////        }
//
//        //////////////////////////////////////////////////////////////
//        // Scans for specified attributes if attribute Map is NOT null
//        ///////////////////////////////////////////////////////////////
//        //HashMap<String,String> attrHMap2 = new HashMap<>();
//        //attrHMap2.put("State","CT");
//        //attrHMap2.put("WeatherEvent", "Rain");
//
//        /*
//         if(attrHMap!=null ){
//            MyScanAyncTask scanAyncTask = new MyScanAyncTask(this);
//            Utility.printMap(attrHMap,TAG);
//            scanAyncTask.delegate=this;
//             scanAyncTask.setScanA(attrHMap);
//            scanAyncTask.execute(attrHMap);
//            attrHMap.clear();
//        }
//        */
//    }
////
////    public void startMapActivity(){
////        Intent i = new Intent(this, MapsActivity.class);
////        Bundle bundle = new Bundle();
////        bundle.putSerializable("reportList", data);
////        i.putExtras(bundle);
////        startActivity(i);
////    }
////
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        switch (item.getItemId()) {
////            case R.id.submit_report:
////                launchSubmitReportActivity();
////                return true;
////            case (R.id.search_reports):
////                launchScanReportActivity();
////                return true;
////            case(R.id.action_settings):
////                Toast.makeText(this, "User Settings Functionality is Still under Development",Toast.LENGTH_LONG).show();
////                return true;
////            case(R.id.user_home_icon):
////                launchUserHomeActivity();
////            default:
////                // If we got here, the user's action was not recognized.
////                // Invoke the superclass to handle it.
////                return super.onOptionsItemSelected(item);
////        }
////    }
////
////
////    public void launchViewReportActivity(SkywarnWSDBMapper clickedReport){
////        Intent intent = new Intent(this, ViewReportActivity.class);
////        intent.putExtra("selectedReport", clickedReport);
////        startActivity(intent);
////    }
////
////    //Not used but has good references
////    public void call() {
////        CognitoCachingCredentialsProvider credPro = new CognitoCachingCredentialsProvider(
////                getApplicationContext(),
////                "us-east-1:75c2afbc-dfba-4e27-bdc0-0d7e65027111",       //user_ID
////                US_EAST_1                                               // Region
////        );
////
////        CognitoSyncManager syncClient = new CognitoSyncManager(
////                getApplicationContext(),
////                Regions.US_EAST_1,
////                credPro
////        );
////
////        ddbClient = new AmazonDynamoDBClient(credPro);
////        mapper = new DynamoDBMapper(ddbClient);
////
////        //////////Read in by ID/////////////////////
////        //selectedRow = (WeatherDBRowMap)mapper.load(WeatherDBRowMap.class,"Marion");
////        //System.out.println(selectedRow.getReporter());
////        ///////////////////////////////////////////
////
////        //////////Scan DB/////////////////////////////
////        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
////        PaginatedScanList<SkywarnWSDBMapper> scanResult = mapper.scan(SkywarnWSDBMapper.class, scanExp);
////        scanResult.loadAllResults();
////
////        if(weatherList != null){
////            weatherList.clear();
////        }
////        //Read scan results to list
////        weatherList = new ArrayList<SkywarnWSDBMapper>(scanResult.size());
////        Iterator<SkywarnWSDBMapper> iterator = scanResult.iterator();
////
////        while (iterator.hasNext()) {
////            SkywarnWSDBMapper dbElement = iterator.next();
////            weatherList.add(dbElement);     //Add data to list
////        }
////
////        //Display results -WORKS
////        // for(WeatherDBRowMap db: list)
////        //    System.out.println(db.getReporter());
////
////        //System.out.println(scanResult.toArray()[0]);
////        ///////////////////////////////////////////////////
////
////
////                 /*
////                //////////DB Entry///////////////////////////////
////                dbmap = new WeatherDBRowMap();
////                dbmap.setLocation("Franklin");
////                dbmap.setDate("10/01/2017");
////                dbmap.setTemp(200);
////                dbmap.setReporter("Freddy Mercury");
////                mapper.save(dbmap);
////                /////////////////////////////////////////////////
////                */
////
////        //////////Query//////////////////////////////////
////
////        // WeatherDBRowMap rowToFind = new WeatherDBRowMap();
////        // rowToFind.setLocation("Marion");
////        String queryString = "Charlie";
////
////        Condition rangeKeyCondition = new Condition()
////                .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
////                .withAttributeValueList(new AttributeValue().withS(queryString.toString()));
////    }
////
////    //After receiving query result launch listview
////    @Override
////    public void processFinish(ArrayList<SkywarnWSDBMapper> result) {
////        /*
////         *  Once database row is received this interface method runs.
////         *  We need to use the data immediately, so send data to listview
////         */
////        data = null;
////        listView = null;
////        listView = (ListView)findViewById(R.id.weather_list_view);
////        //listView = (ListView) findViewById(R.id.listView);
////        data = result.toArray(new SkywarnWSDBMapper[result.size()]);
////        SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(this, data);
////
////        //for(int i=0; i< data.length; i++)
////        // System.out.println("Received in QueryLauncherActivity: "+data[i].getEventCity() + " " + data[i].getComments());
////        listView.setAdapter(skywarnAdapter);
////
////        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id ) {
////                int itemPos = position;
////                SkywarnWSDBMapper itemValue = (SkywarnWSDBMapper) listView.getItemAtPosition(position);
////                launchViewReportActivity(itemValue);
////            }
////        });
////        result = null;
////    }
////
////    @Override
////    public int describeContents() {
////        return 0;
////    }
////
////    @Override
////    public void writeToParcel(Parcel parcel, int i) {
////    }
//}