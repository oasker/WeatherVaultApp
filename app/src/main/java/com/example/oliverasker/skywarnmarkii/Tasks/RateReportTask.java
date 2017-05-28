package com.example.oliverasker.skywarnmarkii.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.oliverasker.skywarnmarkii.Callbacks.BooleanCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliverasker on 3/29/17.
 */
public class RateReportTask extends AsyncTask<Void,Void,Void> {
    private static final String TAG = "RateReportTask";
    private static int reportRating;
    //Variables for S3 calls
    private Context mContext;
    private String reportName;
    private String filePath;
    private String url;
    private String username;
    private String vote;
    private String allText;
    private BooleanCallback callback;
    private boolean userAlreadyRated;

    private String externalStorageCache;

    //Variables for DynamoDB calls
    private AmazonDynamoDBClient ddb;
    private String reportPrimaryKey;
    private String rangeKey;

    public static void updateItem(AmazonDynamoDBClient client, String tableName, String primaryKey, String rangeKey, String val) {
        Log.d(TAG, "updateItem()");
        reportRating = Integer.parseInt(val);
        Log.d(TAG, "primaryKey: " + primaryKey + " rangeKey: " + rangeKey + " val: " + val);

//      Update report in dynamodb
        java.util.Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("DateSubmittedString", new AttributeValue().withS(primaryKey));
        key.put("DateSubmittedEpoch", new AttributeValue().withN(rangeKey));

        Map<String, AttributeValueUpdate> attributeUpdates = new HashMap<String, AttributeValueUpdate>();

        AttributeValueUpdate update = new AttributeValueUpdate()
                .withAction(AttributeAction.PUT)
                .withValue(new AttributeValue().withN(val));

        attributeUpdates.put("NetVote", update);

        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withAttributeUpdates(attributeUpdates);
        client.updateItem(updateItemRequest);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        callback.UserHasRatedReport(userAlreadyRated, reportRating);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... params) {
        url ="https://s3.amazonaws.com/skywarntestbucket/"+ username+".txt";
        URL u = null;
//        try {
//            u = new URL(url);
//            HttpURLConnection c = (HttpURLConnection) u.openConnection();
//            Log.d(TAG, "url.openConnection() Response Code: " + c.getResponseCode());
//            c.setRequestMethod("GET");
//            c.connect();
//            InputStream inputStream = c.getInputStream();
//            final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            File deletefile = new File(filePath + username + ".txt");
            if (deletefile.exists()) {
//                deletefile.delete();
                Log.d(TAG, "path: " + filePath + username + ".txt " + " exists? " + deletefile.exists());
            }
//            Log.d(TAG, "doInBackground(): filePath + username+.txt: " + filePath + username + ".txt");
//            try (OutputStream outputStream = new FileOutputStream(filePath + username + "1.txt")) {
//                byte[] buffer = new byte[1024];
//                inputStream.read(buffer);               // Read from Buffer.
//                byteOutputStream.write(buffer);         // Write Into Buffer
//                byteOutputStream.writeTo(outputStream);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                byteOutputStream.close();
//                inputStream.close();
//            }

////            if text contains report that is being rated exit and update callback with new boolean result
//            Log.i(TAG, byteOutputStream.toString());
//            allText = byteOutputStream.toString().trim();
//            Log.d(TAG, "allTEXT: " + allText);
//
//            if (byteOutputStream.toString().contains(reportName)) {
//                Log.d(TAG, "report is in list");
//                userAlreadyRated = true;
//                callback.UserHasRatedReport(true,reportRating);
//            } else {
//                userAlreadyRated=false;
//                Log.d(TAG, "***************AFTER WRITING TO TEXT FILE**********************");
//                readTextFromFile(filePath + username + ".txt");
//
//                Log.d(TAG, "filePath+  username+txt " + filePath + username + ".txt");
//
//                uploadRatingTextFile(filePath + username + ".txt", username + ".txt");
//            }
//            try {
//                byteOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch(MalformedURLException e){
//            e.printStackTrace();
//        } catch(ProtocolException e){
//            e.printStackTrace();
//        } catch(IOException e){
//            e.printStackTrace();
//        }

        Log.d(TAG, "***************AFTER WRITING TO TEXT FILE**********************");
        readTextFromFile(username + ".txt");
//        createFile(username+".txt");

        Log.d(TAG, "filePath+  username+txt " + filePath + username + ".txt");

        uploadRatingTextFile(filePath + username + ".txt", username + ".txt");
//      Update NetVote value in DynamoDB
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                mContext,
                Constants.IDENTITY_POOL_ID,
                Regions.US_EAST_1);

        ddb = new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));

        Log.d(TAG, "reportRating: before adding vote " + reportRating);

        updateItem(ddb, Constants.REPORTS_TABLE_NAME, reportPrimaryKey, rangeKey, String.valueOf(reportRating));

    return null;
    }

    private void readTextFromFile(String filename){
        //Get the text file
        File file = new File(filename);
        if (file.exists())
            file.delete();
        if (!file.exists()) {
            File dir = new File(externalStorageCache, "previouslyRatedReports");
            file = new File(dir, username + ".txt");
            file.mkdir();
        }


        Log.d(TAG, "file exist? : " + file.exists());
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            text.append(UserInformationModel.getInstance().getRatedReportsTextFileContents());
            br.close();
            Log.d(TAG, "readTextFromFile(): text: " + text);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "readTextFromFile() : " + UserInformationModel.getInstance().getRatedReportsTextFileContents());
    }

    private void writeToTextFile(String DIRECTORY_PATH, String fileName, String textToAdd){
        Log.d(TAG, "writeToTextFile(): fileName: " + DIRECTORY_PATH+fileName);

//        File myFile = new File(DIRECTORY_PATH, fileName);
//        try {
//            PrintWriter out = new PrintWriter(new FileWriter(myFile,true));
////            out.append(UserInformationModel.getInstance().getRatedReportsTextFileContents());
////            out.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void createFile(String fileName) {
        try {
            FileOutputStream fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(UserInformationModel.getInstance().getRatedReportsTextFileContents().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadRatingTextFile(String uploadFileName, String keyName){

        Log.d(TAG, "uploadRatingTextFile() uploadFileName: " + uploadFileName);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext,
                Constants.IDENTITY_POOL_ID, // Identity Pool ID
                Regions.US_EAST_1           // Region
        );

        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);

        TransferUtility transferUtility = new TransferUtility(s3Client,mContext);

        File file = new File(username + ".txt");
        TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME,keyName,file);

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.d(TAG, "onStateChanged: state " + state);
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                Log.d(TAG, "onProgressChanged(): current: "+ bytesCurrent + " total: " + bytesTotal);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d(TAG, "onError(): error: " + ex);
            }
        });
    }


    //////////////////////////////////////////
    ///////     Getters/Setters         /////
    /////////////////////////////////////////

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setUrl(String url) {
        this.url = "https://s3.amazonaws.com/skywarntestbucket/"+url+".txt";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public void setReportRating(int reportRating) {
        RateReportTask.reportRating = reportRating;
    }

    public void setReportPrimaryKey(String reportPrimaryKey) {
        this.reportPrimaryKey = reportPrimaryKey;
    }

    public void setRangeKey(String rangeKey) {
        this.rangeKey = rangeKey;
    }

    public void setCallback(BooleanCallback callback) {
        this.callback = callback;
    }

    public void setExternalStorageCache(String externalStorageCache) {
        this.externalStorageCache = externalStorageCache;
    }
}