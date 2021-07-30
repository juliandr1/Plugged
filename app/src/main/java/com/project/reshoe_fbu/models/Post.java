package com.project.reshoe_fbu.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject implements Comparable<Post> {
    public static final String TAG = "Post";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGES = "images";
    public static final String KEY_USER = "user";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_PRICE = "price";
    public static final String KEY_SIZE = "size";
    public static final String KEY_IS_WOMEN_SIZING = "isWomenSizing";
    public static final String KEY_USERS_LIKED = "usersLiked";
    public static final String KEY_NUM_LIKED = "numLiked";
    public static final String KEY_SHOE_NAME = "shoeName";
    public static final String KEY_SHOE_NAME_SEARCH = "shoeNameSearch";
    public static final String KEY_IS_SOLD = "isSold";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public List<String> getImageUrls() throws JSONException {
        return Post.fromJsonArrayIMG(getJSONArray(KEY_IMAGES));
    }

    public void setImages(List<ParseFile> images) { put(KEY_IMAGES, images); }

    public User getUser() { return new User(getParseUser(KEY_USER)); }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public boolean getIsSold() { return getBoolean(KEY_IS_SOLD); }

    public void setIsSold() {
        put(KEY_IS_SOLD, true);
        saveInBackground(e -> Log.i(TAG, "Set as sold"));
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

    public int getPrice() { return getNumber(KEY_PRICE).intValue(); }

    public void setPrice(int price) { put(KEY_PRICE, price); }

    public double getSize() {
        return getNumber(KEY_SIZE).doubleValue();
    }

    public void setSize(double size) { put(KEY_SIZE, size); }

    public boolean getIsWomenSizing() {
        return getBoolean(KEY_IS_WOMEN_SIZING);
    }

    public void setIsWomenSizing(boolean sizing) { put(KEY_IS_WOMEN_SIZING, sizing); }

    public int getNumLikes() {
        return getNumber(KEY_NUM_LIKED).intValue();
    }

    public void setLikes(int likes) {
        put(KEY_NUM_LIKED, likes);
    }

    public JSONArray getLikes() {
        return getJSONArray(KEY_USERS_LIKED);
    }

    public void like() throws JSONException {
        User currentUser = new User(ParseUser.getCurrentUser());

        if (!didLike(currentUser)) {
            add(KEY_USERS_LIKED, ParseUser.getCurrentUser().getObjectId());
            setLikes(getNumLikes() + 1);
            Log.i(TAG, ParseUser.getCurrentUser().getObjectId());
            saveInBackground();
        }
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

    @Override
    public int compareTo(Post o) {
        if (this.getPrice() < o.getPrice()) {
            return 1;
        } else if (this.getPrice() > o.getPrice()) {
            return -1;
        } else {
            return 0;
        }
    }
}

