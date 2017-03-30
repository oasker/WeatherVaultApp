package com.example.oliverasker.skywarnmarkii.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.oliverasker.skywarnmarkii.Constants;

/**
 * Created by oliverasker on 3/29/17.
 */

public class RateReportTask extends AsyncTask<Void,Void,Void> {
    private Context mContext;
    private static final String TAG = "RateReportTask";


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... params) {

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext, // Context
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider, clientConfiguration);

        TransferUtility transferUtility = new TransferUtility(s3Client, mContext);

        String itemName="";
//        File file = new File();
//        TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, matchingFilesList.get(3), file);
//
//        transferObserver.setTransferListener(new TransferListener() {
//            @Override
//            public void onStateChanged(int id, TransferState state) {
//                Log.d(TAG, "onStateChanged:  state: " + state.toString());
//                if (state == TransferState.COMPLETED) {
//                    Log.i(TAG, "TransferStateCompleted:)");
//                    Bitmap b = BitmapFactory.decodeFile(tempFilePath);
//
//                    Log.d(TAG, "onStateChanged: tempFilePath: " + tempFilePath);
                    //  return;
//                }
//            }
//
//            @Override
//            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                Log.d(TAG, "onProgressChanged: Current: " + bytesCurrent + " Total: " + bytesTotal);
//            }
//
//            @Override
//            public void onError(int id, Exception ex) {
//                Log.e(TAG, "onError: " + ex);
//            }
//        });

        return null;
    }
}
