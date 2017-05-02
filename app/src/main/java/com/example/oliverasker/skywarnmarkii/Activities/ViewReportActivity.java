package com.example.oliverasker.skywarnmarkii.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.BooleanCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.UriCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Fragments.CoastalFloodingViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.GeneralSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.RainFloodViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.SevereViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.WinterViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.RateReportTask;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 *  Activity for viewing report selected from listview
 */

public class ViewReportActivity extends Activity implements UriCallback,BooleanCallback{
    private static final String TAG = "ViewReportActivity";
    public LinearLayout horizontalLinearLayout;
    SkywarnWSDBMapper map;
    private WinterViewReportFragment winterFrag;
    private CoastalFloodingViewReportFragment coastalFloodingFrag;
    private RainFloodViewReportFragment rainFrag;
    private SevereViewReportFragment severeFrag;
    private GeneralSubmitReportFragment generalInfoFrag;
    private ImageView photoIV;
    private TextView username;
    private TextView DateOfEvent;
    private TextView eventType;
    private TextView location;
    private TextView longitude;
    private TextView latitude;
    private TextView comments;
    private TextView reportRating;
    private TextView photoLabelTV;
    private Button upVoteButton;
    private Button downVoteButton;

    //    Video variables
    private boolean bVideoIsBeingTouched = false;
    private Handler mHandler = new Handler();


    private Button viewOnMapButton;

    //    Multiple Maps to hold attributes for different events
    private HashMap<String, String> winterAttrMap;
    private HashMap<String, String> severeAttrMap;
    private HashMap<String, String> rainAttrMap;
    private HashMap<String, String> coastalAttrMap;

    private HashMap<String, String> valueMap;
    private ImageView iconImageView;

    private int numberOfPhotos;
    private int numberOfVideos;

    private int reportImageWidth = 250;
    private int reportImageHeight = 250;
    BitmapCallback callback = new BitmapCallback() {
        @Override
        public void processFinish(ArrayList<Bitmap> result) {

        }
        @Override
        public void processFinish(Bitmap result,String s) {
            if(result !=null) {
                Log.i(TAG, "processFinished(bitmap)");

                ImageView iV = new ImageView(getApplicationContext());
                iV.setImageBitmap(result);
                horizontalLinearLayout.addView(createImageView(result));


            }
        }
        @Override
        public void processFinish(Uri result) {
            //createImageView(result)
            Log.i(TAG, "processFinished(Uri): getPath() "+result.getPath() );
            Bitmap b = null;
            try {
                b = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),result);

                horizontalLinearLayout.addView(createImageView(b));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void processFinish(Bitmap result) {
            if(result !=null) {
                Log.i(TAG, "processFinished(bitmap)");
//          For bitmaps
//                ImageView iV = new ImageView(getApplicationContext());
                ImageView iv = createImageView();
                Ion.with(iv)
                        .resize(reportImageWidth, reportImageHeight)
                        .placeholder(R.drawable.sunny)
                        .error(R.drawable.snow_icon)
                        .load("https://s3.amazonaws.com/skywarntestbucket/" + UserInformationModel.getInstance().getUsername() + ".jpg");
//                BitmapUtility.scaleImage(mContext, iv);

                iv.setImageBitmap(result);
//                horizontalLinearLayout.addView(iv);
//          horizontalLinearLayout.addView(createImageView(result));
            }
        }
        public void processFinish(Bitmap result, ArrayList<String> pathList) {
            if(result !=null) {
                Log.i(TAG, "processFinished(bitmap)");

                ImageView iV = new ImageView(getApplicationContext());
                iV.setImageBitmap(result);
                horizontalLinearLayout.addView(createImageView(result));
            }
        }
    };
    private int reportVideoThumbnailWidth = 300;
    private int reportVideoThumbnailHeight = 300;

    private static boolean storagePermitted(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUESTCODE_WRITE_EXTERNAL_STORAGE);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "storagePermitted: Granted" + ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE.toString()));
            return true;
        } else {
            Log.d(TAG, "storagePermitted: Denied: " + ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE.toString()));

            return false;
        }
    }

    //    Generates thumbnail from video received from url
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();

            if (Build.VERSION.SDK_INT >= 14 & videoPath != null)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    @Override
    public void onProcessComplete(Uri returnedUri) {
        Log.d(TAG, "onProcessComplete()");

//
//        ImageView iv = createImageView();
//        Ion.with(iv)
//                .resize(reportImageWidth,reportImageHeight)
//                .placeholder(R.drawable.sunny)
//                .error(R.drawable.snow_icon)
//                .load("https://s3.amazonaws.com/skywarntestbucket/"+UserInformationModel.getInstance().getUsername()+".jpg");
//        BitmapUtility.scaleImage(mContext, iv);
//        horizontalLinearLayout.addView(iv);
//
//
//        try {
//            Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), returnedUri);
//           // horizontalLinearLayout.addView(createImageView(b));
//        } catch (IOException e) {
//            Log.e(TAG, "onOnProcess complete:", e);
//        }
    }

