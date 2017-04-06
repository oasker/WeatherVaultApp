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

import java.io.Serializable;

/**
 * 10/16/16  --> https://www.youtube.com/watch?v=nOdSARCVYic&list=PL6gx4Cwl9DGBsvRxJJOzG4r4k_zLKrnxl&index=48
 *  Uses custom_row.xml for each list row
 */

//class CustomAdapter extends ArrayAdapter<String> {
public class SkywarnDBAdapter extends ArrayAdapter<SkywarnWSDBMapper> implements Serializable {
    private static final String TAG = "SkywarnDBAdapter";
    private Context mContext;
    SkywarnWSDBMapper[] rows;

    public SkywarnDBAdapter(Context context, SkywarnWSDBMapper[] row) {
        super(context, R.layout.custom_row, row);
        mContext = context;
        rows = row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SkywarnWSDBMapper dbRow = rows[position];


        ViewHolder viewHolder = null;
        //if (convertView == null) {
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gridview_row, parent, false);


            //  Map layout fields to DB Result Row Widgets on listview of returned query results
//            dateTV = (TextView) customView.findViewById(R.id.date_row_text);
//            locationTV = (TextView) customView.findViewById(R.id.location_row_text);
//            usernameTV = (TextView) customView.findViewById(R.id.reporter_username_row_text);
//            weatherEventTV = (TextView) customView.findViewById(R.id.event_type_row_text);
//            messageUserTextTV = (TextView) customView.findViewById(R.id.message_user_text);
//            eventDescript = (TextView) customView.findViewById(R.id.event_description_row_text);
//            timeTV = (TextView) customView.findViewById(R.id.time_row_text);
//            commentsTV = (TextView) customView.findViewById(R.id.view_report_activity_comments);
//            ratingTV = (TextView) customView.findViewById(R.id.report_rating_row_text);
//            //Image to represent event
//            ImageView weatherEventImageView = (ImageView) convertView.findViewById(R.id.image_view);
//
//            //  Set Icon Image Based on Weather Type
//            String weatherType = dbRow.getWeatherEvent().toUpperCase();
//
//            if (weatherType.toUpperCase().contains("SEVERE"))
//                weatherEventImageView.setImageResource(R.drawable.severe);
//            if (weatherType.toUpperCase().contains("RAIN"))
//                weatherEventImageView.setImageResource(R.drawable.rain);
//            if (weatherType.toUpperCase().contains("WINTER"))
//                weatherEventImageView.setImageResource(R.drawable.snow_icon);
//            if (weatherType.toUpperCase().contains("COASTAL"))
//                weatherEventImageView.setImageResource(R.drawable.coastal);
//            if (weatherType.toUpperCase().contains("GENERAL"))
//                weatherEventImageView.setImageResource(R.drawable.sunny);

//            ViewHolder.date = (TextView) convertView.findViewById(R.id.date_row_text);
//            ViewHolder.location = (TextView) convertView.findViewById(R.id.location_row_text);
//            ViewHolder.username = (TextView) convertView.findViewById(R.id.reporter_username_row_text);
//            ViewHolder.weatherEvent = (TextView) convertView.findViewById(R.id.event_type_row_text);
//            ViewHolder.comments = (TextView) convertView.findViewById(R.id.event_description_row_text);
//            ViewHolder.rating = (TextView) convertView.findViewById(R.id.report_rating_row_text);

            ViewHolder.date = (TextView) convertView.findViewById(R.id.date_row_text);
            ViewHolder.location = (TextView) convertView.findViewById(R.id.location_row_text);
            ViewHolder.username = (TextView) convertView.findViewById(R.id.reporter_username_row_text);
            ViewHolder.weatherEvent = (TextView) convertView.findViewById(R.id.event_type_row_text);
            ViewHolder.comments = (TextView) convertView.findViewById(R.id.event_description_row_text);
            ViewHolder.rating = (TextView) convertView.findViewById(R.id.report_rating_row_text);

            //Image to represent weather event type
            ViewHolder.weatherImage = (ImageView) convertView.findViewById(R.id.image_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //SkywarnWSDBMapper dbRow = getItem(position);
//            viewHolder.date.setText("Submitted: " + dbRow.getDateSubmittedString());
//            ViewHolder.location.setText(dbRow.getEventCity() + " ," + dbRow.getEventState());
//            ViewHolder.username.setText(dbRow.getUsername());
//            ViewHolder.weatherEvent.setText(dbRow.getWeatherEvent());
//            ViewHolder.rating.setText("Rating: " + String.valueOf(dbRow.getNetVote()));
//            ViewHolder.comments.setText(dbRow.getComments());
        ViewHolder.date.setText("Submitted: " + dbRow.getDateSubmittedString());
        ViewHolder.location.setText(dbRow.getEventCity() + " ," + dbRow.getEventState());
        ViewHolder.username.setText(dbRow.getUsername());
        ViewHolder.weatherEvent.setText(dbRow.getWeatherEvent());
        ViewHolder.rating.setText("Rating: " + String.valueOf(dbRow.getNetVote()));
        ViewHolder.comments.setText(dbRow.getComments());
        //ViewHolder.pos = position;

        ImageView weatherEventImageView = (ImageView) convertView.findViewById(R.id.image_view);

        //  Set Icon Image Based on Weather Type
        String weatherType = dbRow.getWeatherEvent().toUpperCase();
//
//        if (weatherType.toUpperCase().contains("SEVERE"))
//            weatherEventImageView.setImageResource(R.drawable.severe);
//        if (weatherType.toUpperCase().contains("RAIN"))
//            weatherEventImageView.setImageResource(R.drawable.rain);
//        if (weatherType.toUpperCase().contains("WINTER"))
//            weatherEventImageView.setImageResource(R.drawable.snow_icon);
//        if (weatherType.toUpperCase().contains("COASTAL"))
//            weatherEventImageView.setImageResource(R.drawable.coastal);
//        if (weatherType.toUpperCase().contains("GENERAL"))
//            weatherEventImageView.setImageResource(R.drawable.sunny);

        if (weatherType.toUpperCase().contains("SEVERE"))
            weatherEventImageView.setImageResource(R.drawable.severe);
        if (weatherType.toUpperCase().contains("RAIN"))
            weatherEventImageView.setImageResource(R.drawable.rain);
        if (weatherType.toUpperCase().contains("WINTER"))
            weatherEventImageView.setImageResource(R.drawable.snow_icon);
        if (weatherType.toUpperCase().contains("COASTAL"))
            weatherEventImageView.setImageResource(R.drawable.coastal);
        if (weatherType.toUpperCase().contains("GENERAL"))
            weatherEventImageView.setImageResource(R.drawable.sunny);
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    static class ViewHolder {
        static TextView date;
        static TextView location;
        static TextView weatherEvent;
        static TextView comments;
        static TextView username;
        static TextView rating;
        static ImageView weatherImage;
        static int pos;
    }

}
