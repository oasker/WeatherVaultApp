package com.example.oliverasker.skywarnmarkii.Managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.InvalidParameterException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.oliverasker.skywarnmarkii.Activites.ConfirmNewUserActivity;
import com.example.oliverasker.skywarnmarkii.Activites.LoginActivity;
import com.example.oliverasker.skywarnmarkii.Activites.ResetPasswordActivity;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Dialoges.ResetPasswordDialog;
import com.example.oliverasker.skywarnmarkii.Dialoges.ResetPasswordEnterCodeDialog;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by oliverasker on 1/7/17.
 * http://docs.aws.amazon.com/cognito/latest/developerguide/tutorial-integrating-user-pools-android.html
 *
 *
 *   Methods for getting userID and username setup
     setUserFromId(setUserID(username));
     UserInformationModel.getInstance().setUserID(userID);
     UserInformationModel.getInstance().setCognitoUser(user);
     signUpUser(userID, phone, email, password);
 *
 */

public class CognitoManager extends Activity implements ResetPasswordDialog.ResetPasswordDialogListener, ResetPasswordEnterCodeDialog.ResetPasswordEnterCodeDialogListener {
    private static final String TAG = "CognitoManager";
    private CognitoUserPool userPool = new CognitoUserPool(this, Constants.cognitoPoolID, Constants.cognitoAppClientId, null);
    private CognitoUser user;
    private int signInUserReturnResult = Constants.SIGN_UP_FAILED;
    ForgotPasswordContinuation forgotPasswordContinuation;

    private AlertDialog alert;

    private String callsign;
    private String affiliation;
    private String spotterID;
    private String username;
    private static String userID;
    private static String email;
    private static String firstName;
    private static String lastName;

    private String phone;
    private String password;
    private String resetPasswordReturnString;
    Context context;

    //For password reset
    private String newPassword;
    private String newPasswordVerificationCode;

    HashMap<String, String> attributeMap = new HashMap<String,String>();

