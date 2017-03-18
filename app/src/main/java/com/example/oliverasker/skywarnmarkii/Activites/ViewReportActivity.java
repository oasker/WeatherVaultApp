package com.example.oliverasker.skywarnmarkii.Activites;

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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.example.oliverasker.skywarnmarkii.Callbacks.UriCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Fragments.CoastalFloodingViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.GeneralSubmitReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.RainFloodViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.SevereViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Fragments.WinterViewReportFragment;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/*
 *  Activity for viewing report selected from listview
 */

public class ViewReportActivity extends Activity implements UriCallback{
    private static final String TAG = "ViewReportActivity";

    private WinterViewReportFragment winterFrag;
    private CoastalFloodingViewReportFragment coastalFloodingFrag;
    private RainFloodViewReportFragment rainFrag;
    private SevereViewReportFragment severeFrag;
    private GeneralSubmitReportFragment generalInfoFrag;

    SkywarnWSDBMapper map;
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
    public  LinearLayout horizontalLinearLayout;
    private Layout layout;

    private Button viewOnMapButton;

    private HashMap<String, String> valueMap;
    private ImageView iconImageView;
    AmazonS3Client s3;
    private int numberOfPhotos = 0;
    private int reportImageWidth = 400;
    private int reportImageHeight = 400;

