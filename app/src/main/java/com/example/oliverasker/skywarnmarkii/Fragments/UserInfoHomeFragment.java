package com.example.oliverasker.skywarnmarkii.Fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.UserAttributesCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserCognitoAttributesTask;
import com.example.oliverasker.skywarnmarkii.Utility.BitmapUtility;
import com.koushikdutta.ion.Ion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

//import android.support.v4.app.Fragment;

/**
 * Created by oliverasker on 1/17/17.
 */

public class UserInfoHomeFragment extends Fragment implements BitmapCallback, UserAttributesCallback {
    private final static String TAG = "UserInfoHomeFragment";

    private Button viewUserPhotosButton;
    private Button viewUserReportsButton;
    private Button viewUserInfoButton;

    private ImageView profileImage;
    private int profileImageViewWidth=300;
    private int profileImageViewHeight= 300;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_user_info_home,container, false);

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
//                GetUserSubmittedPhotosTask task = new GetUserSubmittedPhotosTask();
//                task.setmContext(getContext());
//                task.setBitmapCallback(UserInfoHomeFragment.this);
//
//                File cache = getContext().getCacheDir();
//                File appDir= new File(cache+"/bingo.jpg");
//                task.setFile(appDir);
//                task.setFilePath(cache+"/bingo.jpg");
//                task.execute();
                showUserPhotosFragment();

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
        Fragment showUserInfoFragment = new Fragment();
        Bundle b = new Bundle();
        b.putSerializable("attrMap", (Serializable) attrMap);
        showUserInfoFragment.setArguments(b);
        android.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.user_info_frag_container, showUserInfoFragment);
        ft.commit();
    }

    private void showUserPhotosFragment(ArrayList<Bitmap> bitmapArrayList){
        Log.i(TAG, "showUserPhotosFragment");
        Fragment showUserPhotoFragment = new UserHomeUserSubmittedPhotosFragment();

        Bundle b = new Bundle();
        b.putSerializable("bitmapArrayList",bitmapArrayList);
        showUserPhotoFragment.setArguments(b);
        android.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.user_info_frag_container, showUserPhotoFragment);
        ft.commit();
    }


    private void showUserPhotosFragment(){
        Log.i(TAG, "showUserPhotosFragment");
        Fragment showUserPhotoFragment = new UserHomeUserSubmittedPhotosFragment();

        Bundle b = new Bundle();
        //b.putSerializable("bitmapArrayList",bitmapArrayList);
        //showUserPhotoFragment.setArguments(b);

        android.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
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
        android.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.user_info_frag_container, showUserPhotoFragment);
        ft.commit();
    }

    @Override
    public void processFinish(ArrayList<Bitmap> result) {

        showUserPhotosFragment(result);
    }

    @Override
    public void processFinish(Bitmap result, String path) {

    }

    @Override
    public void processFinish(Bitmap result, ArrayList<String> path) {

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
        if(vals!=null) {
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
    }


    //
    public void downloadPhoto(){
        Log.d(TAG, "downloadPhoto()");
        // String filename = "oasker.jpg";
        String filename = UserInformationModel.getInstance().getUsername()+".jpg";

        String externalStorage = getContext().getCacheDir().toString()+"/";
        final String filePath = externalStorage+filename;
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1           // Region
        );

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
//        try {
//            File file =new File(filePath);
//            TransferUtility transferUtility = new TransferUtility(s3Client, getContext());
//            TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, filename, file);
//
//            Log.d(TAG, "file.getPath(): " + file.getPath() );
//
//            transferObserver.setTransferListener(new TransferListener(){
//                @Override
//                public void onStateChanged(int id, TransferState state) {
//                    Log.d(TAG, "onStateChanged: + state: "+ state.toString());
//                    if(state == TransferState.COMPLETED){
//                        Bitmap b = BitmapFactory.decodeFile(filePath);
//                        //     horizontalLinearLayout.addView(createImageView(b));
//                        profileImage.setImageBitmap(b);
//                    }
//                }
//                @Override
//                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                    int percentage = (int) ((bytesCurrent+1) /(bytesTotal+1) * 100);
//                    Log.d(TAG, "onStateChanged(); bytesTotal: " +bytesTotal +" bytesCurrent: "+ bytesCurrent) ;
//                }
//                @Override
//                public void onError(int id, Exception ex) {
//                    Log.e(TAG, "onError",ex);
//                }
//            });
//
//        } catch (AmazonClientException ace) {
//            Log.d(TAG, "Caught an AmazonClientException, which means" +
//                    " the client encountered " +
//                    "an internal error while trying to " +
//                    "communicate with S3, " +
//                    "such as not being able to access the network.");
//            Log.d(TAG, "Error Message: " + ace.getMessage());
//        }
        //profileImage=BitmapUtility.scaleBitmap(profileImage, 400,400);
        Ion.with(profileImage)
                .resize(profileImageViewWidth,profileImageViewHeight)
                //.smartSize(true)
                .placeholder(R.drawable.sunny)
                .error(R.drawable.snow_icon)
                .load("https://s3.amazonaws.com/skywarntestbucket/"+UserInformationModel.getInstance().getUsername()+".jpg");
        BitmapUtility.scaleImage(getContext(),profileImage);
    }

}
