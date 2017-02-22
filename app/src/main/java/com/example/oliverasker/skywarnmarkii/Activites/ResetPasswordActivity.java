package com.example.oliverasker.skywarnmarkii.Activites;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 1/18/17.
 */

public class ResetPasswordActivity extends Activity {
    private final static String TAG ="ResetPasswordActivity";

    EditText pwResetConfirmationCodeTV;
    EditText newPasswordTV;
    Button resetButton;
    private String newPassword;
    private String pwResetConfirmationCode;



    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.reset_password_activity_layout);

        /*
        pwResetConfirmationCodeTV = (EditText)findViewById(R.id.reset_pw_confirmation_codeTV);
        newPasswordTV=(EditText)findViewById(R.id.new_passwordTV);

        resetButton = (Button)findViewById(R.id.submit_reset_pw_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPassword = newPasswordTV.getText().toString().trim();
                pwResetConfirmationCode = pwResetConfirmationCodeTV.getText().toString().trim();
            }
        });
        */
        //launchCognitoPWReset();
        newPasswordTV = (EditText)findViewById(R.id.new_passwordTV);
        pwResetConfirmationCodeTV = (EditText)findViewById(R.id.reset_pw_confirmation_codeTV);

        resetButton = (Button)findViewById(R.id.submit_reset_pw_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCognitoPWReset();
            }
        });
    }



    public void launchCognitoPWReset(){
        newPassword = newPasswordTV.getText().toString();
        pwResetConfirmationCode = pwResetConfirmationCodeTV.getText().toString();

        if(newPassword == "" | pwResetConfirmationCode ==""){
            Toast.makeText(this, "Please enter both fields",Toast.LENGTH_LONG).show();
            Log.d(TAG, "password or confirm code is empty");
        }
        if(newPassword != "" & pwResetConfirmationCode != "") {
            UserInformationModel.getInstance().setNewPassword(newPassword);
            UserInformationModel.getInstance().setPwResetCode(pwResetConfirmationCode);
            setResult(RESULT_OK);
            finish();
        }

        //Intent i = new Intent(this, CognitoManager.class);
        //i.putExtra("event", "request_password_reset_code");
        //startActivityForResult(i, Constants.RESET_PW_CODE);

    }



    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        Log.d(TAG, "onActivityResult()");
        if(resultCode == RESULT_OK){
            Log.d(TAG, ": onActivityResult(): ->launching LoginActivity()");
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
        else
            Toast.makeText(this, "Password Reset Failed", Toast.LENGTH_LONG);
        //Log.d(TAG, "Password reset failed");

    }
    */
}
