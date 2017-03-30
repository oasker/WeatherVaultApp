package com.example.oliverasker.skywarnmarkii.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.oliverasker.skywarnmarkii.Activites.ViewReportActivity;
import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.StringCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Models.SubmittedPhotoModel;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.DownloadPhotoTask;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserSubmittedPhotoNamesTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

/**
/**
* Created by oliverasker on 2/19/17.
*/

public class UserHomeUserSubmittedPhotosFragment extends Fragment implements BitmapCallback, StringCallback,ViewReportActivity.bitmapCallback {
    private static final String TAG = "PersnlPhotosFrag";
    private String username = UserInformationModel.getInstance().getUsername();
    private ArrayList<SubmittedPhotoModel> photoModelList = new ArrayList<>();
    private ViewGroup Containter;
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    private Context mContext;
    private String filePath;
    private ViewGroup container;
    private LinearLayout photoLinearLayout;
    private int imageViewWidth = 650;
    private int imageViewHeight = 650;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup Container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_user_home_submitted_photos, Container, false);
        Log.d(TAG, "onCreateView()");
        container = Container;
        photoLinearLayout = (LinearLayout) v.findViewById(R.id.photoLinearLayout);
        photoModelList = new ArrayList<>();
        mContext = getContext();
        //IV = (ImageView)v.findViewById(R.id.imageView2);
//        GetUserSubmittedPhotosTask2 getPhotos = new GetUserSubmittedPhotosTask2();
//        getPhotos.setmContext(getContext());
//        getPhotos.setUsername(username);
//        getPhotos.setBitmapCallback(this);
        //getPhotos.execute();
        // downloadPhoto();

        // String filename = String.format("%.0f" ,map.getDateSubmittedEpoch()) + "_" + map.getUsername() + "_" +i+".jpg";
        //Log.d(TAG, "launchdownloadPhotoTask(): with filename: " +filename );
//        photoTask.setmContext(this);
//        photoTask.setCallback(callback);
//        photoTask.setFilename(filename);
//        photoTask.setfilePath(getCacheDir() + "/"+filename);
//        photoTask.setNumPhotos(map.getNumberOfImages());
//        photoTask.setLinearLayout(horizontalLinearLayout);
//        photoTask.execute();
//        photoTask.get();

//        GetUserSubmittedPhotosTask getUserPhotosTask = new GetUserSubmittedPhotosTask();
//        getUserPhotosTask.setmContext(getContext());
//        getUserPhotosTask.setUsername(UserInformationModel.getInstance().getUsername());
//        //getUserPhotosTask.setFilePath(getActivity().getCacheDir() + "/"+UserInformationModel.getInstance().getUsername());
//        Log.d(TAG, "cacheDir: " + getActivity().getExternalCacheDir());
//        getUserPhotosTask.setFilePath(getActivity().getExternalCacheDir()+ "/"+"bin");
//        getUserPhotosTask.execute();

        //String filename = String.format("%.0f" ,map.getDateSubmittedEpoch()) + "_" + map.getUsername() + "_" +i+".jpg";
        //ToDo:change filename to current user, not placeholder
//        String filename = "oasker";
//        Log.d(TAG, "launchdownloadPhotoTask(): with filename: " +filename );
//        photoTask.setmContext(getContext());
//        photoTask.setCallback(this);
//        photoTask.setFilename(filename);
//        photoTask.setfilePath(getActivity().getCacheDir() + "/"+filename);
        //photoTask.setNumPhotos(map.getNumberOfImages());
        //photoTask.setLinearLayout(horizontalLinearLayout);
        // photoTask.execute();

        GetUserSubmittedPhotosTask2 task = new GetUserSubmittedPhotosTask2();
        task.setmContext(getContext());
        task.setBitmapCallback(this);
        task.setPhotoLayout(photoLinearLayout);

        File cache = getContext().getCacheDir();
        File appDir = new File(cache + "/");
        task.setFile(appDir);
        task.setFilePath(cache + "/");
        //task.execute();

        GetUserSubmittedPhotoNamesTask task2 = new GetUserSubmittedPhotoNamesTask();
        task2.setmContext(getContext());
        task2.setCallback(this);
        task2.execute();
