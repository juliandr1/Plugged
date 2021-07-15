package com.project.reshoe_fbu.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("UsersMessaged")
public class UsersMessaged extends ParseObject {

    public static final String KEY_LAST_SENT = "last_sent_timestamp";
    public static final String KEY_LAST_READ = "last_read_timestamp";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_USER_ID = "userID";

    public Date getLastSentTimestamp() {
        return getDate(KEY_LAST_SENT);
    }

    public void setLastSentTimestamp(Date lastSent) {
        put(KEY_LAST_SENT, lastSent);
    }

    public Date getLastReadTimestamp() { return getDate(KEY_LAST_READ); }

    public void setLastReadTimestamp(Date lastRead) {
        put(KEY_LAST_READ, lastRead);
    }

    public ParseObject getMessage() {
        return getParseObject(KEY_LAST_MESSAGE);
    }

    public void setLastMessage(Message lastMessage) { put(KEY_LAST_MESSAGE, lastMessage); }

    public String getUserID() { return getString("userID"); }

    public void setUserID(String userID) { put(KEY_USER_ID, userID); }

}
