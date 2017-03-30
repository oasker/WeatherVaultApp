package com.example.oliverasker.skywarnmarkii.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.oliverasker.skywarnmarkii.Callbacks.StringCallback;
import com.example.oliverasker.skywarnmarkii.Constants;

import java.util.ArrayList;

/**
 * Created by oliverasker on 3/29/17.
 */

public class GetUserSubmittedPhotoNamesTask extends AsyncTask<Void,Void,Void> {
    private static final String TAG = "GetPhotoNamesTask";

    private Context mContext;
    private ArrayList<String> matchingFilesList;
    private StringCallback callback;

    @Override
    protected Void doInBackground(Void... ag) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext, // Context
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        matchingFilesList = new ArrayList<>();

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider, clientConfiguration);
        try {
            ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(Constants.BUCKET_NAME);
            ListObjectsV2Result result;
            do {
                String username = "oasker";
                result = s3Client.listObjectsV2(req);
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    Log.i(TAG, "Object Summaries: key: " + objectSummary.getKey()
                            + " size: " + objectSummary.getSize());
                    if (objectSummary.getKey().toString().contains(username)) {
                        Log.i(TAG, "User Submitted Photos Task: " + objectSummary.getKey());
                        matchingFilesList.add(objectSummary.getKey().toString());
                    }
                }
                req.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated() == true);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        for(String s: matchingFilesList)
            Log.d(TAG, s);
        callback.onProcessComplete(matchingFilesList);
    }


    /////////////////////////////////////////////////////
    ////////        Setters/Getters             /////////
    /////////////////////////////////////////////////////
    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setCallback(StringCallback callback) {
        this.callback = callback;
    }
}
