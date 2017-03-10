package com.example.oliverasker.skywarnmarkii.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 1/21/17.
 */

public class mapFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        return inflater.inflate(R.layout.fragment_map_layout,container, false);
    }
}
