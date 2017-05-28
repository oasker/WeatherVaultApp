package com.example.oliverasker.skywarnmarkii.Activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMappingException;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Models.MyImageModel;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility.BitmapUtility;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;
import com.example.oliverasker.skywarnmarkii.Utility.VideoUtility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.String.valueOf;

/**
 * Created by oliverasker on 1/2/17.
 * Resources: https://junjunguo.com/blog/android-take-photo-show-in-list-view-b/
 */

//add photos to sd card
//Command Line: adb push yourfile.xxx /sdcard/yourfile.xxx
public class LaunchCameraActivity extends AppCompatActivity {
    static final int SELECT_EXISTING_MEDIA = 3;
    //Request codes
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_STORE_PHOTOS = 2;
    private static int pictureCount;
    private static int videoCount;
    private static String selectedImagePath;
    private final String TAG = "LaunchCameraActivity";


    //    ArrayList<Uri> bitMapPathList;
    //  Report keys and values
    public AttributeValue[] attributeValArray;
    public String[] keyArray;
    int position = 0;
    ArrayList<String> photoPaths = new ArrayList<>();
    String mCurrentPhotoPath;

    //Manages S3 transfers
    TransferUtility transferUtility;
    private long epoch;
    private String dateSubmittedString;
    private ArrayList<MyImageModel> images = new ArrayList();
    private ArrayList<String> imagePaths = new ArrayList();
    private ArrayList<String> videoPaths = new ArrayList<>();
    private MediaController mediaControls;
    private Button submitButton;
    private Button addExistingPhotostoReportButton;
    private Button addExistingVideostoReportButton;

    private Button takeNewPhotoButton;
    private Button continueNoPhotosButton;
    private Button addPhotostoReportButton;
    private VideoView mVideoView;
    private Uri mCapturedImageURI;
    private LinearLayout previewPhotoLayout;
    private VideoView testVideoView;


    @Override
    protected void onCreate(Bundle savedInstace) {
        super.onCreate(savedInstace);
        setContentView(R.layout.activity_launch_camera_layout);


        //  Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        //  Retreive Data from SubmitReportDetailsActivity to submit after user
        // takes (or doesnt take) a photo
        Intent i = getIntent();
        epoch = i.getLongExtra("epoch", -1);
        dateSubmittedString = i.getStringExtra("DateSubmittedString");
        Object[] tempObj = (Object[]) i.getSerializableExtra("attributeValArray");
        if (tempObj != null) {
            attributeValArray = Arrays.copyOf(tempObj, tempObj.length, AttributeValue[].class);
        }

        if (i.getStringArrayExtra("keyArray") != null) {
            keyArray = i.getStringArrayExtra("keyArray");
            for (int j = 0; j < keyArray.length; j++) {
                Log.d(TAG, " onCreate(): key: " + keyArray[j] + " val: " + attributeValArray[j]);
            }
        }


        previewPhotoLayout = (LinearLayout) findViewById(R.id.preview_photo_holder);

        /////////////////////////////////////////////////////
        //              TAKE NEW PHOTO                     //
        /////////////////////////////////////////////////////
        if (!hasCamera())
            takeNewPhotoButton.setEnabled(false);

        takeNewPhotoButton = (Button) findViewById(R.id.take_new_photo_button);
        takeNewPhotoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isStoragePermissionGranted())
                            launchCamera(view);
                    }
                }
        );

        //BROWSE EXISTING PHOTOS
        addExistingPhotostoReportButton = (Button) findViewById(R.id.upload_existing_button);
        addExistingPhotostoReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // in onCreate or any event where your want the user to select a file
//                if (isStoragePermissionGranted()) {
//                    Intent intent = new Intent();
//                    intent.setType("image/* video/*");
////                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_EXISTING_MEDIA);
//                }
                if (isStoragePermissionGranted()) {
                    Intent intent = new Intent();
                    intent.setType("image/* video/*");
//                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Video"), SELECT_EXISTING_MEDIA);
                }
            }
        });

