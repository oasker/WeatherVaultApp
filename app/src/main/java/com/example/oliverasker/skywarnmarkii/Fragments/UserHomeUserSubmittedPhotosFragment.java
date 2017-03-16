package com.example.oliverasker.skywarnmarkii.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

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
import com.example.oliverasker.skywarnmarkii.Adapters.UserSubmittedPhotosAdapter;
import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Models.SubmittedPhotoModel;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.google.android.gms.internal.zzs;

import java.io.File;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
* Created by oliverasker on 2/19/17.
*/

public class UserHomeUserSubmittedPhotosFragment extends Fragment implements BitmapCallback {
    private static final String TAG = "PersnlPhotosFrag";
    ListView photoListView;
    private String username = UserInformationModel.getInstance().getUsername();
    ArrayList<SubmittedPhotoModel> photoModelList = new ArrayList<>();
    ViewGroup Containter;
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    Context mContext;
    String filePath;
    //private SubmittedPhotoModel photoModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_user_home_submitted_photos, container, false);
        Log.d(TAG, "onCreateView");
        Containter = container;
        ArrayList<SkywarnWSDBMapper> testList = new ArrayList<>();
        photoModelList = new ArrayList<>();

//        SkywarnWSDBMapper test = new SkywarnWSDBMapper();
//        test.setDateSubmittedEpoch((long)124124);
//        test.setDateSubmittedString("12/23/2343");
//        SkywarnWSDBMapper test2 = new SkywarnWSDBMapper();
//        test2.setDateSubmittedEpoch((long)1243333);
//        test2.setDateSubmittedString("1/42/1993");
//        testList.add(test);

        SubmittedPhotoModel photoModel1 = new SubmittedPhotoModel();
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
        photoModel1.setImage(icon);

        photoModelList.add(photoModel1);


        GetUserSubmittedPhotosTask2 getPhotos = new GetUserSubmittedPhotosTask2();
        getPhotos.setmContext(getContext());
        getPhotos.setUsername(username);
        getPhotos.setBitmapCallback(this);
        //getPhotos.execute();
       // downloadPhoto();



        photoListView = (ListView)v.findViewById(R.id.user_submitted_photos_listview);
//        UserSubmittedPhotosAdapter userPhotoAdapter = new UserSubmittedPhotosAdapter(getContext(), testList);
        UserSubmittedPhotosAdapter userPhotoAdapter = new UserSubmittedPhotosAdapter(getContext(), photoModelList);

