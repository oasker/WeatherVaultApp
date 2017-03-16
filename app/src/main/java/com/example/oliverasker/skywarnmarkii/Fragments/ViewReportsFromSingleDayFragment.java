package com.example.oliverasker.skywarnmarkii.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.oliverasker.skywarnmarkii.Activites.MapsActivity;
import com.example.oliverasker.skywarnmarkii.Activites.ViewReportActivity;
import com.example.oliverasker.skywarnmarkii.Adapters.SkywarnDBAdapter;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetAllRecordsForDayTask;
import com.example.oliverasker.skywarnmarkii.Tasks.GetTopRatedReportsTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by oliverasker on 2/19/17.
 */

public class ViewReportsFromSingleDayFragment extends Fragment implements ICallback {
    private ListView listView;
   // MyAsyncTask myAsync = new MyAsyncTask(this);
    private Button addDayButton;
    private Button subtractDayButton;
    private Button viewReportsOnMapToggle;
    private Calendar cal;
    private TextView dateTV;
    private  SkywarnWSDBMapper[] data ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_weather_singleday_listview, container, false);
        GetAllRecordsForDayTask getRecordsForDayTask = null;
        GetTopRatedReportsTask getTopRatedTaslk = null;

//        Bundle b = getArguments();
//        String date = b.getString("date");
//        Log.d(TAG, "DATE::" + date);
        ArrayList<SkywarnWSDBMapper> data = new ArrayList<>();

        cal = Calendar.getInstance();
        final SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        Log.d(TAG, "Date In ViewReports " + String.valueOf(format1.format(cal.getTime())));
        String date = format1.format(cal.getTime());

        //ToDo: Create way to switch between looking at all reports from single day and top rated
        getRecordsForDayTask = new GetAllRecordsForDayTask();
        getRecordsForDayTask.setDate(date);
        getRecordsForDayTask.setContext(getContext());
        getRecordsForDayTask.delegate = this;
        getRecordsForDayTask.execute();

//        getTopRatedTaslk = new GetTopRatedReportsTask();
//        getTopRatedTaslk.setDate(date);
//        getTopRatedTaslk.setContext(getContext());
//        getTopRatedTaslk.delegate = this;
//        getTopRatedTaslk.execute();

        //Set Up Date
//        cal = Calendar.getInstance();
//        final SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
//        Log.d(TAG, "Date In ViewReports " + String.valueOf(format1.format(cal.getTime())));

        dateTV= (TextView)v.findViewById(R.id.date_of_reports_being_submittedTV);
        dateTV.setText(String.valueOf(format1.format(cal.getTime())));

        listView = null;
        listView = (ListView)v.findViewById(R.id.weather_list_view);

        addDayButton = (Button)v.findViewById(R.id.increment_date_by_one_button);
        subtractDayButton = (Button)v.findViewById(R.id.deincrement_day_by_one_button);
        viewReportsOnMapToggle = (ToggleButton)v.findViewById(R.id.toggle_map_list_view);

        addDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal == Calendar.getInstance()){
                    dateTV.setText(format1.format(cal.getTime()));
                    launchGetReportsForOneDayTask(format1.format(cal.getTime()));
                }
                else {
                    cal.add(Calendar.DATE, 1);
                    dateTV.setText(format1.format(cal.getTime()));
                    launchGetReportsForOneDayTask(format1.format(cal.getTime()));
                }
            }
        });

        subtractDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, -1);
                dateTV.setText(format1.format(cal.getTime()));
                launchGetReportsForOneDayTask(format1.format(cal.getTime()));
            }
        });

        viewReportsOnMapToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMapView();
            }
        });



        SkywarnWSDBMapper[]reportArray = data.toArray(new SkywarnWSDBMapper[data.size()]);
        SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(getContext(), reportArray);

        //for(int i=0; i< data.length; i++)
        // System.out.println("Received in QueryLauncherActivity: "+data[i].getEventCity() + " " + data[i].getComments());
//        listView.setAdapter(skywarnAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id ) {
//                int itemPos = position;
//                SkywarnWSDBMapper itemValue = (SkywarnWSDBMapper) listView.getItemAtPosition(position);
//                launchViewReportActivity(itemValue);
//            }
//        });
        return v;
    }

    public void launchMapView(){
        Intent i = new Intent(getContext(), MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("reportList", data);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void launchGetReportsForOneDayTask(String date){
        GetAllRecordsForDayTask getRecordsForDayTask = new GetAllRecordsForDayTask();
        getRecordsForDayTask = new GetAllRecordsForDayTask();
        getRecordsForDayTask.setDate(date);
        getRecordsForDayTask.setContext(getContext());
        getRecordsForDayTask.delegate = this;
        getRecordsForDayTask.execute();
    }
    public void launchViewReportActivity(SkywarnWSDBMapper clickedReport){
        Intent intent = new Intent(getContext(), ViewReportActivity.class);
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
        data = null;

        if(result.size() ==0){
            Toast.makeText(getContext(),"No Reports found",Toast.LENGTH_SHORT).show();
        }
        data = result.toArray(new SkywarnWSDBMapper[result.size()]);
        SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(getContext(), data);

        //for(int i=0; i< data.length; i++)
        // System.out.println("Received in QueryLauncherActivity: "+data[i].getEventCity() + " " + data[i].getComments());
        listView.setAdapter(skywarnAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id ) {
                int itemPos = position;
                SkywarnWSDBMapper itemValue = (SkywarnWSDBMapper) listView.getItemAtPosition(position);
                launchViewReportActivity(itemValue);
            }
        });
        result = null;
    }
}