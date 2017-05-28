package com.example.oliverasker.skywarnmarkii.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by student on 10/27/16.
 */

public class ConfirmSubmitReportActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_confirm_submit_report_layout);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.confirm_submit_report_container,reviewFrag);
//        ft.commit();

    }

    public void launchUserHome(View v){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, TabbedUserHomeActivity.class);
        startActivity(intent);
    }
}
