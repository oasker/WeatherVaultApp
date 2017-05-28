package com.example.oliverasker.skywarnmarkii.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.oliverasker.skywarnmarkii.Callbacks.GetReportsRatedByUserTaskCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by oliverasker on 3/29/17.
 */
public class GetReportsRatedByUserTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "GetReptsRatdByUserTsk";
    private static int reportRating;
    //Variables for S3 calls
    private Context mContext;
    private String reportName;
    private String filePath;
    private String urlString;
    private String username;
    private String vote;
    private String allText;
    private GetReportsRatedByUserTaskCallback callback;
    private boolean userAlreadyRated;

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        callback.setRatedReportString(allText);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... params) {
        urlString = "https://s3.amazonaws.com/skywarntestbucket/" + username + ".txt";
        URL u = null;
        try {
            u = new URL(urlString);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            Log.d(TAG, "url.openConnection() Response Code: " + c.getResponseCode());
            c.setRequestMethod("GET");
            c.connect();

            InputStream inputStream = c.getInputStream();
            final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            File deletefile = new File(filePath + username + ".txt");
            if (deletefile.exists()) {
                deletefile.delete();
                Log.d(TAG, "path: " + filePath + username + ".txt " + " exists? " + deletefile.exists());
            }
            Log.d(TAG, "doInBackground(): filePath + username+.txt: " + filePath + username + ".txt");
            try (OutputStream outputStream = new FileOutputStream(filePath + username + "1.txt")) {
                byte[] buffer = new byte[1024];
                // Read from Buffer.
                inputStream.read(buffer);
                // Write Into Buffer
                byteOutputStream.write(buffer);
                byteOutputStream.writeTo(outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                byteOutputStream.close();
                inputStream.close();
            }

            Log.i(TAG, byteOutputStream.toString());
            allText = byteOutputStream.toString().trim();
            Log.d(TAG, "username.txt : " + allText);

            try {
                byteOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //   Getters/Setters
    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCallback(GetReportsRatedByUserTaskCallback callback) {
        this.callback = callback;
    }
}