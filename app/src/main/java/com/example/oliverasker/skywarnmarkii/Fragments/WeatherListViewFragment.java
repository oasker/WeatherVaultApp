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
import android.widget.ListView;

import com.example.oliverasker.skywarnmarkii.Activities.MapsActivity;
import com.example.oliverasker.skywarnmarkii.Activities.ViewReportActivity;
import com.example.oliverasker.skywarnmarkii.Adapters.SkywarnDBAdapter;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetAllUserReportsTask;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

//import android.support.v4.app.Fragment;

/**
 * Created by oliverasker on 2/19/17.
 */

public class WeatherListViewFragment extends Fragment implements ICallback {
    private ListView listView;
    private GetAllUserReportsTask getUserReportsTask;
    private Button mapViewButton;
    private SkywarnWSDBMapper[] data=null;
    private Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_home_weather_list_view, container, false);
        ArrayList<SkywarnWSDBMapper> data = new ArrayList<>();
//        SkywarnWSDBMapper testReport = new SkywarnWSDBMapper();
//        testReport.setDateSubmittedEpoch((long) 3442311);
//        testReport.setDateSubmittedString("2/12/9000");
//        data.add(testReport);


        mapViewButton = (Button)v.findViewById(R.id.toggle_map_list_view);
        mapViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMapActivity();
            }
        });

        getUserReportsTask = new GetAllUserReportsTask();
        getUserReportsTask.setContext(getActivity());
        getUserReportsTask.delegate = this;
        getUserReportsTask.execute();

        listView = null;
        listView = (ListView)v.findViewById(R.id.weather_list_view);
        SkywarnWSDBMapper[]reportArray = data.toArray(new SkywarnWSDBMapper[data.size()]);
        SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(getContext(), reportArray);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getUserReportsTask = new GetAllUserReportsTask();
        getUserReportsTask.setContext(getContext());
        getUserReportsTask.delegate = this;
       // getUserReportsTask.execute();

    }

    public void launchMapActivity(){
        Intent i = new Intent(getContext(), MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("reportList", data);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void launchViewReportActivity(SkywarnWSDBMapper clickedReport){
        Intent intent = new Intent(getContext(), ViewReportActivity.class);
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
        data = result.toArray(new SkywarnWSDBMapper[result.size()]);

        for(int  i =0; i < result.size(); i ++){
            Log.d(TAG,"processFinished(): "+ String.valueOf(result.get(i).getDateSubmittedEpoch()));
        }

        SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(getActivity(), data);

        //for(int i=0; i< data.length; i++)
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

    @Override
    public void allQueriesComplete() {

    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

}