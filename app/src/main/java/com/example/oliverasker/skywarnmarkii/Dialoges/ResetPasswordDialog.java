package com.example.oliverasker.skywarnmarkii.Dialoges;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 1/18/17.
 */

public class ResetPasswordDialog extends DialogFragment {
    private static final String TAG = "ResetPasswordDialog";

    EditText resetEmailCodeTV;
    EditText newPasswordTV;

    public interface ResetPasswordDialogListener{
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    ResetPasswordDialogListener resetDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //resetEmailCodeTV = (EditText)
        builder.setView(inflater.inflate(R.layout.dialog_reset_password_layout, null))
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                      Log.d(TAG, "PositiveButton onClick");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                         Log.d(TAG, "onNegativeButton onClick");
                        ResetPasswordDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            resetDialogListener = (ResetPasswordDialogListener) activity;
        }catch(Exception e){
            throw new ClassCastException(activity.toString() + " class must implement ResetPasswordDialogListener");
        }
    }
}