//    Todo: severeWeather View needs to have some attributes only added if others are present

    //    Todo: make check when user first looks at report and disable rating buttons if theyve voted or its their report
    @Override
    public void UserHasRatedReport(Boolean b, int netVote) {
        Log.d(TAG, "UserHasRatedReportBefore? " + b);
        //reportRating.setText(String.valueOf(map.getNetVote()));
        if (b != null) {
            upVoteButton.setEnabled(false);
            downVoteButton.setEnabled(false);

            Log.d(TAG, "serHasRatedReportBefore: netVote" + String.valueOf(netVote));
            reportRating.setText("Rating: " + String.valueOf(netVote));
        }
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_view_report_layout);
        Log.d(TAG, "OnCreate()");
        Intent intent = getIntent();
        Bundle viewActivityBundle = intent.getExtras();
        map = (SkywarnWSDBMapper) viewActivityBundle.get("selectedReport");
        initGUI(map);

        horizontalLinearLayout = (LinearLayout) findViewById(R.id.view_report_activity_horizontal_linearlayout);
        photoIV = (ImageView) findViewById(R.id.photoIV);

        valueMap = new HashMap<>();
        winterAttrMap = new HashMap<>();
        severeAttrMap = new HashMap<>();
        rainAttrMap = new HashMap<>();
        coastalAttrMap = new HashMap<>();


        //Rating system widgets
        upVoteButton = (Button) findViewById(R.id.up_vote_button);
        downVoteButton = (Button) findViewById(R.id.down_vote_button);

        //vV = (VideoView) findViewById(R.id.videoView2);

        upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "upVoteButton.OnClick()");
                //text files hold all reports that the user has rated
                RateReportTask rateTask = new RateReportTask();
                rateTask.setmContext(ViewReportActivity.this);
                rateTask.setUrl(UserInformationModel.getInstance().getUsername());
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                rateTask.setReportName(sdf.format(map.getDateOfEvent() * 1000) + "_" + String.format("%.0f", map.getDateSubmittedEpoch()));
                rateTask.setFilePath(getCacheDir() + "/");
                rateTask.setUsername(UserInformationModel.getInstance().getUsername());
                rateTask.setCallback(ViewReportActivity.this);

                Log.d(TAG, "EventType: " + map.getWeatherEvent() + " City: " + map.getCity() + " NetVote: " + map.getNetVote());
                rateTask.setReportRating(map.getNetVote());
                rateTask.setReportPrimaryKey(map.getDateSubmittedString());
                rateTask.setRangeKey(String.valueOf(map.getDateSubmittedEpoch()));
                rateTask.setVote("-U");
                rateTask.execute();
            }
        });

        downVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "downVote Button onClick()");
                RateReportTask rateTask = new RateReportTask();
                rateTask.setmContext(ViewReportActivity.this);
                rateTask.setCallback(ViewReportActivity.this);

                //Url to S3 text file
                rateTask.setUrl(UserInformationModel.getInstance().getUsername());
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                rateTask.setReportName(sdf.format(map.getDateOfEvent() * 1000) + "_" + String.format("%.0f", map.getDateSubmittedEpoch()));
                rateTask.setFilePath(getCacheDir() + "/");
                rateTask.setUsername(UserInformationModel.getInstance().getUsername());

                rateTask.setReportRating(map.getNetVote());
                rateTask.setReportPrimaryKey(map.getDateSubmittedString());
                rateTask.setRangeKey(String.valueOf(map.getDateSubmittedEpoch()));
                rateTask.setVote("-D");
                rateTask.execute();
            }
        });


        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();

        viewOnMapButton = (Button) findViewById(R.id.view_report_on_map_button);
        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMapActivity();
            }
        });

//        Get count of related media
        numberOfPhotos = map.getNumberOfImages();
        numberOfVideos = map.getNumberOfVideos();
