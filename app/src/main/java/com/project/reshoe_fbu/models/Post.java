package com.project.reshoe_fbu.models;

import android.graphics.Bitmap;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String TAG = "Post";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGES = "images";
    public static final String KEY_USER = "user";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_PRICE = "price";
    public static final String KEY_USERS_LIKED = "usersLiked";
    public static final String KEY_NUM_LIKED = "numLiked";
    public static final String KEY_SHOE_NAME = "shoeName";
    public static final String KEY_SHOE_NAME_SEARCH = "shoeNameSearch";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    // Will be used in the future
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public List<String> getImageUrls() throws JSONException {
        return Post.fromJsonArrayIMG(getJSONArray(KEY_IMAGES));
    }

    public void setImages(List<ParseFile> images) { put(KEY_IMAGES, images); }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public int getCondition() {
        return getNumber(KEY_CONDITION).intValue();
    }

    public void setCondition(int condition) {
        put(KEY_CONDITION, condition);
    }

    public String getShoeName() {
        return getString(KEY_SHOE_NAME);
    }

    public void setShoeName(String name) {
        put(KEY_SHOE_NAME, name);
        put(KEY_SHOE_NAME_SEARCH, name.toLowerCase());
    }

    public double getPrice() {
        return getNumber(KEY_PRICE).doubleValue();
    }

    public void setPrice(double price) {
        put(KEY_PRICE, Double.parseDouble(new DecimalFormat(".00").format(price)));
    }

    public int getNumLikes() {
        return getNumber(KEY_NUM_LIKED).intValue();
    }

    public void setLikes(int likes) {
        put(KEY_NUM_LIKED, likes);
    }

    public JSONArray getLikes() {
        return getJSONArray(KEY_USERS_LIKED);
    }

    public void like() {
        add(KEY_USERS_LIKED, ParseUser.getCurrentUser().getObjectId());
        setLikes(getNumLikes() + 1);
        Log.i(TAG, ParseUser.getCurrentUser().getObjectId());
        saveInBackground();
    }

    public void unlike() {
        removeAll(KEY_USERS_LIKED, Arrays.asList(ParseUser.getCurrentUser().getObjectId()));
        if (getNumLikes() > 0) {
            setLikes(getNumLikes() - 1);
        }
        saveInBackground();
    }

    public boolean didLike(User user) throws JSONException {
        JSONArray jsonArray = getLikes();

        // Check if the likes list is null
        if (jsonArray == null) {
            Log.i(TAG, "Empty");
            return false;
        }

        // Get the list of userIds and check if the current user has liked the post.
        List<String> userIds = Post.fromJsonArray(jsonArray);

        for (String userId : userIds) {
            Log.i(TAG, userId);
        }

        return userIds.contains(user.getObjectID());
    }

    public static List<String> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<String> likedUsers = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            likedUsers.add(fromJson(jsonArray.get(i)));
        }

        return likedUsers;
    }

    public static String fromJson(Object object) {
        return object.toString();
    }

    public static String fromJsonIMG(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("url");
    }

    public static List<String> fromJsonArrayIMG(JSONArray jsonArray) throws JSONException{
        List<String> images = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            images.add(fromJsonIMG(jsonArray.getJSONObject(i)));
        }
        return images;
    }
}

