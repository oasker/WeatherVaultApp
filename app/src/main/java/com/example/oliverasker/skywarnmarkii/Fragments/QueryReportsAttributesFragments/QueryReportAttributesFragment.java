package com.example.oliverasker.skywarnmarkii.Fragments.QueryReportsAttributesFragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.example.oliverasker.skywarnmarkii.Activities.ViewQueryResultsActivity;
import com.example.oliverasker.skywarnmarkii.Callbacks.DateCallBack;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.QueryReportAttributesFragmentDialogCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.StringCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Dialoges.QueryReportAttributesSelectWeatherTypeDialog;
import com.example.oliverasker.skywarnmarkii.Dialoges.QueryReportSelectAttributesDialog;
import com.example.oliverasker.skywarnmarkii.Fragments.SubmitMultipleReportsActivityFragments.SubmitMultipleReportsLocation;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.QueryReportAttributesTask;
import com.example.oliverasker.skywarnmarkii.Utility.DateUtility;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QueryReportAttributesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QueryReportAttributesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryReportAttributesFragment extends Fragment implements ICallback, StringCallback, QueryReportAttributesFragmentDialogCallback, DateCallBack {
    private static final String TAG = "QryRprtAttrFragment";
    QueryReportAttributesDateRangeFragment dateRangeFragment;
    private Map masterMap;
    private Button submitSearchButton;
    private ToggleButton useGPSLocationToggle;
    private Context mContext;
    private Switch singleOrRangeDateSwitch;
    private HashMap<String, List<String>> listDataChild;
    private Button selectAttrToQueryButton;
    private HashMap<String, String> matchAttributesToQuery;
    private HashMap<String, float[][]> rangeAttributesToQuery;
    private int weatherEventType;
    private String testDate;
//    1. severe
//    2. winter
//    3. coastal
//    4. raint

    //    Holds results from query
    private SkywarnWSDBMapper[] resultArray;

    private String attributeToQueryDynamoDBName;
    private SubmitMultipleReportsLocation locationFrag;
    private QueryReportAttributesSingleDayFragment singleDayFragment;

//    private  SkywarnWSDBMapper[] data;

    //    Fragment stuff
    private FragmentManager fragMananager;
    private FragmentTransaction transaction;
    private QueryReportAttributesMatchItemFragment matchItem;
    private QueryReportAttributeRangeItemFragment rangeItem;

    //    Date stuff
    private boolean isDateRange;
    private boolean isSingleDay = true;

    //    Date Range variables
    private Calendar startCal;
    private Calendar endCal;

    private Date startDate;
    private Date endDate;

    //    Single Day stuff
    private Date singleDate;


    private ArrayList<SkywarnWSDBMapper> resultArrayList;

    public QueryReportAttributesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QueryReportAttributesFragment.
     */
    public static QueryReportAttributesFragment newInstance(Context context) {
        QueryReportAttributesFragment queryReportFrag = new QueryReportAttributesFragment();

        Bundle args = new Bundle();
        queryReportFrag.setArguments(args);
        return queryReportFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        Todo: variables should be assignedin onCreate();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        matchAttributesToQuery = new HashMap<>();
        rangeAttributesToQuery = new HashMap<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_query_report_attributes2, container, false);

        resultArrayList = new ArrayList<>();
        mContext = getActivity().getApplicationContext();


//         Begin query button
        submitSearchButton = (Button) v.findViewById(R.id.submit_query_attr_button);
        submitSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               Todo: Here we loop thorugh all days and request reports
//                Log.d(TAG, "BEFOREstartCal: " + CalendarToString(startCal) + " endDate: " + CalendarToString(endDate));

//                if(dateRangeFragment != null ) {
                if (isDateRange) {
                    startCal = dateRangeFragment.getStartDate();
                    endCal = dateRangeFragment.getEndDate();

                    startDate = startCal.getTime();
                    endDate = endCal.getTime();


                    Log.d(TAG, "startDate vs EndDate: ");
                    long differenceInDays = DateUtility.getNumberDaysBetweenTwoCalendars(startCal, endCal);

                    Log.d(TAG, "startDate - endDate = " + differenceInDays + " days");
                    do {
                        Log.d(TAG, "startDate: " + startDate + " endDate: " + endDate);

                        testDate = DateUtility.DateToString(startDate);
                        if (DateUtility.DateToString(startDate).equals(DateUtility.DateToString(endDate))) {
                            launchQueryReportAttributesTask(testDate, true);
                            Log.d(TAG, "startDate == endDate");
                            break;
                        } else {
//                            Log.d(TAG, "startDate: " + DateUtility.CalendarToString(tempStartCal) + " enddate2: " + DateUtility.CalendarToString(endCal));
//                            Log.d(TAG, "startDate = endDate?: " + (startDate == endDate));
                            launchQueryReportAttributesTask(testDate, false);
                            Log.d(TAG, "startDate != endDate");
                        }
                        startDate = DateUtility.AddOneDayToDate(startDate);
                        Log.d(TAG, "looping through reports, testdate: " + testDate);
                    }
                    while (4 > 3);
                }

                if (isSingleDay & singleDayFragment != null) {
                    singleDayFragment.setCallBack(QueryReportAttributesFragment.this);
                    singleDate = singleDayFragment.getDateToQuery();
                    launchQueryReportAttributesTask(DateUtility.DateToString(singleDate), true);

//                    launchQueryReportAttributesTask(singleDayFragment.getDateToQueryString(),true);
                }
            }
        });