//
        photoLabelTV = (TextView) findViewById(R.id.view_report_activity_photo_label);
        if (numberOfPhotos <= 0 & numberOfVideos <= 0) {
            photoLabelTV.setText("");
        } else {
            photoLabelTV.setText("Media");
        }


        //Send data to fragments
        //Stupid way to do this i think, but need to brute force it
        valueMap.put("Date Submitted Epoch", String.valueOf(map.getDateSubmittedEpoch()));
        //valueMap.put("Date Of Event", String.valueOf(map.getDateOfEvent()));

        // Winter Event
        winterAttrMap.put("Snowfall", String.valueOf(map.getSnowfall()));
        winterAttrMap.put("Snowfall Rate", String.valueOf(map.getSnowfallRate()));
        winterAttrMap.put("Snow Depth", String.valueOf(map.getSnowDepth()));
        winterAttrMap.put("Water Equivalent", String.valueOf(map.getWaterEquivalent()));
        winterAttrMap.put("Freezing Rain", map.getFreezingRain());
        winterAttrMap.put("Sleet", String.valueOf(map.getSnowFallSleet()));
        winterAttrMap.put("BlowDrift", map.getBlowDrift());
        winterAttrMap.put("Whiteout", map.getWhiteout());
        winterAttrMap.put("Thundersnow", map.getThundersnow());
        winterAttrMap.put("Freeing Rain Accumulation", String.valueOf(map.getFreezingRainAccum()));
        winterAttrMap.put("Number Of Fatalities", String.valueOf(map.getWinterWeatherFatalities()));
        winterAttrMap.put("Number Of Injuries", String.valueOf(map.getWinterWeatherInjuries()));
        winterAttrMap.put("Injuries Comments", String.valueOf(map.getWinterWeatherInjuries()));


//        Coastal Attribites
        coastalAttrMap.put("Number Of Fatalities", String.valueOf(map.getCoastalEventFatalities()));
        coastalAttrMap.put("Number Of Injuries", String.valueOf(map.getCoastalEventInjuries()));
        coastalAttrMap.put("Injuries Comments", String.valueOf(map.getCoastalEventComments()));

        //Severe Weather
        severeAttrMap.put("Severe Weather Type", map.getSevereType());
        if (map.getWindSpeed() != 9999)
            severeAttrMap.put("Wind Speed", String.valueOf(map.getWindSpeed() + " MPH"));
        if (map.getWindGust() != 9999)
            severeAttrMap.put("Wind Gust", String.valueOf(map.getWindGust()) + " MPH");
        severeAttrMap.put("Wind Direction", map.getWindDirection());
        severeAttrMap.put("Hail Size", String.valueOf(map.getHailSize()));
        severeAttrMap.put("Tornado", map.getTornado());
        severeAttrMap.put("Barometer", String.valueOf(map.getBarometer()));
        severeAttrMap.put("Wind Damage Comments", map.getWindDamageComments());
        severeAttrMap.put("Lightning Damage", map.getLightningDamage());
        severeAttrMap.put("Damage Comments", map.getDamageComments());
        severeAttrMap.put("Number Of Fatalities", String.valueOf(map.getSevereFatalities()));
        severeAttrMap.put("Number Of Injuries", String.valueOf(map.getSevereInjuries()));
        severeAttrMap.put("Injuries Comments", String.valueOf(map.getSevereWeatherComments()));


        //Rainfall/flooding
        Log.d(TAG, "Rain: " + map.getRain());
        rainAttrMap.put("Rain", String.valueOf(map.getRain()));
        rainAttrMap.put("Freezing Rain", map.getFreezingRain());
        rainAttrMap.put("Precipitation Rate", String.valueOf(map.getPrecipRate()));
        rainAttrMap.put("Number Of Fatalities", String.valueOf(map.getRainEventFatalities()));
        rainAttrMap.put("Number Of Injuries", String.valueOf(map.getRainEventInjuries()));
        rainAttrMap.put("Injuries Comments", String.valueOf(map.getRainEventComments()));


        //  Get Number of photos and videos associate with a report
        valueMap.put("NumberOfVideos", String.valueOf(map.getNumberOfVideos()));
        valueMap.put("NumberOfImages", String.valueOf(map.getNumberOfImages()));

        // valueMap.put("Zip", map.getZipCode());
        valueMap.put("Username", map.getUsername());
        valueMap.put("Longitude", String.valueOf(map.getLongitude()));
        valueMap.put("Latitude", String.valueOf(map.getLatitude()));

