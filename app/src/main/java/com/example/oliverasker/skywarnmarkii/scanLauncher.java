package com.example.oliverasker.skywarnmarkii;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by oliverasker on 12/3/16.
 */

public class scanLauncher extends queryLauncher implements ICallback {

    public void performFullScan(){
        HashMap<String, String> s = new HashMap<String, String>();
        MyScanAyncTask myAsync = new MyScanAyncTask();
        myAsync.delegate = this;
        myAsync.execute(s);
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