//          creates pop up with attributes specific to event type
        selectAttrToQueryButton = (Button) v.findViewById(R.id.open_attr_select_button);
        selectAttrToQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QueryReportAttributesSelectWeatherTypeDialog attrDialog = new QueryReportAttributesSelectWeatherTypeDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("dialogTag", Constants.SELECT_WEATHER_TYPE_DIALOG);
                attrDialog.setArguments(bundle);
                attrDialog.setCallback(QueryReportAttributesFragment.this);
                attrDialog.show(getFragmentManager(), "attrDialog");
            }
        });


        useGPSLocationToggle = (ToggleButton) v.findViewById(R.id.toggle_button_use_gps_location);
        ArrayAdapter<CharSequence> stateSpinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.states_array, android.R.layout.simple_spinner_item);
        stateSpinnerAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);

//        Add in Fragments
        fragMananager = getFragmentManager();
        transaction = getFragmentManager().beginTransaction();
        singleDayFragment = new QueryReportAttributesSingleDayFragment();
        locationFrag = new SubmitMultipleReportsLocation();
        transaction.replace(R.id.date_picker_container, singleDayFragment);
        transaction.replace(R.id.query_report_attributes_location_fragment_container, locationFrag);
        transaction.commit();


        singleOrRangeDateSwitch = (Switch) v.findViewById(R.id.single_or_range_date_search_switch);
        singleOrRangeDateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!singleOrRangeDateSwitch.isChecked()) {
                    FragmentTransaction trans = fragMananager.beginTransaction();

                    singleDayFragment = new QueryReportAttributesSingleDayFragment();
                    singleDayFragment.setCallBack(QueryReportAttributesFragment.this);
                    trans.replace(R.id.date_picker_container, singleDayFragment);
                    trans.commit();
                    isSingleDay = true;
                    isDateRange = false;
                    Log.d(TAG, "isSingleDay: " + isSingleDay + "  isDateRange: " + isDateRange);
                    //dateRangeFragment=null;
                } else if (singleOrRangeDateSwitch.isChecked()) {
                    FragmentTransaction trans = fragMananager.beginTransaction();

                    dateRangeFragment = new QueryReportAttributesDateRangeFragment();
                    dateRangeFragment.setCallBack(QueryReportAttributesFragment.this);
                    trans.replace(R.id.date_picker_container, dateRangeFragment);
                    trans.commit();
                    isSingleDay = false;
                    isDateRange = true;
                    //singleDayFragment = null;
                    Log.d(TAG, "isSingleDay: " + isSingleDay + "  isDateRange: " + isDateRange);
                }
            }
        });
        return v;
    }