    @Override
    public void onProcessComplete(Uri returnedUri) {
        Log.d(TAG, "onProcessComplete()");
       try {
           Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(),returnedUri);
           horizontalLinearLayout.addView(createImageView(b));
       }catch (IOException e){
           Log.e(TAG, "onOnProcess complete:", e);
       }
    }

    public interface bitmapCallback{
        void onFinishedString(String s);
    }

    BitmapCallback callback = new BitmapCallback() {
        @Override
        public void processFinish(ArrayList<Bitmap> result) {

        }

        @Override
        public void processFinish(Bitmap result) {
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
    };

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_view_report_layout);
        Log.d(TAG, "OnCreate()");
        Intent intent = getIntent();
        Bundle weatherReport = intent.getExtras();

        horizontalLinearLayout = (LinearLayout) findViewById(R.id.view_report_activity_horizontal_linearlayout);
        photoIV = (ImageView) findViewById(R.id.photoIV);
        valueMap = new HashMap<String, String>();
        map = (SkywarnWSDBMapper) weatherReport.get("selectedReport");
        initGUI(map);

        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();

        viewOnMapButton = (Button) findViewById(R.id.view_report_on_map_button);
        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMapActivity();
            }
        });

        numberOfPhotos = map.getNumberOfImages();

        if(numberOfPhotos <= 0) {
            photoLabelTV = (TextView) findViewById(R.id.view_report_activity_photo_label);
            photoLabelTV.setText("");
        }

        //Send data to fragments
        //Stupid way to do this i think, but need to brute force it
       // valueMap.put("Submitted", map.getDateSubmittedString());
        valueMap.put("Date Submitted Epoch", String.valueOf(map.getDateSubmittedEpoch()));
        //valueMap.put("Date Of Event", String.valueOf(map.getDateOfEvent()));

        // Winter Event
        valueMap.put("Snowfall", String.valueOf(map.getSnowfall()));
        valueMap.put("Snowfall Rate", String.valueOf(map.getSnowfallRate()));
        valueMap.put("Snow Depth", String.valueOf(map.getSnowDepth()));
        valueMap.put("Water Equivalent", String.valueOf(map.getWaterEquivalent()));
        valueMap.put("Freezing Rain", map.getFreezingRain());
        valueMap.put("Sleet", String.valueOf(map.getSnowFallSleet()));
        valueMap.put("BlowDrift", map.getBlowDrift());
        valueMap.put("Whiteout", map.getWhiteout());
        valueMap.put("ThunderSnow", map.getThundersnow());
        valueMap.put("Freeing Rain Accumulation", String.valueOf(map.getFreezingRainAccum()));


        valueMap.put("Number Of Fatalities", String.valueOf(map.getFatalities()));
        valueMap.put("Number Of Injuries", String.valueOf(map.getInjuries()));
        valueMap.put("Injuries Comments", String.valueOf(map.getInjuryComments()));

        //Severe Weather
        valueMap.put("Severe Weather Type", map.getSevereType());
        if(map.getWindSpeed() != 9999)
            valueMap.put("Wind Speed", String.valueOf(map.getWindSpeed() + " MPH"));
        if(map.getWindGust() != 9999)
            valueMap.put("Wind Gust", String.valueOf(map.getWindGust()) + " MPH");
        valueMap.put("Wind Direction", map.getWindDirection());
        valueMap.put("Hail Size", String.valueOf(map.getHailSize()));
        valueMap.put("Tornado", map.getTornado());
        valueMap.put("Barometer", String.valueOf(map.getBarometer()));
        valueMap.put("Wind Damage", map.getWindDamage());
        valueMap.put("Lightning Damage", map.getLightningDamage());
        valueMap.put("Damage Comments", map.getDamageComments());

        //Rainfall/flooding
        valueMap.put("Rain", String.valueOf(map.getRain()));
        valueMap.put("Freezing Rain", map.getFreezingRain());
        valueMap.put("Precipitation Rate", String.valueOf(map.getPrecipRate()));

        //  Get Number of photos and videos associate with a report
        valueMap.put("NumberOfVideos", String.valueOf(map.getNumberOfVideos()));
        valueMap.put("NumberOfImages", String.valueOf(map.getNumberOfImages()));

       // valueMap.put("Zip", map.getZipCode());
        valueMap.put("Username", map.getUsername());
        valueMap.put("Longitude", String.valueOf(map.getLongitude()));
        valueMap.put("Latitude", String.valueOf(map.getLatitude()));

        valueMap.put("Weather Event", map.getWeatherEvent());

        valueMap.put("Street", String.valueOf(map.getStreet()));
        //valueMap.put("City", String.valueOf(map.getEventCity()));
        //valueMap.put("State", String.valueOf(map.getState()));

        valueMap.put("Comments", map.getComments());

        valueMap.put("Current Temperature", String.valueOf(map.getCurrentTemperature()) + " F");

        Log.d(TAG, "Street: " + map.getStreet());
        //Remove unwanted views
        //Set<String> keySet = valueMap.keySet();
        Iterator<Map.Entry<String, String>> iter = valueMap.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String,String> entry = iter.next();
            String value = entry.getValue();
            if(value.equals("|") || value.trim().equals("9999") || value.equals("") | value.equals("false") | value.trim().equals("9999.0") | value.equals("0.0")|value.trim().equals("0.0 F") | value.equals("0") | value.equals("null") ) {
                Log.i(TAG, "REMOVING DEFAULT VALUES value: " + value);
                iter.remove();
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

        // Instantiate frags
        rainFrag = new RainFloodViewReportFragment();
        coastalFloodingFrag = new CoastalFloodingViewReportFragment();
        severeFrag = new SevereViewReportFragment();
        generalInfoFrag = new GeneralSubmitReportFragment();

        ///////////////////////////////////////////////////////////////////////////////
        //                          General Report Details                          //
        /////////////////////////////////////////////////////////////////////////////
        LinearLayout ll = (LinearLayout)findViewById(R.id.view_report_attribute_list);
        TableLayout tLayout = (TableLayout)findViewById(R.id.view_report_activity_tablelayout);
        valueMap.remove("NumberOfImages");
        valueMap.remove("NumberOfVideos");
        valueMap.remove("Date Submitted Epoch");
        valueMap.remove("Username");
        valueMap.remove("Street");

        android.widget.TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        int counter = 0;
        for(String key:valueMap.keySet()){
            createAttributeTableRow(key, valueMap.get(key));
            tLayout.addView(createAttributeTableRow(key, valueMap.get(key)));
            counter++;
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

            reportRating.setText(getString(R.string.rating) + map.getNetVote());
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
            Log.i(TAG, "NUMBERPHOTOS: " + numberOfPhotos);

        }
        fragTransaction.commit();
        try {
            launchdownloadPhotoTask();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private TableRow createAttributeTableRow(String labelString, String value){
        TableRow tr = new TableRow(this);
        tr.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView label = new TextView(this);
        label.setText(labelString);
        label.setTextColor(Color.BLACK);
        label.setTextSize(16);
        label.setPadding(3,3,3,3);

        TextView val = new TextView(this);
        val.setText(valueMap.get(labelString));
        val.setPadding(3,3,3,3);
        val.setTextColor(Color.BLACK);
        val.setTextSize(16);
        tr.addView(label);
        tr.addView(val);

        return tr;
    }

    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        b = Bitmap.createScaledBitmap(b, reportImageWidth, reportImageHeight, false);
        return new BitmapDrawable(getResources(), b);
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

    public void launchdownloadPhotoTask() throws ExecutionException, InterruptedException {
        for(int i = 0; i < numberOfPhotos; i++) {
            downloadPhotoTask photoTask = new downloadPhotoTask();
            String filename = String.format("%.0f" ,map.getDateSubmittedEpoch()) + "_" + map.getUsername() + "_" +i+".jpg";
            Log.d(TAG, "launchdownloadPhotoTask(): with filename: " +filename );
            photoTask.setmContext(this);
            photoTask.setCallback(callback);
            photoTask.setFilename(filename);
            photoTask.setfilePath(getCacheDir() + "/"+filename);
            photoTask.setNumPhotos(map.getNumberOfImages());
            photoTask.setLinearLayout(horizontalLinearLayout);
            photoTask.execute();
            photoTask.get();
        }
    }

    public void launchMapActivity() {
        Intent i = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("report", map);
        i.putExtras(bundle);
        startActivity(i);
    }

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

    public ImageView createImageView(int resourceID) {
        ImageView IV = new ImageView(getApplicationContext());
        IV.setId(0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(reportImageWidth, reportImageHeight);
        IV.setLayoutParams(layoutParams);
        IV.setPadding(15, 15, 15, 15);
        IV.setImageResource(resourceID);
        // IV.setScaleType(ImageView.ScaleType.FIT_XY);
        return IV;
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
     }public void setFilePath(String filePath) {
         this.filePath = filePath;
     }public void setmContext(Context mContext) {
         this.mContext = mContext;
     }public void setNumPhotos(int numPhotos) {
         this.numPhotos = numPhotos;
     }public void setfilePath(String Path){
         filePath = Path;
     }

    public void setCallback(BitmapCallback callback) {
        this.callback = callback;
    }

    public void setLinearLayout(LinearLayout ll){
        linearLayout = ll;
    }
}