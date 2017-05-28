package com.example.oliverasker.skywarnmarkii.Fragments.UserHomeFragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.UserAttributesCallback;
import com.example.oliverasker.skywarnmarkii.Fragments.WeatherListViewFragment;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserCognitoAttributesTask;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;

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


    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_user_info_home,container, false);

        mContext = getActivity().getApplicationContext();

        profileImage = (ImageView)v.findViewById(R.id.user_profile_picture_imageview);

        //User report, photo, info buttons
        viewUserInfoButton = (Button)v.findViewById(R.id.view_user_info_button);
        viewUserPhotosButton = (Button)v.findViewById(R.id.view_user_photos_button);
        viewUserReportsButton = (Button)v.findViewById(R.id.view_user_reports_button);

        GetUserCognitoAttributesTask attributesTask = new GetUserCognitoAttributesTask(UserInfoHomeFragment.this);
        // showUserInfoFragment();
        attributesTask.initUserPool(mContext);
        attributesTask.setCognitoUser(UserInformationModel.getInstance().getUserID());
        attributesTask.execute();

        viewUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserCognitoAttributesTask attributesTask = new GetUserCognitoAttributesTask(UserInfoHomeFragment.this);
               // showUserInfoFragment();
                attributesTask.initUserPool(mContext);
                attributesTask.setCognitoUser(UserInformationModel.getInstance().getUserID());
                attributesTask.execute();
            }
        });
        viewUserPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        UserHomePersonalDetailsFragment showUserInfoFragment = new UserHomePersonalDetailsFragment();

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
        Log.d(TAG, "onProceessFinised(): " + vals[0]);
    }

    @Override
    public void onProcessFinished(Map<String,String> vals) {
        if(vals!=null) {

            Log.d(TAG, "***********  onProcessFinished(Map<STring,String>): user attributes   ***********");
            Utility.printMap(vals);
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //
    public void downloadPhoto(){
        Log.d(TAG, "downloadPhoto()");
//        Ion.with(profileImage)
//                .resize(500,500)
//                .placeholder(R.drawable.default_user_photo)
//                .error(R.drawable.snow_icon)
//                .load("https://s3.amazonaws.com/skywarntestbucket/"+UserInformationModel.getInstance().getUsername()+".jpg");
        Log.d(TAG, " filename: https://s3.amazonaws.com/skywarntestbucket/" + UserInformationModel.getInstance().getUsername() + ".jpg");
        profileImage.setImageResource(R.drawable.temp_profile);
    }
}
