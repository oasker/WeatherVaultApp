package com.example.oliverasker.skywarnmarkii.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;

import java.io.File;

/**
 * Created by oliverasker on 1/15/17.
 *
 *  https://developer.android.com/reference/android/app/Fragment.html
 */

public class UserDetailsFragment extends Fragment {
    private static final String TAG = "UserDetailsFragment";
    private String title;
    private int page;
    private int padding = 5;
    private ImageView profileImageView;
    private Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstsance ){
        mContext = getActivity().getApplicationContext();
        View v = inflater.inflate(R.layout.fragment_user_info_home,container,false);
        profileImageView =(ImageView) v.findViewById(R.id.user_profile_picture_imageview);
        downloadPhoto();
        return v;
    }

    public int getShownIndex(){
        return getArguments().getInt("index",0);
    }

    public void downloadPhoto(){
        Log.d(TAG, "downloadPhoto()");
        String filename = UserInformationModel.getInstance().getUsername()+".png";
//        String externalStorage= Environment.getExternalStorageDirectory().toString()+"/";

        String externalStorage = mContext.getCacheDir().toString() + "/";
        final String filePath = externalStorage+filename;
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext,
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1           // Region
        );

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
        try {
            File file =new File(filePath);
            TransferUtility transferUtility = new TransferUtility(s3Client, mContext);
            TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, filename, file);

            Log.d(TAG, "file.getPath(): " + file.getPath() );

            transferObserver.setTransferListener(new TransferListener(){
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d(TAG, "onStateChanged: + state: "+ state.toString());
                    if(state == TransferState.COMPLETED){
                        Bitmap b = BitmapFactory.decodeFile(filePath);
                        //     horizontalLinearLayout.addView(createImageView(b));
                        profileImageView.setImageBitmap(b);
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


}