        photoListView.setAdapter(userPhotoAdapter);
        v = photoListView.getAdapter().getView(0,null,container );
        return v;
    }

    /*
    public void downloadPhoto(){

        ArrayList<String> matchingFilesList = new ArrayList<>();

        Log.d(TAG, "downloadPhoto()");
        //String filename = map.getDateOfEvent() + "_" + map.getUsername()+"_" + photoNumber + ".jpg";
        String filename = UserInformationModel.getInstance().getUsername() +".png";
        String externalStorage = getContext().getCacheDir().toString()+"/";
        final String filePath = externalStorage+filename;
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1           // Region
        );

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
        try {
            ArrayList<String> fileArrayList = new ArrayList<>();

            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setSignerOverride("AWSS3V4SignerType");

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
                            Log.i(TAG, "User Submitted: " + objectSummary.getKey());
                            matchingFilesList.add(objectSummary.getKey().toString());
                        }
                    }
                    req.setContinuationToken(result.getNextContinuationToken());
                } while (result.isTruncated() == true);
            }catch(AmazonServiceException ase){
                Log.e(TAG, ase.toString());
            }

            File file =new File(filePath);
            TransferUtility transferUtility = new TransferUtility(s3Client, getContext());
            TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, filename, file);

            Log.d(TAG, "file.getPath(): " + file.getPath() );

            transferObserver.setTransferListener(new TransferListener(){
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d(TAG, "onStateChanged: + state: "+ state.toString());
                    if(state == TransferState.COMPLETED){
                        //Log.i(TAG, "number Photos: " +numPhoto++);
                        Bitmap b = BitmapFactory.decodeFile(filePath);
                        ImageView Iv = new ImageView(getContext());
                        Iv.setImageBitmap(b);

                        SubmittedPhotoModel photoModel1 = new SubmittedPhotoModel();
                        photoModel1.setImage(b);

                        photoModelList.add(photoModel1);
                    }
                }
                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    int percentage = (int) ((bytesCurrent+1) /(bytesTotal+1) * 100);
                    Log.d(TAG, "onStateChanged(); bytesTotal: " +bytesTotal +" bytesCurrent: "+ bytesCurrent) ;
                }
                @Override
                public void onError(int id, Exception ex) {
                    Log.e(TAG, "onError",ex);
                }
            });

        } catch (AmazonClientException ace) {
            Log.d(TAG, "Caught an AmazonClientException, which means" +
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            Log.d(TAG, "Error Message: " + ace.getMessage());
        }
    }
*/
    public void setBitMapArray(ArrayList<Bitmap> hashmap){
        bitmapArrayList = hashmap;

    }
    public void setString(String s){

    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void processFinish(ArrayList<Bitmap> result) {
        Log.i(TAG , "bitMapcallback processFinished: size: " + result.size());

    }

    @Override
    public void processFinish(Bitmap result) {

    }

    @Override
    public void processFinish(Uri result) {

    }

    public void setBitmapArrayList(ArrayList<Bitmap> bitmapArrayList) {
        this.bitmapArrayList = bitmapArrayList;
    }

}

 class GetUserSubmittedPhotosTask2 extends AsyncTask {

    Context mContext;
    ListObjectsV2Request req = new ListObjectsV2Request();
    ListObjectsV2Result result;
    String username;
    ArrayList<String> matchingFilesList;
    BitmapCallback bitmapCallback;
    ArrayList<Bitmap> bitmapArrayList;
    ArrayList<String> fileArrayList;
     File file;

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
        ArrayList<String> fileArrayList = new ArrayList<>();

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider,clientConfiguration);
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
                        Log.i(TAG, "User Submitted: " + objectSummary.getKey());
                        matchingFilesList.add(objectSummary.getKey().toString());
                    }
                }
                req.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated() == true);

            //Download photos
            try {
                for(String photoName : matchingFilesList) {
                    String filename = photoName;
                    String externalStorage = Environment.getExternalStorageDirectory().toString() + "/";
                    String filePath = externalStorage + filename;

                    file = new File(filePath);
                    TransferUtility transferUtility = new TransferUtility(s3Client, mContext);
                    TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, filename, file);


                    String bitmapPath = file.getPath();
                    fileArrayList.add(bitmapPath);
                    Log.d(zzs.TAG, "file.getPath(): " + file.getPath());
                    File f = new File(Environment.getExternalStorageDirectory().toString());

                    for (File ff : f.listFiles()) {
                        // Log.d(zzs.TAG, "List Files: ff; " + ff);
                    }

                    transferObserver.setTransferListener(new TransferListener() {
                        @Override
                        public void onStateChanged(int id, TransferState state) {
                            Log.d(zzs.TAG, "onStateChanged: + state: " + state.toString());
                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                            int percentage = (int) (bytesCurrent / (bytesTotal + 1) * 100);
                            Log.d(zzs.TAG, "onStateChanged(); bytesTotal: " + bytesTotal + " bytesCurrent: " + bytesCurrent);
                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            Log.e(zzs.TAG, "onError", ex);
                        }
                    });
                }

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
            } catch (AmazonClientException ace) {
                System.out.println("Caught an AmazonClientException, " +
                        "which means the client encountered " +
                        "an internal error while trying to communicate" +
                        " with S3, " +
                        "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
            }
            return null;
        } catch (AmazonServiceException ase) {

        }
        return null;
    }

    @Override
    protected void onProgressUpdate (Object[]values){
        super.onProgressUpdate(values);
    }
    @Override
    protected void onPostExecute (Object o){
       // File file = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/bingo.png");
        Bitmap b = BitmapFactory.decodeFile(file.getPath());
        bitmapArrayList.add(b);
//        Log.d(zzs.TAG, "onPostExecute");
//        for(String s : fileArrayList) {
////            File file = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/bingo.png");
//            File file = new File(s);
//            Bitmap b = BitmapFactory.decodeFile(file.getPath());
//            bitmapArrayList.add(b);
//        }

        // Log.d(zzs.TAG ,"onPostExecute(): bitmapExists?: " + (bitmap != null));


        Bitmap bb = BitmapFactory.decodeFile(file.getPath());
        ImageView Iv = new ImageView(mContext);
        Iv.setImageBitmap(b);
        SubmittedPhotoModel photoModel1 = new SubmittedPhotoModel();
        photoModel1.setImage(b);
        //photoModelList.add(photoModel1);

        //bitmapCallback.processFinish(bitmapArrayList);
        super.onPostExecute(o);
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

    public BitmapCallback getBitmapCallback() {
        return bitmapCallback;
    }

    public void setBitmapCallback(BitmapCallback bitmapCallback) {
        this.bitmapCallback = bitmapCallback;
    }

}

