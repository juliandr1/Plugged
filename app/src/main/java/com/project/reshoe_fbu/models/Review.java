package com.project.reshoe_fbu.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Review")
public class Review extends ParseObject {

    public static final String KEY_AUTHOR = "author";
    public static final String KEY_BODY = "body";
    public static final String KEY_RATING = "rating";

    public ParseUser getAuthor() { return getParseUser(KEY_AUTHOR); }

    public void setAuthor(ParseUser user) { put(KEY_AUTHOR, user); }

    public String getBody() { return getString(KEY_BODY); }

    public void setBody(String body) { put(KEY_BODY, body); }

    public int getRating() { return getNumber(KEY_RATING).intValue(); }

    public void setRating(int rating) { put(KEY_RATING, rating); }
}
