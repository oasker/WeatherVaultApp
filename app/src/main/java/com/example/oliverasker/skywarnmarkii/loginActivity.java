package com.example.oliverasker.skywarnmarkii;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class loginActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.login_activity_layout);

    }
    public loginActivity(){

    }

    public void launchUserHome(View view){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, queryLauncher.class);
        //Intent intent = new Intent(this, SearchDBActivity.class);
        startActivity(intent);
    }
}
