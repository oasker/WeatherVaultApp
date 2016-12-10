package com.example.oliverasker.skywarnmarkii;

import android.os.AsyncTask;

import com.amazonaws.AmazonServiceException;

import java.util.ArrayList;

public class MyAsyncTask extends AsyncTask<Void, Void, ArrayList<SkywarnWSDBMapper>>  {
    public ICallback delegate = null;
    ArrayList<SkywarnWSDBMapper> weatherList = null;

    public MyAsyncTask(ICallback delegate){
        this.delegate=delegate;
    }

    @Override
    protected ArrayList<SkywarnWSDBMapper> doInBackground(Void... voids) {
        weatherList = DynamoDBManager.getTableRow();
        if (weatherList != null) {
            try {
                for (SkywarnWSDBMapper w : weatherList) {
                }
            } catch (AmazonServiceException ex) {
                MainActivity.clientManager.wipeCredentialsonAuthError(ex);
            }
        }
        return weatherList;
    }
    @Override
    protected void onPostExecute(ArrayList<SkywarnWSDBMapper> result){
        delegate.processFinish(result);
    }
}
