package com.example.oliverasker.skywarnmarkii;

import android.os.AsyncTask;

import com.amazonaws.AmazonServiceException;

import java.util.ArrayList;

/**
 * Created by student on 10/27/16.
 * Async class to insert records into database
 */

public class AsyncInsertTask extends AsyncTask<Void, Void, Void> implements ICallback{
    public ICallback delegate = null;
    SkywarnWSDBMapper report;

    public AsyncInsertTask(SkywarnWSDBMapper Report){
    //public AsyncInsertTask(){
       // this.delegate=delegate;
        report = Report;
        System.out.println(report.getReportID());
        //report = new SkywarnWSDBMapper("Backflip_11232322.45243");
        //report.setReportID("Frogger_1224241241.45654");
        //report.setWeatherEvent("Rain");
    }

    @Override
    protected Void doInBackground(Void... voids) {
            try {
                DynamoDBManager.insertRecord(report);

            } catch (AmazonServiceException ex) {
                MainActivity.clientManager.wipeCredentialsonAuthError(ex);
            }
       return null;
    }
    @Override
    protected void onPostExecute(Void v){
       //delegate.processFinish();
    }
    @Override
    public void processFinish(ArrayList<SkywarnWSDBMapper> result) {

    }
}
