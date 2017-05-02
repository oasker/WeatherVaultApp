package com.example.oliverasker.skywarnmarkii.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.oliverasker.skywarnmarkii.Activities.MapsActivity;
import com.example.oliverasker.skywarnmarkii.Activities.ViewReportActivity;
import com.example.oliverasker.skywarnmarkii.Adapters.SkywarnDBAdapter;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetAllRecordsForDayTask;
import com.example.oliverasker.skywarnmarkii.Tasks.GetNearByReportsTask;
import com.example.oliverasker.skywarnmarkii.Tasks.GetTopRatedReportsTask;
import com.example.oliverasker.skywarnmarkii.Utility.DateUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

//import android.support.v4.app.Fragment;

/**
 * Created by oliverasker on 2/19/17.
 */

public class ViewReportsFromSingleDayFragment extends Fragment implements ICallback {
    private static String dateString;
    private ListView listView;
    private ImageButton addDayButton;
    private ImageButton subtractDayButton;
    private Button viewReportsOnMapToggle;
    private Spinner queryTypeSpinner;
    private TextView dateTV;
    private Calendar cal;
    private Date date;
    private int queryTypeInt;
    //  0. Recent
//  1. Top Rated
//  2. Near Me
    private SkywarnWSDBMapper[] data;
    private Context mContext;
    private ArrayList<SkywarnWSDBMapper> resultList;
    private LinearLayout dateSelectLinearLayout;

    private ViewReportsFromSingleDayFragment instance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("dateString", dateString);
        outState.putSerializable("date", date);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getString("dateString") != null) {
                dateString = savedInstanceState.getString("dateString");
            }
            if (savedInstanceState.getSerializable("date") != null) {
                this.date = (Date) savedInstanceState.getSerializable("date");
            }
            Log.d(TAG, "dateString : " + dateString + "  date(Ojb): " + DateUtility.DateToString(date));
            cal = Calendar.getInstance();
            cal.setTime(date);
            dateString = DateUtility.DateToString(date);
        } else {
            cal = Calendar.getInstance();
            date = cal.getTime();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        Log.d(TAG, "onCreateView");
        setRetainInstance(true);

        View v = inflater.inflate(R.layout.fragment_weather_singleday_listview, container, false);
        GetAllRecordsForDayTask getRecordsForDayTask = null;
        GetTopRatedReportsTask getTopRatedTaslk = null;

        dateSelectLinearLayout = (LinearLayout) v.findViewById(R.id.date_day_select_linear_layout);

        queryTypeSpinner = (Spinner) v.findViewById(R.id.relevant_reports_query_type_spinner);
        queryTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String queryType = parent.getItemAtPosition(position).toString();
                switch (queryType) {
                    case "Top Rated":
                        Log.d(TAG, "Query Type: Top Rated");
                        queryTypeInt = 0;
                        dateSelectLinearLayout.setVisibility(LinearLayout.VISIBLE);
                        break;

                    case "Near Me":
                        Log.d(TAG, "Query Type: Near Me");
                        queryTypeInt = 2;
                        dateSelectLinearLayout.setVisibility(LinearLayout.GONE);
                        break;

                    case "Recent":
                        Log.d(TAG, "Query Type: Recent");
                        queryTypeInt = 1;
                        dateSelectLinearLayout.setVisibility(LinearLayout.VISIBLE);
                        break;
                    default:
                        break;
                }
                launchQuery(dateString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        queryTypeSpinner.setSelection(queryTypeInt);

        cal = Calendar.getInstance();

        final ArrayList<SkywarnWSDBMapper> data = new ArrayList<>();

        final SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Log.d(TAG, "Date In ViewReports " + String.valueOf(format1.format(cal.getTime())));
        dateString = format1.format(cal.getTime());

        dateTV = (TextView) v.findViewById(R.id.date_of_reports_being_submittedTV);
        dateTV.setText(String.valueOf(format1.format(cal.getTime())));

        listView = (ListView) v.findViewById(R.id.weather_list_view);

        addDayButton = (ImageButton) v.findViewById(R.id.increment_date_by_one_button);
        subtractDayButton = (ImageButton) v.findViewById(R.id.deincrement_date_by_one_button);

        addDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "date.toString(): " + DateUtility.DateToString(date) + " dateSTring: " + dateString);
//                cal.setTime(date);
                if (cal.getTime() == Calendar.getInstance().getTime()) {
                    date = cal.getTime();
                    dateString = format1.format(cal.getTime());
                    dateTV.setText(dateString);
                    launchQuery(dateString);
                } else {
                    cal.setTime(date);
                    cal.add(Calendar.DATE, 1);
                    dateString = format1.format(cal.getTime());
                    date = cal.getTime();
                    dateTV.setText(dateString);
                    launchQuery(dateString);
                }
            }
        });

        subtractDayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cal.setTime(date);
                cal.add(Calendar.DATE, -1);
                date = cal.getTime();
                dateString = format1.format(cal.getTime());
                dateTV.setText(dateString);
                launchQuery(dateString);
            }
        });

        viewReportsOnMapToggle = (ToggleButton) v.findViewById(R.id.toggle_map_list_view);
        viewReportsOnMapToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMapView();
            }
        });

        SkywarnWSDBMapper[] reportArray = data.toArray(new SkywarnWSDBMapper[data.size()]);
