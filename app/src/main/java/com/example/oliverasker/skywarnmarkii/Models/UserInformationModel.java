package com.example.oliverasker.skywarnmarkii.Models;

import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.Map;

/*
 * Singleton Holding Cogntion Atributes for use, persists through session
 */

public class UserInformationModel {
    private static final String TAG = "UserInformationModel";
//  Provides model for cognito user
//    private static String affiliation="TEST_AFFILIATION";
//    private static String spotterID="TEST SpotterID";
//    private static String callsign="TEST CallSign";
//    private static String firstName = "TEST FirstName";
//    private static String lastName="TEST LastName";

    private static String affiliation="|";
    private static String spotterID="|";
    private static String callsign="|";
    private static String firstName = "|";
    private static String lastName="|";
    private static String phone;
    private static String email;
    //private static String username ="oasker";
    private static String username ="|";

    private static String password;
    private static String userID="|";
    private static int numberOfImages = 0;
    private static int numberOfVideos = 0;

    //  Cognito session and AWS Resources
    private static CognitoCachingCredentialsProvider credentialsProvider;
    private static String idToken;
    private static CognitoUserSession cognitoUserSession;
    private static CognitoUser cognitoUser;
    private static boolean isNewUser;
    private static UserInformationModel userInformationModel = new UserInformationModel();
    public AmazonDynamoDBClient amazonDynamoDBClient;
    private boolean userIsVerified;
    //For passwordReset
    private String newPassword;
    private String pwResetCode;
    private String ratedReportsTextFileContents;

    private UserInformationModel(){}

    public static UserInformationModel getInstance(){
        return userInformationModel;
    }

    public static boolean isNewUser() {
        return isNewUser;
    }

    public static void setIsNewUser(boolean isNewUser) {
        UserInformationModel.isNewUser = isNewUser;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        UserInformationModel.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        UserInformationModel.lastName = lastName;
    }

    public static CognitoCachingCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public static void setCredentialsProvider(CognitoCachingCredentialsProvider credentialsProvider) {
        UserInformationModel.credentialsProvider = credentialsProvider;
    }

    public static String getIdToken() {
        return idToken;
    }

    public static void setIdToken(String idToken) {
        UserInformationModel.idToken = idToken;
    }

    public static CognitoUserSession getCognitoUserSession() {
        return cognitoUserSession;
    }

    public static void setCognitoUserSession(CognitoUserSession cognitoUserSession) {
        UserInformationModel.cognitoUserSession = cognitoUserSession;
    }

    public static int getNumberOfImages() {
        return numberOfImages;
    }

    public static void setNumberOfImages(int numberOfImages) {
        UserInformationModel.numberOfImages = numberOfImages;
    }

    public static int getNumberOfVideos() {
        return numberOfVideos;
    }

    public static void setNumberOfVideos(int numberOfVideos) {
        UserInformationModel.numberOfVideos = numberOfVideos;
    }

    //Set Attributes if User is a certified spotter
    public void setAllAttributes(String Username, String FirstName, String LastName, String Email, String PhoneNumber, String CallSign, String Affiliation, String SpotterID, String Password){
        //general attributes
        username = Username;
        firstName = FirstName;
        lastName = LastName;
        email = Email;
        phone = PhoneNumber;

        //Weather spotter attributes
        affiliation = Affiliation;
        callsign = CallSign;
        spotterID = SpotterID;
        password = Password;
    }

    //    setters/getters
    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        UserInformationModel.affiliation = affiliation;
    }

    public String getSpotterID() {
        return spotterID;
    }

    public void setSpotterID(String spotterID) {
        UserInformationModel.spotterID = spotterID;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        UserInformationModel.callsign = callsign;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        UserInformationModel.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        UserInformationModel.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        UserInformationModel.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        UserInformationModel.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        UserInformationModel.userID = userID;
    }

    public CognitoUser getCognitoUser() {
        return cognitoUser;
    }

    public void setCognitoUser(CognitoUser user) {
        cognitoUser = user;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPwResetCode() {
        return pwResetCode;
    }

    public void setPwResetCode(String pwResetCode) {
        this.pwResetCode = pwResetCode;
    }

    public AmazonDynamoDBClient getAmazonDynamoDBClient() {
        return amazonDynamoDBClient;
    }

    public void setAmazonDynamoDBClient(AmazonDynamoDBClient amazonDynamoDBClient) {
        this.amazonDynamoDBClient = amazonDynamoDBClient;
    }

    public void setCredentialProviderLogins(Map<String,String> logins){
        credentialsProvider.setLogins(logins);
    }

    public void clearAllUserInformation(){
        Log.d(TAG, "clearAllUserInformation()");
        //On logout clear all attributes
        affiliation="";
        spotterID="";
        callsign="";
        firstName="";
        lastName="";
        phone="";
        email="";
        username="";
        password="";
        userID="";
        credentialsProvider=null;
        idToken="";
        cognitoUserSession=null;
        cognitoUser=null;
        amazonDynamoDBClient = null;
    }

    public boolean isUserIsVerified() {
        return userIsVerified;
    }

    public void setUserIsVerified(boolean userIsVerified) {
        this.userIsVerified = userIsVerified;
    }

    public String getRatedReportsTextFileContents() {
        return ratedReportsTextFileContents;
    }

    public void setRatedReportsTextFileContents(String ratedReportsTextFileContents) {
        this.ratedReportsTextFileContents = ratedReportsTextFileContents;
    }
}
