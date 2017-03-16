package com.example.oliverasker.skywarnmarkii.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.UserAttributesCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserCognitoAttributesTask;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserSubmittedPhotosTask;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by oliverasker on 1/17/17.
 */

public class UserInfoHomeFragment extends Fragment implements BitmapCallback, UserAttributesCallback {
    private final static String TAG = "UserInfoHomeFragment";
    private TextView usernameTV;
    private TextView lastActiveTV;
    private TextView numberReportsSubmittedTV;
    private TextView emailTV;
    private TextView nameTV;
    private ImageView profileImage;
    private Button viewUserPhotosButton;
    private Button viewUserReportsButton;
    private Button viewUserInfoButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.user_info_home_fragment_layout,container, false);

        profileImage = (ImageView)v.findViewById(R.id.user_profile_picture_imageview);

        //User report, photo, info buttons
        viewUserInfoButton = (Button)v.findViewById(R.id.view_user_info_button);
        viewUserPhotosButton = (Button)v.findViewById(R.id.view_user_photos_button);
        viewUserReportsButton = (Button)v.findViewById(R.id.view_user_reports_button);

        GetUserCognitoAttributesTask attributesTask = new GetUserCognitoAttributesTask(UserInfoHomeFragment.this);
        // showUserInfoFragment();
        attributesTask.initUserPool(getContext());
        attributesTask.setCognitoUser(UserInformationModel.getInstance().getUserID());
        attributesTask.execute();

        viewUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserCognitoAttributesTask attributesTask = new GetUserCognitoAttributesTask(UserInfoHomeFragment.this);
               // showUserInfoFragment();
                attributesTask.initUserPool(getContext());
                attributesTask.setCognitoUser(UserInformationModel.getInstance().getUserID());
                attributesTask.execute();
            }
        });
        viewUserPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showUserPhotosFragment();
                GetUserSubmittedPhotosTask task = new GetUserSubmittedPhotosTask();
                task.setmContext(getContext());
                task.setBitmapCallback(UserInfoHomeFragment.this);
                task.execute();
            }
        });
        viewUserReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserReportsFragment();
            }
        });
        downloadPhoto();
        return v;
    }


    private void showUserInfoFragment(Map<String,String> attrMap){
        Log.i(TAG, "showUserInfoFragment()");
        Fragment showUserInfoFragment = new UserHomePersonalDetailsFragment();
        Bundle b = new Bundle();
        b.putSerializable("attrMap", (Serializable) attrMap);
        showUserInfoFragment.setArguments(b);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.user_info_frag_container, showUserInfoFragment);
        ft.commit();
    }

    private void showUserPhotosFragment(ArrayList<Bitmap> bitmapArrayList){
        Log.i(TAG, "showUserPhotosFragment");
        Fragment showUserPhotoFragment = new UserHomeUserSubmittedPhotosFragment();

        Bundle b = new Bundle();
        b.putSerializable("bitmapArrayList",bitmapArrayList);
        showUserPhotoFragment.setArguments(b);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.user_info_frag_container, showUserPhotoFragment);
        ft.commit();
    }

    private void showUserReportsFragment(){
        Log.i(TAG, "showUserReportsFragment()");
        Fragment showUserPhotoFragment = new WeatherListViewFragment();
        Bundle b = new Bundle();
        String[] cognitoVals = {"SpotterIDTEST", "CALLSIGNTEST","AffilliationTEST","USERNAMETST"};
        b.putStringArray("cognitoVals" ,cognitoVals);
        showUserPhotoFragment.setArguments(b);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.user_info_frag_container, showUserPhotoFragment);
        ft.commit();
    }

    public TextView getUsernameTV() {
        return usernameTV;
    }

    public void setUsernameTV(TextView usernameTV) {
        this.usernameTV = usernameTV;
    }

    public TextView getLastActiveTV() {
        return lastActiveTV;
    }

    public void setLastActiveTV(TextView lastActiveTV) {
        this.lastActiveTV = lastActiveTV;
    }

    public TextView getNumberReportsSubmittedTV() {
        return numberReportsSubmittedTV;
    }

    public void setNumberReportsSubmittedTV(TextView numberReportsSubmittedTV) {
        this.numberReportsSubmittedTV = numberReportsSubmittedTV;
    }

    public ImageView getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ImageView profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public void processFinish(ArrayList<Bitmap> result) {
        //Pass bitmaps to fragment
        showUserPhotosFragment(result);
    }

    @Override
    public void processFinish(Bitmap result) {

    }

    @Override
    public void processFinish(Uri result) {

    }

    @Override
    public void onProcessFinished( String[] vals) {
        //showUserInfoFragment(mapVals);
        //showUserInfoFragment(vals);
        //Log.i(TAG, "onProcessFinished() : return size"+ mapVals.size());
        Log.d(TAG, "onProceessFinised(): " + vals[0]);
    }

    @Override
    public void onProcessFinished(Map<String,String> vals) {
        //showUserInfoFragment(mapVals);
        UserInformationModel.getInstance().setPhone(vals.get("phone_number"));
        UserInformationModel.getInstance().setAffiliation(vals.get("custom:Affiliation"));
        UserInformationModel.getInstance().setCallsign(vals.get("custom:CallSign"));
        UserInformationModel.getInstance().setSpotterID(vals.get("custom:SpotterID"));
        UserInformationModel.getInstance().setEmail(vals.get("email"));
        UserInformationModel.setFirstName(vals.get("given_name"));
        UserInformationModel.setLastName(vals.get("family_name"));
        //Log.d(TAG, "phoneAFF " + UserInformationModel.getInstance().getAffiliation());

        showUserInfoFragment(vals);
        Log.i(TAG, "onProcessFinished() : CALLSIGN: " + UserInformationModel.getInstance().getCallsign());
        //Log.d(TAG, "onProceessFinised(): " + vals.get("phone_number"));
    }

    public void downloadPhoto(){
        Log.d(TAG, "downloadPhoto()");
//        String filename = UserInformationModel.getInstance().getUsername()+".png";
         String filename = "oasker.png";

        String externalStorage = getContext().getCacheDir().toString()+"/";
        final String filePath = externalStorage+filename;
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1           // Region
        );

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
        try {
            File file =new File(filePath);
            TransferUtility transferUtility = new TransferUtility(s3Client, getContext());
            TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, filename, file);

            Log.d(TAG, "file.getPath(): " + file.getPath() );

            transferObserver.setTransferListener(new TransferListener(){
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d(TAG, "onStateChanged: + state: "+ state.toString());
                    if(state == TransferState.COMPLETED){
                        Bitmap b = BitmapFactory.decodeFile(filePath);
                        //     horizontalLinearLayout.addView(createImageView(b));
                        profileImage.setImageBitmap(b);
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