    public CognitoUserPool getUserPool(){
        if(userPool == null){
            CognitoUserPool userPool = new CognitoUserPool(this, Constants.cognitoPoolID, Constants.cognitoAppClientId, null);
        }
        return userPool;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Log.d(TAG, "onCreate()");
        context = getApplicationContext();

        Bundle results = getIntent().getExtras();
        String event = results.getString("event");
        Log.d(TAG, "onCreate(): EVENT: " + event);

        switch (event) {
            case "sign_up":
                //Get attributes from singleton
                password = UserInformationModel.getInstance().getPassword();

                if(UserInformationModel.getInstance().getEmail() != null) {
                    email = UserInformationModel.getInstance().getEmail();
                   attributeMap.put("email", email);
                }

                if(UserInformationModel.getInstance().getPhone() != null) {
                    phone = UserInformationModel.getInstance().getPhone();
                    attributeMap.put("phone_number", phone);
                }

                if(UserInformationModel.getFirstName() != null) {
                    firstName = UserInformationModel.getFirstName();
                    attributeMap.put("given_name", firstName);
                }

                if(UserInformationModel.getLastName() != null) {
                    lastName = UserInformationModel.getLastName();
                    attributeMap.put("family_name", lastName);
                }

                if(UserInformationModel.getInstance().getAffiliation() != null) {
                    affiliation = UserInformationModel.getInstance().getAffiliation();
                    attributeMap.put("custom:Affiliation", affiliation);
                }

                if(UserInformationModel.getInstance().getCallsign() != null) {
                    callsign = UserInformationModel.getInstance().getCallsign();
                    attributeMap.put("custom:CallSign", callsign);
                }
                if(UserInformationModel.getInstance().getSpotterID() != null) {
                    spotterID = UserInformationModel.getInstance().getSpotterID();
                    attributeMap.put("custom:SpotterID", spotterID);
                }

                //Get Username, it is NOT specified like the other attributes tho
                if(UserInformationModel.getInstance().getUsername() != null) {
                    username = UserInformationModel.getInstance().getUsername();
                }


                Log.d(TAG, "userID: " + userID);
                Log.d(TAG, "sign_up:   "
                        + "email: " + email
                        + " firstname: " + firstName
                        + "lastName: "+ lastName
                        + " phone: " + phone
                        + " password: " + password );
                Log.d(TAG, "Create New User: Username: " +userID );

                setUserFromId(setUserID(username));
                UserInformationModel.getInstance().setUserID(userID);
                UserInformationModel.getInstance().setCognitoUser(user);
                signUpUser(userID, phone, email, password);
                Log.d(TAG,"Create New User -> email:" + email + " userID: " + userID);
                break;

            //  SEE TOP NOTES FOR THE CORRECT WAY TO SETUP USERID, USERNAME ...ETC

            case "login":
                determineUserID();

                if(UserInformationModel.getInstance().getEmail() != null){
                    email = UserInformationModel.getInstance().getEmail();
                }
                if(UserInformationModel.getInstance().getPassword() != null){
                    password = UserInformationModel.getInstance().getPassword();
                }

                Log.d(TAG, "email: " + email + "  password: " + password);
                setUserFromId(setUserID(email));

                signInUser();
                break;



            case "confirm_new_user":
                Log.d(TAG, "confirm_new_user");

                //setUserFromId(setUserID(username));
               //userID =  UserInformationModel.getInstance().setUserID();
                UserInformationModel.getInstance().setCognitoUser(user);
                username = UserInformationModel.getInstance().getUsername();
                setUserFromId(userID);
                Log.d(TAG, "case: confirm new user: username: " + username + " email: " + email+ " userID: " +userID);

                String confirmCode = results.getString("code");
                confirmUser(confirmCode);
                break;

            case "get_attributes":
                username = UserInformationModel.getInstance().getUsername();
                userID = UserInformationModel.getInstance().getUserID();
                Log.d(TAG, "get_attributes: username" + username);

                Log.d(TAG, "get_attributes: userId" + userID);
                //setUserFromId(setUserID(email));
                setUserFromId(userID);
                getUserDetails();
                //finish();
                break;

            case "signout":
                signOutUser(setUserFromId(setUserID(username)));
                break;

            case "resend_confirmation_code":
                Log.d(TAG, "resend_confirmation_code");

                Log.d(TAG, "UserModel Email: " + UserInformationModel.getInstance().getEmail());
                Log.d(TAG, "UserModel UserID: " + UserInformationModel.getInstance().getUserID());
                determineUserID();
                requestConfirmationCode();
                finish();
                break;


            case"request_password_reset_code":
                Log.d(TAG, "request_password_reset_code");
                determineUserID();
                getPasswordResetcode();
                //Intent intent = new Intent();
               // intent.putExtra("", resetPasswordReturnString);
                //finish();
                break;
        }
    }


    public String setUserID(String pref_un) {
        long epoch = System.currentTimeMillis();
        //userID = email + "_" + epoch;
        //return userID;
        userID = pref_un;
        return pref_un;
    }
    public CognitoUser setUserFromId(String userId) {
        user = userPool.getUser(userId);
        Log.d(TAG, "USERID: " + user.getUserId());
        return user;
    }

    public void determineUserID(){
        if(UserInformationModel.getInstance().getEmail() != null)
             setUserFromId(setUserID(UserInformationModel.getInstance().getEmail()));

        if(UserInformationModel.getInstance().getUsername() != null)
            setUserFromId(setUserID(UserInformationModel.getInstance().getUsername()));
        Log.d(TAG, "determineUserID() user: " + userID);

        if(UserInformationModel.getInstance().getUserID() != null)
            setUserFromId(setUserID(UserInformationModel.getInstance().getUserID()));
        Log.d(TAG, "determineUserID() user: " + userID);
    }


