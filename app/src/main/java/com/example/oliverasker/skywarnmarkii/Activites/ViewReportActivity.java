package com.example.oliverasker.skywarnmarkii.Activites;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Fragments.CoastalFloodingViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.GeneralSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.RainFloodViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.SevereViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.WinterViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.io.File;
import java.util.HashMap;

/*
 *  Activity for viewing report selected from listview
 */

public class ViewReportActivity extends Activity {
    private static final String TAG = "ViewReportActivity";

    private WinterViewReportFragment winterFrag;
    private CoastalFloodingViewReportFragment coastalFloodingFrag;
    private RainFloodViewReportFragment rainFrag;
    private SevereViewReportFragment severeFrag;
    private GeneralSubmitReportFragment generalInfoFrag;

    SkywarnWSDBMapper map;

    ImageView photoIV;
    TextView username;
    TextView DateOfEvent;
    TextView eventType;
    TextView location;
    TextView longitude;
    TextView latitude;
    TextView comments;
    TextView reportRating;
    LinearLayout horizontalLinearLayout;
    Layout layout;

    private Button viewOnMapButton;

    HashMap<String,String> valueMap;
    ImageView iconImageView;
    AmazonS3Client s3;
    private int numberOfPhotos=0;
    private int reportImageWidth = 375;
    private int reportImageHeight = 375;
    private int numPhoto=0;
    private int photoNumber;

    //Download photo callback
    public interface bitmapCallback{
        void onFinishedBitmap(Bitmap b);
        void onFinishedString(String s);
    }
    bitmapCallback callback  = new bitmapCallback() {
        @Override
        public void onFinishedString(String s) {
            Log.d(TAG, "onFinished(String): s: "+ s);
            //Bitmap b = BitmapFactory.decodeFile("/storage/emulated/0/Android/data/com.example.oliverasker.skywarnmarkii/cache/1488476818_tjpereira1995_0");
            Bitmap b = BitmapFactory.decodeFile(s);
            //iconImageView.setImageBitmap(b);
        }

        //ToDo: Set correct photos when viewing report
        @Override
        public void onFinishedBitmap( Bitmap b) {
        }
    };


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_view_report_layout);
        Log.d(TAG, "OnCreate()");
        Intent intent = getIntent();
        Bundle weatherReport = intent.getExtras();

        horizontalLinearLayout = (LinearLayout)findViewById(R.id.view_report_activity_horizontal_linearlayout);
        photoIV = (ImageView)findViewById(R.id.photoIV);
