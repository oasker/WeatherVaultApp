package com.example.oliverasker.skywarnmarkii.Tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.example.oliverasker.skywarnmarkii.Constants;
import com.example.oliverasker.skywarnmarkii.Utility;

import java.util.Iterator;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by oliverasker on 2/20/17.
 */

public class GetUserDetailsTask extends AsyncTask<Void,Void,Void> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... params) {
            final String[] attributes=null;
            //Get User Details
        // CognitoUser user = UserInformationModel.getCognitoUserSession();
        //user.getDetailsInBackground(getDetailsHandler);
            GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
                @Override
                public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                    Map userAttr = cognitoUserDetails.getAttributes().getAttributes();
                    Utility.printMap(userAttr);

                    Iterator it = userAttr.entrySet().iterator();
                    int index =0;
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        //System.out.println("AttrMap contains: "+pair.getKey() + " = " + pair.getValue());
                        Log.d(TAG, "AttrMap contains: " + pair.getKey() + " = " + pair.getValue());
                        attributes[index] = pair.getValue().toString();
                        it.remove(); // avoids a ConcurrentModificationException
//                    index++;
//                    UserInformationModel.getInstance().setPhone(attributes[index]);
                    }
                    index = 0;

                    Log.d(TAG, "phone number: "+userAttr.get("phone_number"));
                    // UserInformationModel.getInstance().setPhone(userAttr.get("phone_number").toString());

                   // Log.d(TAG, "getDetailsHandler(): username: " + username + "  callsign: " + callsign);
                    for(int i =0; i <userAttr.size(); i++){
                        attributes[i] =(String)userAttr.get(i);
                        Log.d(TAG, "onSuccessHandler"+attributes[i]);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("attributeArray", attributes);
                    Intent i = new Intent();
                    i.putExtras(bundle);
                }

                @Override
                public void onFailure(Exception e) {
                    String errorCode ="";
                    Log.e(TAG, "getDetailsHandler failed: ", e);
                    if( e instanceof com.amazonaws.mobileconnectors.cognitoidentityprovider.exceptions.CognitoNotAuthorizedException ){
                        errorCode = Constants.GET_USER_ATTRIBUTES_ERROR_NOT_AUTHENTICATED;
                    }
                    //launchConfirmUser();
                    Intent i = new Intent();
                    i.putExtra("errorCode",errorCode);
                }
            };
        return null;

    }

            @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
