package com.example.oliverasker.skywarnmarkii.Fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.Activites.LaunchCameraActivity;
import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 1/2/17.
 */

public class PreviewUserSubmitPhotoFragment extends Fragment implements LaunchCameraActivity.PreviewPhotoCommunicator{
    static final String TAG = "PrevUserSubPhotoFrag";


    //Fields
    ImageView userSubmittedImage;
    TextView photoTitleTV;

    //Values
    String photoTitle= "Photo Title Bro";
    Bitmap userImagePreview;

    protected void OnCreate(Bundle savedInstace){
        super.onCreate(savedInstace);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        userSubmittedImage = (ImageView) view.findViewById(R.id.user_submitted_photo);
        photoTitleTV = (TextView) view.findViewById(R.id.user_submitted_photo_title_);
       // userSubmittedImage.setImageBitmap(userImagePreview);
       // userSubmittedImage.setBackground(getResources().getDrawable(R.drawable.cloud));
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        //Set all view fields using values set from setters
        // userSubmittedImage.setImageBitmap(userImagePreview);
        //photoTitleTV.setText(photoTitle);

      //  userSubmittedImage.setBackground(getResources().getDrawable(R.drawable.cloud));

        Log.d(TAG, "inflate view has been called");
        return inflater.inflate(R.layout.add_photo_to_report_fragment,container, false);
    }

    public Bitmap getUserImagePreview() {
        return userImagePreview;
    }

    public void setUserImagePreview(Bitmap userImagePreview) {
        this.userImagePreview = userImagePreview;
    }

    public ImageView getUserSubmittedImage() {
        return userSubmittedImage;
    }

    public void setUserSubmittedImage(ImageView userSubmittedImage) {
        this.userSubmittedImage = userSubmittedImage;
    }

    public TextView getPhotoTitle() {
        return photoTitleTV;
    }

    public void setPhotoTitle(TextView photoTitleTV) {
        this.photoTitleTV = photoTitleTV;
    }

    @Override
    public void setImage(Bitmap b) {
        userImagePreview = b;
       userSubmittedImage.setImageBitmap(userImagePreview);
    }
}