//        //BROWSE EXISTING Videos
//        addExistingVideostoReportButton = (Button) findViewById(R.id.upload_existing_video_button);
//        addExistingVideostoReportButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                // in onCreate or any event where your want the user to select a file
//                if (isStoragePermissionGranted()) {
//                    Intent intent = new Intent();
//                    intent.setType("image/* video/*");
////                    intent.setType("video/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Video"), SELECT_EXISTING_VIDEO);
//                }
//            }
//        });


        //SEND PHOTOS TO S3
        addPhotostoReportButton = (Button) findViewById(R.id.submit_user_photo_button);
        addPhotostoReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, " AddPhotosToReport.onClick()");
                for (int i = 0; i < images.size(); i++) {
                    Log.d(TAG, "addPhotosToReportButton: paths: " + images.get(i).getPath());

//                    beginUpload(images.get(i).getPath(), ".jpg");
//                    uploadFiles(images.get(i).getPath(),".jpg");
                    imagePaths.add(images.get(i).getPath());
                    pictureCount++;
                }

//              Upload images
                uploadFiles(imagePaths, ".jpg");
//              Upload videos
                uploadFiles(videoPaths, ".mp4");
                videoCount++;

//               Update NumberOfImages
                int numberImagesIndex = Arrays.asList(keyArray).indexOf("NumberOfImages");
                if (numberImagesIndex != -1) {
                    attributeValArray[numberImagesIndex] = new AttributeValue().withN(valueOf(pictureCount));
                }
//              Update NumberOfVideos
                int numberVideosIndex = Arrays.asList(keyArray).indexOf("NumberOfVideos");
                if (numberVideosIndex != -1) {
                    attributeValArray[numberVideosIndex] = new AttributeValue().withN(valueOf(videoCount));
                }
                AsyncInsertTask2 insertTask2 = new AsyncInsertTask2(attributeValArray);
                insertTask2.execute(keyArray);
                launchConfirmSubmitReport();
            }
        });

        continueNoPhotosButton = (Button) findViewById(R.id.continue_no_photos_button);
        continueNoPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncInsertTask2 insertTask2 = new AsyncInsertTask2(attributeValArray);
                insertTask2.execute(keyArray);
                launchConfirmReportActivity();
            }
        });

//        takeNewVideoButton = (Button) findViewById(R.id.take_new_video_button);
//        takeNewVideoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "takeVideoButton onClick()");
        //launchCaptureNewVideo();
        //launchVideoCamera(v);

//                String timestamp="1";
//                timestamp = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss aa").format(Calendar.getInstance().getTime());
//                File filepath = Environment.getExternalStorageDirectory();
//                File dir = new File(filepath.getAbsolutePath()+ "/samplevideofolder/");
//                dir.mkdirs();
//                File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/samplevideofolder/Video_"+timestamp+".avi");
//                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                //Uri fileUri = Uri.fromFile(mediaFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFile);
//                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
//                startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);

//                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                String fName = "VideoFileName.mp4";
//                File f = new File(fName);
//               // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
//
////                File f = new File(getCacheDir().toString()+"/testVideo1.mp4");
//                File f = new File(Environment.getExternalStorageDirectory().toString() + "TESTVIDEO");
//                if(!f.exists()){
//                    f.mkdir();
//                }
//                video_file = new File(f, "sample_video.mp4");
//                videoURI = Uri.fromFile(f);
//                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//
//
//                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, video_file);
//
//                if(takeVideoIntent.resolveActivity(getPackageManager()) !=null){
//                    startActivityForResult(takeVideoIntent,REQUEST_VIDEO_CAPTURE);
//                }
//                if (isStoragePermissionGranted())
//                    dispatchTakeVideoIntent();
//            }
//        });
        // takeNewVideoButton.setEnabled(false);

        if (mediaControls == null) {
            mediaControls = new MediaController(LaunchCameraActivity.this);
        }
        ////// AWS Example method
        //https://github.com/awslabs/aws-sdk-android-samples/blob/master/S3TransferUtilitySample/src/com/amazonaws/demo/s3transferutility/UploadActivity.java
        /////////////////// ///////////////////
        transferUtility = Utility.getTransferUtility(this);
    }

