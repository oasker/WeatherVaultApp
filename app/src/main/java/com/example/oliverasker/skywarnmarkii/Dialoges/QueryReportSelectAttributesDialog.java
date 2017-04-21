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
 */

public class QueryReportSelectAttributesDialog extends DialogFragment {
    private static final String TAG = "QryRprtAttrSelectDialog";
    private StringCallback stringCallback;
    private String attributeToQuery;
    private int dialogTag;
    private QueryReportAttributesFragmentDialogCallback callback;
    private int attrDynamoDBNameId;

    //  string array id from string xml resources is assigned to this value
    private int attrArrayID;
    private String[] attrArray;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialogTag = this.getArguments().getInt("dialogTag");

        attrArray = getResources().getStringArray(attrArrayID);
        final AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        builder.setTitle("Select Attributes to Query")
                .setItems(attrArrayID, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        attributeToQuery = attrArray[which];

                        callback.onProcessFinished(attributeToQuery, getResources().getStringArray(attrDynamoDBNameId)[which], dialogTag);
                        Log.d(TAG, "onClick() index: " + which + " attrArray[which]: " + attrArray[which]);
                    }
                })
//            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Log.d(TAG, "onClick() positive Button: which: "  + which);
//                    //stringCallback.onProcessError(attributeToQuery);
//                    callback.onProcessFinished(attributeToQuery,dialogTag);
//                }
//            })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick() negative Button: which: " + which);
                    }
                });
        return builder.create();
    }


    //////////////////   Setters/Getters     ///////////////////
    public void setEventType(int id) {
//        Use int as id to set specific array of attributes
//        0. Winter
//        1. Severe
//        2. Rain/Flood
//        3. Coastal

        switch (id) {
            case 0:
                attrArrayID = R.array.SevereAttributesValues;
                attrDynamoDBNameId = R.array.SevereAttributesNames;
                break;
            case 1:
                attrArrayID = R.array.WinterAttributesValues;
                attrDynamoDBNameId = R.array.WinterAttributesNames;

                break;
            case 2:
                attrArrayID = R.array.CoastalAttributesValues;
                attrDynamoDBNameId = R.array.CoastalAttributesNames;

                break;
            case 3:
                attrArrayID = R.array.RainAttributesValues;
                attrDynamoDBNameId = R.array.RainAttributesNames;

                break;
        }
    }

    public void setStringCallback(StringCallback stringCallback) {
        this.stringCallback = stringCallback;
    }

    public void setCallback(QueryReportAttributesFragmentDialogCallback callback) {
        this.callback = callback;
    }
}
