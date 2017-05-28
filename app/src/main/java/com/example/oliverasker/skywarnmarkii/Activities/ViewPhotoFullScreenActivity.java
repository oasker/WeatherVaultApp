package com.example.oliverasker.skywarnmarkii.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.R;
import com.koushikdutta.ion.Ion;

public class ViewPhotoFullScreenActivity extends AppCompatActivity {
    private static final String TAG = "ViewFullSCreenPhoto";

    private ImageView fullScreenImageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo_full_screen);


        String imageURL = getIntent().getStringExtra("imageURL");
        Log.d(TAG, "imageURL: " + imageURL);

//        For testing
//        imageURL = "https://s3.amazonaws.com/skywarntestbucket/"+ UserInformationModel.getInstance().getUsername()+".jpg";

        fullScreenImageView = (ImageView) findViewById(R.id.full_screen_imageview);

        Ion.with(fullScreenImageView)
                .resize(Constants.VIEW_REPORT_ACTIVITY_IMAGE_WIDTH, Constants.VIEW_REPORT_ACTIVITY_IMAGE_HEIGHT)
                .error(R.drawable.snow_icon)
                .crossfade(true)
                .load(imageURL);
    }
}
