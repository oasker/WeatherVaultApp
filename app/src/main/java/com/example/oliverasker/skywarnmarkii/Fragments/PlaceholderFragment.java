package com.example.oliverasker.skywarnmarkii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 2/1/17.
 */

public class PlaceholderFragment extends Fragment {
    private static final String TAG = "PlaceholderFragment";

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_placeholder_layout, container, false);
        Log.d(TAG, "onCreateView");
        return view;
    }
}
