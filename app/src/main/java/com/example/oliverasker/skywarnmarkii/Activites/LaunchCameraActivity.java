package com.example.oliverasker.skywarnmarkii.Activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TableRow;
import android.widget.VideoView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Created by oliverasker on 1/2/17.
 */

//add photos to sd card
//Command Line: adb push yourfile.xxx /sdcard/yourfile.xxx
public class LaunchCameraActivity extends AppCompatActivity {
    private final String TAG = "LaunchCameraActivity";
    private static final int DOWNLOAD_SELECTION_REQUEST_CODE = 1;
    private static int pictureCount;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_VIDEO_CAPTURE =2;
    static final int SELECT_PICTURE =3;
    private long epoch;
    File video_file;
    private static final File EXTERNAL_STORAGE_DIRECTORY = getDirectory("EXTERNAL_STORAGE", "/sdcard");
    Uri photoURI;
    Uri videoURI;
    ArrayList<Uri> bitMapPathList;

    private MediaController mediaControls;


    private Button submitButton;
    private Button addExistingPhotostoReportButton;
    private Button takeNewPhotoButton;
    private Button continueNoPhotosButton;
    private Button addPhotostoReportButton;
    private Button takeNewVideoButton;
    private VideoView mVideoView;

    int position =0;

    private LinearLayout previewPhotoLayout;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static int fragmentCount;
    private static String selectedImagePath;

    ArrayList<String> photoPaths;
    String mCurrentPhotoPath;

    //Manages S3 transfers
    TransferUtility transferUtility;

    PreviewPhotoCommunicator prePhotoCom;

    //Interface to bridge asyncTask and this class
    public interface PreviewPhotoCommunicator {
        void setImage(Bitmap b);
    }

