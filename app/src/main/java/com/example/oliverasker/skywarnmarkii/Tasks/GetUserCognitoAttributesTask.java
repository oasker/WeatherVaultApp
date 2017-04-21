package com.example.oliverasker.skywarnmarkii.Tasks;

/**
 * Created by oliverasker on 2/23/17.
 */
// Todo: allow users to update cognito attributes
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.example.oliverasker.skywarnmarkii.Callbacks.UserAttributesCallback;
import com.example.oliverasker.skywarnmarkii.Constants;

import java.util.HashMap;
import java.util.Map;

public class GetUserCognitoAttributesTask extends AsyncTask<Void, Void, Void> {
    private static String TAG = "GetCognitoAttrsTask";
    public UserAttributesCallback delegate = null;
    private CognitoUserPool userPool;
    private Context mContext;
    private CognitoUser user;
    private HashMap<String,String>  attrMap = new HashMap<>();
    private String[] vals = new String[5];
    private Map<String, String> userAttrMap;

    public GetUserCognitoAttributesTask(UserAttributesCallback delegate) {
        this.delegate = delegate;
    }

    // 1. Set up CognitoUserPool
    public void initUserPool(Context context){
        mContext = context;
        userPool = new CognitoUserPool(mContext, Constants.cognitoPoolID, Constants.cognitoAppClientId, null);

    }

    // 2. set CognitoUser
    public void setCognitoUser(String userID){
        user =  userPool.getUser(userID);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        GetDetailsHandler handler = new GetDetailsHandler() {
            @Override
            public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                //attrMap = new HashMap<>();
                Log.i(TAG, "onSuccess()");
                //Map userAttr = cognitoUserDetails.getAttributes().getAttributes();
                CognitoUserAttributes userAttr2 =  cognitoUserDetails.getAttributes();
                userAttrMap = userAttr2.getAttributes();
               // Utility.printMap(userAttr2.getAttributes());
                Log.d(TAG, "given name: "+userAttrMap.get("given_name"));
                //Iterator it = userAttr2.entrySet().iterator();
                int index =0;
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onFailure() : " + exception);
            }
        };
        user.getDetails(handler);
        return null;
    }

    //Handles data after thread finishes
    @Override
    protected void onPostExecute(Void v){
        delegate.onProcessFinished(userAttrMap);
        delegate.onProcessFinished(vals);
        delegate = null;
    }
}
