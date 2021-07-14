package com.example.reshoe_fbu.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String TAG = "Post";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_PRICE = "price";
    public static final String KEY_USERS_LIKED = "usersLiked";
    public static final String KEY_NUM_LIKED = "numLiked";
    public static final String KEY_SHOE_NAME = "shoeName";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) { put(KEY_USER, user); }

    public int getCondition() {
        return getNumber(KEY_USER).intValue();
    }

    public void setCondition(int condition) { put(KEY_CONDITION, condition); }

    public String getShoeName() {
        return getString(KEY_SHOE_NAME);
    }

    public void setShoeName(String name) { put(KEY_SHOE_NAME, name); }

    public double getPrice() {
        return getNumber(KEY_PRICE).doubleValue();
    }

    public void setPrice(double price){ put(KEY_USER, Double.parseDouble(new DecimalFormat(".00").format(price))); }

    public int getNumLikes() {
        return getNumber(KEY_NUM_LIKED).intValue();
    }

    public void setLikes(int likes){ put(KEY_NUM_LIKED, likes); }

    public JSONArray getLikes() {return getJSONArray(KEY_USERS_LIKED);}

    public void like(ParseUser user) {
        add(KEY_USERS_LIKED, user.getObjectId());
        setLikes(getNumLikes() + 1);
        saveInBackground();
    }

    public void unlike(ParseUser user) {
        removeAll(KEY_USERS_LIKED, Arrays.asList(user.getObjectId()));
        if (getNumLikes() > 0) {
            setLikes(getNumLikes() - 1);
        }
        saveInBackground();
    }

    public boolean didLike(ParseUser user) throws JSONException {
        JSONArray jsonArray = getLikes();

        // Check if the likes list is null
        if (jsonArray == null) {
            Log.i(TAG, "Empty");
            return false;
        }

        // Get the list of userIds and check if the current user has liked the post.
        List<String> userIds = Post.fromJsonArray(jsonArray);
        return userIds.contains(user.getObjectId());
    }

    public static String calculateTimeAgo(Date createdAt) {
        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "hr";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }

    public static List<String> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<String> likedUsers = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            likedUsers.add(fromJson(jsonArray.get(i)));
        }

        return likedUsers;
    }

    public static String fromJson(Object object) throws JSONException {
        return object.toString();
    }
}
