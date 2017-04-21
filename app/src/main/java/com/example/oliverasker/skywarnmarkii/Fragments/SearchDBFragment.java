package com.example.oliverasker.skywarnmarkii.Fragments;

//import android.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.oliverasker.skywarnmarkii.Activities.SearchDBActivity;
import com.example.oliverasker.skywarnmarkii.R;

import java.util.HashMap;

/**
 * Created by student on 10/27/16.
 */

public class SearchDBFragment extends Fragment {
    private static final String TAG = "SearchDBFragment";
    EditText zip;
    EditText city;
    EditText street;
    EditText state;
   //Date Fields and Values
    DatePicker startDateRange;
    DatePicker endDateRange;
    Button submitButton;
    HashMap<String, String> testAttr = new HashMap<String, String>();
    private int startMonth;
    private int endMonth;
    private int startDay;
    private int endDay;
    private int startYear;
    private int endYear;
    private OnItemSelectedListener listener;
    private int childCount;

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
    public void onAttach(Context context){
        super.onAttach(context);
        /*
        if(context instanceof OnItemSelectedListener){
            listener = (OnItemSelectedListener)context;
        }else {
            throw new ClassCastException(context.toString()
                    + "must implement onitemSeletedListener");
        }
        */
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.search_db_fragment, container, false);
        return view;
    }

    public interface OnItemSelectedListener {
        HashMap<String, String> sup = new HashMap<String, String>();

        void onSubmitScan();

        void onSubmitScan(HashMap<String, String> tempMap);
    }
}

//Icons provided by: https://icons8.com/web-app/for/android/next