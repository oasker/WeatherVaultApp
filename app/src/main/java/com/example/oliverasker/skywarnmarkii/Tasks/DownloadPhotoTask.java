package com.example.oliverasker.skywarnmarkii.Tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.oliverasker.skywarnmarkii.Activites.ViewReportActivity;
import com.example.oliverasker.skywarnmarkii.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by oliverasker on 2/1/17.
 * http://docs.aws.amazon.com/AmazonS3/latest/dev/RetrievingObjectUsingJava.html
 */

public class DownloadPhotoTask extends AsyncTask<Void,Void,Bitmap> {
    Bitmap bitmap;
    String bitmapPath;
    Context mContext;
    final int maxMemory = (int)(Runtime.getRuntime().maxMemory())/24;

    ViewReportActivity.bitmapCallback callback;
    final int cacheSize =maxMemory/8;
    private LruCache<String, Bitmap> mMemoryCache;

    String filePath;
    String[] filePaths;
    String fileName;

    //get S3 images if they exist

    private static void displayTextInputStream(InputStream input) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while(true){
            String line = reader.readLine();
            if(line == null)
                break;
            Log.d(TAG, line);
        }
    }
    //Accepts context
    public DownloadPhotoTask(Context context, String filename ){
        fileName = filename;
        String externalStorage= Environment.getExternalStorageDirectory().toString()+"/";
        filePath =externalStorage+filename;
        mContext = context;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext, // Context
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
        try {
            //File file =new File(Environment.getExternalStorageDirectory().toString()+ "/Download/bingo.png");
            File file =new File(filePath);
            TransferUtility transferUtility = new TransferUtility(s3Client, mContext);
            TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, "bingo.png", file);

            bitmapPath=file.getPath();
            Log.d(TAG, "file.getPath(): " + file.getPath() );
            File f = new File(Environment.getExternalStorageDirectory().toString());

            for(File ff : f.listFiles()) {
                Log.d(TAG, "List Files: ff; "+ ff);
            }

//            bitmap = BitmapFactory.decodeFile(file.getPath());
//            Log.d(TAG ,"doInBackground(): bitmapExists?: " + (bitmap != null));
//            Log.d(TAG,"bitmap: " + bitmap.getByteCount() + " width: "  + bitmap.getWidth());



            transferObserver.setTransferListener(new TransferListener(){
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d(TAG, "onStateChanged: + state: "+ state.toString());
                }
                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    int percentage = (int) (bytesCurrent/(bytesTotal+1) * 100);
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
        return null;
    }



    @Override
    protected void onPostExecute(Bitmap bitmap){
        Log.d(TAG, "onPostExecute");
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/bingo.png");
        bitmap = BitmapFactory.decodeFile(file.getPath());
        Log.d(TAG ,"onPostExecute(): bitmapExists?: " + (bitmap != null));
       if( callback!=null) {
           callback.onFinishedString(bitmapPath);
           callback.onFinishedBitmap(bitmap);
       }
    }
//

    //////// For storing in cache eventually \\\\\\\\\\\\\\\\\\
//    mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
//        @Override
//        protected int sizeOf(String key, Bitmap bitmap){
//            return bitmap.getByteCount()/1024;
//        }
//    };

    public void addBitMapToMemoryCache(String key, Bitmap b){
        if(getBitmapFromMemCache(key)==null)
            mMemoryCache.put(key,bitmap);
    }

    public Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);
    }

    ////////////////////////////////////////////////////////////////////////
    //                          callback METHODS                          //
    ////////////////////////////////////////////////////////////////////////

    public void setcallback(ViewReportActivity.bitmapCallback callback) {
        this.callback = callback;
    }

    static File getDirectory(String variableName, String defaultPath) {
        String path = System.getenv(variableName);
        return path == null ? new File(defaultPath) : new File(path);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
  }
