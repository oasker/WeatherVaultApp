package com.example.oliverasker.skywarnmarkii.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Utility.AsyncTaskHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliverasker on 2/17/17.
 */

public class GetAllRecordsForDayTask extends AsyncTask<Void,Void,Void> {
    private static final String TAG = "GetAllRecordsForDayTask";
    public ICallback delegate = null;
    ArrayList<SkywarnWSDBMapper> reportList = new ArrayList<>();
    Context mContext;
    private String[] reportAttributes;
    private AmazonDynamoDBClient ddb;
    private String date;

    public void setDelegate(ICallback delegate){
        this.delegate=delegate;
    }

    public void setReportAttributesArray(String[] reportAttr){
        reportAttributes=reportAttr;
        for(String s: reportAttributes)
            Log.d(TAG, "setReportAttributesArray: " +s);
    }

    public void setDate(String Date){
        date = Date;
    }
    public void setContext(Context c){
        mContext=c;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(TAG, "GetAllRecordsForDayTask called");
        //AmazonDynamoDBClient dynamoDB = MainActivity.clientManager.ddb(); //Use MainActivity client manager
       // DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                mContext,
                Constants.IDENTITY_POOL_ID,
                Regions.US_EAST_1);

        ddb= new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));

        //FINALLY WORKS
        //https://aws.amazon.com/blogs/mobile/amazon-dynamodb-on-mobile-part-4-local-secondary-indexes/
        Map keyCondition = new HashMap();

        //Create condition for hashkey
        Condition hashKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(date));

        keyCondition.put("DateSubmittedString", hashKeyCondition);

        // Create Rangekeycondition
        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.GT.toString())
                .withAttributeValueList(new AttributeValue().withN("0"));
        keyCondition.put("DateSubmittedEpoch", rangeKeyCondition);

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(Constants.REPORTS_TABLE_NAME)
                .withKeyConditions(keyCondition);
        queryRequest.setScanIndexForward(false);

        QueryResult queryResult = ddb.query(queryRequest);
        AsyncTaskHelper taskHelper = new AsyncTaskHelper();
        reportList = taskHelper.addReportsToMap(queryResult);

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        delegate.processFinish(reportList);
        delegate = null;
    }
}
