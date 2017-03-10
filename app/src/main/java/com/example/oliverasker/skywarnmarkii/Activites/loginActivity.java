package com.example.oliverasker.skywarnmarkii.Activites;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Dialoges.ResetPasswordDialog;
import com.example.oliverasker.skywarnmarkii.Dialoges.ResetPasswordEnterCodeDialog;
import com.example.oliverasker.skywarnmarkii.Managers.CognitoManager;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;

public class LoginActivity extends AppCompatActivity implements ResetPasswordDialog.ResetPasswordDialogListener, ResetPasswordEnterCodeDialog.ResetPasswordEnterCodeDialogListener {

    private EditText emailTV;
    private EditText passwordTV;
    private Button signUpButton;
    private Button signInButton;
    private TextView forgotPasswordTV;

    private String email;
    private String password;
    private static final String TAG = "LoginActivity";

    private boolean inputFieldsVerified = true;
    // Holds message to show user if input is incorrect
    StringBuilder logStatus= new StringBuilder();

    public LoginActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        //Initialize widgets
        setContentView(R.layout.activiy_login_layout);
        emailTV = (EditText) findViewById(R.id.login_email_tv);


        passwordTV = (EditText) findViewById(R.id.login_password_tv);

        forgotPasswordTV = (TextView)findViewById(R.id.forgot_passwordTV);
        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchForgotPasswordActivity();
            }
        });

        signInButton = (Button) findViewById(R.id.login_button_text);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanchSignInActivity();
            }
        });

        signUpButton = (Button) findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInformationModel.setIsNewUser(true);
                launchSignUp(view);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult()");

            if(resultCode == RESULT_CANCELED & requestCode == Constants.SIGN_IN_CODE){
                Log.d(TAG, " SIGN IN RESULT CANCELLED");
                //Toast.makeText(this, "Invaled Login/Password Combination",Toast.LENGTH_LONG).show();
                Log.d(TAG, data.getStringExtra("errorCode"));
            }

            if(resultCode == RESULT_OK & requestCode == Constants.SIGN_IN_CODE){
                Log.d(TAG, " SIGN IN RESULT OK");
                launchUserHomeActivity();
            }
//            if (requestCode == Constants.SIGN_IN_CODE) {
//                /*
//               if(UserInformationModel.getInstance().isUserIsVerified()){
//                   launchUserHomeActivity();
//               }
//                else {
//                   Log.d(TAG, "determineIfLoginVerified() failed");
//               }
//               */
//
//            }
        }
    }

    public void launchForgotPasswordActivity(){
        Log.d(TAG, "launchForgotPasswordGetUsernameActivity()");
        //Received help from this link:
            //  http://stackoverflow.com/questions/6626006/android-custom-dialog-cant-get-text-from-edittext
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View inflator = layoutInflater.inflate(R.layout.dialog_reset_password_layout,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Recover Password");
        //alert.setMessage("Message?");
        alert.setView(inflator);

        final EditText usernameInput = (EditText)inflator.findViewById(R.id.reset_password_username_inputTV);

        alert.setPositiveButton("Request Password Reset Code", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userInput = usernameInput.getText().toString();
                Log.d(TAG, "Recover Password Dialog onPositiveClick()  username: " + userInput);
                if(userInput != "") {
                    UserInformationModel.getInstance().setUserID(userInput);

                    Intent intent = new Intent(LoginActivity.this, CognitoManager.class);
                    intent.putExtra("event", "request_password_reset_code");
                    startActivity(intent);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "Recover Password Dialog onNegativeClick()");
                dialogInterface.cancel();
            }
        });
        alert.show();


//        Intent i = new Intent(this, CognitoManager.class);
//        i.putExtra("event", "request_password_reset_code");
//        startActivity(i);
    }

    public boolean verifyInputs() {
        inputFieldsVerified= true;
        if ((emailTV == null | passwordTV == null )| ( emailTV.getText().toString()=="" | passwordTV.getText().toString()=="")) {
            inputFieldsVerified = false;
            Toast.makeText(this,"Please enter a username and a password",Toast.LENGTH_LONG).show();
            logStatus.append("emailTV or passwordTV is null");
            return false;
        }

        //Must fill in both fields
        if ( emailTV.getText().toString().isEmpty() | passwordTV.getText().toString().isEmpty()){
            logStatus.append( "Please Enter Username and Password");
            return false;
        }
        //Password must be at least 8 digits long
        if(passwordTV.getText().toString().length() < 8){
            logStatus.append("Please Enter a valid Username and Password");
            return false;
        }
        else
            return true;
    }

    public void lanchSignInActivity() {

        if (verifyInputs()) {
            email = emailTV.getText().toString();
            password = passwordTV.getText().toString();
            UserInformationModel.setIsNewUser(false);

            UserInformationModel.getInstance().setUserID(email);
            UserInformationModel.getInstance().setEmail(email);
            UserInformationModel.getInstance().setUsername(email);
            UserInformationModel.getInstance().setPassword(password);

            Intent i = new Intent(this, CognitoManager.class);
            i.putExtra("event", "login");
            startActivityForResult(i, Constants.SIGN_IN_CODE);
        }
        else{
            Toast.makeText(this, logStatus,Toast.LENGTH_LONG).show();
        }
    }

    public void launchUserHomeActivity() {
        Intent i = new Intent(this, TabbedUserHomeActivity.class);
        //Intent i = new Intent(this, UserHomeActivity.class);
        startActivity(i);
    }

    public void launchSignUp(View view) {
        UserInformationModel.setIsNewUser(true);
        Intent intent = new Intent(this, CreateNewUserActivity.class);
        startActivity(intent);
    }

    public void launchUserHome(View view) {

        //1. Verify fields are filled in properly
        //2. Verify using cognito

        if (inputFieldsVerified) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, TabbedUserHomeActivity.class);
            startActivity(intent);
        }
    }


    //  Listeners for dialog where user actually enters username to set UserID so
    // Cognito can send code
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.d(TAG, "onDialogPositiveClick() " );
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.d(TAG, "onDialogNegativeClick() " );
    }

    //  Listeners for dialog where user actually enters new passowrd verification code and new password
    //  to reset password
    @Override
    public void onEnterCodeDialogPositiveClick(DialogFragment dialog) {
        Log.d(TAG, "onEnterCodeDialogPositiveClick() " );
    }

    @Override
    public void onEnterCodeDialogNegativeClick(DialogFragment dialog) {
        Log.d(TAG, "onEnterCodeDialogNegativeClick() " );
    }

}
