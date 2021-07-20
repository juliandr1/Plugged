package com.project.reshoe_fbu.models;

import com.parse.ParseClassName;
import com.parse.ParseDecoder;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

@ParseClassName("Thread")
public class Thread extends ParseObject {

    private ParseUser otherUser;
    private String preview, username;

    public static final String KEY_LAST_SENT = "last_timestamp_sent";
    public static final String KEY_LAST_READ = "last_timestamp_read";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_USER_ID = "sent_id";

    public Thread(ParseUser otherUser) throws JSONException {
        ParseUser user = ParseUser.getCurrentUser();

        this.otherUser = otherUser;
        this.username = otherUser.getUsername();

        JSONArray jsonArray = user.getJSONArray("usersMessaged");
        HashMap<String, ParseObject> usersMessaged = Thread.fromJsonArray(jsonArray);

        this.preview = usersMessaged.get(otherUser.getObjectId()).getParseObject("lastMessage").getString("body");
    }

    public String getPreview() {
        return preview;
    }

    public String getUsername() {
        return username;
    }

    public ParseUser getOtherUser() {
        return otherUser;
    }

    public static HashMap<String, ParseObject> fromJsonArray(JSONArray jsonArray) throws JSONException {
        HashMap<String, ParseObject> usersMessaged = new HashMap<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            usersMessaged.put(fromJsonString(jsonObject), fromJsonUsersMessaged(jsonObject));
        }

        return usersMessaged;
    }

    public static String fromJsonString(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("userID");
    }
    public static ParseObject fromJsonUsersMessaged(JSONObject jsonObject) throws JSONException {
        return ParseObject.fromJSON(jsonObject, "UsersMessaged.class", ParseDecoder.get());
    }

    public Date getLastSentTimestamp() { return getDate(KEY_LAST_SENT); }

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
