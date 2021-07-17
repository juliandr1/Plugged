package com.project.reshoe_fbu.models;

import com.parse.ParseDecoder;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MessagePreview {

    private ParseUser otherUser;
    private String preview, username;

    public MessagePreview (ParseUser otherUser) throws JSONException {
        ParseUser user = ParseUser.getCurrentUser();

        this.otherUser = otherUser;
        this.username = otherUser.getUsername();

        JSONArray jsonArray = user.getJSONArray("usersMessaged");
        HashMap<String, ParseObject> usersMessaged = MessagePreview.fromJsonArray(jsonArray);

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
}
