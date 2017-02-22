package com.example.oliverasker.skywarnmarkii.Fragments;

import android.graphics.Bitmap;
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

import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserSubmittedPhotosTask;

import java.util.ArrayList;

/**
 * Created by oliverasker on 1/17/17.
 */

public class UserInfoHomeFragment extends Fragment implements BitmapCallback {
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
        usernameTV = (TextView)v.findViewById(R.id.username_home_activityTV);
        usernameTV.setText(UserInformationModel.getInstance().getUsername());
        emailTV = (TextView)v.findViewById(R.id.emailTV);
        emailTV.setText(UserInformationModel.getInstance().getEmail());
        nameTV= (TextView)v.findViewById(R.id.nameTV);
        nameTV.setText(UserInformationModel.getFirstName() + " " + UserInformationModel.getLastName());
        usernameTV.setText(UserInformationModel.getInstance().getUserID());

        //User report, photo, info buttons
        viewUserInfoButton = (Button)v.findViewById(R.id.view_user_info_button);
        viewUserPhotosButton = (Button)v.findViewById(R.id.view_user_photos_button);
        viewUserReportsButton = (Button)v.findViewById(R.id.view_user_reports_button);

        viewUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserInfoFragment();
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
        return v;
    }


    private void showUserInfoFragment(){
        Log.i(TAG, "showUserInfoFragment");
        Fragment showUserInfoFragment = new UserHomePersonalDetailsFragment();
        Bundle b = new Bundle();
        String[] cognitoVals = {"SpotterIDTEST", "CALLSIGNTEST","AffilliationTEST","USERNAMETST"};
        b.putStringArray("cognitoVals" ,cognitoVals);
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
        Log.i(TAG, "showUserReportsFragment");
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

}
