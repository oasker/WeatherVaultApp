package com.example.oliverasker.skywarnmarkii;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * Created on 10/22/16.
 *  One of four fragments that that will be dynamically added to submit report form
 */

public class RainFloodSubmitReportFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View view = inflater.inflate(R.layout.rain_flood_submit_fragment, container, false);
        return view;
    }
}
