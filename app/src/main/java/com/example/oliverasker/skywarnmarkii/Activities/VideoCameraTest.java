package com.example.oliverasker.skywarnmarkii.Activities;

/**
 * Created by oliverasker on 4/13/17.
 */


import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

public class VideoCameraTest extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_VIDEO_PERMISSION = 200;
    private static String mFileName = null;

    //private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    // private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.CAPTURE_VIDEO_OUTPUT};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startRecording(getFilesDir() + "/test.mp4");
    }

    protected void startRecording(String file) {
        MediaRecorder recorder = new MediaRecorder();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //	recorder.setOutputFile(audiofile.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
    }
}