package com.example.oliverasker.skywarnmarkii.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.oliverasker.skywarnmarkii.Activites.ViewReportActivity;
import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.StringCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Models.SubmittedPhotoModel;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.DownloadPhotoTask;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserSubmittedPhotoNamesTask;

import java.io.File;
import java.util.ArrayList;

/**
/**
* Created by oliverasker on 2/19/17.
*/

public class UserHomeUserSubmittedPhotosFragment extends Fragment implements BitmapCallback, StringCallback,ViewReportActivity.bitmapCallback {
    private static final String TAG = "PersnlPhotosFrag";
    private String username = UserInformationModel.getInstance().getUsername();
    private ArrayList<SubmittedPhotoModel> photoModelList = new ArrayList<>();
    private ViewGroup Containter;
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    private Context mContext;
    private String filePath;
    private ViewGroup container;
    private LinearLayout photoLinearLayout;
    private int imageViewWidth = 650;
    private int imageViewHeight = 650;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup Container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_user_home_submitted_photos, Container, false);
        Log.d(TAG, "onCreateView()");
        container = Container;
        photoLinearLayout = (LinearLayout) v.findViewById(R.id.photoLinearLayout);
        photoModelList = new ArrayList<>();
        mContext = getContext();

        //ToDo:change filename to current user, not placeholder
        GetUserSubmittedPhotoNamesTask task2 = new GetUserSubmittedPhotoNamesTask();
        task2.setmContext(getContext());
        task2.setCallback(this);
        task2.execute();
        return v;
    }

    @Override
    public void processFinish(ArrayList<Bitmap> result) {
        Log.d(TAG, "bitMapcallback processFinished: size: " + result.size());
        for (Bitmap b : result) {
            ImageView iV = new ImageView(getContext());
        }
    }

    @Override
    public void processFinish(Bitmap result) {
        Log.d(TAG, "processFinished(Bitmap)");
        //Log.d(TAG, "processFinished() bitmapSize: "+ result.getByteCount());
        //File f = new File("/data/user/0/com.example.oliverasker.skywarnmarkii/cache/1489603471441_oasker_1.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile("/data/user/0/com.example.oliverasker.skywarnmarkii/cache/1489603471441_oasker_1.jpg");
        ImageView iV = new ImageView(getContext());
        ImageView iV2 = new ImageView(getContext());
        if (result != null) {
            iV.setImageBitmap(result);
            photoLinearLayout.addView(iV);
            iV2.setImageBitmap(bitmap);
            // photoLinearLayout.addView(iV2);
        }
    }

    @Override
    public void processFinish(Bitmap result, String path) {
        Log.d(TAG, "processFinished(Bitmap)");
        Log.d(TAG, "processFinsihed(Bitmap) path: " + path);
        //Log.d(TAG, "processFinished() bitmapSize: "+ result.getByteCount());
        //Bitmap bitmap = BitmapFactory.decodeFile("/data/user/0/com.example.oliverasker.skywarnmarkii/cache/1489603471441_oasker_1.jpg");

//        ImageView iV2 = new ImageView(getContext());
        ImageView iV = new ImageView(mContext);
        if (result != null) {
            iV.setImageBitmap(result);
            photoLinearLayout.addView(iV);
            // iV2.setImageBitmap(bitmap);
            // photoLinearLayout.addView(iV2);
        }
    }

    @Override
    public void processFinish(Bitmap result, ArrayList<String> path) {
        Log.i(TAG, "processFinished(bitmap, arrayList<string>: size() " + path.size());
        for (String s : path) {
            Log.d(TAG, "PATHHH: " + s);
        }
    }

    @Override
    public void processFinish(Uri result) {
        Log.d(TAG, "processFinished(Uri)");
    }


    public void setString(String s) {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    // STRING CALLBACKs
    @Override
    public void onProcessComplete(String s) {
    }

    @Override
    public void onProcessComplete(ArrayList<String> s) {
        Log.d(TAG, "onProcessComplete(ArrayList<String>");
        for (String str : s) {
            Log.d(TAG, str);
            DownloadPhotoTask photoTask = new DownloadPhotoTask();
            photoTask.setmContext(getContext());
            photoTask.setcallback(this);
            //photoTask.execute();
            downloadPhoto(s);
        }
    }

    @Override
    public void onFinishedString(String s) {
        File f = new File(s);
        Bitmap b = BitmapFactory.decodeFile(s);
        ImageView i = new ImageView(getContext());
        i.setImageBitmap(b);

        photoLinearLayout.addView(i);
    }

    public void downloadPhoto(ArrayList<String> paths) {
        Log.d(TAG, "downloadPhoto()");
        for(String s: paths) {
            String filename =s;
            String externalStorage = mContext.getCacheDir().toString() + "/";
            final String filePath = externalStorage + filename;
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getContext(),
                    Constants.IDENTITY_POOL_ID, // Identity Pool ID
                    Regions.US_EAST_1           // Region
            );

            AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
            try {
                File file = new File(filePath);
                TransferUtility transferUtility = new TransferUtility(s3Client, getContext());
                TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, filename, file);
                Log.d(TAG, "file.getPath(): " + file.getPath());

                transferObserver.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        Log.d(TAG, "onStateChanged: + state: " + state.toString());
                        if (state == TransferState.COMPLETED) {
                            Bitmap b = BitmapFactory.decodeFile(filePath);

                            //ToDo: Add text view to user submitted photos giving date photo was taken
                            //TextView dateTV = new TextView(mContext);
                            //s.indexOf("_");
                            //dateTV.setText(Utility.epochToDateTimeString());


                            ImageView v = new ImageView(mContext);
                            v=resizeImageView(imageViewWidth, imageViewHeight, v);
                            v.setImageBitmap(b);
                            photoLinearLayout.addView(v);
                        }
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        int percentage = (int) ((bytesCurrent + 1) / (bytesTotal + 1) * 100);
                        Log.d(TAG, "onStateChanged(); bytesTotal: " + bytesTotal + " bytesCurrent: " + bytesCurrent);
                    }

                    @Override
                    public void onError(int id, Exception ex) {
                        Log.e(TAG, "onError", ex);
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
    }

    public ImageView resizeImageView(int width, int height, ImageView i) {
//        final ImageView picture1 = (ImageView)findViewById(R.id.imageView1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        i.setLayoutParams(layoutParams);
        return i;
    }
}