//        valueMap.put("Weather Event", map.getWeatherEvent());
        valueMap.put("Street", String.valueOf(map.getStreet()));
        valueMap.put("Comments", map.getComments());
        valueMap.put("Current Temperature", String.valueOf(map.getCurrentTemperature()) + " F");

//        Remove unwanted views
        Iterator<Map.Entry<String, String>> iter = valueMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            String value = entry.getValue();
            if (value.equals("|") || value.trim().equals("9999") || value.equals("") || value.equals("false") || value.trim().equals("9999.0") || value.equals("0.0") || value.equals("0.0 F") || value.trim().equals("0") || value.equals("null")) {
                iter.remove();
            } else {
                Log.d(TAG, "NOT DELETED: key: " + entry + " val: " + value);
            }
        }


        Log.d(TAG, "********* Severe ********");
        Iterator<Map.Entry<String, String>> severeIter = severeAttrMap.entrySet().iterator();
        while (severeIter.hasNext()) {
            Map.Entry<String, String> entry = severeIter.next();
            String value = entry.getValue();
            if (value.equals("|") || value.trim().equals("9999") || value.equals("") || value.equals("false") || value.trim().equals("9999.0") || value.equals("0.0") || value.equals("0.0 F") || value.trim().equals("0") || value.equals("null")) {
//                Log.d(TAG, "key: " + entry +" value: " + value);
                severeIter.remove();
            } else {
                Log.d(TAG, "NOT DELETED: key: " + entry + " val: " + value);

            }
        }


        Log.d(TAG, "********* Winter ********");
        Iterator<Map.Entry<String, String>> winterIter = winterAttrMap.entrySet().iterator();
        while (winterIter.hasNext()) {
            Map.Entry<String, String> entry = winterIter.next();
            String value = entry.getValue();

            if (value.equals("|") || value.trim().equals("9999") || value.equals("") || value.equals("false") || value.trim().equals("9999.0") || value.equals("0.0") || value.equals("0.0 F") || value.trim().equals("0") || value.equals("null")) {
//                Log.d(TAG, "key: " + entry +" value: " + value);
                winterIter.remove();
            } else {
                Log.d(TAG, "NOT DELETED: key: " + entry + " val: " + value);

            }
        }


        Log.d(TAG, "********* Rain ********");
        Iterator<Map.Entry<String, String>> rainIter = rainAttrMap.entrySet().iterator();
        while (rainIter.hasNext()) {
            Map.Entry<String, String> entry = rainIter.next();
            String value = entry.getValue();
            if (value.equals("|") || value.trim().equals("9999") || value.trim().equals("") || value.trim().equals("false") || value.trim().equals("9999.0") || value.equals("0.0") || value.equals("0.0 F") || value.trim().equals("0") || value.equals("null")) {
//                Log.d(TAG, "key: " + entry +" value: " + value);
                rainIter.remove();
            } else {
                Log.d(TAG, "NOT DELETED: key: " + entry + " val: " + value);
            }
        }

        Log.d(TAG, "********* Coastal ********");
        Iterator<Map.Entry<String, String>> coastalIter = coastalAttrMap.entrySet().iterator();
        while (coastalIter.hasNext()) {
            Map.Entry<String, String> entry = coastalIter.next();
            String value = entry.getValue();
            if (value.equals("|") || value.trim().equals("9999") || value.equals("") || value.equals("false") || value.trim().equals("9999.0") || value.equals("0.0") || value.equals("0.0 F") || value.trim().equals("0") || value.equals("null")) {
//                Log.d(TAG, "key: " + entry +" value: " + value);
                coastalIter.remove();
            } else {
                Log.d(TAG, "NOT DELETED: key: " + entry + " val: " + value);
            }
        }


        //  Set image based on type of report
        iconImageView = (ImageView) findViewById(R.id.view_report_image_view);
        if (map.getWeatherEvent().toUpperCase().contains("SEVERE")) {
            iconImageView.setImageResource(R.drawable.severe);
        }
        if (map.getWeatherEvent().toUpperCase().contains("RAIN")) {
            iconImageView.setImageResource(R.drawable.rain);
        }
        if (map.getWeatherEvent().toUpperCase().contains("WINTER")) {
            iconImageView.setImageResource(R.drawable.snow_icon);
        }
        if (map.getWeatherEvent().toUpperCase().contains("COASTAL")) {
            iconImageView.setImageResource(R.drawable.coastal);
        }
        if (map.getWeatherEvent().toUpperCase().contains("GENERAL")) {
            iconImageView.setImageResource(R.drawable.sunny);
        }

        // Instantiate different weather event type frags
        rainFrag = new RainFloodViewReportFragment();
        coastalFloodingFrag = new CoastalFloodingViewReportFragment();
        severeFrag = new SevereViewReportFragment();
        generalInfoFrag = new GeneralSubmitReportFragment();

        ///////////////////////////////////////////////////////////////////////////////
        //                          General Report Details                          //
        /////////////////////////////////////////////////////////////////////////////
        // LinearLayout ll = (LinearLayout)findViewById(R.id.view_report_attribute_list);
        TableLayout tLayout = (TableLayout) findViewById(R.id.view_report_activity_tablelayout);
        valueMap.remove("NumberOfImages");
        valueMap.remove("NumberOfVideos");
        valueMap.remove("Date Submitted Epoch");
        valueMap.remove("Username");
        valueMap.remove("Street");

        android.widget.TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        if (valueMap.size() > 0) {
            tLayout.addView(createAttributeTitleRow("General Details"));
            int counter = 0;
            for (String key : valueMap.keySet()) {
                createAttributeTableRow(key, valueMap.get(key));
                tLayout.addView(createAttributeTableRow(key, valueMap.get(key)));
                counter++;
            }
        }
        if (severeAttrMap.size() > 0) {
            tLayout.addView(createAttributeTitleRow("Severe Attributes"));
            int counter2 = 0;
            for (String key : severeAttrMap.keySet()) {
                createAttributeTableRow(key, severeAttrMap.get(key));
                tLayout.addView(createAttributeTableRow(key, severeAttrMap.get(key)));
                counter2++;
            }
        }

        if (winterAttrMap.size() > 0) {
            tLayout.addView(createAttributeTitleRow("Winter Attributes"));
            int counter3 = 0;
            for (String key : winterAttrMap.keySet()) {
                createAttributeTableRow(key, winterAttrMap.get(key));
                tLayout.addView(createAttributeTableRow(key, winterAttrMap.get(key)));
                counter3++;
            }
        }

        if (rainAttrMap.size() > 0) {
            tLayout.addView(createAttributeTitleRow("Rain Attributes"));
            int counter4 = 0;
            for (String key : rainAttrMap.keySet()) {
                createAttributeTableRow(key, rainAttrMap.get(key));
                tLayout.addView(createAttributeTableRow(key, rainAttrMap.get(key)));
                counter4++;
            }
        }

        if (coastalAttrMap.size() > 0) {
            tLayout.addView(createAttributeTitleRow("Coastal Attributes"));
            int counter5 = 0;
            for (String key : coastalAttrMap.keySet()) {
                createAttributeTableRow(key, coastalAttrMap.get(key));
                tLayout.addView(createAttributeTableRow(key, coastalAttrMap.get(key)));
                counter5++;
            }
        }

        username = (TextView) findViewById(R.id.view_report_activity_reporter);
        username.setText(getString(R.string.submited_by) + map.getUsername());

        ///////////////////////////////////////////////
        //               Date Format Stuff           //
        ///////////////////////////////////////////////
        DateOfEvent = (TextView) findViewById(R.id.view_report_activity_date);
        String date = Utility.epochToDateTimeString(map.getDateOfEvent());
        DateOfEvent.setText(date);


        comments = (TextView) findViewById(R.id.view_report_activity_comments);
        if(!map.getComments().equals("|"))
            comments.setText(map.getComments());
        else
            ((ViewGroup) comments.getParent()).removeView(comments);

        eventType = (TextView) findViewById(R.id.view_report_activity_weather_event);
        eventType.setText(map.getWeatherEvent());

        location = (TextView) findViewById(R.id.view_report_activity_location);

        //Build String for location textview
        StringBuilder locationString = new StringBuilder();
        if(!map.getStreet().trim().equals("|"))
            locationString.append(map.getStreet() + " ");
        if(map.getCity()!="|")
            locationString.append(map.getCity() );
        if(map.getState()!="|")
            locationString.append(", " + map.getState());
        Log.i(TAG, "locationSTring: " +locationString.toString());
        location.setText(locationString.toString());
        reportRating = (TextView) findViewById(R.id.view_report_activity_report_rating);

        reportRating.setText("Rating: " + String.valueOf(map.getNetVote()));
