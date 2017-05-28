package com.example.oliverasker.skywarnmarkii.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.oliverasker.skywarnmarkii.Callbacks.BooleanCallback;
import com.example.oliverasker.skywarnmarkii.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliverasker on 3/29/17.
 */
public class UpdateReportRatingTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "RateReportTask";
    private static int reportRating;
    //Variables for S3 calls
    private Context mContext;
    private String reportName;
    private String filePath;
    private String url;
    private String username;
    private String vote;
    private String allText;
    private BooleanCallback callback;
    private boolean userAlreadyRated;

    //Variables for DynamoDB calls
    private AmazonDynamoDBClient ddb;
    private String reportPrimaryKey;
    private String rangeKey;

    public static void updateItem(AmazonDynamoDBClient client, String tableName, String primaryKey, String rangeKey, String val) {
        reportRating = Integer.parseInt(val);
        Log.d(TAG, "primaryKey: " + primaryKey + " rangeKey: " + rangeKey + " val: " + val);

//      Update report in dynamodb
        java.util.Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("DateSubmittedString", new AttributeValue().withS(primaryKey));
        key.put("DateSubmittedEpoch", new AttributeValue().withN(rangeKey));

        Map<String, AttributeValueUpdate> attributeUpdates = new HashMap<String, AttributeValueUpdate>();

        AttributeValueUpdate update = new AttributeValueUpdate()
                .withAction(AttributeAction.PUT)
                .withValue(new AttributeValue().withN(val));

        attributeUpdates.put("NetVote", update);

        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withAttributeUpdates(attributeUpdates);
        client.updateItem(updateItemRequest);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        callback.UserHasRatedReport(userAlreadyRated, reportRating);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... params) {
        url = "https://s3.amazonaws.com/skywarntestbucket/" + username + ".txt";

//      Update NetVote value in DynamoDB
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                mContext,
                Constants.IDENTITY_POOL_ID,
                Regions.US_EAST_1);

        ddb = new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));

        switch (vote) {
            case "-D":
                reportRating--;
                break;
            case "-U":
                reportRating++;
                break;
        }

        updateItem(ddb, Constants.REPORTS_TABLE_NAME, reportPrimaryKey, rangeKey, String.valueOf(reportRating));

        return null;
    }


    private void writeToTextFile(String DIRECTORY_PATH, String fileName, String textToAdd) {
        Log.d(TAG, "writeToTextFile(): fileName: " + DIRECTORY_PATH + fileName);

        File myFile = new File(DIRECTORY_PATH, fileName);
        try {
            PrintWriter out = new PrintWriter(new FileWriter(myFile, true));

            out.append(allText);
            out.append('\n');
            out.append(textToAdd);
            out.append('\n');
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadRatingTextFile(String uploadFileName, String keyName) {
        Log.d(TAG, "uploadRatingTextFile() uploadFileName: " + uploadFileName);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext,
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1           // Region
        );

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);

        TransferUtility transferUtility = new TransferUtility(s3Client, mContext);

        File file = new File(uploadFileName);
        TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, keyName, file);

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d(TAG, "onStateChanged: state " + state);
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Log.d(TAG, "onProgressChanged(): current: " + bytesCurrent + " total: " + bytesTotal);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d(TAG, "onError(): error: " + ex);
            }
        });
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
//            //File file = new File(uploadFileName);
//            Log.d(TAG, "keyName: " + keyName + " file.getPath(): " + file.getPath());
//            s3Client.putObject(new PutObjectRequest(
//                    Constants.BUCKET_NAME, keyName, file));
//            PutObjectRequest request = new PutObjectRequest(
//                    Constants.BUCKET_NAME,
//                    keyName,
//                    file
//            );

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            // ... error handling based on exception details
        }
    }

    //////////////////////////////////////////
    ///////     Getters/Setters         /////
    /////////////////////////////////////////

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setUrl(String url) {
        this.url = "https://s3.amazonaws.com/skywarntestbucket/" + url + ".txt";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public void setReportRating(int reportRating) {
        UpdateReportRatingTask.reportRating = reportRating;
    }

    public void setReportPrimaryKey(String reportPrimaryKey) {
        this.reportPrimaryKey = reportPrimaryKey;
    }

    public void setRangeKey(String rangeKey) {
        this.rangeKey = rangeKey;
    }

    public void setCallback(BooleanCallback callback) {
        this.callback = callback;
    }
}