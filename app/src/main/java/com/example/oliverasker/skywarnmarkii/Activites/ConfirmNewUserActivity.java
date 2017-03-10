package com.example.oliverasker.skywarnmarkii.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Managers.CognitoManager;
import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 1/11/17.
 */

public class ConfirmNewUserActivity extends AppCompatActivity{
    private final static String TAG = "ConfirmNewUserActivity";
    Button submitConfirmationButton;
    Button resendConfirmationCodeButton;
    EditText confirmationCodeTV;


    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_confirm_new_user_layout);

        Log.d(TAG, "onCreate()");

        resendConfirmationCodeButton = (Button)findViewById(R.id.resend_confirmation_code_button);
        resendConfirmationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendConfirmationCode();
            }
        });

        submitConfirmationButton = (Button)findViewById(R.id.submit_confirmation_button);
        submitConfirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmationCodeTV.getText().toString().trim() != ""){
                   confirmUser(confirmationCodeTV.getText().toString());
                }
            }
        });

        confirmationCodeTV = (EditText)findViewById(R.id.confirmation_code_input_TV);
    }

    //Resend confirmation code
    protected void resendConfirmationCode(){
        Intent i = new Intent(this, CognitoManager.class);
        i.putExtra("event", "resend_confirmation_code");
        startActivityForResult(i, Constants.RESEND_CONFIRMATION_CODE);

    }

    protected void confirmUser(String code){
        Intent i = new Intent(this,  CognitoManager.class);
        i.putExtra("code", code);
        i.putExtra("event", Constants.CONFIRM_NEW_USER_STRING);
        startActivityForResult(i, Constants.CONFIRM_NEW_USER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");

        if (resultCode == RESULT_CANCELED) {
            if (data != null) {
                String errorCode = data.getStringExtra("confirmUserReturnString");
                Log.d(TAG, "onActivityResult(): RESULT_CANCELLED: " + errorCode);

                // Wrong confirmation code entered
                if (errorCode.compareToIgnoreCase(Constants.CONFIRM_USER_ERROR_WRONG_CODE)==0) {
                    Toast.makeText(this, "The code entered is incorrect, please try again", Toast.LENGTH_LONG).show();
                }
                if (errorCode.compareToIgnoreCase(Constants.CONFIRM_USER_ERROR_ACCOUNT_WITH_EMAIL_ALREADY_EXISTS) ==0) {
                    Log.d(TAG, errorCode = "RESULT CANCELLED email already linked to account");
                    Toast.makeText(this, "An account using this email already exists", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                }
                if(errorCode.compareToIgnoreCase(Constants.CONFIRM_USER_ERROR_EXPIRED_CODE)==0){
                    Log.d(TAG, "RESULT CANCELLED code has expired "+ errorCode);
                    Toast.makeText(this,"The authentication code provided has expired, click above to send a new one",Toast.LENGTH_LONG).show();
                }
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.GET_USER_ATTRIBUTES) {
                Log.d(TAG, "onACtivityResult RESULT_OK: GET USER ATTRIBUTES");
                String confirmReturnString = data.getStringExtra("confirmUserReturnString");
                Log.d(TAG, "onActivityResult(): confirmReturnString: " + confirmReturnString);

                // Launch user home activity
                Intent userHomeIntent = new Intent(this, TabbedUserHomeActivity.class);
                //startActivity(userHomeIntent);
            }

            if (requestCode==Constants.CONFIRM_NEW_USER) {
                Log.d(TAG, "onActivityResult(): requestCode = CONFIRM_NEW_USER");
                launchUserHome();
            }
        }
    }
    public void launchUserHome(){
    Intent userHomeIntent = new Intent(this, TabbedUserHomeActivity.class);
    startActivity(userHomeIntent);
    }
}
