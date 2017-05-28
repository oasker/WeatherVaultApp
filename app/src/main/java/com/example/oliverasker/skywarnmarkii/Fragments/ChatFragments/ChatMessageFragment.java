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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.example.oliverasker.skywarnmarkii.Callbacks.ChatDBCallback;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Fragments.ChatFragments.dummy.DummyContent;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Models.ChatMessageModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.RecyclerViewDecoration.DividerItemDecoration;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


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
    private double mostRecentDate = 0;
    private ArrayList<ChatMessageModel> masterMessageList;
    private String username;
    private String message;
    private String dateSent;
    private Context mContext;


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
        masterMessageList = new ArrayList<>();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        updateDisplay();
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                LinearLayoutManager manager = new LinearLayoutManager(context);
                manager.setStackFromEnd(true);
                recyclerView.setLayoutManager(manager);

            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

//          Todo: Add row of custom items here
            Log.d(TAG, "setting adapter: size: " + test.size());
            adapter = new MyChatMessageRecyclerViewAdapter(masterMessageList, mListener);
            recyclerView.setAdapter(adapter);
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
        Log.d(TAG, "chatDBCallback called");
        if (messages.size() > 0) {
            if (messages.get(messages.size() - 1).dateMessageSent < messages.get(0).dateMessageSent)
                mostRecentDate = messages.get(0).dateMessageSent;
            else
                mostRecentDate = messages.get(messages.size() - 1).dateMessageSent;
            Log.d(TAG, "mostRecentDate: " + mostRecentDate);
        }
        Log.d(TAG, "onProcessFinish(ArrayList<SkywarnWSDBMapper> result)  size: " + messages.size());

        masterMessageList.addAll(messages);
        Log.d(TAG, "messageList size: " + masterMessageList.size());

//        adapter = new MyChatMessageRecyclerViewAdapter(masterMessageList, mListener);
        adapter.notifyItemChanged(masterMessageList.size());

//        recyclerView.setAdapter(adapter);
    }

    private void updateDisplay() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Log.d(TAG, "run() timer is dopeee");
                GetRecentChatMessagesTask getMessagesTask = new GetRecentChatMessagesTask();
                getMessagesTask.setState("MA");
                getMessagesTask.setmContext(mContext);
                getMessagesTask.setCallback(ChatMessageFragment.this);
                getMessagesTask.setMostRecentReportDate(mostRecentDate);
//            getMessagesTask.setCurrentUsersUsername();
                getMessagesTask.execute();
            }

        }, 0, 5000);//Update text every second
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

    //    READ FROM CHATDB TASK
    public class GetRecentChatMessagesTask extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "GetRecentChatMsgTask";
        ArrayList<SkywarnWSDBMapper> reportList = new ArrayList<>();
        Context mContext;
        private ChatDBCallback callback = null;
        private AmazonDynamoDBClient ddb;
        private String state;
        private ArrayList<ChatMessageModel> messageList = new ArrayList<>();
        private String currentUsersUsername;
        private String message;
        private double messageDate;
        private String username;

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

            QueryResult queryResult = ddb.query(queryRequest);

            boolean isCurrentUsersMessage;
            for (Map item : queryResult.getItems()) {
                if (item.containsKey("Username")) {
                    username = Utility.parseDynamoDBResultValuesToString(item.get("Username").toString());
                }
                if (item.containsKey("Comment"))
                    message = Utility.parseDynamoDBResultValuesToString(item.get("Comment").toString());
                if (item.containsKey("MessageDate"))
                    messageDate = Utility.parseDynamoDBResultValuesToLong(item.get("MessageDate").toString());

                if (item.containsKey("State"))
                    state = Utility.parseDynamoDBResultValuesToString(item.get("State").toString());
//                if(currentUsersUsername.equals(username))
//                    isCurrentUsersMessage = true;
//                else
//                    isCurrentUsersMessage = false;

                Log.d(TAG, "******INCOMING CHAT******");
                Log.d(TAG, "username: " + username + " message: " + message + " state: " + state);
                ChatMessageModel chatModel = new ChatMessageModel(
                        state,
                        message,
                        username,
                        messageDate,
                        true
                );
                messageList.add(chatModel);
                Log.d(TAG, "messageList.size() " + messageList.size());
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
            Log.d(TAG, "onPostExecute()");
            callback.chatDBCallbackComplete(messageList);
            callback = null;
        }


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
