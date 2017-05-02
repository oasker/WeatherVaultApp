package com.example.oliverasker.skywarnmarkii.Callbacks;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;

/**
 * Created by oliverasker on 4/26/17.
 */

public interface CognitoUserCallback {
    void setCognitoUser(CognitoUser cognitoUser);
}