//    private void initData(){
//        //Todo: Figure out why the number of expandable list views doesnt match expectation
//
//        expandableHeaders = new ArrayList<>();
//        listDataChild = new HashMap<>();
//
//        List winterAttributesList = new ArrayList<String>();
//        winterAttributesList.add("Snowfall Depth");
//        winterAttributesList.add("Snowfall Rate");
//        winterAttributesList.add("Water Equivalent");
//        winterAttributesList.add("Sleet");
//        winterAttributesList.add("Freezing Rain");
//        winterAttributesList.add("Freezing Rain Accumulation");
//        winterAttributesList.add("Blow Drift");
//        winterAttributesList.add("Whiteout");
//        winterAttributesList.add("Thudersnow");
//        winterAttributesList.add("Winter Comments");
//        winterAttributesList.add("Winter Injuries");
//        winterAttributesList.add("Winter Fatalities");
//
//        List severeAttributesList = new ArrayList<String>();
//        severeAttributesList.add("Severe Weather Type");
//        severeAttributesList.add("Wind Gust");
//        severeAttributesList.add("Wind Speed");
//        severeAttributesList.add("Wind Direction");
//        severeAttributesList.add("Hail Size");
//        severeAttributesList.add("Tornado");
//        severeAttributesList.add("Barometer");
//        severeAttributesList.add("Wind Damage");    //boolean
//        severeAttributesList.add("Wind Damage Comments");
//        severeAttributesList.add("Lightning Damage");
//        severeAttributesList.add("Lightning Damage Comments");
//        severeAttributesList.add("Severe Weather Injuries");
//        severeAttributesList.add("Severe Weather Fatalities");
//
//        List rainAttributesList = new ArrayList<String>();
//        rainAttributesList.add("Rain Comments");
//        rainAttributesList.add("Precipitation Rate");
//        rainAttributesList.add("Flood Comments");
//        rainAttributesList.add("Rain/Flood Injuries");
//        rainAttributesList.add("Rain/Flood Fatalities");
////
//        List coastalAttributesList = new ArrayList<String>();
//        coastalAttributesList.add("Storm Surge");
//        coastalAttributesList.add("Coastal Event Comments");
//        coastalAttributesList.add("Coastal Injuries");
//        coastalAttributesList.add("Coastal Fatalities");
//
//        //Set Headers
//        expandableHeaders.add("Winter Attributes");
//        expandableHeaders.add("Severe Attributes");
//        expandableHeaders.add("Coastal Attributes");
//        expandableHeaders.add("Rain/Flood Attributes");
//
//        Log.d(TAG, "expandableHeaders size: " + expandableHeaders.size());
//
//        listDataChild.put(expandableHeaders.get(0), winterAttributesList);
//        listDataChild.put(expandableHeaders.get(1), severeAttributesList);
//        listDataChild.put(expandableHeaders.get(2), coastalAttributesList);
//        listDataChild.put(expandableHeaders.get(3), rainAttributesList);
//    }

