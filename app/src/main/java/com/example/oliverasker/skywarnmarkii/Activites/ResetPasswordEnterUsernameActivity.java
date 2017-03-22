package com.example.oliverasker.skywarnmarkii.Activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 1/18/17.
 */

public class ResetPasswordEnterUsernameActivity extends Activity {
    private final static String TAG = "ResetPWEnterUNActivity";
    private TextView resetUsernameTV;
    private Button submitUsernameButton;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_reset_password_enter_username_layout);


        resetUsernameTV = (TextView)findViewById(R.id.pw_reset_enter_usernameTV);

        submitUsernameButton = (Button)findViewById(R.id.get_username_button);

        submitUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String username = resetUsernameTV.getText().toString();
                if(resetUsernameTV.getText() != null & resetUsernameTV.getText().toString() != ""){
                    String username = resetUsernameTV.getText().toString();
                    launchForgotPassword();
                    UserInformationModel.getInstance().setUserID(username);
                    Log.d(TAG, "submitButton: onClick():username: " + username);
                }
                else{
                    Log.d(TAG, "submitButton: onClick():username == null");
                    //Toast.makeText(this,"Please Enter the username",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void launchForgotPassword(){
        Intent i = new Intent(this, ResetPasswordActivity.class);
        startActivity(i);
    }
}
