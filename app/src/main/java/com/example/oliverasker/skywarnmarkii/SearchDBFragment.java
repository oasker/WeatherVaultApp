package com.example.oliverasker.skywarnmarkii;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by student on 10/27/16.
 */

public class SearchDBFragment extends Fragment {

    TextView Rain;
    HashMap<String, String> values;


    // public static WinterViewReportFragment newInstance(HashMap<String,String> m){
    public static SearchDBActivity newInstance(HashMap<String, String> tempMap) {
        SearchDBActivity rainView = new SearchDBActivity();
        Bundle args = new Bundle();
        //args.putString("snowfall", s);
        args.putSerializable("hMap", tempMap);
       // rainView.setArguments(args);
        return rainView;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        //get arguments

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        /*
        Bundle b = this.getArguments();
        if (b != null) {
            //String s = b.getStringArrayList("winterMap","");
            HashMap<String, String> s = (HashMap<String, String>) b.getSerializable("hMap");
        }
        */

        View view = inflater.inflate(R.layout.search_db_fragment, container, false);

        //SET textfields if necessary
        /*
        Rain = (TextView) view.findViewById(R.id.rain_field_tv);
        Rain.setText(values.get("Rain"));
        */
        return view;
    }



}

//Icons provided by: https://icons8.com/web-app/for/android/next