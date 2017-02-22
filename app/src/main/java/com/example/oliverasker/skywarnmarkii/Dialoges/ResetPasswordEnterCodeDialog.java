package com.example.oliverasker.skywarnmarkii.Dialoges;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.oliverasker.skywarnmarkii.R;

/**
 * Created by oliverasker on 1/18/17.
 */

public class ResetPasswordEnterCodeDialog extends DialogFragment {
    private static final String TAG = "ResetPasswordDialog";

    public interface ResetPasswordEnterCodeDialogListener{
        void onEnterCodeDialogPositiveClick(DialogFragment dialog);
        void onEnterCodeDialogNegativeClick(DialogFragment dialog);
    }
    ResetPasswordEnterCodeDialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_reset_password_layout, null))
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface diaglo, int id) {
                      Log.d(TAG, "PositiveButton onClick");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                         Log.d(TAG, "onNegativeButton onClick");
                        ResetPasswordEnterCodeDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            dialogListener = (ResetPasswordEnterCodeDialogListener) activity;
        }catch(Exception e){
            throw new ClassCastException(activity.toString() + " must implement ResetPasswordEnterCodeDialogListener");

        }

    }

}