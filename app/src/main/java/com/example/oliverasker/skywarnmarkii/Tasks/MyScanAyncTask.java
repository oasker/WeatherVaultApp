package com.example.oliverasker.skywarnmarkii.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.example.oliverasker.skywarnmarkii.Activities.MainActivity;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Managers.DynamoDBManager;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by oliverasker on 11/24/16.
 */

public class MyScanAyncTask extends AsyncTask<HashMap<String,String>, Void, ArrayList<SkywarnWSDBMapper>> {

    private static final String TAG = "MyScanAsyncTask";
    public ICallback delegate = null;
    ArrayList<SkywarnWSDBMapper> weatherList = null;
    HashMap<String,String> scanA = new HashMap<String,String>();

    public MyScanAyncTask(ICallback delegate) {
        this.delegate = delegate;
    }

    public void setScanA(HashMap<String,String> tempMap){
        scanA = tempMap;
    }

    @Override
    protected ArrayList<SkywarnWSDBMapper> doInBackground(HashMap<String,String>... scanAttributes) {
        //scanA.put("City", "Egypt");
        scanA = scanAttributes[0];
        weatherList = null;
        weatherList = DynamoDBManager.scanDB(scanA);
        if (weatherList != null) {
            try {
                for (SkywarnWSDBMapper w : weatherList) {
                    Log.d(TAG,"City: "+w.getEventCity());
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
        delegate= null;
    }
}
