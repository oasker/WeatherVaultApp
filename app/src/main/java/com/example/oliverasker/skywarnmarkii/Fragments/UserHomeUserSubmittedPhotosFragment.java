package com.example.oliverasker.skywarnmarkii.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.oliverasker.skywarnmarkii.Adapters.UserSubmittedPhotosAdapter;
import com.example.oliverasker.skywarnmarkii.Callbacks.BitmapCallback;
import com.example.oliverasker.skywarnmarkii.Mappers.SkywarnWSDBMapper;
import com.example.oliverasker.skywarnmarkii.Models.SubmittedPhotoModel;
import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Tasks.GetUserSubmittedPhotosTask;

import java.util.ArrayList;

/**
* Created by oliverasker on 2/19/17.
*/

public class UserHomeUserSubmittedPhotosFragment extends Fragment implements BitmapCallback {
    private static final String TAG = "PersnlPhotosFrag";
    private ListView photoListView;
    private String username = "tjpereira1995";
    private ArrayList<SubmittedPhotoModel> photoModelList;
    ViewGroup Containter;
    private ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.fragment_user_home_submitted_photos, container, false);
        Log.d(TAG, "onCreateView");
        Containter = container;
        ArrayList<SkywarnWSDBMapper> testList = new ArrayList<>();
        photoModelList = new ArrayList<>();

        SkywarnWSDBMapper test = new SkywarnWSDBMapper();
        test.setDateSubmittedEpoch((long)124124);
        test.setDateSubmittedString("12/23/2343");
        SkywarnWSDBMapper test2 = new SkywarnWSDBMapper();
        test2.setDateSubmittedEpoch((long)1243333);
        test2.setDateSubmittedString("1/42/1993");
        testList.add(test);

        SubmittedPhotoModel photoModel1 = new SubmittedPhotoModel();
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
        photoModel1.setImage(icon);
        photoModelList.add(photoModel1);


        GetUserSubmittedPhotosTask getPhotos = new GetUserSubmittedPhotosTask();
        getPhotos.setmContext(getContext());
        getPhotos.setUsername(username);
        getPhotos.setBitmapCallback(this);
       // getPhotos.execute();



        photoListView = (ListView)v.findViewById(R.id.user_submitted_photos_listview);
//        UserSubmittedPhotosAdapter userPhotoAdapter = new UserSubmittedPhotosAdapter(getContext(), testList);
        UserSubmittedPhotosAdapter userPhotoAdapter = new UserSubmittedPhotosAdapter(getContext(), photoModelList);

        photoListView.setAdapter(userPhotoAdapter);

        v = photoListView.getAdapter().getView(0,null,container );
        return v;
    }
    public void setBitMapArray(ArrayList<Bitmap> hashmap){
        bitmapArrayList = hashmap;

    }
    public void setString(String s){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void processFinish(ArrayList<Bitmap> result) {
        Log.i(TAG , "bitMapcallback processFinished: size: " + result.size());

    }

    public ArrayList<Bitmap> getBitmapArrayList() {
        return bitmapArrayList;
    }

    public void setBitmapArrayList(ArrayList<Bitmap> bitmapArrayList) {
        this.bitmapArrayList = bitmapArrayList;
    }
}
