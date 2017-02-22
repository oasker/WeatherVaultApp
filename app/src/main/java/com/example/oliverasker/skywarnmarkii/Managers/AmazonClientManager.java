package com.example.oliverasker.skywarnmarkii.Managers;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.oliverasker.skywarnmarkii.Constants;


/**
 * Created by student on 10/12/16.
 */

public class AmazonClientManager {
    private static final String LOG_TAG = "AmazonClientManager";
    private AmazonDynamoDBClient ddb = null;
    private Context context;

    public AmazonClientManager(Context c){
        context = c;
    }

    public AmazonDynamoDBClient ddb(){
        validateCredentials();
        return ddb;
    }
    public boolean hasCredentials(){
        return(!(Constants.IDENTITY_POOL_ID.equalsIgnoreCase("us-east-1:75c2afbc-dfba-4e27-bdc0-0d7e65027111"))||
                Constants.TEST_TABLE_NAME.equalsIgnoreCase("SkywarnWSDB_rev4"));
            //Constants.TEST_TABLE_NAME.equalsIgnoreCase("Test_Table"));
    }
    public void validateCredentials(){
        if(ddb==null)
            initClients();
    }

    private void initClients(){
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                context,
                Constants.IDENTITY_POOL_ID,
                Regions.US_EAST_1);

        ddb= new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));
    }
    public boolean wipeCredentialsonAuthError(AmazonServiceException ex){
        Log.e(LOG_TAG, "Error, wideCredentialsOnAuthError called" + ex);
        return ex.getErrorCode().equals("IncompleteSignature")
                || ex.getErrorCode().equals("InternalFailure")
                || ex.getErrorCode().equals("InvalidClientTokenId")
                || ex.getErrorCode().equals("OptInRequired")
                || ex.getErrorCode().equals("RequestExpired")
                || ex.getErrorCode().equals("ServiceUnavailable")

                // DynamoDB
                // http://docs.amazonwebservices.com/amazondynamodb/latest/developerguide/ErrorHandling.html#APIErrorTypes
                || ex.getErrorCode().equals("AccessDeniedException")
                || ex.getErrorCode().equals("IncompleteSignatureException")
                || ex.getErrorCode().equals(
                "MissingAuthenticationTokenException")
                || ex.getErrorCode().equals("ValidationException")
                || ex.getErrorCode().equals("InternalFailure")
                || ex.getErrorCode().equals("InternalServerError");

    }
}
