package com.project.reshoe_fbu.models;

import android.util.Log;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Thread")
public class Thread extends ParseObject {

    private static final String TAG = "Thread";

    public static final String KEY_LAST_SENT = "last_timestamp_sent";
    public static final String KEY_LAST_READ = "last_timestamp_read";
    public static final String KEY_LAST_MESSAGE_RECEIVED = "last_message_received";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_USER = "user";
    public static final String KEY_OTHER_USER = "otherUser";
    public static final String KEY_MESSAGES = "messages";

    public Date getLastSentTimestamp() { return getDate(KEY_LAST_SENT); }

    public void setLastSentTimestamp(Date lastSent) {
        put(KEY_LAST_SENT, lastSent);
    }

    public Date getLastReadTimestamp() { return getDate(KEY_LAST_READ); }

    public void setLastReadTimestamp(Date lastRead) {
        put(KEY_LAST_READ, lastRead);
    }

    public Date getLastMessageReceived() { return getDate(KEY_LAST_MESSAGE_RECEIVED); }

    public void setLastMessageReceived(Date lastReceived) { put(KEY_LAST_MESSAGE_RECEIVED, lastReceived); }

    public ParseUser getOtherUser() { return getParseUser(KEY_OTHER_USER); }

    public void setOtherUser(User otherUser) { put(KEY_OTHER_USER, otherUser.getUser()); }

    public ParseUser getUser() { return getParseUser(KEY_USER); }

    public void setUser(User user) { put(KEY_USER, user.getUser()); }

    public Message getLastMessage() { return (Message) getParseObject(KEY_LAST_MESSAGE); }

    public void setLastMessage(Message message) { put(KEY_OTHER_USER, message); }

    public List<Message> getThreadMessages() throws ParseException {
        List<Message> messages;

        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

        ParseUser user = getUser();
        ParseUser otherUser = getOtherUser();

        query.whereEqualTo(Message.KEY_AUTHOR, user);
        query.whereEqualTo(Message.KEY_OTHER, otherUser);

        messages = query.find();

        query = ParseQuery.getQuery(Message.class);

        query.whereEqualTo(Message.KEY_AUTHOR, otherUser);
        query.whereEqualTo(Message.KEY_OTHER_ID, user);

        messages.addAll(query.find());

        Log.i(TAG, messages.size() + "");

        return messages;
    }

    public void addMessage(Message newMessage) { add(KEY_MESSAGES, newMessage); }
}
