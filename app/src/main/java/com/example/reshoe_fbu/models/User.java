package com.example.reshoe_fbu.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String TAG = "User";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_IS_SELLER = "isSeller";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_LIKED_SELLERS = "likedSellers";

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public void setPassword(String password) { put(KEY_PASSWORD, password); }

    public String getEmail() {
        return getString(KEY_EMAIL);
    }

    public void setEmail(String email) {
        put (KEY_EMAIL, email);
    }

    public void setFirstName(String firstName) { put (KEY_FIRST_NAME, firstName); }

    public void setLastName(String lastName) { put (KEY_LAST_NAME, lastName); }

    public void setIsSeller(Boolean isSeller) { put (KEY_IS_SELLER, isSeller); }

    public void setProfilePic(ParseFile file) { put (KEY_IS_SELLER, file); }

    public JSONArray getLikes() {return getJSONArray(KEY_LIKED_SELLERS);}

    public void like(ParseUser user) {
        add(KEY_LIKED_SELLERS, user.getObjectId());
        saveInBackground();
    }

    public void unlike(ParseUser user) {
        removeAll(KEY_LIKED_SELLERS, Arrays.asList(user.getObjectId()));
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
        List<String> userIds = User.fromJsonArray(jsonArray);
        return userIds.contains(user.getObjectId());
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
