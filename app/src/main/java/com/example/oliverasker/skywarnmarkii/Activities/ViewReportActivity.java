package com.example.oliverasker.skywarnmarkii.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.BooleanCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.UriCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.RateReportTask;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;
import com.example.oliverasker.skywarnmarkii.Utility.VideoUtility;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.text.DecimalFormat;
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
//                horizontalLinearLayout.addView(createImageView(result));
            }
        }
        @Override
        public void processFinish(Uri result) {
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
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://s3.amazonaws.com/skywarntestbucket/" + UserInformationModel.getInstance().getUsername() + ".jpg");
            if(result !=null) {
                Log.i(TAG, "processFinished(bitmap)");
//          For bitmaps
//                ImageView iV = new ImageView(getApplicationContext());
                ImageView iv = createImageView(urlBuilder.toString());
                Ion.with(iv)
                        .resize(Constants.VIEW_REPORT_ACTIVITY_IMAGE_WIDTH, Constants.VIEW_REPORT_ACTIVITY_IMAGE_HEIGHT)
                        .placeholder(R.drawable.sunny)
                        .error(R.drawable.snow_icon)
                        .load(urlBuilder.toString());

                iv.setImageBitmap(result);
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
    private TextView username;
    private TextView DateOfEvent;
    private TextView eventType;
    private TextView location;
    private TextView longitude;
    private TextView latitude;
    private TextView comments;
    private TextView reportRatingTV;
    private TextView photoLabelTV;
    private Button upVoteButton;
    private Button downVoteButton;
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
    private int reportVideoThumbnailWidth = 350;
    private int reportVideoThumbnailHeight = 350;
    private String reportName;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private String todaysDateString;

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

//    Todo: severeWeather View needs to have some attributes only added if others are present
    //    Todo: make check when user first looks at report and disable rating buttons if theyve voted or its their report
    @Override
    public void UserHasRatedReport(Boolean b, int netVote) {
        Log.d(TAG, "UserHasRatedReportBefore? " + b);
//        //reportRating.setText(String.valueOf(map.getNetVote()));
//        if (b != null) {
//            upVoteButton.setEnabled(false);
//            downVoteButton.setEnabled(false);
//
//            Log.d(TAG, "serHasRatedReportBefore: netVote" + String.valueOf(netVote));
//            reportRating.setText("Rating: " + String.valueOf(netVote));
//        }
    }


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_view_report_layout);
        Log.d(TAG, "OnCreate()");
        Intent intent = getIntent();
        Bundle viewActivityBundle = intent.getExtras();
        map = (SkywarnWSDBMapper) viewActivityBundle.get("selectedReport");

//        Remove scientific notation from report name
        DecimalFormat df = new DecimalFormat("0");
        reportName = df.format(map.getDateSubmittedEpoch());

        todaysDateString = sdf.format(System.currentTimeMillis());

        initGUI(map);



        valueMap = new HashMap<>();
        winterAttrMap = new HashMap<>();
        severeAttrMap = new HashMap<>();
        rainAttrMap = new HashMap<>();
        coastalAttrMap = new HashMap<>();


        horizontalLinearLayout = (LinearLayout) findViewById(R.id.view_report_activity_horizontal_linearlayout);

        //Rating system widgets
        upVoteButton = (Button) findViewById(R.id.up_vote_button);
        downVoteButton = (Button) findViewById(R.id.down_vote_button);


        if (UserInformationModel.getInstance().getRatedReportsTextFileContents().trim().contains(reportName)) {
            Log.d(TAG, "report has been previously DOWNvoted");
            upVoteButton.setEnabled(false);
            downVoteButton.setEnabled(false);
        }

        upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!UserInformationModel.getInstance().getRatedReportsTextFileContents().contains(reportName)) {

                    String ratedReportString = UserInformationModel.getInstance().getRatedReportsTextFileContents();

                    StringBuilder newRatedReportsSB = new StringBuilder();
                    newRatedReportsSB.append(ratedReportString + "\n"
                            + todaysDateString + "_" + reportName + "-U");

                    UserInformationModel.getInstance().setRatedReportsTextFileContents(newRatedReportsSB.toString());

                    Log.d(TAG, "REPORTNAME: " + reportName + "\n NEW RATEDREPORTS: " + UserInformationModel.getInstance().getRatedReportsTextFileContents());
                    Log.d(TAG, "EXT: " + getExternalCacheDir());

//                    increment netvote
                    map.setNetVote(map.getNetVote() + 1);

                    //text files hold all reports that the user has rated
                    RateReportTask rateTask = new RateReportTask();
                    rateTask.setmContext(ViewReportActivity.this);
                    rateTask.setUrl(UserInformationModel.getInstance().getUsername());
                    rateTask.setReportName(todaysDateString + "_" + reportName);
                    rateTask.setFilePath(getCacheDir() + "/");
                    rateTask.setUsername(UserInformationModel.getInstance().getUsername());
                    rateTask.setCallback(ViewReportActivity.this);
                    rateTask.setExternalStorageCache(getFilesDir().toString());
                    rateTask.setReportRating(map.getNetVote());

                    rateTask.setReportPrimaryKey(map.getDateSubmittedString());
                    rateTask.setRangeKey(String.valueOf(map.getDateSubmittedEpoch()));
                    rateTask.setVote("-U");
                    rateTask.execute();

                    reportRatingTV.setText("Rating: " + String.valueOf(map.getNetVote()));

                    downVoteButton.setEnabled(false);
                    upVoteButton.setEnabled(false);
                }
            }
        });

        downVoteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!UserInformationModel.getInstance().getRatedReportsTextFileContents().trim().contains(reportName)) {
                    String ratedReportString = UserInformationModel.getInstance().getRatedReportsTextFileContents();
                    UserInformationModel.getInstance().setRatedReportsTextFileContents(ratedReportString.concat("\n"
                            + todaysDateString + "_"
                            + reportName + "-D"));

                    Log.d(TAG, "REPORTNAME: " + reportName + " NEW RATEDREPORTS: " + UserInformationModel.getInstance().getRatedReportsTextFileContents());


//                  decrement net vote
                    map.setNetVote(map.getNetVote() - 1);

                    RateReportTask rateTask = new RateReportTask();
                    rateTask.setmContext(ViewReportActivity.this);
                    rateTask.setCallback(ViewReportActivity.this);


                    //Url to S3 text file


                    rateTask.setUrl(UserInformationModel.getInstance().getUsername());
                    rateTask.setReportName(todaysDateString + "_" + reportName);
                    rateTask.setFilePath(getCacheDir() + "/");
                    rateTask.setUsername(UserInformationModel.getInstance().getUsername());

                    rateTask.setReportRating(map.getNetVote());
                    rateTask.setReportPrimaryKey(map.getDateSubmittedString());
                    rateTask.setRangeKey(String.valueOf(map.getDateSubmittedEpoch()));

                    rateTask.setVote("-D");
                    rateTask.execute();


                    reportRatingTV.setText("Rating: " + String.valueOf(map.getNetVote()));

                    downVoteButton.setEnabled(false);
                    upVoteButton.setEnabled(false);
                }
            }
        });


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
            if (value.trim().equals("|") || value.trim().equals("9999") || value.trim().equals("") || value.trim().equals("false") || value.trim().equals("9999.0") || value.trim().equals("0.0") || value.trim().equals("0.0F") || value.trim().equals("0") || value.trim().equals("null")) {
//                Log.d(TAG, "key: " + entry +" value: " + value);
                coastalIter.remove();
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


//                       Date Format Stuff

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
        reportRatingTV = (TextView) findViewById(R.id.view_report_activity_report_rating);

        reportRatingTV.setText("Rating: " + String.valueOf(map.getNetVote()));
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
                ImageView iv = createImageView(urlSB.toString());

                Ion.with(iv)
                        .resize(Constants.VIEW_REPORT_ACTIVITY_IMAGE_WIDTH, Constants.VIEW_REPORT_ACTIVITY_IMAGE_HEIGHT)
                        .placeholder(R.drawable.sunny)
                        .error(R.drawable.snow_icon)
                        .load(urlSB.toString());
                horizontalLinearLayout.addView(iv);
                urlSB.setLength(0);
            }
        }

        if (numberOfVideos > 0) {
            for (int i = 0; i < numberOfVideos; i++) {
                StringBuilder url = new StringBuilder();

                url.append("https://s3.amazonaws.com/skywarntestbucket/" + String.format("%.0f", map.getDateSubmittedEpoch()) + "_" + map.getUsername() + "_" + i + ".mp4");
                AddVideosToUI(url.toString());

//              Set length of string builder to zero so it doesnt append
//              over previous url
                url.setLength(0);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    private void initGUI(SkywarnWSDBMapper Map) {
        map = Map;
    }

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

    //  Methods that create view from images and videos
    public void AddVideosToUI(String videoURL) {
        Log.d(TAG, "VIDEO TEST");
        try {
            Bitmap videoThumbnail = VideoUtility.retriveVideoFrameFromVideo(videoURL);
            horizontalLinearLayout.addView(createVideoThumbnailView(videoThumbnail, videoURL));
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

    //Create imageview with either bitmap or resourceID
    public ImageView createImageView(Bitmap b) {
        Log.d(TAG, "createImageView()");
        final Bitmap tempBitmap = b;
        ImageView IV = new ImageView(getApplicationContext());
        IV.setId(0);
        IV.requestLayout();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Constants.VIEW_REPORT_ACTIVITY_IMAGE_WIDTH, Constants.VIEW_REPORT_ACTIVITY_IMAGE_HEIGHT);
        IV.setLayoutParams(layoutParams);
        IV.setPadding(15, 15, 15, 15);
        IV.setImageBitmap(b);
        IV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(ViewReportActivity.this);
//                LayoutInflater inflater = getLayoutInflater();
//                View alertLayout = inflater.inflate(R.layout.dialog_view_photo_layout, null);
//                TextView dateTV = (TextView) alertLayout.findViewById(R.id.dialog_dateTV);
//                ImageView imageView = (ImageView) alertLayout.findViewById((R.id.imageView));
//                imageView.setImageBitmap(tempBitmap);
//                builder.setIcon(R.drawable.sunny)
//                        .setMessage("message")
//                        .setTitle("Title");
//                builder.show();

            }

            //IV.setScaleType(ImageView.ScaleType.FIT_XY);
            // b = Bitmap.createScaledBitmap(b,IV.getWidth(),IV.getHeight(),false);
        });
        return IV;
    }

    //Create imageview with either bitmap or resourceID
    public ImageView createImageView(final String url) {
        Log.d(TAG, "createImageView()");
//        final Bitmap tempBitmap = b;
        final ImageView IV = new ImageView(getApplicationContext());
        IV.setId(0);
        IV.requestLayout();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Constants.VIEW_REPORT_ACTIVITY_IMAGE_WIDTH, Constants.VIEW_REPORT_ACTIVITY_IMAGE_HEIGHT);
        IV.setLayoutParams(layoutParams);
        IV.setPadding(15, 15, 15, 15);
//        IV.setImageBitmap(b);

//        If selected show image fullscreen
        IV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent fullScreenImageIntent = new Intent(getApplicationContext(), ViewPhotoFullScreenActivity.class);
                fullScreenImageIntent.putExtra("imageURL", url);
                startActivity(fullScreenImageIntent);
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
        IV.setImageResource(R.drawable.video_placeholder_thumbnail);
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

    //  Callback methods
    @Override
    public void onProcessComplete(Uri returnedUri) {
        Log.d(TAG, "onProcessComplete(Uri returnedUri)");
    }

    //  Callbacks
    public interface bitmapCallback {
        void onFinishedString(String s);
    }
}