    ////////////////////////////////////////////////////////////////////
    //       Forgot password: Get reset password code                 //
    ////////////////////////////////////////////////////////////////////
    public void getPasswordResetcode(){
        // Create Handler
        ForgotPasswordHandler handler = new ForgotPasswordHandler() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "ForgotPasswordHandler : onSucchess()");
                resetPasswordReturnString = "reset success";
            }

            @Override
            public void getResetCode(final ForgotPasswordContinuation continuation) {

                Log.d(TAG, "ForgotPasswordHandler : getResetCode()");

                String codeSentTo = continuation.getParameters().toString();
                Log.d(TAG, "ForgotPasswordHanderl getResetCode(): Reset Code was sent to " + codeSentTo);


                Intent i = new Intent(CognitoManager.this, ResetPasswordActivity.class);
                forgotPasswordContinuation  = continuation;
                startActivityForResult(i, Constants.FORGOT_PASSWORD_CODE);

            }
            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "ForgotPasswordHandler : onFailure(): Exception: ",exception);
                resetPasswordReturnString = "reset failed";
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        };
        user.forgotPasswordInBackground(handler);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            forgotPasswordContinuation.setPassword(UserInformationModel.getInstance().getNewPassword());
            forgotPasswordContinuation.setVerificationCode(UserInformationModel.getInstance().getPwResetCode());
            forgotPasswordContinuation.continueTask();
            Log.d(TAG, "PasswordReset onActivityResult()");
            Intent  i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }


    //////////////////////////////////////////////////////////////
    //                  Sign up new Users                  //
    //////////////////////////////////////////////////////////////
    public void signUpUser(String userName, String userPhoneNumber, String userEmail, String userPassword) {

        //Create CognitoUser
        //user = setUserFromId(userEmail);
        CognitoUserAttributes userAttributes = new CognitoUserAttributes();

       /*
        Phone numbers must follow these formatting rules:
        A phone number must start with a plus (+) sign, followed immediately
        by the country code. A phone number can only contain the
        + sign and digits.
        */

        Iterator it = attributeMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Log.d(TAG, pair.getKey().toString() + ":  "+ pair.getValue().toString());
            userAttributes.addAttribute(pair.getKey().toString(), pair.getValue().toString());
        }

        //Callback handler for sign up
        SignUpHandler signUpCallback = new SignUpHandler() {
            @Override
            public void onSuccess(CognitoUser user, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                Intent returnIntent = new Intent();

                if (!userConfirmed) {
                    // confirmation code was sent to user
                    //find out how using cognitoUserCodeDeliveryDetails
                    Log.d(TAG, "signUpCallBack() user is NOT confirmed");

                    returnIntent.putExtra("isUserConfirmed", Constants.SIGN_UP_USER_NOT_CONFIRMED);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                } else {
                    //User has already been confimred
                    Log.d(TAG, "signUpCallBack() user IS confirmed");
                    returnIntent.putExtra("isUserConfirmed", Constants.SIGN_UP_USER_IS_CONFIRMED);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }
                Log.d(TAG, "userID: " + user.getUserId());
            }

            @Override
            public void onFailure(Exception e) {
                String errorCode="";

                //Username already exists
                if(e instanceof com.amazonaws.services.cognitoidentityprovider.model.UsernameExistsException){
                    errorCode = Constants.SIGN_UP_ERROR_USER_ALREADY_EXISTS;
                }

                if(e instanceof com.amazonaws.services.cognitoidentityprovider.model.InvalidParameterException){

                }

                Log.e(TAG, "signupHandler.onFailure()", e);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("errorCode",errorCode);
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        };
        userPool.signUpInBackground(userID, userPassword, userAttributes, null, signUpCallback);
    }


    //////////////////////////////////////////////////////////////
    //                  Confirm new Users                       //
    //////////////////////////////////////////////////////////////
    public void confirmUser(String code) {

        // Create a callback handler for confirming a User
        com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler handler = new com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler() {
            @Override
            public void onSuccess() {
              Log.d(TAG, "confirmUser onSuccess(): ");
                setResult(Activity.RESULT_OK);
                finish();
            }
            @Override
            public void onFailure(Exception e) {
                String errorCode ="";

                Log.d(TAG, "confirmUser(): onFailure(): userID: " + userID + " username: " + username + " user: " + user);
                Log.e(TAG, "confirmUser onFailure(): ", e);


                // HERE SET RETURN STRING SO CONFIRMUSER ACTIVITY KNOWS WHAT TO DISPLAYT TO USER
                // Use code values in constants file to make it easy

                //Empty Text
                if(e instanceof com.amazonaws.services.cognitoidentityprovider.model.InvalidParameterException){
                    Log.d(TAG, "confirmation code is empty");
                }

                //Wrong Confirmation Code
                if(e instanceof  com.amazonaws.services.cognitoidentityprovider.model.CodeMismatchException
                        | e instanceof  com.amazonaws.services.cognitoidentityprovider.model.ExpiredCodeException){
                    Log.d(TAG, " confirmUser(): onFailure():  CodeMismatchException : aka wrong code");
                    errorCode = Constants.CONFIRM_USER_ERROR_WRONG_CODE;
                }

                //Email linked to another account
                if(e instanceof com.amazonaws.services.cognitoidentityprovider.model.AliasExistsException){
                    Log.d(TAG, "confirmUser() onFailure() Account with this email already exists");
                    errorCode = Constants.CONFIRM_USER_ERROR_ACCOUNT_WITH_EMAIL_ALREADY_EXISTS;
                }

                //Confirmation Code expired
                if(e instanceof com.amazonaws.services.cognitoidentityprovider.model.ExpiredCodeException){
                    Log.d(TAG, "confirm User error: CODE EXPIRED");
                    errorCode = Constants.CONFIRM_USER_ERROR_EXPIRED_CODE;
                }


                Intent returnIntent = new Intent();
                returnIntent.putExtra("confirmUserReturnString", errorCode);
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        };
        Log.d(TAG, "right before confirmSignUpInBackground(): userID:  " + userID + "  code:  " + code + "  user: " + user );
        user.confirmSignUpInBackground(code, false, handler);
}


    //////////////////////////////////////////////////////////////
    //                  Resend Confirmation Code                //
    //////////////////////////////////////////////////////////////
    public void requestConfirmationCode(){
        Log.d(TAG, "requestConfirmationCode() userID: " + userID + " email: " + email + " username: " + username );
        // create a callback handler for the confirmation code request
        VerificationHandler verificationHandler = new VerificationHandler() {
            @Override
            public void onSuccess(CognitoUserCodeDeliveryDetails verificationCodeDeliveryMedium) {
                Log.d(TAG, "requestConfirmationCode() onSuccess");
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "requestConfirmationCode() onFailure",exception);
               try {
               }
                   catch(InvalidParameterException er){
                       Log.e(TAG, "requestConfirmationCode() onFailure inside InvaldParameters catch" );
                   }
            }
        };
        user.resendConfirmationCodeInBackground(verificationHandler);
    }



    //////////////////////////////////////////////////////////////
    //                  Sign in existing Users                  //
    //////////////////////////////////////////////////////////////
    public void signInUser() {
        AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
            String mfaVerificationCode = "";
            String signInFailureString="";

            @Override
            public void onSuccess(CognitoUserSession userSession) {
                //signInUserReturnResult = Constants.SIGN_UP_CONFIRMED;
               // UserInformationModel.getInstance().setUserIsVerified(true);
                //  Code below gives User singleton credentials to access other AWS resources
                //  (DynamoDB, S3...etc)

                UserInformationModel.setCognitoUserSession(userSession);

                CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(), Constants.cognitoPoolID, Regions.US_EAST_1);
                Map<String,String> logins = new HashMap<String, String>();
                logins.put("cognito-idp:us-east-1:754079778705:userpool/us-east-1_WRGmqh79l", userSession.getAccessToken().getJWTToken());
                UserInformationModel.setCredentialsProvider(credentialsProvider);

                AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
                UserInformationModel.getInstance().setAmazonDynamoDBClient(ddbClient);

                Log.d(TAG, "Login successful: AuthenticationHandler onSuccess() " + userSession.getIdToken().toString());
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String UserId) {
                Log.d(TAG, "getAuthenticationDetails() authContinutation:UserId: " +UserId);
                AuthenticationDetails authenticationDetails;
                // AuthenticationDetails authenticationDetails = new AuthenticationDetails(email, password, null);
                String userIdString ="";

                if(UserId != null) {
                     //authenticationDetails = new AuthenticationDetails(UserId, password, null);
                    userIdString = UserId;
                }
                else if(UserInformationModel.getInstance().getEmail() != null & UserInformationModel.getInstance().getEmail() != "") {
                     //authenticationDetails = new AuthenticationDetails(email, password, null);\
                    userIdString = UserInformationModel.getInstance().getEmail();
                }
                else if(UserInformationModel.getInstance().getUserID() != null & UserInformationModel.getInstance().getUserID() != "") {
                     //authenticationDetails = new AuthenticationDetails(userID, password, null);
                    userIdString = UserInformationModel.getInstance().getUserID();
                }

                authenticationDetails = new AuthenticationDetails(userIdString, password, null);
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                authenticationContinuation.continueTask();

                Intent i = new Intent();
                i.putExtra("login_success_or_failure", signInUserReturnResult);
                setResult(Activity.RESULT_OK,i);
                finish();
            }
            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                continuation.setMfaCode(mfaVerificationCode);
                continuation.continueTask();
            }

            @Override
            public void onFailure(Exception e) {
                String errorCode="";

                    UserInformationModel.getInstance().setUserIsVerified(false);
                   // Log.d(TAG, e.getErrorCode());

                    //User is not authenticated
                    if(e instanceof com.amazonaws.mobileconnectors.cognitoidentityprovider.exceptions.CognitoNotAuthorizedException){
                        Log.d(TAG, "signUpUser() onFailure() user not authenticated");
                        errorCode = Constants.SIGN_IN_ERROR_USER_NOT_AUTHENTICATED;
                    }


                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("errorCode", errorCode);
                    setResult(RESULT_CANCELED,returnIntent);
                    finish();
            }
        };
        user.getSessionInBackground(authenticationHandler);
    }




    //////////////////////////////////////////////////////////////
    //                  Sign Out Users                          //
    //////////////////////////////////////////////////////////////
    public void signOutUser(CognitoUser user){
        Log.d(TAG, "user signed out");
        user.signOut();
    }



    public void getAttributeVerificationCode(String attributeName){
        VerificationHandler handler = new VerificationHandler (){
            @Override
            public void onSuccess(CognitoUserCodeDeliveryDetails verificationCodeDeliveryMedium) {

            }

            @Override
            public void onFailure(Exception exception) {
                // Attribute verification code request failed, probe exception for details
            }
        };
        user.getAttributeVerificationCode(attributeName, handler);
    }


    //////////////////////////////////////////////////////////////
    //                 Get User Attributes                      //
    //////////////////////////////////////////////////////////////
    public void getUserDetails() {
        final String[] attributes=null;
        //Get User Details
        GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
            @Override
            public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                Map userAttr = cognitoUserDetails.getAttributes().getAttributes();
                Utility.printMap(userAttr);

                Iterator it = userAttr.entrySet().iterator();
                int index =0;
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    //System.out.println("AttrMap contains: "+pair.getKey() + " = " + pair.getValue());
                    Log.d(TAG, "AttrMap contains: " + pair.getKey() + " = " + pair.getValue());
                    attributes[index] = pair.getValue().toString();
                    it.remove(); // avoids a ConcurrentModificationException
//                    index++;
//                    UserInformationModel.getInstance().setPhone(attributes[index]);
                }
                index = 0;

                Log.d(TAG, "phone number: "+userAttr.get("phone_number"));
               // UserInformationModel.getInstance().setPhone(userAttr.get("phone_number").toString());

                Log.d(TAG, "getDetailsHandler(): username: " + username + "  callsign: " + callsign);
                for(int i =0; i <userAttr.size(); i++){
                    attributes[i] =(String)userAttr.get(i);
                    Log.d(TAG, "onSuccessHandler"+attributes[i]);
                }
                Bundle bundle = new Bundle();
                bundle.putStringArray("attributeArray", attributes);
                Intent i = new Intent();
                i.putExtras(bundle);
                setResult(RESULT_OK,i);
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                String errorCode ="";
                Log.e(TAG, "getDetailsHandler failed: ", e);
                if( e instanceof com.amazonaws.mobileconnectors.cognitoidentityprovider.exceptions.CognitoNotAuthorizedException ){
                    errorCode = Constants.GET_USER_ATTRIBUTES_ERROR_NOT_AUTHENTICATED;
                }
                //launchConfirmUser();
                Intent i = new Intent();
                i.putExtra("errorCode",errorCode);
                setResult(RESULT_CANCELED,i);
                finish();
            }
        };

        user.getDetailsInBackground(getDetailsHandler);
    }

    public void launchConfirmUser(){
        Intent i = new Intent(this,ConfirmNewUserActivity.class);
        startActivity(i);
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

