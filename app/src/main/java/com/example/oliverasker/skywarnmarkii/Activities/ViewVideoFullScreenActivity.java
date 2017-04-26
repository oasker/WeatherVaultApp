package com.example.oliverasker.skywarnmarkii.Activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 4/23/17.
 */

public class ViewVideoFullScreenActivity extends Activity implements MediaPlayer.OnCompletionListener {
    private static final String TAG = "VVideoFullScrnActivity";
    VideoView vv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        Bundle b = getIntent().getExtras();
        String videoURL = b.getString("videoURL");

        setContentView(R.layout.activity_view_video_full_screen);

        vv = (VideoView)findViewById(R.id.video_view_full_screen);

        MediaController mc = new MediaController(this);
        mc.setAnchorView(vv);
        vv.setMediaController(mc);
        vv.setKeepScreenOn(true);

        vv = (VideoView)findViewById(R.id.video_view_full_screen);
        vv.setVideoURI(Uri.parse(videoURL));
        vv.start();
        vv.requestFocus();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }
}
