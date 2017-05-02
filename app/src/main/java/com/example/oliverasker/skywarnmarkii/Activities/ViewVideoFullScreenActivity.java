package com.example.oliverasker.skywarnmarkii.Activities;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.R;

import java.io.IOException;
import java.net.URL;

/**
 * Created by oliverasker on 4/23/17.
 */

public class ViewVideoFullScreenActivity extends Activity //implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener
{
    private static final String TAG = "ViewVideoFullScrnActy";
    private static final String MEDIA = "media";
    private static final int STREAM_VIDEO = 5;
    private VideoView videoView;
    private int mVideoWidth;
    private int mVideoHeight;
    //    private MediaPlayer mMediaPlayer;
//    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private String path;
    private Bundle extras;
    private SurfaceHolder holder;
    private int mCurrentVideoPosition;

    //        prepared url to s3 video
    private URL objectURL;
    private String NonS3VideoURL = "http://techslides.com/demos/sample-videos/small.mp4";


//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate()");
//        setContentView(R.layout.activity_view_video_full_screen);
//
//
////        Setup video stuff
//        mPreview = (SurfaceView) findViewById(R.id.surfaceView);
//        holder = mPreview.getHolder();
//        holder.addCallback(ViewVideoFullScreenActivity.this);
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//        Bundle b = getIntent().getExtras();
//        path = b.getString("videoURL");
//        path = "https://s3.amazonaws.com/skywarntestbucket/1493426650833_oasker_0.mp4";
//        path ="http://samples.mplayerhq.hu/MPEG-4/Concession_LAN_800k.mp4";
//
////        videoView = (VideoView) findViewById(R.id.video_view_full_screen);
//
////        AWSCredentials myCredentials = new BasicAWSCredentials(Constants.MY_ACCESS_KEY_ID, Constants.MY_SECRET_KEY);
////        AmazonS3 s3client = new AmazonS3Client(myCredentials);
////        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(Constants.BUCKET_NAME, "1493426650833_oasker_0.mp4");
////        URL objectURL = s3client.generatePresignedUrl(request);
////
////        MediaController mediaCtrl = new MediaController(this);
////        mediaCtrl.setAnchorView(videoView);
////        getWindow().setFormat(PixelFormat.TRANSLUCENT);
////        mediaCtrl = new MediaController(this);
////        mediaCtrl.setMediaPlayer(videoView);
////
////        videoView.setMediaController(mediaCtrl);
////        Uri clip = Uri.parse(objectURL.toString());
//////        videoView.setVideoURI(clip);
////        videoView.setVideoURI(Uri.parse(videoURL));
////
////        videoView.requestFocus();
////        videoView.start();
////
////        Uri videoUri = Uri.parse(videoURL);
////        MediaPlayer mediaPlayer = new MediaPlayer();
////        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
////        mediaPlayer.setDisplay(holder);
////        try {
////            mediaPlayer.setDataSource(ViewVideoFullScreenActivity.this, videoUri);
////            mediaPlayer.prepare();
////            mediaPlayer.start();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////Original
////        MediaController mc = new MediaController(this);
////        mc.setAnchorView(videoView);
////        videoView.setMediaController(mc);
////        videoView.setKeepScreenOn(true);
////
////        videoView = (VideoView) findViewById(R.id.video_view_full_screen);
////        videoView.setVideoURI(Uri.parse(videoURL));
////        videoView.start();
////        videoView.requestFocus();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_view_video_full_screen);

        videoView = (VideoView) findViewById(R.id.video_view_full_screen);


        AWSCredentials myCredentials = new BasicAWSCredentials(Constants.MY_ACCESS_KEY_ID, Constants.MY_SECRET_KEY);
        AmazonS3 s3client = new AmazonS3Client(myCredentials);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(Constants.BUCKET_NAME, "1493426650833_oasker_0.mp4");
        objectURL = s3client.generatePresignedUrl(request);

        MediaController mc = new MediaController(this);
        mc.setAnchorView(videoView);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);
        videoView.setVideoURI(Uri.parse(objectURL.toString()));
//            videoView.setVideoURI(Uri.parse("https://s3.amazonaws.com/skywarntestbucket/1493426650833_oasker_0.mp4"));
        videoView.requestFocus();
        videoView.start();


//            CustomMediaPlayer customMediaPlayer = new CustomMediaPlayer ();
//            customMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            try {
//                customMediaPlayer.setDataSource("https://s3.amazonaws.com/skywarntestbucket/1493426650833_oasker_0.mp4");
//                customMediaPlayer.prepare(); // might take long! (for buffering, etc)
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////            String url1 = customMediaPlayer.getDataSource();// get your url here
//            customMediaPlayer.start();

//
//            SurfaceView v = (SurfaceView) findViewById(R.id.surfaceView);
//
//            holder = v.getHolder();
//            holder.setFixedSize(400,300);
//            holder.addCallback(this);
//
//            AWSCredentials myCredentials = new BasicAWSCredentials(Constants.MY_ACCESS_KEY_ID, Constants.MY_SECRET_KEY);
//            AmazonS3 s3client = new AmazonS3Client(myCredentials);
//            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(Constants.BUCKET_NAME, "1493426650833_oasker_0.mp4");
//            objectURL = s3client.generatePresignedUrl(request);
//            Log.d(TAG, "objectURL.toString(): " + objectURL.toString() );
////            mMediaPlayer = MediaPlayer.create(this, Uri.parse(objectURL.toString()));
//            mMediaPlayer = new MediaPlayer();
//        };
    }
//        @Override
//        public void surfaceCreated(SurfaceHolder holder){
//                Log.d(TAG, "SurfaceCreated()");
//                mMediaPlayer.setDisplay(holder);
//                try {
////                    mMediaPlayer.start();
//                    mMediaPlayer.prepareAsync();
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            Log.d(TAG, "SurfaceChanged()");
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            Log.d(TAG, "SurfaceDestroyed()");
////            mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
////            mMediaPlayer.release();
////            mMediaPlayer = null;
//
//        }

//        void playVideo(){
//            Log.d(TAG,"playVideo()");
//
//            try {
//                if (mMediaPlayer == null)
//                    mMediaPlayer = new MediaPlayer();
//                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mMediaPlayer.setDataSource(objectURL.toString());
////                mMediaPlayer.setOnPreparedListener(ViewVideoFullScreenActivity.this);
//                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//
//
//                    @Override
//                    public boolean onError(MediaPlayer mp, int what, int extra) {
//                        mp.reset();
//                        return false;
//                    }
//                });
//
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    //        @Override
//        public void onPrepared(MediaPlayer mp) {
//
//            if (mCurrentVideoPosition != 0) {
//                mp.seekTo(mCurrentVideoPosition);
//                mp.start();
//            }
//            else{
//                mp.start();
//            }
//        }
    @Override
    protected void onPause() {
        super.onPause();
//        releaseMediaPlayer();
//        doCleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        releaseMediaPlayer();
//        doCleanUp();
    }


}

class CustomMediaPlayer extends MediaPlayer {
    String dataSource;

    public String getDataSource() {
        return dataSource;
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        // TODO Auto-generated method stub
        super.setDataSource(path);
        dataSource = path;
    }
}