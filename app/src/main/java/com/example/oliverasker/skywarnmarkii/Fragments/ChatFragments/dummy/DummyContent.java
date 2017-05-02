package com.example.oliverasker.skywarnmarkii.Fragments.ChatFragments.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {


    public static final List<ChatMessage> ITEMS = new ArrayList<ChatMessage>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, ChatMessage> ITEM_MAP = new HashMap<String, ChatMessage>();

    private static final int COUNT = 25;

//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createChatMessageItem(i));
//        }
//    }

    private static void addItem(ChatMessage item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);

    }

    private static ChatMessage createChatMessageItem(int position) {
        return new ChatMessage(String.valueOf(position), "Message Item " + position, "Message Author", makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class ChatMessage {
        //        id is epoch of message sent
        public final String id;
        public final String messageAuthor;
        public final String message;
        public final String dateMessageSent;

        public ChatMessage(String id, String message, String messageAuthor, String dateMessageSent) {
            this.id = id;
            this.message = message;
            this.messageAuthor = messageAuthor;
            this.dateMessageSent = dateMessageSent;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
