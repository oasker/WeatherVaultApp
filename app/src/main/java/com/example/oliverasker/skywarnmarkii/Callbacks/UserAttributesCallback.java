package com.example.oliverasker.skywarnmarkii.Callbacks;

import java.util.Map;

/**
 * Created by oliverasker on 2/23/17.
 */

public interface UserAttributesCallback {
    void onProcessFinished(Map<String, String> vals);
    void onProcessFinished(String[] vals);
}
