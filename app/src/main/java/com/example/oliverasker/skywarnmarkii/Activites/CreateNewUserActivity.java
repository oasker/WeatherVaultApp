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
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 1/8/17.
 */

public class CreateNewUserActivity extends AppCompatActivity {
    private final static String TAG = "CreateNewUserActivity";


    UserInformationModel userInformationModel = UserInformationModel.getInstance();

    EditText callsignTV;
    EditText spotterIDTV;
    EditText affiliationTV;
    EditText firstNameTV;
    EditText lastNameTV;
    EditText emailTV;
    EditText phoneTV;
    EditText usernameTV;
    EditText passwordTV;
    Button signUpButton;

    CognitoManager cognitoManager= new CognitoManager();

    private String affiliation;
    private String spotterID;
    private String callsign;
    private String firstName;
    private String lastName;
    private String phone;
    private String password;
    private String email;
    private String username;

    String event = "sign_up";

    @Override
    protected  void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.create_new_user_layout);

        //Assign
        signUpButton = (Button)findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });

        firstNameTV = (EditText)findViewById(R.id.first_name_inputTV);
        lastNameTV = (EditText)findViewById(R.id.last_name_inputTV);
        spotterIDTV = (EditText)findViewById(R.id.weather_spotter_id);
        affiliationTV = (EditText)findViewById(R.id.affiliation_inputTV);
        emailTV = (EditText)findViewById(R.id.email_inputTV);
        phoneTV = (EditText)findViewById(R.id.phone_inputTV);
        usernameTV = (EditText)findViewById(R.id.username_inputTV);
        passwordTV = (EditText)findViewById(R.id.password_inputTV);
    }


    public boolean verifyInputs() {
        //Ensures all textfield inputs are not empty or in wrong format
        boolean verified = true;

        //If any of required fields are not filled in show toast to user and dont launch
        if(firstNameTV.getText().toString().isEmpty()
                |lastNameTV.getText().toString().isEmpty()
                |emailTV.getText().toString().isEmpty()
                |usernameTV.getText().toString().isEmpty()
                |phoneTV.getText().toString().isEmpty()
                |passwordTV.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter all required fields",Toast.LENGTH_LONG).show();
            return false;
        }


        //Verify Email has '@' symbol,  DONE
        // at least one period          DONE
        // no invalid characters (!)       partial
        if (!emailTV.getText().toString().contains("@") | !emailTV.getText().toString().contains(".") | emailTV.getText().toString().contains("!")) {
            Toast.makeText(this, "Please enter a valid email address",Toast.LENGTH_LONG).show();
            Log.d(TAG, "email verified: " + verified);
            return false;
        }

        //Phone Verification
        //Source for phone number verification: http://stackoverflow.com/questions/25763935/how-to-check-phone-number-format-is-valid-or-not-from-telephony-manager
        //matches 9999999999, 1-999-999-9999 and 999-999-9999

        //String regexStr = "^(1\\-)?[0-9]{3}\\-?[0-9]{3}\\-?[0-9]{4}$";
        String regexStr = "[0-9]+";
        if (!phoneTV.getText().toString().matches(regexStr) | phoneTV.getText().toString().length() > 11 |phoneTV.getText().toString().length() < 10) {
            verified = false;
            Log.d(TAG, "LLAMA");
            Toast.makeText(this, "Please enter a valid phone number, including the area code",Toast.LENGTH_LONG).show();
            return false;
        }

        if(!verified)
            Log.d(TAG,"Please Check input fields");
        else
            Log.d(TAG, "Inputs Verified");
//        Toast.makeText(this,toastString, Toast.LENGTH_LONG).show();
        return verified;
    }


    public void createNewUser(){
        if(verifyInputs()) {
            email = emailTV.getText().toString().trim();

            if(phoneTV.getText().toString().trim().length() == 10) {
                phone ="+1"+ phoneTV.getText().toString().trim();
            }
            else if(phoneTV.getText().toString().trim().length() == 11){
                phone = "+" +phoneTV.getText().toString().trim();
            }


            password = passwordTV.getText().toString();
            username = usernameTV.getText().toString();

            if(firstNameTV != null)
                firstName = firstNameTV.getText().toString();
            if(lastNameTV != null)
                lastName = lastNameTV.getText().toString();
            if(callsignTV != null)
                callsign = callsignTV.getText().toString();
            if(affiliationTV != null)
                affiliation = affiliationTV.getText().toString();
            if(spotterIDTV != null)
                spotterID = spotterIDTV.getText().toString();

            //Set singleton attributes
            userInformationModel.setAllAttributes(username, firstName, lastName, email, phone, callsign, affiliation, spotterID, password );
            Log.d(TAG,  " createNewUser(): userInformationModel attributes set");

            Intent intent = new Intent(this, CognitoManager.class);
            intent.putExtra("event", event);
            startActivityForResult(intent, Constants.SIGN_UP_CODE);
        }
    }

    // launches activity where user enters confirmation code that has been emailed to them
    private void launchConfirmNewUserActivity(){
        Intent i = new Intent(this, ConfirmNewUserActivity.class);
        startActivity(i);
    }

    private void launchTabbedUserHomeActivity(){
        Intent i = new Intent(this, TabbedUserHomeActivity.class);
        startActivity(i);
    }

    //Called after CognitoManager processes request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK) {
            //result on sign up
            if (requestCode == Constants.SIGN_UP_CODE) {
                if (data.getStringExtra("isUserConfirmed") != null) {
                    String isUserConfirmed = data.getStringExtra("isUserConfirmed");
                    Log.d(TAG, isUserConfirmed);


                   // if (isUserConfirmed.toString() == Constants.SIGN_UP_USER_NOT_CONFIRMED) {
                    Log.d(TAG, "compare data: "+isUserConfirmed.compareToIgnoreCase(Constants.SIGN_UP_USER_NOT_CONFIRMED));
                     if(isUserConfirmed.compareToIgnoreCase(Constants.SIGN_UP_USER_NOT_CONFIRMED)==0){
                        Log.d(TAG, "onActivityResult(): " + Constants.SIGN_UP_USER_NOT_CONFIRMED);
                        launchConfirmNewUserActivity();
                    }

                    if (isUserConfirmed.compareToIgnoreCase(Constants.SIGN_UP_USER_IS_CONFIRMED)==0) {
                        Log.d(TAG, "onActivityResult(): " + Constants.SIGN_UP_USER_IS_CONFIRMED);
                        launchTabbedUserHomeActivity();
                    }
                }
            }
        }

        if(resultCode == RESULT_CANCELED){
            String errorCode2 = data.getStringExtra("errorCode");
            Log.d(TAG, "onActivityResult() RESULT_CANCELLED: ErrorCode: "+ errorCode2);

            //Username is already taken
            //if(data.getStringExtra("errorCode").toString() == Constants.SIGN_UP_ERROR_USER_ALREADY_EXISTS) {
                Toast.makeText(this, "That username is taken, please enter a different one.", Toast.LENGTH_LONG).show();
                Log.d(TAG, "ASDOFJAOSDFJ");
           // }
        }
    }
}
