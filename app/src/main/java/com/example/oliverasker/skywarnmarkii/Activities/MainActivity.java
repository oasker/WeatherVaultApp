package com.example.oliverasker.skywarnmarkii.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.oliverasker.skywarnmarkii.Managers.AmazonClientManager;
import com.example.oliverasker.skywarnmarkii.R;


public class MainActivity extends AppCompatActivity {
    public static AmazonClientManager clientManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup client manager
        clientManager = new AmazonClientManager(this);

        Intent intent = new Intent(this, LoginActivity.class);
//        Intent intent = new Intent(this, TabbedUserHomeActivity.class);
//        Intent intent = new Intent(this, QueryLauncherActivity.class);
//        Intent intent =new Intent(this, LaunchCameraActivity.class);
//        Intent intent = new Intent(this,SubmitReportActivity.class);
//        Intent intent = new Intent(this, VideoCameraTest.class);
//        Intent intent = new Intent(this, SubmitMultipleReportsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
