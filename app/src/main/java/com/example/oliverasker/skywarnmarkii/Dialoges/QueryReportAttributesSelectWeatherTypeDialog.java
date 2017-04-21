package com.example.oliverasker.skywarnmarkii.Dialoges;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.example.oliverasker.skywarnmarkii.Callbacks.QueryReportAttributesFragmentDialogCallback;
import com.example.oliverasker.skywarnmarkii.Callbacks.StringCallback;
import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 4/18/17.
 * Opens Dialog that asks user if they want to query for winter, severe, coastal, or rain/flood
 * attributes.
 */


public class QueryReportAttributesSelectWeatherTypeDialog extends DialogFragment {
    private static final String TAG = "QryRptAtrWtherTypDialog";
    private StringCallback stringCallback;
    private String attributeToQuery;
    private QueryReportAttributesFragmentDialogCallback callback;
    //  string array id from string xml resources is assigned to this value
    private int attrArrayResourceID = R.array.AllWeatherEventTypes;
    private String[] attrArray;

    //  For distinguishing callback calls from selecting attribute dialog
    private int dialogTag;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogTag = this.getArguments().getInt("dialogTag");

        attrArray = getResources().getStringArray(attrArrayResourceID);

        final AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        builder.setTitle("Select Type of Weather Event")
                .setItems(attrArrayResourceID, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        attributeToQuery = attrArray[which];
                        callback.onProcessFinished(String.valueOf(which), null, dialogTag);
                        Log.d(TAG, "onClick() index: " + which + " attrArray[which]: " + attrArray[which]);
                    }
                })
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick() positive Button: which: " + which);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick() negative Button: which: " + which);
                    }
                });
        return builder.create();
    }


    //////////////////   Setters/Getters     ///////////////////
//    public void setStringCallback(StringCallback stringCallback) {
//        this.stringCallback = stringCallback;
//    }

    public void setCallback(QueryReportAttributesFragmentDialogCallback callback) {
        this.callback = callback;
    }
}
