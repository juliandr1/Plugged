package com.project.reshoe_fbu.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.reshoe_fbu.databinding.ActivityCreateReviewBinding;
import com.example.reshoe_fbu.databinding.ActivitySignupBinding;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class User  {
    public static final String TAG = "User";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERNAME_SEARCH = "user_search";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_IS_SELLER = "isSeller";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_LIKED_SELLERS = "likedSellers";
    public static final String KEY_RATING = "rating";
    public static final String KEY_NUM_REVIEWS = "numReviews";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_OBJECT_ID = "objectId";

    private ParseUser user;

    public User (ParseUser user) {
        this.user = user;
    }

    public String getUsername() {
        return user.getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        user.put(KEY_USERNAME, username);
        user.put(KEY_USERNAME_SEARCH, username.toLowerCase()); }

    public void setPassword(String password) { user.put(KEY_PASSWORD, password); }

    public String getEmail() { return user.getString(KEY_EMAIL); }

    public void setEmail(String email) {
        user.put(KEY_EMAIL, email);
    }

    public String getDescription() { return user.getString(KEY_DESCRIPTION); }

    public void setDescription(String description) {
        user.put(KEY_DESCRIPTION, description);
    }

    public String getFirstName() {
        return user.getString(KEY_FIRST_NAME);
    }

    public void setFirstName(String firstName) { user.put(KEY_FIRST_NAME, firstName); }

    public String getLastName() {
        return user.getString(KEY_LAST_NAME);
    }

    public void setLastName(String lastName) { user.put(KEY_LAST_NAME, lastName); }

    public boolean getIsSeller() { return user.getBoolean("isSeller"); }

    public void setIsSeller(Boolean isSeller) { user.put(KEY_IS_SELLER, isSeller); }

    public String getProfilePicURL() { return user.getParseFile(KEY_PROFILE_PIC).getUrl(); }

    public void setProfilePic(ParseFile file) { user.put(KEY_PROFILE_PIC, file); }

    public double getRating() { return user.getNumber(KEY_RATING).doubleValue(); }

    public void setRating(double rating) { user.put(KEY_RATING, rating); }

    public int getNumReviews() { return user.getNumber(KEY_NUM_REVIEWS).intValue(); }

    public void setNumReviews(int numReviews) { user.put(KEY_NUM_REVIEWS, numReviews); }

    public String getObjectID() { return user.getObjectId(); }

    public ParseUser getUser() { return user; }

    public JSONArray getLikes() {return user.getJSONArray(KEY_LIKED_SELLERS);}

    public void like(ParseUser user) {
        user.add(KEY_LIKED_SELLERS, user.getObjectId());
        user.saveInBackground();
    }

    public void unlike(ParseUser user) {
        user.removeAll(KEY_LIKED_SELLERS, Arrays.asList(user.getObjectId()));
        user.saveInBackground();
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

    public static String fromJson(Object object) {
        return object.toString();
    }
}