//    Todo: hook up user weather type specific attributes to search task

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    //  Callback for report query.
//  Adds reports to list
    @Override
    public void processFinish(ArrayList<SkywarnWSDBMapper> result) {
//        resultArray = result.toArray(new SkywarnWSDBMapper[result.size()]);
        List tempList = new ArrayList(Arrays.asList(result));
//        tempList.addAll(Arrays.asList(resultArray));
//        resultArray =  tempList.toArray(new );
        Log.d(TAG, "processFinish(ArrayList<SkywarnWSDB> result.size():" + result.size());
        resultArrayList.addAll(result);
//        launchViewQueryResult(resultArray);
//        Log.i(TAG, "processFinish()");
//        Log.i(TAG, "result null?: " + (result == null) + " size(): " + result.size());
//
//        for(SkywarnWSDBMapper item:result){
//            Log.i(TAG, "CITY: " + item.getCity() + " DateSubmittedString: " + item.getDateSubmittedString());
//        }
//
//        if(result.size() ==0){
//            Toast.makeText(mContext,"No Reports found",Toast.LENGTH_SHORT).show();
//        }
//        data = result.toArray(new SkywarnWSDBMapper[result.size()]);
//        SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(mContext, data);
//
//        //for(int i=0; i< data.length; i++)
//        // System.out.println("Received in QueryLauncherActivity: "+data[i].getEventCity() + " " + data[i].getComments());
//        listView.setAdapter(skywarnAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id ) {
//                SkywarnWSDBMapper itemValue = (SkywarnWSDBMapper) listView.getItemAtPosition(position);
//                launchViewReportActivity(itemValue);
//            }
//        });
//        result = null;
    }


    @Override
    public void allQueriesComplete() {
        Log.d(TAG, "allQueriesComplete");
        resultArray = new SkywarnWSDBMapper[resultArrayList.size()];
        resultArray = resultArrayList.toArray(resultArray);
        launchViewQueryResult(resultArray);
    }

    //    These methods are callbacks that fire when dialog asking for attributes to query on finishes
    @Override
    public void onProcessComplete(String county, String lat, String lng) {
        Log.d(TAG, "onProcessComplete()");
    }

    @Override
    public void onProcessError(String attributeToQuery) {
        Log.d(TAG, "onProcessComplete(String) attributeToQuery: " + attributeToQuery);
//
////        Send fragment attribute
//        if(isRangeAttribute(attributeToQuery)) {
//            Bundle bundle = new Bundle();
//            bundle.putString("attributeString", attributeToQuery);
//
//            QueryReportAttributeRangeItemFragment rangeItem = new QueryReportAttributeRangeItemFragment();
//            rangeItem.setArguments(bundle);
//
//            fragMananager = getFragmentManager();
//            transaction = getFragmentManager().beginTransaction();
//
//            transaction.replace(R.id.query_report_attributes_container, rangeItem);
//            transaction.commit();
//        }
//
////        if attribute is not queried with range use single input field
//        if(isMatchAttribute(attributeToQuery)) {
//            Bundle bundle = new Bundle();
//            bundle.putString("attributeString", attributeToQuery);
//
//            QueryReportAttributesMatchItemFragment matchItem = new QueryReportAttributesMatchItemFragment();
//            matchItem.setArguments(bundle);
//
//            fragMananager = getFragmentManager();
//            transaction = getFragmentManager().beginTransaction();
//
//            transaction.replace(R.id.query_report_attributes_container, matchItem);
//            transaction.commit();
//        }
    }

    @Override
    public void onProcessComplete(ArrayList<String> items) {
        String[] test = getResources().getStringArray(R.array.WinterAttributesNames);
        Log.d(TAG, "onProcessComplete(ArrayList<String> items) ");
        for (String s : items) {
            Log.d(TAG, "return attributes: " + s);
//            ToDo: Create methods to create range/literal query input
//            TextView v = new TextView(mContext);
//            v.setText(s);
//            attrLV.addView(v);
        }
    }
    /*
   *   For QueryReportSelectWeatherTypeDialog and QueryReportSelectAttributesDialog
   *   @attribute is selected weather type or attribute
   *   @dialogTag tells us which dialog is responding
   */
    @Override
    public void onProcessFinished(String attributeToQuery, String attrDynamoDBName, int dialogTag) {
        attributeToQueryDynamoDBName = attrDynamoDBName;
        Log.d(TAG, "attributeDBNAme: " + attributeToQueryDynamoDBName);
        switch (dialogTag) {
            case Constants.SELECT_WEATHER_TYPE_DIALOG:
                Log.d(TAG, "onProcessFinished(String attribute, int dialogTag): SELECT_WEATHER_TYPE_DIALOG");
                Log.d(TAG, "attributeToQuery: " + attributeToQuery);
                Bundle selectWeatherTypeBundle = new Bundle();
                selectWeatherTypeBundle.putInt("dialogTag", Constants.SELECT_ATTRIBUTES_TO_QUERY_DIALOG);
                QueryReportSelectAttributesDialog attrDialog = new QueryReportSelectAttributesDialog();
                attrDialog.setCallback(QueryReportAttributesFragment.this);
                attrDialog.setArguments(selectWeatherTypeBundle);

                attrDialog.setEventType(Integer.parseInt(attributeToQuery));
                weatherEventType = Integer.parseInt(attributeToQuery);
                attrDialog.show(fragMananager, "attrDialog");
//                switch (attributeToQuery){
//                    case "0":
//                        attrDialog.setEventType(0);
//                        attrDialog.show(fragMananager, "attrDialog");
//                        break;
//                    case "1":
//                        attrDialog.setEventType(1);
//                        attrDialog.show(fragMananager, "attrDialog");
//                        break;
//                    case "2":
//                        attrDialog.setEventType(2);
//                        attrDialog.show(fragMananager, "attrDialog");
//                        break;
//                    case "3":
//                        attrDialog.setEventType(3);
//                        attrDialog.show(fragMananager, "attrDialog");
//                        break;
//                }
//                break;
            case Constants.SELECT_ATTRIBUTES_TO_QUERY_DIALOG:
                Log.d(TAG, "onProcessFinished(String attribute, int dialogTag): dialogTag: SELECT_ATTRIBUTES_TO_QUERY_DIALOG");
                Log.d(TAG, "attributeToQuery: " + attributeToQuery);

//        Send fragment attribute
                if (isRangeAttribute(attributeToQuery)) {
                    Log.d(TAG, "isRangeAttribute = true");
                    Bundle rangeAttributeBundle = new Bundle();
                    rangeAttributeBundle.putString("attributeString", attributeToQuery);
                    rangeAttributeBundle.putInt("dialogTag", Constants.SELECT_ATTRIBUTES_TO_QUERY_DIALOG);

                    rangeItem = new QueryReportAttributeRangeItemFragment();
                    rangeItem.setArguments(rangeAttributeBundle);

                    transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.query_report_attributes_container, rangeItem);
                    transaction.commit();

                    //  matchItem=null;
                }

//        if attribute is not queried with range use single input field
                if (isMatchAttribute(attributeToQuery)) {
                    Log.d(TAG, "isMatchAttribute = true");
                    Bundle matchAttributeBundle = new Bundle();
                    matchAttributeBundle.putString("attributeString", attributeToQuery);
                    matchAttributeBundle.putInt("dialogTag", Constants.SELECT_ATTRIBUTES_TO_QUERY_DIALOG);

                    matchItem = new QueryReportAttributesMatchItemFragment();
                    matchItem.setArguments(matchAttributeBundle);

                    transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.query_report_attributes_container, matchItem);
                    transaction.commit();
                    //  rangeItem=null;
                }
                break;
        }
    }
