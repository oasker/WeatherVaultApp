package com.example.oliverasker.skywarnmarkii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by student on 10/16/16.
 */

public class UserHomeActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        Intent i = getIntent();
        setContentView(R.layout.user_home_activity_layout);

        //Setup actionbar
        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void launchSubmitReportActivity(View v){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, SubmitReportActivity.class);
        startActivity(intent);
    }

    public void launchQueryActivity(View v){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, queryLauncher.class);
        startActivity(intent);
    }
}
