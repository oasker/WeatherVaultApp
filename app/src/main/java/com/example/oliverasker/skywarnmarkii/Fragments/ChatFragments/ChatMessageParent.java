package com.example.oliverasker.skywarnmarkii.Fragments.ChatFragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.example.oliverasker.skywarnmarkii.Activities.MainActivity;
import com.example.oliverasker.skywarnmarkii.Callbacks.ICallback;
import com.example.oliverasker.skywarnmarkii.Models.UserInformationModel;
import com.example.oliverasker.skywarnmarkii.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliverasker on 5/1/17.
 */

public class ChatMessageParent extends Fragment {
    private static final String TAG = "ChageMessageParents";
    ChatMessageFragment recyclerMssageFragment;
    private ImageButton submitMessageButton;
    private EditText messageInputField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_chat_recyclerview_holder, container, false);

        submitMessageButton = (ImageButton) view.findViewById(R.id.chat_parent_submit_message_button);
        submitMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "submitMessageButton onClick()");
                String message = messageInputField.getText().toString();
                Log.d(TAG, "onClick() message: " + message);
                ChatInsertTask chatInsertTask = new ChatInsertTask();
                chatInsertTask.setComment(messageInputField.getText().toString());
                chatInsertTask.setMessageAuthor(UserInformationModel.getInstance().getUsername());
                chatInsertTask.setState("MA");
                chatInsertTask.setMessageDate(Calendar.getInstance().getTimeInMillis());
                chatInsertTask.execute();
                messageInputField.setText("");
            }
        });


//      Users entered chat post
        messageInputField = (EditText) view.findViewById(R.id.chat_parent_message_input);

//      Add fragment containing chat recycler view
        recyclerMssageFragment = new ChatMessageFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.chat_recyclerview_container, recyclerMssageFragment);
        ft.commit();

        return view;
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

}
