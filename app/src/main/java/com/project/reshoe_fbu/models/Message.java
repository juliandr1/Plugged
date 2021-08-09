package com.project.reshoe_fbu.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

    public static final String KEY_AUTHOR_ID = "authorID";
    public static final String KEY_OTHER_ID = "otherUserID";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_OTHER = "otherUser";
    public static final String KEY_BODY = "body";

    public User getAuthor() { return new User(getParseUser(KEY_AUTHOR)); }

    public void setAuthor(User author) {
        put(KEY_AUTHOR, author.getUser());
        setAuthorId(author.getObjectID());
    }

    public void setOtherUser(User otherUser) {
        put(KEY_OTHER, otherUser.getUser());
        setOtherId(otherUser.getObjectID());
    }

    public String getAuthorId() { return getString(KEY_AUTHOR_ID); }

    public void setAuthorId(String userId) { put(KEY_AUTHOR_ID, userId); }

    public void setOtherId(String userId) { put(KEY_OTHER_ID, userId); }

    public String getBody() { return getString(KEY_BODY); }

    public void setBody(String body) { put(KEY_BODY, body); }

}
