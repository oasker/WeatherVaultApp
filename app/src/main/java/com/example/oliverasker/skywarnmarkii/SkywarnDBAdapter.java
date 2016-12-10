package com.example.oliverasker.skywarnmarkii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Random;

/**
 * 10/16/16  --> https://www.youtube.com/watch?v=nOdSARCVYic&list=PL6gx4Cwl9DGBsvRxJJOzG4r4k_zLKrnxl&index=48
 *  Uses custom_row.xml for each list row
 */

//class CustomAdapter extends ArrayAdapter<String> {
class SkywarnDBAdapter extends ArrayAdapter<SkywarnWSDBMapper> implements Serializable{

    //  CustomAdapter(Context context, int resource){
    SkywarnDBAdapter(Context context,SkywarnWSDBMapper[] row){
        super(context, R.layout.custom_row, row);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        //  View customView = inflater.inflate(R.layout.custom_row, parent, false);
        View customView = inflater.inflate(R.layout.gridview_row, parent, false);

        //  Set viewHolder
        //  ViewHolders are used to prevent frequent calls to findViewById() which slows down listview scrolling
        ViewHolder viewHolder = new ViewHolder();
        //  Connect received weatherrowdb object to layout on class
        SkywarnWSDBMapper dbRow = getItem(position);


        //  Map layout fields to DB Result Row Widgets
        //Need to add longitude, lattitude,
        TextView dateTV = (TextView) customView.findViewById(R.id.date_row_text);
        TextView locationTV = (TextView) customView.findViewById(R.id.location_row_text);
        TextView usernameTV = (TextView) customView.findViewById(R.id.reporter_username_row_text);
        TextView weatherEventTV = (TextView) customView.findViewById(R.id.event_type_row_text);
        TextView messageUserTextVT = (TextView) customView.findViewById(R.id.message_user_text);
        TextView eventDescript = (TextView) customView.findViewById(R.id.event_description_row_text);
        TextView timeTV =(TextView)customView.findViewById(R.id.time_row_text);

        //Image to represent event
        ImageView imageView = (ImageView)customView.findViewById(R.id.image_view);

        // Save fields to ViewHolder
        viewHolder.date = dateTV;
        viewHolder.location = locationTV;
        viewHolder.username = usernameTV;
        viewHolder.weatherEvent =weatherEventTV;
        viewHolder.comments = messageUserTextVT;
        viewHolder.weatherImage = imageView;


        //  Set text fields in layout to value of row items
        //  Using epoch for time
//        String date = new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date (Integer.parseInt(dbRow.getDate())*1000));

        dateTV.setText(dbRow.getDate());
       // timeTV.setText(dbRow.getTime());
        //  Combine town and state fields into one textview
        locationTV.setText(dbRow.getEventCity()+" ," +dbRow.getEventState());
        usernameTV.setText(dbRow.getUsername());
        weatherEventTV.setText(dbRow.getWeatherEvent() );
        eventDescript.setText(dbRow.getComments());
        messageUserTextVT.setText("Submitted: " + getRandomNumber());


        //  Set Icon Image Based on Weather Type
        String weatherType = dbRow.getWeatherEvent().toUpperCase();
        /*
        switch (weatherType.toUpperCase()){
            case"SEVERE":
                imageView.setImageResource(R.drawable.severe);
                break;
            case"RAIN/FLOOD":
                imageView.setImageResource(R.drawable.rain);
                break;
            case "WINTER":
                imageView.setImageResource(R.drawable.snow_icon);
                break;
            case ""
            default:imageView.setImageResource(R.drawable.sunny); //Eventuall will set default to logo of app
        }
        */

        if(weatherType.toUpperCase().contains("SEVERE"))
            imageView.setImageResource(R.drawable.severe);
        if(weatherType.toUpperCase().contains("RAIN"))
            imageView.setImageResource(R.drawable.rain);
        if(weatherType.toUpperCase().contains("WINTER"))
            imageView.setImageResource(R.drawable.snow_icon);
        if(weatherType.toUpperCase().contains("COASTAL"))
            imageView.setImageResource(R.drawable.coastal);

        return customView;
    }

    static class ViewHolder{
        TextView date;
        TextView location;
        TextView weatherEvent;
        TextView comments;
        TextView username;
        ImageView weatherImage;
    }

    public String getRandomNumber() {
        int min =0;
        int max =100;
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return Integer.toString(randomNum);
    }
}
