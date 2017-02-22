package com.example.oliverasker.skywarnmarkii.Dialoges;

//import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by oliverasker on 1/20/17.
 */

public class LogoutDialog extends DialogFragment{
    private final static String TAG= "LogoutDialog";
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "Logout button in actionbar pressed");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "Cancel Logout button in actionbar pressed");
                    }
                });
        return builder.create();
    }
    public void showLogoutDialog(){
        LogoutDialog logoutDialog = new LogoutDialog();
        logoutDialog.show(getFragmentManager(), "Logout Dialog");
    }
}