    @Override
    protected void onCreate(Bundle savedInstace) {
        super.onCreate(savedInstace);
        setContentView(R.layout.activity_launch_camera_layout);

        photoPaths = new ArrayList<>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent i = getIntent();
        epoch = i.getLongExtra("epoch",-1);
        Log.d(TAG, "epoch: "+epoch);

       // epochString = String.format("%.0f", epoch);
        previewPhotoLayout = (LinearLayout)findViewById(R.id.preview_photo_holder);

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
                        launchCamera(view);
                    }
                }
        );

        //BROWSE EXISTING PHOTOS
        addExistingPhotostoReportButton = (Button) findViewById(R.id.upload_existing_button);
        addExistingPhotostoReportButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    // in onCreate or any event where your want the user to select a file
                    Intent intent = new Intent();
                  //  intent.setType("image/* video/*");
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                }
        });

        //SEND PHOTOS TO S3
        addPhotostoReportButton = (Button) findViewById(R.id.submit_user_photo_button);
        addPhotostoReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, " AddPhotosToReport.onClick()");
                for(String path:photoPaths){
                    Log.d(TAG, "addPhotosToREportButton: paths: " + path);
                    beginUpload(path);
                }
                launchConfirmtSubmitReport();
            }
        });
        continueNoPhotosButton = (Button)findViewById(R.id.continue_no_photos_button);
        continueNoPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchConfirmReportActivity();
            }
        });

        takeNewVideoButton = (Button)findViewById(R.id.take_new_video_button);
        takeNewVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "takeVideoButton onClick()");
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
                dispatchTakeVideoIntent();
            }
        });
        if (mediaControls == null) {
            mediaControls = new MediaController(LaunchCameraActivity.this);
        }

        mVideoView =(VideoView)findViewById(R.id.video_view);
        mVideoView.setMediaController(mediaControls);
        /////////////////// ///////////////////
        ////// AWS Example method
        //https://github.com/awslabs/aws-sdk-android-samples/blob/master/S3TransferUtilitySample/src/com/amazonaws/demo/s3transferutility/UploadActivity.java
        /////////////////// ///////////////////
        transferUtility = Utility.getTransferUtility(this);
    }

    public void launchCaptureNewVideo(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(takeVideoIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takeVideoIntent,REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        fragmentCount++;

        if(resultCode == RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {

            Log.d(TAG, "requestCode == REQUEST_TAKE_PHOTO: resultCode: " + resultCode);
            Log.i(TAG, "photo saved to: " + photoURI.getPath());

            try {
                String path = getPath(photoURI);
               // beginUpload(path);
                // beginUpload(mCurrentPhotoPath);
                Log.d(TAG, "OnACtivityReslt new Photo : mCurrentPhotoPath: " + mCurrentPhotoPath);
                Log.d(TAG, "OnACtivityReslt new Photo : photoURI.getPath: " + photoURI.getPath());

                photoPaths.add(mCurrentPhotoPath);

                Bitmap newBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                previewPhotoLayout.addView(createdScaledImageView(newBitmap));
                createdScaledImageView(newBitmap);
            } catch (URISyntaxException e) {
                Log.d(TAG, "URISyntaxException", e);
            } catch (IOException e) {
                Log.d(TAG, "IOException", e);
            }
        }

        //Existing Photo
        //if(resultCode ==Activity.RESULT_OK && requestCode == SELECT_PICTURE){
        if(requestCode==SELECT_PICTURE){
            Bitmap existingBitmap = null;
            if(data!=null){
                try{
                    existingBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData() );
                    //bitMapPathList.add(data.getData());
                    Uri uri = getImageUri(this, existingBitmap);
                    Log.d(TAG, "URI: " + uri);
                    String path = getRealPathFromURI(uri);
                    Log.d(TAG, "path: " + path);
                    //beginUpload(path);
                    photoPaths.add(path);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
           // Uri pickedImage = data.getData();

            createdScaledImageView(existingBitmap);
            ImageView existingImageIV = createdScaledImageView(existingBitmap);
            previewPhotoLayout.addView(existingImageIV);
        }

        //For Videos
        //if(resultCode ==Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE){
        if(requestCode == REQUEST_VIDEO_CAPTURE) {
//            Log.i(TAG, "REQUEST_VIDEO_CAPTURE");
//            //File f = new File(Environment.getExternalStorageDirectory().toString() + "/samplevideofolder/VideoFileName.mp4");
//            //Log.i(TAG,"f.getPath(): " +f.getPath());
//            Uri videoUri = data.getData();
//            Log.d(TAG, videoUri.getPath());
//            mVideoView.setVideoURI(videoUri);
//            //mVideoView.setVideoPath(f.getAbsolutePath());
//            for (File temp : f.listFiles()) {
//                Log.i(TAG, temp.getPath());
//                if (temp.getName().equals("VideoFileName.mp4")) {
//                    f = temp;
//                   // videoUri = f.getAbsoluteFile()
//                    Log.i(TAG, "VIDEO FILE Path: " + f.getAbsolutePath() );
//                    break;
//                }
//            }
            //mVideoView.setVideoURI(Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/samplevideofolder/VideoFileName.mp4")));                    m
//            //Log.i(TAG, "videoURI.getPath(): " + getRealPathFromURI(videoURI));
//            //Log.i(TAG, "data.getData(): " + data.getData().getPath());
//            //mVideoView.setVideoURI(data.getData());
//           // mVideoView.setVideoPath(getRealPathFromURI(videoURI));
//            String path = getCacheDir().toString();
//            Log.i(TAG,"path: " + path);
//            File directory = new File(path);
//            File[] files = directory.listFiles();
//            for(File f :files)
//                Log.i(TAG, "file name: " + f.getName() + " path: " + f.getPath());
//           // videoURI = Uri.fromFile(new File("));
//            mVideoView.setVideoURI(videoURI);
            if (requestCode == REQUEST_VIDEO_CAPTURE ){//&& resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
               // mVideoView.setVideoURI(videoUri);
                mVideoView.setVideoURI(data.getData());
                mVideoView.requestFocus();
                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        // close the progress bar and play the video
                        //progressDialog.dismiss();
                        //if we have a position on savedInstanceState, the video playback should start from here
                        mVideoView.seekTo(position);
                        if (position == 0) {
                            mVideoView.start();
                        } else {
                            //if we come from a resumed activity, video playback will be paused
                            //mVideoView.pause();
                        }
                    }
                });

            }
        }

    }

    // I COPIED AND PASTED THIS METOHD AND HEAVILY REFERENCED OTHER METHODS FROM
    // EXAMPLE CODE FOUND AT THIS LINK:
    // https://github.com/awslabs/aws-sdk-android-samples/blob/master/S3TransferUtilitySample/src/com/amazonaws/demo/s3transferutility/UploadActivity.java

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /*
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
*/

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void beginUpload(String filePath) {
        Log.d(TAG, "beginUpload()");
        Log.d(TAG, "beginUpload(): filepath: " + filePath);
        //File file = new File (mCurrentPhotoPath);
        File file = new File (filePath);


        Log.d(TAG, "fileName: " + file.getName() + "   filePath: " + filePath + " pictureCount: " + pictureCount);
        String filename = epoch+ "_"+UserInformationModel.getInstance().getUsername()+"_"+pictureCount+".jpg";
        Log.d(TAG, "beginUpload(): filename: " + filename);
        TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, filename, file);
        observer.setTransferListener(new UploadListener());
        pictureCount++;
    }

    private void launchConfirmReportActivity(){
        Intent i = new Intent(this, ConfirmSubmitReportActivity.class);
        startActivity(i);
    }

    static File getDirectory(String variableName, String defaultPath) {
        String path = System.getenv(variableName);
        return path == null ? new File(defaultPath) : new File(path);
    }

    private File createImageFile() throws IOException {
        if(isStoragePermissionGranted()) {
            Log.d(TAG, "storage permissions granted");
        }

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
       // String imageFileName = "JPEG_" + timeStamp +"_" ;
        String imageFileName = epoch+"_"+UserInformationModel.getInstance().getUsername()+".jpg";
        File storageDir = EXTERNAL_STORAGE_DIRECTORY;
        Log.d(TAG, "External Storage Directory " + EXTERNAL_STORAGE_DIRECTORY);
        // File storageDir = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Weather/");
        //File storageDir = Environment.getExternalStorageDirectory();

        File image = new File(storageDir, imageFileName);
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d(TAG, "createImageFile(): mCurrentPhotoPath = " + mCurrentPhotoPath);
        return image;
    }

    private void launchVideoCamera(View view){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(takeVideoIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    //Launch Camera to take new photo to upload
    private void launchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d(TAG,"launchCamera(): Error creating ImageFile " + ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.oliverasker.skywarnmarkii",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private class UploadListener implements TransferListener {
        //Updates UI when notified
        @Override
        public void onStateChanged(int id, TransferState state) {
            Log.d(TAG,"onStateChanged: " + id + state);

        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG,String.format("onProgressChanged:  %d, total %d, current: %d", id, bytesTotal,bytesCurrent));

        }

        @Override
        public void onError(int id, Exception ex) {
            Log.d(TAG,"Error during upload: " + ex);
        }
        //
    }
    @Override
    protected void onResume(){
        super.onResume();
    }

    //ToDo: submit reports when this button is clicked instead of at time of selection
    //check if user has button Disable button if user does not have camera
    private void launchConfirmtSubmitReport() {
        Intent intent = new Intent(this, ConfirmSubmitReportActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();

        /*
        // Clear transfer listeners to prevent memory leak, or
        // else this activity won't be garbage collected.
        if (observers != null && !observers.isEmpty()) {
            for (TransferObserver observer : observers) {
                observer.cleanTransferListener();
            }
        }
        */
    }

    @SuppressLint("NewApi")
    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];

            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[] {
                        split[1]
                };
            }
        }
        Log.d(TAG, "content:" + "content".toString());

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    // Check if URI authority is ExternalStorageProvider
    public static boolean isExternalStorageDocument(Uri uri){
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    // Check if URI authority is DownloadsProvider
    public static boolean isDownloadsDocument(Uri uri){
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    // Check if URI authority is DownloadsProvider
    public static boolean isMediaDocument(Uri uri){
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    private TableRow createTableRow(Bitmap bitmap){
        TableRow tableRow = new TableRow(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        //ImageView newIV = createdScaledImageView(bitmap);
        Button deleteButton = new Button(this);

        deleteButton.setText("Delete");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linearLayout.addView(createdScaledImageView(bitmap));

        tableRow.addView(linearLayout);

        return tableRow;
    }

    private ImageView createdScaledImageView(Bitmap bitmap){
        int width =0;
        int height =0;
        ImageView newImageView = new ImageView(this);

        try{
            width = bitmap.getWidth();
        }catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find image");
        }
        height = bitmap.getHeight();
        int bounding = dpToPx(250);
//        Log.i("Test", "original width = " + Integer.toString(width));
//        Log.i("Test", "original height = " + Integer.toString(height));
//        Log.i("Test", "bounding = " + Integer.toString(bounding));

        float xScale = ((float) bounding/width);
        float yScale = ((float) bounding/height);
        float scale = (xScale <= yScale) ? xScale:yScale;
//        Log.i("Test", "xScale = " + Float.toString(xScale));
//        Log.i("Test", "yScale = " + Float.toString(yScale));
//        Log.i("Test", "scale = " + Float.toString(scale));

        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);

        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        width = scaledBitmap.getWidth();
        height=scaledBitmap.getHeight();

        BitmapDrawable result = new BitmapDrawable(getApplicationContext().getResources(),scaledBitmap);
//        Log.i("Test", "scaled width = " + Integer.toString(width));
//        Log.i("Test", "scaled height = " + Integer.toString(height));
        newImageView.setImageDrawable(result);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 50));
        params.setMargins(10,10,10,10);
        params.width = width;
        params.height = height;
        newImageView.setLayoutParams(params);

        newImageView.setImageBitmap(bitmap);
        // previewPhotoLayout.addView(newImageView,params);
        return newImageView;
    }
    private int dpToPx(int dp){
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp*density);
    }


}