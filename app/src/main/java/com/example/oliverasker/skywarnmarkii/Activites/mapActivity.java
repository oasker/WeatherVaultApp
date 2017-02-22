package com.example.oliverasker.skywarnmarkii.Activites;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 1/21/17.
 */

public class mapActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.map_activity_layout);
        /*
        if(findViewById(R.id.fragment_container) !=null){
            if(savedInstance != null){
                return;
            }
            mapFragment mapFrag = new mapFragment();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, mapFrag);
            ft.addToBackStack(null);
            ft.commit();

        }
        */
    }
}