//
//        longitude = (TextView) findViewById(R.id.view_report_activity_long_latt);
//        //longitude.setText("Lat/Long: " + map.getLattitude()+ (char)0x00B0 + " " + map.getLongitude()+(char)0x00B0 );
//        generalInfoFrag = new GeneralSubmitReportFragment().newInstance(valueMap);
//        fragTransaction.add(R.id.frag0_container, generalInfoFrag, "general_info_submit_fragment");

//        if (map.getWeatherEvent().toUpperCase().contains("SEVERE")) {
//            severeFrag = new SevereViewReportFragment().newInstance(valueMap);
//            fragTransaction.add(R.id.frag1_container, severeFrag, "severeFrag");
//        }
//        if (map.getWeatherEvent().toUpperCase().contains("WINTER")) {
//            winterFrag = new WinterViewReportFragment().newInstance(valueMap);
//            fragTransaction.add(R.id.frag2_container, winterFrag, "winterFrag");
//        }
//        if (map.getWeatherEvent().toUpperCase().contains("COASTAL")) {
//            coastalFloodingFrag = new CoastalFloodingViewReportFragment().newInstance(valueMap);
//            fragTransaction.add(R.id.frag3_container, coastalFloodingFrag, "coastalFrag");
//        }
//        if (map.getWeatherEvent().toUpperCase().contains("RAIN")) {
//            rainFrag = new RainFloodViewReportFragment().newInstance(valueMap);
//            fragTransaction.add(R.id.frag4_container, rainFrag, "rainFrag");
//        }

        storagePermitted(this);
        Log.d(TAG, "Epoch:" + map.getDateOfEvent() + " NumberPhotos: " + map.getNumberOfImages());
        if (numberOfPhotos > 0) {
            for (int i = 0; i < numberOfPhotos; i++) {

                StringBuilder urlSB = new StringBuilder();
                urlSB.append("https://s3.amazonaws.com/skywarntestbucket/" + String.format("%.0f", map.getDateSubmittedEpoch()) + "_" + map.getUsername() + "_" + i + ".jpg");
                Log.d(TAG, "S3 URL: " + urlSB);
                ImageView iv = createImageView();
                Ion.with(iv)
                        .resize(reportImageWidth, reportImageHeight)
                        .placeholder(R.drawable.sunny)
                        .error(R.drawable.snow_icon)
                        .load(urlSB.toString());
//                BitmapUtility.scaleImage(mContext, iv);
                horizontalLinearLayout.addView(iv);
                urlSB.setLength(0);
            }
        }
        fragTransaction.commit();


