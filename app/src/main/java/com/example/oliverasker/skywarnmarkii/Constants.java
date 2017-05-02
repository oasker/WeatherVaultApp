package com.example.oliverasker.skywarnmarkii;

/**
 * Created by student on 10/12/16.
 */

public class Constants {
    //////////////////////////////////////////////////////////////////////
    /// DynamoDB Constants
    public static final String IDENTITY_POOL_ID ="us-east-1:75c2afbc-dfba-4e27-bdc0-0d7e65027111";

    //    DB for reports
    public static final String REPORTS_TABLE_NAME = Constants.REPORTS_TABLE_NAME;

    //   DB for Chat
    public static final String CHATDB_TABLE_NAME = "ChatDB";

    //////////////////////////////////////////////////////////////////////



    //////////////////////////////////////////////////////////////////////
    //S3 Constants
    // ****Only use if u change permissions to everyone in console
    public static final String BUCKET_NAME = "skywarntestbucket";
    public static final String MY_ACCESS_KEY_ID = "AKIAI2VVVMGIYYOW2EAQ";
    public static final String MY_SECRET_KEY = "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";
    //////////////////////////////////////////////////////////////////////


    public static final int DOWNLOAD_PHOTO_TASK= 9;
    public static final int REQUESTCODE_WRITE_EXTERNAL_STORAGE = 23;


    //////////////////////////////////////////////////////////////////////
    //                      Cognito Constants                           //
    //////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    // For Skywarn_Dev_Pool
    public static final String cognitoPoolID = "us-east-1_AL5NNXU0B";
    public static final String cognitoAppName = "SkywarnWeatherDatabase_Android";
    public static final String cognitoAppClientId ="1asojk5gin4llm4dmkv98uvs6m";
    public static final String cognitoRegion = "Regions.US_EAST_1";
    //////////////////////////////////////////////////////////////////////




    //////////////////////////////////////////////////////////////////////
    //                      CognitoManager Codes                           //
    //////////////////////////////////////////////////////////////////////
    public static final int SIGN_UP_CODE = 0;
    public static final int SIGN_IN_CODE = 1;
    public static final int CONFIRM_NEW_USER= 2;
    public static final int GET_USER_ATTRIBUTES=5;
    public static final int RESEND_CONFIRMATION_CODE = 6;
    public static final int FORGOT_PASSWORD_CODE = 7;

    public static final String CONFIRM_NEW_USER_STRING= "confirm_new_user";


    public static final int SIGN_UP_FAILED= 1;

    //////////////////////////////////////////////////////////////////////
    //                      Cognito UserHomeActivty Constants           //
    //////////////////////////////////////////////////////////////////////
    public static final int NUM_TAB_ITEMS =3;


    //////////////////////////////////////////////////////////////////////
    //                      Reset Password Constants                    //
    //////////////////////////////////////////////////////////////////////
    public static final int RESET_PW_CODE=1;






    //Confirm User Handler Error Codes
    public static final String CONFIRM_USER_ERROR_WRONG_CODE= "wrong authentication code";
    public static final String CONFIRM_USER_ERROR_ACCOUNT_WITH_EMAIL_ALREADY_EXISTS = "account already exists with email";
    public static final String CONFIRM_USER_ERROR_EXPIRED_CODE = "expired Code";

    //Sign In Error Codes
    public static final String SIGN_IN_ERROR_USER_NOT_AUTHENTICATED = "not autheticated";


    //Sign Up Error Codes
    public static final String SIGN_UP_ERROR_USER_ALREADY_EXISTS = "username is taken";
    public static final String SIGN_UP_USER_NOT_CONFIRMED = "user is not Confirmed";
    public static final String SIGN_UP_USER_IS_CONFIRMED = "user is Confirmed";


    //Get details Handler Error Codes
    public static final String GET_USER_ATTRIBUTES_ERROR_NOT_AUTHENTICATED = "user is not authenticated";


    //  QuertReportAttributesFragments Dialog Constants
    public static final int SELECT_WEATHER_TYPE_DIALOG = 0;
    public static final int SELECT_ATTRIBUTES_TO_QUERY_DIALOG = 1;

}
