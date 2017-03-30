package com.example.oliverasker.skywarnmarkii.Tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by oliverasker on 2/20/17.
 */

public class GetUserSubmittedPhotosTask extends AsyncTask {
    private static final String TAG = "GetUserPhotosTask";
    Context mContext;
    ListObjectsV2Request req = new ListObjectsV2Request();
    ListObjectsV2Result result;
    String username;
    ArrayList<String> matchingFilesList;
    BitmapCallback bitmapCallback;
    ArrayList<Bitmap> bitmapArrayList;
    ArrayList<String> fileArrayList;
    String filePath;
    File file;
    String tempFilePath;
    LinearLayout photoLayout;
    ArrayList<String> bitMapPaths;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext, // Context
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        bitmapArrayList = new ArrayList<>();
        matchingFilesList = new ArrayList<>();
        bitMapPaths = new ArrayList<>();

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider, clientConfiguration);
        try {
            ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(Constants.BUCKET_NAME);
            ListObjectsV2Result result;
            do {
                username = "oasker";
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

            //Download photos
            try {

                for (String photoName : matchingFilesList) {
                    TransferUtility transferUtility = new TransferUtility(s3Client, mContext);
                    tempFilePath = filePath + photoName;
                    Log.d(TAG, "matchingFileList[0]: " + photoName + " tempFilePath: " + tempFilePath);

                    file = new File(tempFilePath);

                    Log.d(TAG, "NEW FILEPATH: " + file.getAbsolutePath());
                      TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, photoName, file);
                   // TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, photoName, file);

                    try {
                        transferObserver.setTransferListener(new TransferListener() {
                            @Override
                            public void onStateChanged(int id, TransferState state) {
                                Log.d(TAG, "onStateChanged:  state: " + state.toString());
                                if (state == TransferState.COMPLETED) {
                                    Log.i(TAG, "TransferStateCompleted:)");
                                    Bitmap b = BitmapFactory.decodeFile(tempFilePath);
                                    bitMapPaths.add(tempFilePath);
                                   // Log.d(TAG, "onStateChanged: tempFilePath: " + tempFilePath);
                                    bitmapArrayList.add(b);
                                    //bitmapCallback.processFinish(b, tempFilePath);
                                    bitmapCallback.processFinish(b, bitMapPaths);
                                    //  return;
                                }
                            }

                            @Override
                            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                                Log.d(TAG, "onProgressChanged: Current: " + bytesCurrent + " Total: " + bytesTotal);
                            }

                            @Override
                            public void onError(int id, Exception ex) {
                                Log.e(TAG, "onError: " + ex);
                            }
                        });
                    } catch (AmazonServiceException ase) {
                        System.out.println("Caught an AmazonServiceException, " +
                                "which means your request made it " +
                                "to Amazon S3, but was rejected with an error response " +
                                "for some reason.");
                        System.out.println("Error Message:    " + ase.getMessage());
                        System.out.println("HTTP Status Code: " + ase.getStatusCode());
                        System.out.println("AWS Error Code:   " + ase.getErrorCode());
                        System.out.println("Error Type:       " + ase.getErrorType());
                        System.out.println("Request ID:       " + ase.getRequestId());
                    } catch (AmazonClientException ase) {
                        System.out.println("Caught an AmazonClientException, " +
                                "which means the client encountered " +
                                "an internal error while trying to communicate" +
                                " with S3, " +
                                "such as not being able to access the network.");
                    }
                }
            }
            catch(Exception e){
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    @Override
    protected void onPostExecute (Object o){
        //File file = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/bingo.png");
        File file = new File(tempFilePath);
        Bitmap b = BitmapFactory.decodeFile(file.getPath());

        for(Bitmap bitmap: bitmapArrayList) {
            ImageView bitMapIV = new ImageView(mContext);
            bitMapIV.setImageBitmap(bitmap);
           // photoLayout.addView(bitMapIV);
        }
        for (String photoName : matchingFilesList) {
            Log.d(TAG, "matcingFileList: "+ photoName);
        }

//        for(String s: bitMapPaths) {
//            File file2 = new File(s);
//            Bitmap b2 = BitmapFactory.decodeFile(file2.getPath());
//            ImageView bitMapIV = new ImageView(mContext);
//            bitMapIV.setImageBitmap(b2);
//            photoLayout.addView(bitMapIV);
//        }
        //bitmapCallback.processFinish(bitmapArrayList);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //////////////////////////////////////
    //          Setters and Getters     //
    //////////////////////////////////////
    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBitmapCallback(BitmapCallback bitmapCallback) {
        this.bitmapCallback = bitmapCallback;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFile(File file) {
        this.file = file;
    }

}