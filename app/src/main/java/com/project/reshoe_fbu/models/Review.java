package com.project.reshoe_fbu.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Review")
public class Review extends ParseObject {

    public static final String KEY_AUTHOR = "author";
    public static final String KEY_BODY = "body";
    public static final String KEY_RATING = "rating";
    public static final String KEY_REVIEWEE = "reviewee";

    public User getAuthor() { return new User(getParseUser(KEY_AUTHOR)); }

    public void setAuthor(User user) { put(KEY_AUTHOR, user.getUser()); }

    public User getReviewee() { return new User(getParseUser(KEY_REVIEWEE)); }

    public void setReviewee(User user) { put(KEY_REVIEWEE, user.getUser()); }

    public String getBody() { return getString(KEY_BODY); }

    public void setBody(String body) { put(KEY_BODY, body); }

    public double getRating() { return getNumber(KEY_RATING).intValue(); }

    public void setRating(double rating) { put(KEY_RATING, rating); }
}
