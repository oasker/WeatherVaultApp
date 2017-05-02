package com.example.oliverasker.skywarnmarkii.Callbacks;

import com.example.oliverasker.skywarnmarkii.Models.ChatMessageModel;

import java.util.ArrayList;

/**
 * Created by oliverasker on 5/1/17.
 */

public interface ChatDBCallback {
    void chatDBCallbackComplete(ArrayList<ChatMessageModel> messages);
}
