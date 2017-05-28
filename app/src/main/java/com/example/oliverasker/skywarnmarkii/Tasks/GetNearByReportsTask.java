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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliverasker on 2/17/17.
 */

public class GetNearByReportsTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "GetTopRatedReportsTask";
    public ICallback delegate = null;
    ArrayList<SkywarnWSDBMapper> reportList;
    SkywarnWSDBMapper testReport;
    AmazonDynamoDBClient ddb;
    Context mContext;
    ArrayList<SkywarnWSDBMapper> weatherList;
    private String[] reportAttributes;
    private String date;
    private String user;
    private String countyToQuery;

    private AsyncTaskHelper taskHelper;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(TAG, "GetTopRatedReportsTask called");
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                mContext,
                Constants.IDENTITY_POOL_ID,
                Regions.US_EAST_1);

        ddb = new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(Regions.US_EAST_1));

        reportList = new ArrayList<>();

        //FINALLY WORKS
        //https://aws.amazon.com/blogs/mobile/amazon-dynamodb-on-mobile-part-4-local-secondary-indexes/
        Map keyCondition = new HashMap();

        Condition hashKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS("Plymouth County"));
        keyCondition.put("County", hashKeyCondition);


//         Create Rangekeycondition
        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.GT.toString())
                .withAttributeValueList(new AttributeValue().withN("0"));
        keyCondition.put("DateSubmittedEpoch", rangeKeyCondition);


//          Create QueryRequest that retreives all reports a user has submitted.
//          ProfilePageSorter is secdondary Index that groups reports by user who submitted them
        QueryRequest queryRequest = new QueryRequest()
                .withTableName("SkywarnWSDB_rev4")
                .withKeyConditions(keyCondition)
                .withIndexName("CountyIndex");
        queryRequest.setScanIndexForward(false);

        QueryResult queryResult = ddb.query(queryRequest);
        AsyncTaskHelper taskHelper = new AsyncTaskHelper();
        taskHelper = new AsyncTaskHelper();

//        List<Map<String, AttributeValue>> valMap = queryResult.getItems();
        if (queryResult.getCount() > 0) {
            reportList = taskHelper.addReportsToMap(queryResult);
        }

        Log.d(TAG, "QueryResultSize: " + queryResult.getCount());

        //Prints all report attributes
        //Log.d(TAG, "All report Attributes: " + item.toString());
        //Gets attribute values
        //AttributeValue val = (AttributeValue)  item.get("DateSubmittedEpoch");
        //AttributeValue val  = (AttributeValue) item.get(reportAttributes[counter]);
        //if(((AttributeValue) item.get(reportAttributes[counter])).getS() == null) {
        //for(int i =0; i < reportAttributes.length-1; i++){
        //Log.i(TAG, "attribute: " + reportAttributes[i] + " report: " + item.get("DateSubmittedEpoch")+ " has: " +  item.get(reportAttributes[i]).toString());
        // if(item.get("WaterEquivalent")!= null)
        //Log.d(TAG)
        // }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

//        for(int i=0; i < reportList.size();i++)
//            Log.d(TAG, "onPostExecute Values: "+String.valueOf(reportList.get(i).getDateSubmittedEpoch()));

//        reportList=sortByRating(reportList);
        Log.d(TAG, "onPostExecute() NetVote in descending ordrer : ");
        for (int i = 0; i < reportList.size(); i++) {
            Log.d(TAG, "onPostExecute() NetVote: " + reportList.get(i).getNetVote());
        }
        delegate.processFinish(reportList);
        delegate = null;
    }

    public ArrayList<SkywarnWSDBMapper> sortByRating(ArrayList list) {
        Collections.sort(list, new Comparator<SkywarnWSDBMapper>() {
            @Override
            public int compare(SkywarnWSDBMapper o1, SkywarnWSDBMapper o2) {
                if (o1.getNetVote() >= o2.getNetVote())
                    return 1;
                if (o1.getNetVote() < o2.getNetVote())
                    return -1;
                else
                    return 0;
            }
        });
        return list;
    }

    //Getters and Setters
    public void setDelegate(ICallback delegate) {
        this.delegate = delegate;
    }

    public void setReportAttributesArray(String[] reportAttr) {
        reportAttributes = reportAttr;
        for (String s : reportAttributes)
            Log.d(TAG, "setReportAttributesArray: " + s);
    }

    public void setDate(String Date) {
        date = Date;
    }

    public void setContext(Context c) {
        mContext = c;
    }

    public void setUser(String User) {
        user = User;
    }

    public void setCountyToQuery(String countyToQuery) {
        this.countyToQuery = countyToQuery;
    }

    public void setTaskHelper(AsyncTaskHelper taskHelper) {
        this.taskHelper = taskHelper;
    }
}