//        SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(mContext, reportArray);
//        listView.setAdapter(skywarnAdapter);

        return v;
    }


    public void launchMapView() {
        Intent i = new Intent(mContext, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("reportList", data);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void launchViewReportActivity(SkywarnWSDBMapper clickedReport) {
        Intent intent = new Intent(mContext, ViewReportActivity.class);
        Log.i(TAG, "launchViewREport(): ClickedReport.getUserName: " + clickedReport.getUsername());
        intent.putExtra("selectedReport", clickedReport);
        startActivity(intent);
    }

    @Override
    public void processFinish(ArrayList<SkywarnWSDBMapper> result) {
        /*
         *  Once database row is received this interface method runs.
         *  We need to use the data immediately, so send data to listview
         */

        resultList = result;
        data = null;
//      Todo: uncomment code below, its the original that works
        data = result.toArray(new SkywarnWSDBMapper[result.size()]);
        SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(mContext, data);


        //for(int i=0; i< data.length; i++)
        // System.out.println("Received in QueryLauncherActivity: "+data[i].getEventCity() + " " + data[i].getComments());
        listView.setAdapter(skywarnAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SkywarnWSDBMapper itemValue = (SkywarnWSDBMapper) listView.getItemAtPosition(position);
                launchViewReportActivity(itemValue);
            }
        });
    }

    @Override
    public void allQueriesComplete() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        launchQuery(dateString);
//        switch(queryTypeInt){
//            case 0:
//                Log.d(TAG, "Query Type: Top Rated");
//                GetTopRatedReportsTask getTopRatedTaslk = new GetTopRatedReportsTask();
//                getTopRatedTaslk.setDate(dateString);
//                getTopRatedTaslk.setContext(mContext);
//                getTopRatedTaslk.delegate = ViewReportsFromSingleDayFragment.this;
//                getTopRatedTaslk.execute();
//                queryTypeInt = 1;
//                break;
//            case 1:
//                Log.d(TAG, "Query Type: Recent");
//                GetAllRecordsForDayTask getRecordsForDayTask = new GetAllRecordsForDayTask();
//                getRecordsForDayTask.setDate(dateString);
//                getRecordsForDayTask.setContext(mContext);
//                getRecordsForDayTask.delegate = ViewReportsFromSingleDayFragment.this;
//                getRecordsForDayTask.execute();
//                queryTypeInt =0;
//                break;
//
//            case 2:
//                Log.d(TAG, "Query Type: Near Me");
//                queryTypeInt = 2;
//                break;
//        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
//        GetAllRecordsForDayTask getRecordsForDayTask = new GetAllRecordsForDayTask();
//        getRecordsForDayTask.setDate(dateString);
//        getRecordsForDayTask.setContext(mContext);
//        getRecordsForDayTask.delegate = this;
//        getRecordsForDayTask.execute();
        launchQuery(dateString);
    }

    private void launchQuery(String stringDate) {
        switch (queryTypeInt) {
            case 0:
                Log.d(TAG, "Query Type: Top Rated");
                GetTopRatedReportsTask getTopRatedTaslk = new GetTopRatedReportsTask();
                getTopRatedTaslk.setDate(stringDate);
                getTopRatedTaslk.setContext(mContext);
                getTopRatedTaslk.delegate = ViewReportsFromSingleDayFragment.this;
                getTopRatedTaslk.execute();
                break;
            case 1:
                Log.d(TAG, "Query Type: Recent");
                GetAllRecordsForDayTask getRecordsForDayTask = new GetAllRecordsForDayTask();
                getRecordsForDayTask.setDate(stringDate);
                getRecordsForDayTask.setContext(mContext);
                getRecordsForDayTask.delegate = ViewReportsFromSingleDayFragment.this;

                getRecordsForDayTask.execute();
                break;

            case 2:
                Log.d(TAG, "Query Type: Near Me");
                GetNearByReportsTask getNearByReportsTask = new GetNearByReportsTask();
                getNearByReportsTask.setDate(stringDate);
                getNearByReportsTask.setCountyToQuery("Plymouth");
                getNearByReportsTask.setContext(mContext);
                getNearByReportsTask.delegate = ViewReportsFromSingleDayFragment.this;
                getNearByReportsTask.execute();
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mContext = context;
    }
}