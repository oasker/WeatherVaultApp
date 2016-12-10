package com.example.oliverasker.skywarnmarkii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by student on 10/27/16.
 */

public class ConfirmSubmitReportActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.confirm_submit_report_activity_layout);
    }

    public void launchUserHome(View v){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, queryLauncher.class);
        startActivity(intent);
    }
}
