package com.example.oliverasker.skywarnmarkii.Fragments.ChatFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMappingException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.example.oliverasker.skywarnmarkii.Activities.MainActivity;
import com.example.oliverasker.skywarnmarkii.Callbacks.ChatDBCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Fragments.ChatFragments.dummy.DummyContent;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Models.ChatMessageModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.RecyclerViewDecoration.DividerItemDecoration;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ChatMessageFragment extends Fragment implements ChatDBCallback {
    private final static String TAG = "ChatMessageFrag";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private MyChatMessageRecyclerViewAdapter adapter;

    private RecyclerView recyclerView;


    public ChatMessageFragment() {
    }


    public static ChatMessageFragment newInstance(int columnCount) {
        ChatMessageFragment fragment = new ChatMessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatmessage_list, container, false);


//        TEst
        ArrayList<ChatMessageModel> test = new ArrayList<ChatMessageModel>();

        ChatMessageModel model = new ChatMessageModel("1", "" +
                "Message my man, long words, to show how the screen deals when it is forced to wrap and stuff",
                "Arthur Douglas",
                342, false);
        test.add(model);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            ((RecyclerView) view).addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

//            ChatInsertTask chatInsertTask = new ChatInsertTask();

//          Todo: Add row of custom items here
            Log.d(TAG, "setting adapter: size: " + test.size());
            adapter = new MyChatMessageRecyclerViewAdapter(test, mListener);
//            recyclerView.setAdapter(adapter);


            ChatInsertTask chatInsertTask = new ChatInsertTask();
            chatInsertTask.setComment("What did i miss?");
            chatInsertTask.setMessageAuthor("Birdperson");
            chatInsertTask.setState("MA");
            chatInsertTask.setMessageDate(Calendar.getInstance().getTimeInMillis());
//            chatInsertTask.execute();

            GetRecentChatMessagesTask getMessagesTask = new GetRecentChatMessagesTask();
            getMessagesTask.setState("MA");
            getMessagesTask.setmContext(getActivity().getApplicationContext());
            getMessagesTask.setCallback(this);
            getMessagesTask.setMostRecentReportDate(0);
//            getMessagesTask.setCurrentUsersUsername();
            getMessagesTask.execute();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void chatDBCallbackComplete(ArrayList<ChatMessageModel> messages) {
        Log.d(TAG, "onProcessFinish(ArrayList<SkywarnWSDBMapper> result)  size: " + messages.size());
        adapter = new MyChatMessageRecyclerViewAdapter(messages, mListener);
        recyclerView.setAdapter(adapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.ChatMessage item);
    }


    //      INSERT To CHATDB TASK
    class ChatInsertTask extends AsyncTask<String[], Void, Void> {

        private static final String TAG = "ChatInsertTask";
        AmazonDynamoDBClient ddb = MainActivity.clientManager.ddb();

        ICallback callback;
        String state;
        String messageAuthor;
        String comment;
        long messageDate;


        public ChatInsertTask() {
//            attributeVals = new AttributeValue[attributeVals.length];
        }

        @Override
        protected Void doInBackground(String[]... params) {

            Log.i(TAG, "ChatInsertTask doInBackground() ");
            Map<String, AttributeValue> attributeValueMap = new HashMap<>();


            Log.d(TAG, "Author: " + messageAuthor + " message: " + comment + " messageDate: " + messageDate);

            attributeValueMap.put("State", new AttributeValue().withS(state));
            attributeValueMap.put("MessageDate", new AttributeValue().withN(String.valueOf(messageDate)));
            attributeValueMap.put("Username", new AttributeValue().withS(messageAuthor));
            attributeValueMap.put("Comment", new AttributeValue().withS(comment));

            try {
                PutItemRequest putItemRequest = new PutItemRequest("ChatDB", attributeValueMap);
                PutItemResult putItemResult = ddb.putItem(putItemRequest);
            } catch (DynamoDBMappingException dynamoDBMappingException) {
                Log.e(TAG, dynamoDBMappingException.toString());
            } catch (com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException dynamoDBMappingException) {
                Log.e(TAG, dynamoDBMappingException.toString());
            } catch (AmazonServiceException ex) {
                MainActivity.clientManager.wipeCredentialsonAuthError(ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Log.d(TAG, "onPostExecute()");
            //delegate.processFinish();
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setMessageAuthor(String messageAuthor) {
            this.messageAuthor = messageAuthor;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setMessageDate(long messageDate) {
            this.messageDate = messageDate;
        }

        public void setCallback(ICallback callback) {
            this.callback = callback;
        }
    }


    //    READ FROM CHATDB TASK
    public class GetRecentChatMessagesTask extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "GetAllRecordsForDayTask";
        ArrayList<SkywarnWSDBMapper> reportList = new ArrayList<>();
        Context mContext;
        private ChatDBCallback callback = null;
        private AmazonDynamoDBClient ddb;
        private String state;
        private ArrayList<ChatMessageModel> messageList = new ArrayList<>();
        private String currentUsersUsername;

        private double mostRecentReportDate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "GetAllRecordsForDayTask called");
            //AmazonDynamoDBClient dynamoDB = MainActivity.clientManager.ddb(); //Use MainActivity client manager
            CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                    mContext,
                    Constants.IDENTITY_POOL_ID,
                    Regions.US_EAST_1);

            ddb = new AmazonDynamoDBClient(credentials);
            ddb.setRegion(Region.getRegion(Regions.US_EAST_1));


            Log.d(TAG, "state: " + state + " date: " + mostRecentReportDate);

            //FINALLY WORKS
            Map keyCondition = new HashMap();

            //Create condition for hashkey
            Condition hashKeyCondition = new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ.toString())
                    .withAttributeValueList(new AttributeValue().withS(state));

            keyCondition.put("State", hashKeyCondition);

            // Create Rangekeycondition
            Condition rangeKeyCondition = new Condition()
                    .withComparisonOperator(ComparisonOperator.GT.toString())
                    .withAttributeValueList(new AttributeValue().withN(String.valueOf(mostRecentReportDate)));
            keyCondition.put("MessageDate", rangeKeyCondition);

            QueryRequest queryRequest = new QueryRequest()
                    .withTableName("ChatDB")
                    .withKeyConditions(keyCondition);
            queryRequest.setScanIndexForward(false);

            QueryResult queryResult = ddb.query(queryRequest);

            boolean isCurrentUsersMessage;
            for (Map item : queryResult.getItems()) {
                String username = Utility.parseDynamoDBResultValuesToString(item.get("Username").toString());

//                if(currentUsersUsername.equals(username))
//                    isCurrentUsersMessage = true;
//                else
//                    isCurrentUsersMessage = false;

                ChatMessageModel chatModel = new ChatMessageModel(Utility.parseDynamoDBResultValuesToString(item.get("State").toString()),
                        Utility.parseDynamoDBResultValuesToString(item.get("Comment").toString()),
                        username,
                        Utility.parseDynamoDBResultValuesToLong(item.get("MessageDate").toString()),
                        true
                );
                messageList.add(chatModel);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callback.chatDBCallbackComplete(messageList);
            callback = null;
        }


        //
        public void setmContext(Context mContext) {
            this.mContext = mContext;
        }

        public void setMostRecentReportDate(double mostRecentReportDate) {
            this.mostRecentReportDate = mostRecentReportDate;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setContext(Context c) {
            mContext = c;
        }

        public void setCallback(ChatDBCallback callback) {
            this.callback = callback;
        }

        public void setCurrentUsersUsername(String currentUsersUsername) {
            this.currentUsersUsername = currentUsersUsername;
        }
    }

}