//        //Get s3 images
//        launchDownloadPhotoTask();
//
        valueMap = new HashMap<String, String>();
        map = (SkywarnWSDBMapper) weatherReport.get("selectedReport");
        initGUI(map);

        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();

        viewOnMapButton = (Button)findViewById(R.id.view_report_on_map_button);
        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMapActivity();
            }
        });

        numberOfPhotos = map.getNumberOfImages();

        //Send data to fragments
        //Stupid way to do this i think, but need to brute force it
        valueMap.put("DateSubmittedString", map.getDateSubmittedString());
        valueMap.put("DateSubmittedEpoch", String.valueOf(map.getDateSubmittedEpoch()));
        valueMap.put("DateOfEvent", String.valueOf(map.getDateOfEvent()));

        // Winter Event
        valueMap.put("Snowfall", String.valueOf(map.getSnowfall()));
        valueMap.put("SnowfallRate", String.valueOf(map.getSnowfallRate()));
        valueMap.put("SnowDepth", String.valueOf(map.getSnowDepth()));
        valueMap.put("WaterEquivalent", String.valueOf(map.getWaterEquivalent()));
        valueMap.put("FreezingRain", map.getFreezingRain());
        valueMap.put("Sleet", String.valueOf(map.getSnowFallSleet()));
        valueMap.put("BlowDrift", map.getBlowDrift());
        valueMap.put("Whiteout", map.getWhiteout());
        valueMap.put("ThunderSnow", map.getThundersnow());
        valueMap.put("FreeingRainAccum",String.valueOf(map.getFreezingRainAccum()));


        valueMap.put("NumberOfFatalities",String.valueOf(map.getFatalities()));
        valueMap.put("NumberOfInjuries",String.valueOf(map.getInjuries()));
        valueMap.put("InjuriesComments",String.valueOf(map.getInjuryComments()));

        //Severe Weather
        valueMap.put("SevereType", map.getSevereType());
        valueMap.put("WindSpeed", String.valueOf(map.getWindSpeed() + " MPH"));
        valueMap.put("WindGust", String.valueOf(map.getWindGust()) + " MPH");
        valueMap.put("WindDirection", map.getWindDirection());
        valueMap.put("HailSize", String.valueOf(map.getHailSize()));
        valueMap.put("Tornado", map.getTornado());
        valueMap.put("Barometer", String.valueOf(map.getBarometer()));
        valueMap.put("WindDamage", map.getWindDamage());
        valueMap.put("LightningDamage", map.getLightningDamage());
        valueMap.put("DamageComments", map.getDamageComments());

        //Rainfall/flooding
        valueMap.put("Rain", String.valueOf(map.getRain()));
        valueMap.put("FreezingRain", map.getFreezingRain());
        valueMap.put("PrecipRate", String.valueOf(map.getPrecipRate()));

        //  Get Number of photos and videos associate with a report
        valueMap.put("NumberOfVideos",String.valueOf(map.getNumberOfVideos()));
        valueMap.put("NumberOfImages",String.valueOf(map.getNumberOfImages()));

        valueMap.put("ZipCode", map.getZipCode());
        valueMap.put("Username", map.getUsername());
        valueMap.put("Longitude", String.valueOf(map.getLongitude()));
        valueMap.put("Latitude", String.valueOf(map.getLatitude()));

        valueMap.put("WeatherEvent",map.getWeatherEvent());

        valueMap.put("Street", String.valueOf(map.getStreet()));
        valueMap.put("City", String.valueOf(map.getEventCity()));
        valueMap.put("State", String.valueOf(map.getState()));
        valueMap.put("Comments", map.getComments());
        valueMap.put("CurrentTemperature", String.valueOf(map.getCurrentTemperature()) + " F");




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
        if(map.getWeatherEvent().toUpperCase().contains("GENERAL")) {
            iconImageView.setImageResource(R.drawable.sunny);
        }

        // Instantiate frags
        rainFrag = new RainFloodViewReportFragment();
        coastalFloodingFrag = new CoastalFloodingViewReportFragment();
        severeFrag = new SevereViewReportFragment();
        generalInfoFrag= new GeneralSubmitReportFragment();


        ///////////////////////////////////////////////////////////////////////////////
        //                          General Report Details                          //
        /////////////////////////////////////////////////////////////////////////////
        username = (TextView)findViewById(R.id.view_report_activity_reporter);
        username.setText(getString(R.string.submited_by) + map.getUsername());

        ///////////////////////////////////////////////
        //               Date Format Stuff           //
        ///////////////////////////////////////////////
        DateOfEvent = (TextView)findViewById(R.id.view_report_activity_date);
        String date = Utility.epochToDateTimeString(map.getDateOfEvent());
        DateOfEvent.setText(date);

        comments = (TextView)findViewById(R.id.view_report_activity_comments);
        comments.setText(map.getComments());

        eventType = (TextView)findViewById(R.id.view_report_activity_weather_event);
        eventType.setText(map.getWeatherEvent() );

        location = (TextView)findViewById(R.id.view_report_activity_location);
        location.setText(map.getStreet() + " " + map.getEventCity() + ", " + map.getEventState());

        reportRating = (TextView)findViewById(R.id.view_report_activity_report_rating);
        reportRating.setText(getString(R.string.rating) + map.getRating());

        longitude = (TextView)findViewById(R.id.view_report_activity_long_latt);
        //longitude.setText("Lat/Long: " + map.getLattitude()+ (char)0x00B0 + " " + map.getLongitude()+(char)0x00B0 );
        generalInfoFrag = new GeneralSubmitReportFragment().newInstance(valueMap);
        fragTransaction.add(R.id.frag0_container, generalInfoFrag, "general_info_submit_fragment");



        if(map.getWeatherEvent().toUpperCase().contains("SEVERE")) {
            severeFrag = new SevereViewReportFragment().newInstance(valueMap);
            fragTransaction.add(R.id.frag1_container, severeFrag, "severeFrag");
        }
        if(map.getWeatherEvent().toUpperCase().contains("WINTER")) {
            winterFrag = new WinterViewReportFragment().newInstance(valueMap);
            fragTransaction.add(R.id.frag2_container, winterFrag, "winterFrag");
        }
        if(map.getWeatherEvent().toUpperCase().contains("COASTAL")) {
            coastalFloodingFrag = new CoastalFloodingViewReportFragment().newInstance(valueMap);
            fragTransaction.add(R.id.frag3_container, coastalFloodingFrag, "coastalFrag");
        }
        if(map.getWeatherEvent().toUpperCase().contains("RAIN")) {
            rainFrag = new RainFloodViewReportFragment().newInstance(valueMap);
            fragTransaction.add(R.id.frag4_container, rainFrag, "rainFrag");
        }


        storagePermitted(this);
        Log.d(TAG, "Epoch:" + map.getDateOfEvent() + " NumberPhotos: " + map.getNumberOfImages());
        if(numberOfPhotos >0) {
            Log.i(TAG, "NUMBERPHOTOS: " + numberOfPhotos);
            launchDownloadPhotoTask();
        }
        fragTransaction.commit();
    }

    private Drawable resize(Drawable image){
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        b = Bitmap.createScaledBitmap(b, reportImageWidth,reportImageHeight,false);
        return new BitmapDrawable(getResources(),b);
    }

    private void initGUI(SkywarnWSDBMapper Map){
        map = Map;
    }

    public void launchDownloadPhotoTask(){
        Log.d(TAG, "launchDownloadPhotoTask()");
        downloadPhoto();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        if(resultCode == RESULT_OK){
            Log.d(TAG, "OnActivityResult");
            data.getExtras();
        }
        if(resultCode == RESULT_CANCELED){
            Log.d(TAG, "Result_CANCELED");
        }
    }

    public void launchMapActivity(){
        Intent i = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("report", map);
        i.putExtras(bundle);
        startActivity(i);
    }

    private static boolean storagePermitted(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUESTCODE_WRITE_EXTERNAL_STORAGE);

        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "storagePermitted: Granted" + ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE.toString()));
            return true;
        }
        else {
            Log.d(TAG, "storagePermitted: Denied: "+ ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE.toString()));

            return false;
        }
    }

    //Create imageview with either bitmap or resourceID
    public ImageView createImageView(Bitmap b){
        Log.d(TAG, "createImageView()");
        final Bitmap tempBitmap = b;
        ImageView IV = new ImageView(getApplicationContext());
        IV.setId(0);
        IV.requestLayout();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(reportImageWidth,reportImageHeight);
        IV.setLayoutParams(layoutParams);
        IV.setPadding(15,15,15,15);
        IV.setImageBitmap(b);
        IV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewReportActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_view_photo_layout, null);
                TextView dateTV = (TextView)alertLayout.findViewById(R.id.dialog_dateTV);
                ImageView imageView = (ImageView)alertLayout.findViewById((R.id.imageView));
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

    public ImageView createImageView(int resourceID){
        ImageView IV = new ImageView(getApplicationContext());
        IV.setId(0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(reportImageWidth,reportImageHeight);
        IV.setLayoutParams(layoutParams);
        IV.setPadding(15,15,15,15);
        IV.setImageResource(resourceID);
        // IV.setScaleType(ImageView.ScaleType.FIT_XY);
        return IV;
    }

    public void downloadPhoto(){
        Log.d(TAG, "downloadPhoto()");
        String filename = map.getDateOfEvent() + "_" + map.getUsername()+"_" + photoNumber + ".jpg";
       String externalStorage = this.getCacheDir().toString()+"/";
        final String filePath = externalStorage+filename;
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                this,
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1           // Region
        );

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
        try {
            File file =new File(filePath);
            TransferUtility transferUtility = new TransferUtility(s3Client, this);
            TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, filename, file);

            Log.d(TAG, "file.getPath(): " + file.getPath() );

            transferObserver.setTransferListener(new TransferListener(){
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d(TAG, "onStateChanged: + state: "+ state.toString());
                    if(state == TransferState.COMPLETED){
                        Log.i(TAG, "number Photos: " +numPhoto++);
                        Bitmap b = BitmapFactory.decodeFile(filePath);
                        horizontalLinearLayout.addView(createImageView(b));
                    }
                }
                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    int percentage = (int) ((bytesCurrent+1) /(bytesTotal+1) * 100);
                    Log.d(TAG, "onStateChanged(); bytesTotal: " +bytesTotal +" bytesCurrent: "+ bytesCurrent) ;
                }
                @Override
                public void onError(int id, Exception ex) {
                    Log.e(TAG, "onError",ex);
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
    }
}
