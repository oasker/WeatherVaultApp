package com.example.oliverasker.skywarnmarkii.Fragments.ChatFragments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.Fragments.ChatFragments.ChatMessageFragment.OnListFragmentInteractionListener;
import com.example.oliverasker.skywarnmarkii.Models.ChatMessageModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility.Utility;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link .ChatMessage} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyChatMessageRecyclerViewAdapter extends RecyclerView.Adapter<MyChatMessageRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ChatRecyclerAdapter";
    private final List<ChatMessageModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyChatMessageRecyclerViewAdapter(List<ChatMessageModel> items, OnListFragmentInteractionListener listener) {
//        Log.d(TAG, "adapter data list size(): " + items.size());
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chatmessage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.message.setText(mValues.get(position).message);
        holder.messageAuthor.setText(mValues.get(position).messageAuthor);


        String date = Utility.epochToDateTimeString(mValues.get(position).dateMessageSent / 1000);

//        Log.d(TAG, "date CHAT: " + date);

        holder.dateMessageSent.setText(date);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "holder.onClick() " + holder.getAdapterPosition());
//                if (null != mListener) {
//                    Log.d(TAG, );
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView messageAuthor;
        public final TextView message;
        public final TextView dateMessageSent;
        public ChatMessageModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            message = (TextView) view.findViewById(R.id.chat_message_list_item_message_tv);
            messageAuthor = (TextView) view.findViewById(R.id.chat_message_list_item_username_tv);
            dateMessageSent = (TextView) view.findViewById(R.id.chat_message_list_item_date_sent_tv);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + messageAuthor.getText() + "'";
        }
    }


}
