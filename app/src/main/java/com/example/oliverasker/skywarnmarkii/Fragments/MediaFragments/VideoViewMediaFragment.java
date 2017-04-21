package com.example.oliverasker.skywarnmarkii.Fragments.MediaFragments;


import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoViewMediaFragment extends Fragment {

    // ALWAYS set context
    private Context mContext;

    //    Title - which is the date it was taken
    private TextView dateTitleTV;


    //    Video Variables
    private VideoView videoView;
    private Uri videoUri;
    private String videoURL;
    //  For now  the video title is just the date, but I think weater event then date is better
    private String videoTitle;
    private View view;


    public VideoViewMediaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        View v = inflater.inflate(R.layout.fragment_videoview, container, false);

        videoView = (VideoView) v.findViewById(R.id.video_view);
        dateTitleTV = (TextView) v.findViewById(R.id.videovide_fragment_dateTV);
        videoView.start();
        view = v;
        return v;
    }


    ////////////////     Setters/Getters       ///////////////////
    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    public void setVideoURL(String videoURL) {
        setVideoUri(Uri.parse(videoURL));
        this.videoURL = videoURL;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public View getView() {
        return view;
    }
}
