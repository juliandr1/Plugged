package com.project.reshoe_fbu.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

    public static final String AUTHOR_ID = "authorID";
    public static final String BODY_KEY = "body";

    public String getUserId() { return getString(AUTHOR_ID); }

    public void setUserId(String userId) { put(AUTHOR_ID, userId); }

    public String getBody() { return getString(BODY_KEY); }

    public void setBody(String body) { put(BODY_KEY, body); }

}
