package com.example.oliverasker.skywarnmarkii.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.io.Serializable;

/**
 * 10/16/16  --> https://www.youtube.com/watch?v=nOdSARCVYic&list=PL6gx4Cwl9DGBsvRxJJOzG4r4k_zLKrnxl&index=48
 *  Uses custom_row.xml for each list row
 */

//class CustomAdapter extends ArrayAdapter<String> {
public class SkywarnDBAdapter extends ArrayAdapter<SkywarnWSDBMapper> implements Serializable{
    private static final String TAG = "SkywarnDBAdapter";

    //  CustomAdapter(Context context, int resource){
    public SkywarnDBAdapter(Context context, SkywarnWSDBMapper[] row){
        super(context, R.layout.custom_row, row);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.gridview_row, parent, false);

        //  Set viewHolder: ViewHolders are used to prevent frequent calls
        //  to findViewById() which slows down listview scrolling
        ViewHolder viewHolder = new ViewHolder();

        //  Connect received weatherrowdb object to layout on class
        SkywarnWSDBMapper dbRow = getItem(position);

        //  Map layout fields to DB Result Row Widgets on listview of returned query results
        TextView dateTV = (TextView) customView.findViewById(R.id.date_row_text);
        TextView locationTV = (TextView) customView.findViewById(R.id.location_row_text);
        TextView usernameTV = (TextView) customView.findViewById(R.id.reporter_username_row_text);
        TextView weatherEventTV = (TextView) customView.findViewById(R.id.event_type_row_text);
        TextView messageUserTextVT = (TextView) customView.findViewById(R.id.message_user_text);
        TextView eventDescript = (TextView) customView.findViewById(R.id.event_description_row_text);
        TextView timeTV =(TextView)customView.findViewById(R.id.time_row_text);
        TextView commentsTV = (TextView) customView.findViewById(R.id.view_report_activity_comments);

        //Image to represent event
        ImageView imageView = (ImageView)customView.findViewById(R.id.image_view);

        // Save fields to ViewHolder
        ViewHolder.date = dateTV;
        ViewHolder.location = locationTV;
        ViewHolder.username = usernameTV;
        ViewHolder.weatherEvent =weatherEventTV;
        ViewHolder.comments = messageUserTextVT;
        ViewHolder.weatherImage = imageView;

        //  Set text fields in layout to value of row items
        dateTV.setText("Submitted On: " +dbRow.getDateSubmittedString());
//        Log.d(TAG, "dbRow.getDateSubmittedString(): " + dbRow.getDateSubmittedString());
//        Log.d(TAG, "dbRow.getDateSubmittedEpoch(): " + dbRow.getDateSubmittedEpoch());
//        Log.d(TAG, "dbRow.getDateOfEvent() (epoch) : " + dbRow.getDateOfEvent());
//        Log.d(TAG, "dbRow.getUsername(): " + dbRow.getUsername());


        //  Combine town and state fields into one textview
        locationTV.setText(dbRow.getEventCity()+" ," +dbRow.getEventState());
        usernameTV.setText(dbRow.getUsername());
        weatherEventTV.setText(dbRow.getWeatherEvent() );
        eventDescript.setText(dbRow.getComments());
        dateTV.setText(Utility.epochToDateTimeString(dbRow.getDateOfEvent()));

        //  Set Icon Image Based on Weather Type
        String weatherType = dbRow.getWeatherEvent().toUpperCase();

        if(weatherType.toUpperCase().contains("SEVERE"))
            imageView.setImageResource(R.drawable.severe);
        if(weatherType.toUpperCase().contains("RAIN"))
            imageView.setImageResource(R.drawable.rain);
        if(weatherType.toUpperCase().contains("WINTER"))
            imageView.setImageResource(R.drawable.snow_icon);
        if(weatherType.toUpperCase().contains("COASTAL"))
            imageView.setImageResource(R.drawable.coastal);
        if(weatherType.toUpperCase().contains("GENERAL"))
            imageView.setImageResource(R.drawable.sunny);
        return customView;
    }

    static class ViewHolder{
        static TextView date;
        static TextView location;
        static TextView weatherEvent;
        static TextView comments;
        static TextView username;
        static ImageView weatherImage;
    }

}
