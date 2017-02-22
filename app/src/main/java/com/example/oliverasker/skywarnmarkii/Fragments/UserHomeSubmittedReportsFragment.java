package com.example.oliverasker.skywarnmarkii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 2/19/17.
 */

public class UserHomeSubmittedReportsFragment extends Fragment {
    private static final String TAG = "PersnlSubmittedReportsFrag";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_user_home_submitted_reports, container, false);

        return v;
    }

}