//    public void grabImage(ImageView imageView, Uri mImageUri) {
//        this.getContentResolver().notifyChange(mImageUri, null);
//        ContentResolver cr = this.getContentResolver();
//        Bitmap bitmap;
//        try {
//            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
//            imageView.setImageBitmap(bitmap);
//        } catch (Exception e) {
//            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "Failed to load", e);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(data!=null && data.getExtras() !=null){
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Log.d(TAG, "onActivityResult() REQUEST_IMAGE_CAPTURE");
                Bundle extras = data.getExtras();
                Bitmap b = (Bitmap) extras.get("data");

                Uri photoURI = BitmapUtility.getImageUri(this, b);
                File finalFile = new File(BitmapUtility.getRealPathFromURI(photoURI, this));

                MyImageModel image = new MyImageModel();
                image.setTitle("Test");
                image.setDescription(
                        "test take a photo and add it to list view");
                image.setDatetime(System.currentTimeMillis());
                image.setPath(finalFile.getPath());
                images.add(image);
                ImageView existingImageIV = BitmapUtility.createdScaledImageView(b, this);
                previewPhotoLayout.addView(existingImageIV);
            }
        }


        //Existing Photo
//        if(resultCode == RESULT_OK && requestCode == SELECT_EXISTING_PICTURE){
////        if (requestCode == SELECT_EXISTING_PICTURE) {
//            Bitmap existingBitmap = null;
//            if (data != null) {
//                try {
//                    existingBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
//                    Uri uri = BitmapUtility.getImageUri(this, existingBitmap);
//                    Log.d(TAG, "URI: " + uri);
//                    String path = BitmapUtility.getRealPathFromURI(uri, this);
//                    Log.d(TAG, "path: " + path);
//
//                    MyImageModel image = new MyImageModel();
//                    image.setTitle("Test");
//                    image.setDescription(
//                            "test take a photo and add it to list view");
//                    image.setDatetime(System.currentTimeMillis());
//                    image.setPath(path);
//                    images.add(image);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            ImageView existingImageIV = BitmapUtility.createdScaledImageView(existingBitmap, this);
//            previewPhotoLayout.addView(existingImageIV);
//        }


        if (requestCode == SELECT_EXISTING_MEDIA) {
            Log.d(TAG, "onActivityResult()-> requestCode = SELECT_EXISTING_VIDEO");
            if (data != null) {
                Log.d(TAG, "onActivityResult() data!=null");

                Uri selectedMediaUri = data.getData();

                if (!selectedMediaUri.toString().contains("video")) {
                    Log.d(TAG, "image Uri returned");

                    Bitmap existingBitmap = null;
                    try {
                        existingBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        Uri uri = BitmapUtility.getImageUri(this, existingBitmap);
                        Log.d(TAG, "URI: " + uri);
                        String path = BitmapUtility.getRealPathFromURI(uri, this);
                        Log.d(TAG, "path: " + path);

                        MyImageModel image = new MyImageModel();
                        image.setTitle("Test");
                        image.setDescription(
                                "test take a photo and add it to list view");
                        image.setDatetime(System.currentTimeMillis());
                        image.setPath(path);
                        images.add(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ImageView existingImageIV = BitmapUtility.createdScaledImageView(existingBitmap, this);
                    previewPhotoLayout.addView(existingImageIV);
                }
                if (selectedMediaUri.toString().contains("video")) {
                    Log.d(TAG, "video Uri returned: " + data.getData().toString());
                    Log.d(TAG, "video Uri.getRealPath(): " + BitmapUtility.getRealPathFromURI(data.getData(), this));
                    if (isStoragePermissionGranted()) {
                        final VideoView videoViewPreview = new VideoView(this);

                        FrameLayout frameLayout = new FrameLayout(this);

                        videoViewPreview.setMediaController(new MediaController(this));
                        videoViewPreview.setVideoURI(data.getData());
                        videoViewPreview.setLayoutParams(new FrameLayout.LayoutParams(400, 400));
//                        videoViewPreview.seekTo(1000);


//                        Create preview image for video
                        final ImageView videoThumbnail = new ImageView(this);

                        BitmapDrawable b = VideoUtility.getVideoThumbnailBitmap(data.getData(), videoViewPreview, this);
                        videoThumbnail.setImageDrawable(b);
//                        videoThumbnail.setLayoutParams();
//                        videoThumbnail.setLayoutParams(Lheight=400;
//                        videoThumbnail.getLayoutParams().width=400;

//                        VideoUtility.createVideoThumbnail(data.getData(), videoViewPreview,this);

                        videoViewPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                videoThumbnail.setVisibility(View.INVISIBLE);

                            }
                        });

                        frameLayout.addView(videoViewPreview);
                        frameLayout.addView(videoThumbnail);

                        previewPhotoLayout.addView(frameLayout);

                        videoPaths.add(BitmapUtility.getRealPathFromURI(data.getData(), this));
//
                    }
                }
            }
        }
    }

    private void uploadFiles(ArrayList<String> filePaths, String fileType) {
        Log.d(TAG, "beginUpload()");
        Log.d(TAG, "beginUpload(): filepaths length: " + filePaths.size());

        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));
            String filename = epoch + "_" + UserInformationModel.getInstance().getUsername() + "_" + i + fileType;
            Log.d(TAG, "beginUpload(): filename: " + filename);
            TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, filename, file);
            observer.setTransferListener(new UploadListener());
            if (fileType.equals(".mp4"))
                videoCount++;
            if (fileType.equals(".jpg"))
                pictureCount++;
        }
    }



    //Launch Camera to take new photo to upload
    private void launchCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI = getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    private void launchConfirmReportActivity() {
        Intent i = new Intent(this, ConfirmSubmitReportActivity.class);
        startActivity(i);
    }

    //check if user has button Disable button if user does not have camera
    private void launchConfirmSubmitReport() {
        Intent intent = new Intent(this, ConfirmSubmitReportActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    //  Handles user response to request for external storage
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORE_PHOTOS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG, "onRequestPermissionResult(): Permission Granted By User");

                } else {
                    Log.d(TAG, "onRequestPermissionResult(): Permission Denied By User");
                    takeNewPhotoButton.setEnabled(false);
//                    takeNewVideoButton.setEnabled(false);
                    addExistingPhotostoReportButton.setEnabled(false);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //Interface to bridge asyncTask and this class


    public Bitmap getVideoBitmap(Uri videoUri) {
        File videoFile = new File(BitmapUtility.getRealPathFromURI(videoUri, this));
        return ThumbnailUtils.createVideoThumbnail(videoFile.getAbsolutePath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
    }



    private class UploadListener implements TransferListener {
        //Updates UI when notified
        @Override
        public void onStateChanged(int id, TransferState state) {
            Log.d(TAG, "onStateChanged: " + id + state);
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, format("onProgressChanged:  %d, total %d, current: %d", id, bytesTotal, bytesCurrent));

        }

        @Override
        public void onError(int id, Exception ex) {
            Log.d(TAG, "Error during upload: " + ex);
        }
    }


    //    Submits Users report into DynamoDB Table
//
    class AsyncInsertTask2 extends AsyncTask<String[], Void, Void> implements ICallback {

        private static final String TAG = "AsyncInsertTask2";
        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();
        AttributeValue[] attributeValues = null;

        public AsyncInsertTask2(AttributeValue[] attributeVals) {
            attributeVals = new AttributeValue[attributeVals.length];
        }

        @Override
        protected Void doInBackground(String[]... params) {

            Log.i(TAG, "In AsyncTask2");
            Map<String, AttributeValue> attributeValueMap = new HashMap<>();

            for (int i = 0; i < keyArray.length; i++) {
                Log.d(TAG, "key: " + keyArray[i] + " val: " + attributeValArray[i]);
                attributeValueMap.put(keyArray[i], attributeValArray[i]);
            }
            try {
                PutItemRequest putItemRequest = new PutItemRequest("SkywarnWSDB_rev4", attributeValueMap);
                PutItemResult putItemResult = ddb.putItem(putItemRequest);
            } catch (DynamoDBMappingException dynamoDBMappingException) {
                Log.e(TAG, dynamoDBMappingException.toString());
            } catch (com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException dynamoDBMappingException) {
                Log.e(TAG, dynamoDBMappingException.toString());
            } catch (AmazonServiceException ex) {
                MainActivity.clientManager.wipeCredentialsonAuthError(ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            //delegate.processFinish();
        }

        @Override
        public void processFinish(ArrayList<SkywarnWSDBMapper> result) {

        }

        @Override
        public void allQueriesComplete() {
        }
    }
}