//      Todo: CHANGE URL TO DYNAMICALLY CREATED ONE
        if (numberOfVideos > 0) {
            for (int i = 0; i < numberOfVideos; i++) {
                StringBuilder url = new StringBuilder();

//                 url.append("https://s3.amazonaws.com/skywarntestbucket/" +  String.format("%.0f" ,map.getDateSubmittedEpoch())+"_" + map.getUsername() + "_" + i +".mp4");
                url.append("https://s3.amazonaws.com/skywarntestbucket/1493426650833_oasker_0.mp4");
                Log.d(TAG, "video URL: " + url);
                Log.d(TAG, " real URL: https://s3.amazonaws.com/skywarntestbucket/1493426650833_oasker_0.mp4");
                AddVideosToUI(url.toString());

//                Set length of string builder to zero so it doesnt append over previous url
                url.setLength(0);
            }
        }
    }

    public void AddVideosToUI(String videoURL) {
        Log.d(TAG, "VIDEO TEST");
//                For videos
        try {
            Bitmap videoThumbnail = retriveVideoFrameFromVideo(videoURL);
//            FrameLayout frameLayout = new FrameLayout(this);
//            videoThumbnail
//          Create play icon for over video thumbnail
//            Bitmap playIcon = BitmapFactory.decodeResource(this.getResources(),
//                    R.drawable.mr_ic_play_dark);
//            playIcon = Bitmap.createScaledBitmap(playIcon, 50, 50, false);

//            frameLayout.addView(createVideoThumbnailView(videoThumbnail,videoURL));

//            frameLayout.addView(createImageView(playIcon));
//            Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(videoURL,MediaStore.Images.Thumbnails.MINI_KIND);


            horizontalLinearLayout.addView(createVideoThumbnailView(videoThumbnail, videoURL));


//            horizontalLinearLayout.addView(frameLayout);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private TableRow createAttributeTableRow(String labelString, String value){
        TableRow tr = new TableRow(this);
        tr.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView label = new TextView(this);
        label.setText(labelString);
        label.setTextColor(Color.BLACK);
        label.setTextSize(16);
        label.setPadding(10, 10, 10, 10);

        TextView val = new TextView(this);
        val.setText(value);
        val.setTextColor(Color.BLACK);
        val.setTextSize(16);
        val.setPadding(10, 10, 10, 10);

        tr.addView(label);
        tr.addView(val);

        return tr;
    }

    private TableRow createAttributeTitleRow(String title) {
        TableRow tr = new TableRow(this);
        tr.setBackgroundResource(R.color.backgroundColor);
        tr.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView label = new TextView(this);

        label.setText(title);
        label.setTextColor(Color.BLACK);
        label.setTextSize(16);
        label.setPadding(10, 10, 10, 10);
        label.setTextColor(Color.WHITE);
        label.setBackgroundResource(R.color.backgroundColor);
        tr.addView(label);

        return tr;
    }

    private void initGUI(SkywarnWSDBMapper Map) {
        map = Map;
    }

//    public void launchdownloadPhotoTask() throws ExecutionException, InterruptedException {
//        for(int i = 0; i < numberOfPhotos; i++) {
//            downloadPhotoTask photoTask = new downloadPhotoTask();
//            String filename = String.format("%.0f" ,map.getDateSubmittedEpoch()) + "_" + map.getUsername() + "_" +i+".jpg";
//            Log.d(TAG, "launchdownloadPhotoTask(): with filename: " +filename );
//            photoTask.setmContext(this);
//            photoTask.setCallback(callback);
//            photoTask.setFilename(filename);
//            photoTask.setfilePath(getCacheDir() + "/"+filename+".jpg");
//
//            File cache = this.getCacheDir();
//            File appDir= new File(cache+"/bingo.jpg");
//           // p.setFile(appDir);
//
//           // photoTask.setfilePath(cache+"/bingo.jpg");
//
//            photoTask.setNumPhotos(map.getNumberOfImages());
//            photoTask.setLinearLayout(horizontalLinearLayout);
//            photoTask.execute();
//            photoTask.get();
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "OnActivityResult");
            data.getExtras();
        }
        if (resultCode == RESULT_CANCELED) {
            Log.d(TAG, "Result_CANCELED");
        }
    }

    public void launchMapActivity() {
        Intent i = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("report", map);
        i.putExtras(bundle);
        startActivity(i);
    }

    //Create imageview with either bitmap or resourceID
    public ImageView createImageView(Bitmap b) {
        Log.d(TAG, "createImageView()");
        final Bitmap tempBitmap = b;
        ImageView IV = new ImageView(getApplicationContext());
        IV.setId(0);
        IV.requestLayout();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(reportImageWidth, reportImageHeight);
        IV.setLayoutParams(layoutParams);
        IV.setPadding(15, 15, 15, 15);
        IV.setImageBitmap(b);
        IV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewReportActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_view_photo_layout, null);
                TextView dateTV = (TextView) alertLayout.findViewById(R.id.dialog_dateTV);
                ImageView imageView = (ImageView) alertLayout.findViewById((R.id.imageView));
                imageView.setImageBitmap(tempBitmap);
                builder.setIcon(R.drawable.sunny)
                        .setMessage("message")
                        .setTitle("Title");
                builder.show();
            }

            //IV.setScaleType(ImageView.ScaleType.FIT_XY);
            // b = Bitmap.createScaledBitmap(b,IV.getWidth(),IV.getHeight(),false);
        });
        return IV;
    }

    public ImageView createVideoThumbnailView(Bitmap b, final String url) {
        Log.d(TAG, "createImageView()");
        final Bitmap tempBitmap = b;
        ImageView IV = new ImageView(getApplicationContext());
        IV.setId(0);
        IV.requestLayout();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(reportVideoThumbnailWidth, reportVideoThumbnailHeight);
        IV.setLayoutParams(layoutParams);
//        IV.setPadding(15, 15, 15, 15);
        IV.setImageResource(R.drawable.cloud);
//        IV.setImageBitmap(b);
        IV.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "VideoView onClick()");
                Intent fullScreenVideoIntent = new Intent(ViewReportActivity.this, ViewVideoFullScreenActivity.class);
                Bundle b = new Bundle();
                fullScreenVideoIntent.putExtra("videoURL", url);
                startActivity(fullScreenVideoIntent);

                return false;
            }
        });
        return IV;
    }


    //Create imageview with either bitmap or resourceID
    public ImageView createImageView() {
        Log.d(TAG, "createImageView()");
//        final Bitmap tempBitmap = b;
        ImageView IV = new ImageView(getApplicationContext());
        IV.setId(0);
        IV.requestLayout();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(reportImageWidth, reportImageHeight);
        IV.setLayoutParams(layoutParams);
        IV.setPadding(15, 15, 15, 15);
//        IV.setImageBitmap(b);
        IV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewReportActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_view_photo_layout, null);
                TextView dateTV = (TextView) alertLayout.findViewById(R.id.dialog_dateTV);
                ImageView imageView = (ImageView) alertLayout.findViewById((R.id.imageView));
