package com.example.oliverasker.skywarnmarkii.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 1/15/17.
 *
 *  https://developer.android.com/reference/android/app/Fragment.html
 */

public class UserDetailsFragment extends Fragment {
    private String title;
    private int page;
    private int padding = 5;

//    public static UserHomeActivity newInstance(int page, String title){
//        UserHomeActivity homeActivity = new UserHomeActivity();
//
//        Bundle args = new Bundle();
//        args.putInt("page", page);
//        args.putString("title",title);
//
//        //homeActivity.setArguments(args);
//        return homeActivity;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstsance ){
        return inflater.inflate(R.layout.user_info_home_fragment_layout, container, false);
    }
    public int getShownIndex(){
        return getArguments().getInt("index",0);
    }




    /*
    @Override
    public View onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        super.onCreate(inflater, container, savedInstance);
       /*
        if(container == null){
            return null;
        }
        ScrollView scroller = new ScrollView(getActivity()):

        TextView text = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4,getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
        scroller.addView(text);
        text.setText("IT WORKEDDD");

        return scroller;
    }
*/

}
