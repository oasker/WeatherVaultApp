package com.example.oliverasker.skywarnmarkii.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.oliverasker.skywarnmarkii.Adapters.SkywarnDBAdapter;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 4/19/17.
 */

public class ViewQueryResultsActivity extends Activity {
    private static final String TAG = "ViewQuerResultAct";
    private SkywarnWSDBMapper[] queryResult;
    private ToggleButton viewReportsOnMapToggle;

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_list_view);
        Log.d(TAG, "onCreate()");

        listView = (ListView) findViewById(R.id.weather_list_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getSerializable("queryResultsArray") != null) {
            queryResult = (SkywarnWSDBMapper[]) bundle.getSerializable("queryResultsArray");
            if (queryResult.length == 0) {
                Toast.makeText(this, "No Reports found", Toast.LENGTH_SHORT).show();
            }

            SkywarnDBAdapter skywarnAdapter = new SkywarnDBAdapter(this, queryResult);
            listView.setAdapter(skywarnAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SkywarnWSDBMapper itemValue = (SkywarnWSDBMapper) listView.getItemAtPosition(position);
                    launchViewReportActivity(itemValue);
                }
            });
        }

//        Setup map view button
        viewReportsOnMapToggle = (ToggleButton) findViewById(R.id.toggle_map_list_view);
        viewReportsOnMapToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMapView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void launchViewReportActivity(SkywarnWSDBMapper clickedReport) {
        Intent intent = new Intent(this, ViewReportActivity.class);
        Log.i(TAG, "launchViewREport(): ClickedReport.getUserName: " + clickedReport.getUsername());
        intent.putExtra("selectedReport", clickedReport);
        startActivity(intent);
    }

    public void launchMapView() {
        Intent i = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("reportList", queryResult);
        i.putExtras(bundle);
        startActivity(i);
    }

}
