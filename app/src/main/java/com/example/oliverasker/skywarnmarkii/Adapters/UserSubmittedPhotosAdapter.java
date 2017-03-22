package com.example.oliverasker.skywarnmarkii.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.Models.SubmittedPhotoModel;
import com.example.oliverasker.skywarnmarkii.R;

import java.util.ArrayList;

/**
 * Created by oliverasker on 2/19/17.
 */

public class UserSubmittedPhotosAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<SubmittedPhotoModel> imageModelsList;

    Bitmap bitmap;
//    public UserSubmittedPhotosAdapter(Context context, ArrayList<SkywarnWSDBMapper> ReportList){
//        //super(context,0,ReportList);
//        mContext = context;
//        reportList = ReportList;
//    }
    public UserSubmittedPhotosAdapter(Context context, ArrayList<SubmittedPhotoModel> ReportList){
        //super(context,0,ReportList);
        mContext = context;
        imageModelsList = ReportList;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public SubmittedPhotoModel getItem(int position) {
        return imageModelsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        SkywarnWSDBMapper report = getItem(position);
        SubmittedPhotoModel report = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_user_submitted_photo, parent,false);
            //convertView = inflater.inflate(R.layout.list_item_user_submitted_photo, parent,false);
        }

        SubmittedPhotoModel report2 = getItem(position);

        ImageView userSubmittedPhoto = (ImageView) convertView.findViewById(R.id.user_submitted_photo_IV);

        //userSubmittedPhoto.setImageResource(R.drawable.sunny);
        userSubmittedPhoto.setImageBitmap(report.getImage());
        TextView tV = (TextView)convertView.findViewById(R.id.date_submitted_tv);
        tV.setText(report.getDateTaken());
        TextView tV2 = (TextView)convertView.findViewById(R.id.photolistItem2);
        //tV.setText("repor");

//        LinearLayout layout = new LinearLayout(mContext);
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//        TextView text = new TextView(mContext);
//        text.setText("Test");
//
//        ImageView image = new ImageView(mContext);
//        image.setImageResource(android.R.drawable.ic_menu_report_image);
//        layout.addView(image);
//        layout.addView(text);

        ImageView image = new ImageView(mContext);
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(250,250);
        image.setLayoutParams(imageLayoutParams);
        image.setImageResource(android.R.drawable.ic_menu_report_image);

        return convertView;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