//                imageView.setImageBitmap(tempBitmap);
                builder.setIcon(R.drawable.sunny)
                        .setMessage("message")
                        .setTitle("Title");
                builder.show();
            }

            //IV.setScaleType(ImageView.ScaleType.FIT_XY);
            // b = Bitmap.createScaledBitmap(b,IV.getWidth(),IV.getHeight(),false);
        });
        return IV;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");

    }

    public interface bitmapCallback {
        void onFinishedString(String s);
    }
}


class downloadPhotoTask extends AsyncTask<Void,Void,Bitmap> {
     private static final String TAG = "download";
     String filename;
     String filePath;
     Context mContext;
     int numPhotos;
     BitmapCallback callback;
     File file;
     LinearLayout linearLayout;
    CognitoCachingCredentialsProvider credentialsProvider;

     @Override
     protected Bitmap doInBackground(Void... params) {
         Log.d(TAG, "doInBackground() downloadPhoto()");
         CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                 mContext,
                 Constants.IDENTITY_POOL_ID, // Identity Pool ID
                 Regions.US_EAST_1           // Region
         );

         AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
         try {
             file = new File(filePath);
             TransferUtility transferUtility = new TransferUtility(s3Client, mContext);
             TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, filename, file);

             Log.d(TAG, "file.getPath(): " + file.getPath());

