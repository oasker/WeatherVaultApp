package com.example.oliverasker.skywarnmarkii.Models;

/**
 * Created by oliverasker on 5/1/17.
 */

public class ChatMessageModel {
    //        id is epoch of message sent
    public final String state;
    public final String messageAuthor;
    public final String message;
    public final double dateMessageSent;
    public final boolean isCurrentUsersMessage;

    public ChatMessageModel(String id, String message, String messageAuthor, double dateMessageSent, boolean currentUsersMessage) {
        this.state = id;
        this.message = message;
        this.messageAuthor = messageAuthor;
        this.dateMessageSent = dateMessageSent;
        this.isCurrentUsersMessage = currentUsersMessage;
    }

    @Override
    public String toString() {
        return message;
    }
}