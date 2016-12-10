package com.example.oliverasker.skywarnmarkii;

import android.os.AsyncTask;
import com.amazonaws.AmazonServiceException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by oliverasker on 11/24/16.
 */

public class MyScanAyncTask extends AsyncTask<HashMap<String,String>, Void, ArrayList<SkywarnWSDBMapper>> {

    public ICallback delegate = null;
    ArrayList<SkywarnWSDBMapper> weatherList = null;
    String scanString;

    public void MyScanAyncTask(ICallback delegate){
        this.delegate= delegate;
    }

    public void setScanString(String ScanString){
        scanString = ScanString;
    }

    @Override
    protected ArrayList<SkywarnWSDBMapper> doInBackground(HashMap<String,String>... scanAttributes) {
        HashMap<String,String> scanA = new HashMap<String,String>();
        scanA.put("ThunderSnow", "No");
        weatherList = DynamoDBManager.scanDB(scanA);
        if (weatherList != null) {
            try {
                for (SkywarnWSDBMapper w : weatherList) {
                    System.out.println("City: "+w.getEventCity());
                }
            } catch (AmazonServiceException ex) {
                MainActivity.clientManager.wipeCredentialsonAuthError(ex);
            }
        }
        return weatherList;
    }

    @Override
    public void onPostExecute(ArrayList<SkywarnWSDBMapper> result){
        delegate.processFinish(result);
    }
}