//    private TableRow createRangeTableRow(String attributeName){
//        QueryReportAttributeRangeItemFragment rangeItem = new QueryReportAttributeRangeItemFragment();
//        rangeItem.setAttrlabelString(attributeName);
//
//
//       // return tr;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    private void launchViewQueryResult(SkywarnWSDBMapper[] results) {
        Intent i = new Intent(mContext, ViewQueryResultsActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("queryResultsArray", results);
        i.putExtras(b);
        startActivity(i);
        results = null;
    }

    //Todo: query attributes should have two tasks, one for date range, one for single day
    private void launchQueryReportAttributesTask(String testDate, boolean isLastDate) {
        QueryReportAttributesTask queryTask = new QueryReportAttributesTask();
        queryTask.setContext(mContext);
        queryTask.setDelegate(this);

        if (isLastDate)
            queryTask.setLastQuery(true);

        masterMap = new HashMap();

        if (!locationFrag.getCity().trim().equals("")) {
            matchAttributesToQuery.put("City", locationFrag.getCity());
//            masterMap.put("City", locationFrag.getCity());
        }

        if (!locationFrag.getState().trim().equals("")) {
            matchAttributesToQuery.put("State", locationFrag.getState());
//            masterMap.put("State", locationFrag.getState());
        }

        if (!locationFrag.getZip().trim().equals("")) {
            matchAttributesToQuery.put("ZipCode", locationFrag.getZip());
//            masterMap.put("ZipCode", locationFrag.getZip());
        }

        if (!locationFrag.getStreet().trim().equals("")) {
            matchAttributesToQuery.put("Street", locationFrag.getStreet());
//            masterMap.put("Street", locationFrag.getStreet());
        }


//        Log.d(TAG, "LOCATION: city: " + locationFrag.getCity() + " state: " + locationFrag.getState() + " zip: " + locationFrag.getZip());

//        Get input attrbute to query
        if (matchItem != null) {
            matchAttributesToQuery.put(attributeToQueryDynamoDBName, matchItem.getQueryInput());
            Log.d(TAG, "dynamoName: " + attributeToQueryDynamoDBName + " query Input: " + matchItem.getQueryInput());
        }

        if (matchAttributesToQuery.size() > 0) {
            String[] matchAttributeDynamoDBNames = new String[matchAttributesToQuery.size()];
            String[] matchAttributeQueryValues = new String[matchAttributesToQuery.size()];
            int counter = 0;

            for (String s : matchAttributesToQuery.keySet()) {
//                matchAttributeDynamoDBNames[counter]=getDynamoDBAttributeName(s);
                Log.d(TAG, "Attributes: " + s);
                matchAttributeDynamoDBNames[counter] = s;
                matchAttributeQueryValues[counter] = matchAttributesToQuery.get(s);
                Log.d(TAG, "Size: " + matchAttributesToQuery.size() + " Counter: " + counter);
                counter++;
            }
//            matchAttributeDynamoDBNames[0]="City";
//            matchAttributeQueryValues[0]="West Bridgewater";
            masterMap = generateMatchDynamoDBQueryConditionMap(matchAttributeDynamoDBNames, matchAttributeQueryValues, masterMap);
            Log.d(TAG, "*************** Print Map for matchItems ***************");
            Utility.printMap(masterMap);
        }

        if (rangeItem != null) {
            rangeAttributesToQuery.put(attributeToQueryDynamoDBName,
                    new float[][]{{Float.parseFloat(rangeItem.getMinRangeString()), Float.parseFloat(rangeItem.getMaxRangeString())}});
            String[] rangeAttributeDynamoDBNames = new String[rangeAttributesToQuery.size()];
            List rangeVals = new ArrayList<>();
            int counter = 0;
            for (String s : rangeAttributesToQuery.keySet()) {
                rangeAttributeDynamoDBNames[counter] = s;
                rangeVals.add(rangeAttributesToQuery.get(s));
                counter++;
            }
            masterMap = generateRangeDynamoDBQueryConditionMap(rangeAttributeDynamoDBNames, rangeVals, masterMap);

//        matchAttributesToQuery.put("NetVote", "100");
//        Log.d(TAG, "launchQueryReportAttributesTask()");
//        Utility.printMap(matchAttributesToQuery);

//        queryTask.setQueryConditionMap(generateDynamoDBQueryConditionMap(matchAttributesToQuery,rangeAttributesToQuery));
            //MatchAttributes
            //Utility.printMap(matchAttributesToQuery);

            //Range
            // Utility.printMap(rangeAttributesToQuery);

//      Remember there are two types of conditions used to query
//        1 for strings where we check if ddb attribute CONTAINS the string
//        1 for numeric where a range is searched
//        matchAttributesToQuery.put("CallSign", "KD1CY");
//        matchAttributesToQuery.put("City","Peabody");

//        queryTask.setMatchAttributesToQuery(matchAttributesToQuery);
//        queryTask.setRangeAttributesToQuery(rangeAttributesToQuery);
            Log.d(TAG, "************PRINT MASTER MAP*************");
            Utility.printMap(masterMap);
            queryTask.setQueryConditionMap(masterMap);
        }

        queryTask.setDate(testDate);
        queryTask.setQueryConditionMap(masterMap);
            queryTask.execute();
//
//            masterMap.clear();
//            rangeAttributesToQuery.clear();
//            matchAttributesToQuery.clear();
        }

    //    Creates Map of attribute namesin dynamodb and AttributeValues
    public Map generateRangeDynamoDBQueryConditionMap(String[] attributeNames, List<float[][]> attributeValsToQuery, Map queryConditionMap) {
        Log.d(TAG, "generateRangeDynamoDBQueryConditionMap");

        // attributeValsToQuery.add(new float[][] {{9f,100f}});
        Log.d(TAG, "array 00: " + (String.valueOf(attributeValsToQuery.get(0)[0][0])));
        for (int i = 0; i < attributeNames.length; i++) {
            Log.i(TAG, "key: " + attributeNames[i] + " minRange: " + attributeValsToQuery.get(i)[0][0] + " maxRange: " + attributeValsToQuery.get(i)[0][1]);

            //            Set max range
            Condition maxCondition = new Condition()
                    .withComparisonOperator(ComparisonOperator.BETWEEN)
                    .withAttributeValueList(new AttributeValue().withN(String.valueOf(attributeValsToQuery.get(i)[0][0])))
                    .withAttributeValueList(new AttributeValue().withN(String.valueOf(attributeValsToQuery.get(i)[0][1])));
            Log.d(TAG, "querConditionMap.size() added condition ");
            queryConditionMap.put(attributeNames[i], maxCondition);


//            Set min range
            Condition minCondition = new Condition()
                    .withComparisonOperator(ComparisonOperator.GE)
                    .withAttributeValueList(new AttributeValue().withN(String.valueOf(attributeValsToQuery.get(i)[0][0])));
            Log.d(TAG, "querConditionMap.size() added condition ");
//            queryConditionMap.put(attributeNames[i], minCondition);
        }
        return queryConditionMap;
    }
    public Map generateMatchDynamoDBQueryConditionMap(String[] attributeNames, String[] attributeValsToQuery, Map queryConditionMap) {
        for (int i = 0; i < attributeNames.length; i++) {
            Log.d(TAG, "generateMatchDynamoDBCondition() attrName: " + attributeNames[i] + " valsToQuery: " + attributeValsToQuery[i]);
        }

        Log.d(TAG, "generateMatchDynamoDBQueryConditionMap");
        for (int i = 0; i < attributeNames.length; i++) {
            Log.i(TAG, "key: " + attributeNames[i] + " value: " + attributeValsToQuery[i]);
            Condition queryCondition = new Condition()
                    .withComparisonOperator(ComparisonOperator.CONTAINS)
                    .withAttributeValueList(new AttributeValue().withS(attributeValsToQuery[i]));

            Log.d(TAG, "querConditionMap.size() added condition ");
            queryConditionMap.put(attributeNames[i], queryCondition);
        }
        return queryConditionMap;
    }

    //    Determine if attribute requires range or match layout
    private boolean isMatchAttribute(String s) {
        List<String> matchAttrList = Arrays.asList(getResources().getStringArray(R.array.MatchAttributes));
        if (matchAttrList.contains(s)) {
            Log.d(TAG, "isMatchAttribute()->TRUE : val:  " + s);
            return true;
        }
        return false;
    }

    private boolean isRangeAttribute(String s) {
        List<String> rangeAttrList = Arrays.asList(getResources().getStringArray(R.array.RangeAttributes));
        if (rangeAttrList.contains(s)) {
            Log.d(TAG, "isRangeAttribute()->TRUE : val:  " + s);
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
//        rangeAttributesToQuery.clear();
//        matchAttributesToQuery.clear();
//        masterMap.clear();
//        resultArrayList.clear();
//        resultArray=null;
//        startDate = null;
//        endDate=null;
    }

    @Override
    public void onResume() {
        super.onResume();
        resultArrayList = new ArrayList<>();
        masterMap = new HashMap();
        matchAttributesToQuery = new HashMap<>();
        rangeAttributesToQuery = new HashMap<>();
    }

    @Override
    public void startDateChanged(Calendar startCal) {
        Log.d(TAG, "startDateChanged()");
        startDate = startCal.getTime();
    }

    @Override
    public void endDateChanged(Calendar endCal) {
        Log.d(TAG, "endDateChanged()");
        endDate = endCal.getTime();
    }

    @Override
    public void setSingleDateToQuery(Calendar singleDateCalendar) {
        singleDate = singleDateCalendar.getTime();
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
