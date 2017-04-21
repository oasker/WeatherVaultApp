package com.example.oliverasker.skywarnmarkii.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.oliverasker.skywarnmarkii.Activities.SearchDBActivity;
import com.example.oliverasker.skywarnmarkii.R;

import java.util.HashMap;

/**
 * Created by oliverasker on 2/2/17.
 */

public class ReportListViewFragment extends Fragment implements AdapterView.OnItemClickListener {

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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_report_list_view,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_LONG).show();
    }
}
