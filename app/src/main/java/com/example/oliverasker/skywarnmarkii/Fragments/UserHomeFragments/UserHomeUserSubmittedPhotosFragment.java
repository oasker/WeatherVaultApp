package com.example.oliverasker.skywarnmarkii.Fragments.UserHomeFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.Activities.ViewPhotoFullScreenActivity;
import com.example.oliverasker.skywarnmarkii.Activities.ViewReportActivity;
import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.StringCallback;
import com.example.oliverasker.skywarnmarkii.Models.SubmittedPhotoModel;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserSubmittedPhotoNamesTask;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

//import android.support.v4.app.Fragment;

/**
/**
* Created by oliverasker on 2/19/17.
*/

public class UserHomeUserSubmittedPhotosFragment extends Fragment implements BitmapCallback, StringCallback, ViewReportActivity.bitmapCallback {
    private static final String TAG = "PersnlPhotosFrag";
    private String username = UserInformationModel.getInstance().getUsername();
    private ArrayList<SubmittedPhotoModel> photoModelList = new ArrayList<>();
    private Context mContext;
    private LinearLayout photoLinearLayout;
    private int imageViewWidth = 400;
    private int imageViewHeight = 400;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup Container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_user_home_submitted_photos, Container, false);
        Log.d(TAG, "onCreateView()");
       // container = Container;
        photoLinearLayout = (LinearLayout) v.findViewById(R.id.photoLinearLayout);
        photoModelList = new ArrayList<>();
        mContext = getActivity().getApplicationContext();

        //ToDo:change filename to current user, not placeholder
        GetUserSubmittedPhotoNamesTask task2 = new GetUserSubmittedPhotoNamesTask();
        task2.setmContext(mContext);
        task2.setCallback(this);
        task2.execute();
        return v;
    }

    //    Todo: clean up/rename callbacks for each specific function
//  UNUSED `///////////////////////////////////////
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void processFinish(ArrayList<Bitmap> result) {
    }
    @Override
    public void processFinish(Bitmap result) {
        Log.d(TAG, "processFinished(Bitmap)");
    }
    @Override
    public void processFinish(Bitmap result, String path) {
        Log.d(TAG, "processFinished(Bitmap, String)");
    }
    @Override
    public void processFinish(Bitmap result, ArrayList<String> path) {
    }
    @Override
    public void processFinish(Uri result) {
        Log.d(TAG, "processFinished(Uri)");
    }
//    //////////////////////////////////////////////



    // STRING CALLBACKs
    //  After finding the names of all photos submitted to S3 this callback is fired
    //  which adds them to the UI using ion API
    @Override
    public void onProcessComplete(ArrayList<String> s) {
        Log.d(TAG, "onProcessComplete(ArrayList<String> s)");

        final StringBuilder sb = new StringBuilder();

//        Remove profile photo
        if (s.contains(UserInformationModel.getInstance().getUsername() + ".jpg"))
            s.remove(UserInformationModel.getInstance().getUsername() + ".jpg");

        for (String url : s) {

            sb.append("https://s3.amazonaws.com/skywarntestbucket/" + url);
            Log.d(TAG, "sb.toSTring() " + sb.toString() + "  vs  " + url);
            ImageView v = new ImageView(mContext);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fullScreenImageIntent = new Intent(mContext, ViewPhotoFullScreenActivity.class);
                    Log.d(TAG, "sb.toSTring2(): " + sb.toString());
                    fullScreenImageIntent.putExtra("imageURL", sb.toString());

                    startActivity(fullScreenImageIntent);
                }
            });

            TextView tv = new TextView(mContext);
            tv.setTextColor(mContext.getResources().getColor(R.color.darkTextColor));
            tv.setPadding(5,5,5,5);

            LinearLayout ll = new LinearLayout(mContext);
            LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(7,7,7,7);

            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setGravity(Gravity.CENTER_HORIZONTAL);
            Ion.with(v)
                    .resize(imageViewWidth,imageViewHeight)
                    .placeholder(R.drawable.sunny)
                    .error(R.drawable.snow_icon)
                    .load("https://s3.amazonaws.com/skywarntestbucket/"+url);
//            Log.d(TAG,"https://s3.amazonaws.com/skywarntestbucket/"+url );
            ll.addView(v);
            if (url.contains("_") & url.contains(".jpg")) {
                String text = url.split("_")[0];
                text=Utility.epochToDateTimeString(Long.parseLong(text)/1000);
                tv.setText(text.toString());
                ll.addView(tv);
            }
            photoLinearLayout.addView(ll);

//            Clear string builder for next iteration
            sb.setLength(0);
        }
    }

    @Override
    public void onFinishedString(String s) {
//        File f = new File(s);
//        Bitmap b = BitmapFactory.decodeFile(s);
//        ImageView i = new ImageView(getContext());
//        i.setImageBitmap(b);

//        photoLinearLayout.addView(i);
    }

//    public void downloadPhoto(ArrayList<String> paths) {
//        Log.d(TAG, "downloadPhoto()");
//        for(String s: paths) {
//            String filename =s;
//            String externalStorage = mContext.getCacheDir().toString() + "/";
//            final String filePath = externalStorage + filename;
//            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                    getContext(),
//                    Constants.IDENTITY_POOL_ID, // Identity Pool ID
//                    Regions.US_EAST_1           // Region
//            );
//
//            AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
//            try {
//                File file = new File(filePath);
//                TransferUtility transferUtility = new TransferUtility(s3Client, getContext());
//                TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, filename, file);
//                Log.d(TAG, "file.getPath(): " + file.getPath());
//
//                transferObserver.setTransferListener(new TransferListener() {
//                    @Override
//                    public void onStateChanged(int id, TransferState state) {
//                        Log.d(TAG, "onStateChanged: + state: " + state.toString());
//                        if (state == TransferState.COMPLETED) {
//                            Bitmap b = BitmapFactory.decodeFile(filePath);
//
//                            //ToDo: Add text view to user submitted photos giving date photo was taken
//                            //TextView dateTV = new TextView(mContext);
//                            //s.indexOf("_");
//                            //dateTV.setText(Utility.epochToDateTimeString());
//
//
//                            ImageView v = new ImageView(mContext);
//                            v=BitmapUtility.resizeImageView(imageViewWidth, imageViewHeight, v);
//                            v.setImageBitmap(b);
//                            photoLinearLayout.addView(v);
//                        }
//                    }
//
//                    @Override
//                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                        int percentage = (int) ((bytesCurrent + 1) / (bytesTotal + 1) * 100);
//                        Log.d(TAG, "onStateChanged(); bytesTotal: " + bytesTotal + " bytesCurrent: " + bytesCurrent);
//                    }
//
//                    @Override
//                    public void onError(int id, Exception ex) {
//                        Log.e(TAG, "onError", ex);
//                    }
//                });
//
//
//            } catch (AmazonClientException ace) {
//                Log.d(TAG, "Caught an AmazonClientException, which means" +
//                        " the client encountered " +
//                        "an internal error while trying to " +
//                        "communicate with S3, " +
//                        "such as not being able to access the network.");
//                Log.d(TAG, "Error Message: " + ace.getMessage());
//            }
//        }
//    }

    @Override
    public void onProcessComplete(String county, String lat, String lng) {

    }

    @Override
    public void onProcessError(String error) {

    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}