             transferObserver.setTransferListener(new TransferListener() {
                 @Override
                 public void onStateChanged(int id, TransferState state) {
                     Log.d(TAG, "onStateChanged: + state: " + state.toString());
                     if (state == TransferState.COMPLETED) {
                         Log.i(TAG, "number Photos: " + numPhotos++);
                         Bitmap b = BitmapFactory.decodeFile(filePath);
                         callback.processFinish(b);

                         Log.i(TAG, "onSTateChanged(): completed: bitmap Uri:" +getImageUri(mContext,b) );
                        // horizontalLinearLayout.addView(createImageView(b));
                         Log.i(TAG, "on StateChange: bitMap size(): " + b.getByteCount());
                         return;
                     }
                 }
                 @Override
                 public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                     int percentage = (int) ((bytesCurrent + 1) / (bytesTotal + 1) * 100);
                     Log.d(TAG, "onProgressChanged(): " + "Download status: " + percentage + " bytesTotal: " + bytesTotal + " bytesCurrent: " + bytesCurrent);
                 }

                 @Override
                 public void onError(int id, Exception ex) {
                     Log.e(TAG, "onError", ex);
                 }
             });

         } catch (AmazonClientException ace) {
             Log.d(TAG, "Caught an AmazonClientException, which means" +
                     " the client encountered " +
                     "an internal error while trying to " +
                     "communicate with S3, " +
                     "such as not being able to access the network.");
             Log.d(TAG, "Error Message: " + ace.getMessage());
         }
         return BitmapFactory.decodeFile(filePath);
     }
    @Override
     protected void onPreExecute() {
         CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                 mContext,
                 Constants.IDENTITY_POOL_ID, // Identity Pool ID
                 Regions.US_EAST_1           // Region
         );
         super.onPreExecute();
     }

     @Override
     protected void onPostExecute(Bitmap bm) {
         super.onPostExecute(bm);
     }

     @Override
     protected void onProgressUpdate(Void[] values) {
         super.onProgressUpdate(values);

     }
     public Uri getImageUri(Context inContext, Bitmap inImage) {
         ByteArrayOutputStream bytes = new ByteArrayOutputStream();
         inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
         String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
         return Uri.parse(path);
     }
     //////////////// setters and getters //////////////////////////
     public void setFilename(String filename) {
         this.filename = filename;
     }
     public void setFilePath(String filePath) {
         this.filePath = filePath;
     }
     public void setmContext(Context mContext) {
         this.mContext = mContext;
     }public void setNumPhotos(int numPhotos) {
         this.numPhotos = numPhotos;
     }
     public void setfilePath(String Path){
         filePath = Path;
     }
     public void setCallback(BitmapCallback callback) {
        this.callback = callback;
    }
     public void setLinearLayout(LinearLayout ll){
        linearLayout = ll;
    }

}