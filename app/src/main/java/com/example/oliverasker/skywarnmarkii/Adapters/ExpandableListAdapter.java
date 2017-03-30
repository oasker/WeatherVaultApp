package com.example.oliverasker.skywarnmarkii.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oliverasker.skywarnmarkii.R;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.util.HashMap;
import java.util.List;

/**
 * Created by oliverasker on 3/21/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "ExpandableLViewAdapter";
    private Context mContext;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private HashMap<String, String> allAttributesToSearchFor = new HashMap<>();

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String> >listChildData) {
        Log.d(TAG, "listdataHeader size: " + listDataHeader.size() + " listChildData size: " + listChildData.size());
        mContext = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
//        Log.i(TAG, "getGroup()");
        return listDataHeader.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
//        Log.i(TAG, "getGroupId() " + groupPosition);
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        Log.i(TAG, "getGroupView():  ");
        String headerTitle = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_group, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        View currentFocus = ((Activity)mContext).getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final  String childText = (String) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_item, null);
        }
        TextView txListChild = (TextView)convertView.findViewById(R.id.lblListItem);
        txListChild.setText(childText);
        final EditText eTChild = (EditText)convertView.findViewById(R.id.input_text);
        eTChild.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, s.toString());
                eTChild.getText().toString();
                if (!eTChild.getText().toString().equals("")) ;
                Log.i(TAG, "textField Value:" + eTChild.getText().toString());
               // if (!allAttributesToSearchFor.containsKey(childText))
                    allAttributesToSearchFor.put(childText, eTChild.getText().toString());
//                else {
//                    allAttributesToSearchFor.remove(childText);
//                    allAttributesToSearchFor.put(childText, eTChild.getText().toString());
//                }
            }
        });
        View currentFocus = ((Activity)mContext).getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        return convertView;
    }

    public HashMap<String, List<String>> getAllKeyValuePair(){
        return listDataChild;
    }

    public void printAllKeyValuePairs(){
        Utility.printMap(allAttributesToSearchFor);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