//        photoListView = (ListView)v.findViewById(R.id.user_submitted_photos_listview);
//        UserSubmittedPhotosAdapter userPhotoAdapter = new UserSubmittedPhotosAdapter(getContext(), photoModelList);
//
//        photoListView.setAdapter(userPhotoAdapter);
//        v = photoListView.getAdapter().getView(0,null,container);
        return v;
    }

    @Override
    public void processFinish(ArrayList<Bitmap> result) {
        Log.d(TAG, "bitMapcallback processFinished: size: " + result.size());
        for (Bitmap b : result) {
            ImageView iV = new ImageView(getContext());
        }
    }

    @Override
    public void processFinish(Bitmap result) {
        Log.d(TAG, "processFinished(Bitmap)");
        //Log.d(TAG, "processFinished() bitmapSize: "+ result.getByteCount());
        //File f = new File("/data/user/0/com.example.oliverasker.skywarnmarkii/cache/1489603471441_oasker_1.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile("/data/user/0/com.example.oliverasker.skywarnmarkii/cache/1489603471441_oasker_1.jpg");
        ImageView iV = new ImageView(getContext());
        ImageView iV2 = new ImageView(getContext());
        if (result != null) {
            iV.setImageBitmap(result);
            photoLinearLayout.addView(iV);
            iV2.setImageBitmap(bitmap);
            // photoLinearLayout.addView(iV2);
        }
    }

    @Override
    public void processFinish(Bitmap result, String path) {
        Log.d(TAG, "processFinished(Bitmap)");
        Log.d(TAG, "processFinsihed(Bitmap) path: " + path);
        //Log.d(TAG, "processFinished() bitmapSize: "+ result.getByteCount());
        Bitmap bitmap = BitmapFactory.decodeFile("/data/user/0/com.example.oliverasker.skywarnmarkii/cache/1489603471441_oasker_1.jpg");

//        ImageView iV2 = new ImageView(getContext());
        ImageView iV = new ImageView(mContext);
        if (result != null) {
            iV.setImageBitmap(result);
            photoLinearLayout.addView(iV);
            // iV2.setImageBitmap(bitmap);
            // photoLinearLayout.addView(iV2);
        }
    }

    @Override
    public void processFinish(Bitmap result, ArrayList<String> path) {
        Log.i(TAG, "processFinished(bitmap, arrayList<string>: size() " + path.size());
        for (String s : path) {
            Log.d(TAG, "PATHHH: " + s);
        }
    }

    @Override
    public void processFinish(Uri result) {
        Log.d(TAG, "processFinished(Uri)");
    }


    public void setString(String s) {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    // STRING CALLBACKs
    @Override
    public void onProcessComplete(String s) {
    }

    @Override
    public void onProcessComplete(ArrayList<String> s) {
        Log.d(TAG, "onProcessComplete(ArrayList<String>");
        for (String str : s) {
            Log.d(TAG, str);
            DownloadPhotoTask photoTask = new DownloadPhotoTask();
            photoTask.setmContext(getContext());
            photoTask.setcallback(this);
            //photoTask.execute();
            downloadPhoto(s);
        }
    }

    @Override
    public void onFinishedString(String s) {
        File f = new File(s);
        Bitmap b = BitmapFactory.decodeFile(s);
        ImageView i = new ImageView(getContext());
        i.setImageBitmap(b);

        photoLinearLayout.addView(i);
    }

    public void downloadPhoto(ArrayList<String> paths) {
        Log.d(TAG, "downloadPhoto()");
        // String filename = "oasker.jpg";
        for(String s: paths) {
//            String filename = UserInformationModel.getInstance().getUsername() + ".jpg";
            String filename =s;
            String externalStorage = mContext.getCacheDir().toString() + "/";
            final String filePath = externalStorage + filename;
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getContext(),
                    Constants.IDENTITY_POOL_ID, // Identity Pool ID
                    Regions.US_EAST_1           // Region
            );

            AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
            try {
                File file = new File(filePath);
                TransferUtility transferUtility = new TransferUtility(s3Client, getContext());
                TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, filename, file);

                Log.d(TAG, "file.getPath(): " + file.getPath());

                transferObserver.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        Log.d(TAG, "onStateChanged: + state: " + state.toString());
                        if (state == TransferState.COMPLETED) {
                            Bitmap b = BitmapFactory.decodeFile(filePath);
                            //     horizontalLinearLayout.addView(createImageView(b));
                            // profileImage.setImageBitmap(b);


                            //ToDo: Add text view to user submitted photos giving date photo was taken
                            //TextView dateTV = new TextView(mContext);
                            //s.indexOf("_");
                            //dateTV.setText(Utility.epochToDateTimeString());

                            ImageView v = new ImageView(getContext());
                            v =resizeImageView(imageViewWidth, imageViewHeight, v);
                            v.setImageBitmap(b);
                            photoLinearLayout.addView(v);
                        }
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        int percentage = (int) ((bytesCurrent + 1) / (bytesTotal + 1) * 100);
                        Log.d(TAG, "onStateChanged(); bytesTotal: " + bytesTotal + " bytesCurrent: " + bytesCurrent);
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
        }

    }

    public ImageView resizeImageView(int width, int height, ImageView i) {
//        final ImageView picture1 = (ImageView)findViewById(R.id.imageView1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        i.setLayoutParams(layoutParams);
        return i;
    }


    class GetUserSubmittedPhotosTask2 extends AsyncTask<Void, Void, ArrayList<Bitmap>> {
        private static final String TAG = "GetUserPhotosTask";
        Context mContext;
        ListObjectsV2Request req = new ListObjectsV2Request();
        ListObjectsV2Result result;
        String username;
        ArrayList<String> matchingFilesList;
        BitmapCallback bitmapCallback;
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
        ArrayList<String> fileArrayList;
        String filePath;
        File file;
        String tempFilePath;
        LinearLayout photoLayout;
        ArrayList<String> bitMapPaths;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(Void... ag) {
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    mContext, // Context
                    Constants.IDENTITY_POOL_ID, // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            matchingFilesList = new ArrayList<>();
            bitMapPaths = new ArrayList<>();

            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setSignerOverride("AWSS3V4SignerType");

            AmazonS3 s3Client = new AmazonS3Client(credentialsProvider, clientConfiguration);
            try {
                ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(Constants.BUCKET_NAME);
                ListObjectsV2Result result;
                do {
                    username = "oasker";
                    result = s3Client.listObjectsV2(req);
                    for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                        Log.i(TAG, "Object Summaries: key: " + objectSummary.getKey()
                                + " size: " + objectSummary.getSize());
                        if (objectSummary.getKey().toString().contains(username)) {
                            Log.i(TAG, "User Submitted Photos Task: " + objectSummary.getKey());
                            matchingFilesList.add(objectSummary.getKey().toString());
                        }
                    }
                    req.setContinuationToken(result.getNextContinuationToken());
                } while (result.isTruncated() == true);

                //Download photos
                try {
                    for (String photoName : matchingFilesList) {
                        TransferUtility transferUtility = new TransferUtility(s3Client, mContext);
                        tempFilePath = filePath + photoName;
                        Log.d(TAG, "matchingFileList[0]: " + photoName + " tempFilePath: " + tempFilePath);

                        file = new File(tempFilePath);

                        Log.d(TAG, "NEW FILEPATH: " + file.getAbsolutePath());
//                    TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, matchingFilesList.get(3), file);
                        TransferObserver transferObserver = transferUtility.download(Constants.BUCKET_NAME, photoName, file);

                        try {
                            transferObserver.setTransferListener(new TransferListener() {
                                @Override
                                public void onStateChanged(int id, TransferState state) {
                                    Log.d(TAG, "onStateChanged:  state: " + state.toString());
                                    if (state == TransferState.COMPLETED) {
                                        Log.i(TAG, "TransferStateCompleted:)");
                                        Bitmap b = BitmapFactory.decodeFile(tempFilePath);
                                        bitMapPaths.add(tempFilePath);
                                        Log.d(TAG, "onStateChanged: tempFilePath: " + tempFilePath);
                                        bitmapArrayList.add(b);
                                        bitmapCallback.processFinish(b, tempFilePath);
                                        bitmapCallback.processFinish(b, bitMapPaths);
                                        //  return;
                                    }
                                }

                                @Override
                                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                                    Log.d(TAG, "onProgressChanged: Current: " + bytesCurrent + " Total: " + bytesTotal);
                                }

                                @Override
                                public void onError(int id, Exception ex) {
                                    Log.e(TAG, "onError: " + ex);
                                }
                            });
                        } catch (AmazonServiceException ase) {
                            System.out.println("Caught an AmazonServiceException, " +
                                    "which means your request made it " +
                                    "to Amazon S3, but was rejected with an error response " +
                                    "for some reason.");
                            System.out.println("Error Message:    " + ase.getMessage());
                            System.out.println("HTTP Status Code: " + ase.getStatusCode());
                            System.out.println("AWS Error Code:   " + ase.getErrorCode());
                            System.out.println("Error Type:       " + ase.getErrorType());
                            System.out.println("Request ID:       " + ase.getRequestId());
                        } catch (AmazonClientException ase) {
                            System.out.println("Caught an AmazonClientException, " +
                                    "which means the client encountered " +
                                    "an internal error while trying to communicate" +
                                    " with S3, " +
                                    "such as not being able to access the network.");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmapArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmapList) {
            File file = new File(tempFilePath);
            Bitmap b = BitmapFactory.decodeFile(file.getPath());

            for (String s : bitMapPaths) {
                Log.d(TAG, "bithmapARrayList: " + s);
            }
            for (String photoName : matchingFilesList) {
                Log.d(TAG, "matcingFileList: " + photoName);
            }

//        for(String s: bitMapPaths) {
//            File file2 = new File(s);
//            Bitmap b2 = BitmapFactory.decodeFile(file2.getPath());
//            ImageView bitMapIV = new ImageView(mContext);
//            bitMapIV.setImageBitmap(b2);
//            photoLayout.addView(bitMapIV);
//        }
            bitmapCallback.processFinish(bitmapList);
            bitmapCallback.processFinish(bitmapArrayList);
        }

        public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }

        //////////////////////////////////////
        //          Setters and Getters     //
        //////////////////////////////////////
        public Context getmContext() {
            return mContext;
        }

        public void setmContext(Context mContext) {
            this.mContext = mContext;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setBitmapCallback(BitmapCallback bitmapCallback) {
            this.bitmapCallback = bitmapCallback;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public void setPhotoLayout(LinearLayout photoLayout) {
            this.photoLayout = photoLayout;
        }


    }
}


